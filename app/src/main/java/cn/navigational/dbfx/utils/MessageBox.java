package cn.navigational.dbfx.utils;

import java.io.*;
import java.nio.file.*;

public class MessageBox<T extends Serializable> {

    public static final long NAP_TIME = 100;

    final private String folder;
    final private Class<T> messageClass;
    final int pollingTime;
    final Path messageFile;
    final FileMutex boxMutex;
    final FileMutex messageMutex;

    private PollingThread<T> pollingThread;
    private Delegate<T> delegate;

    public MessageBox(String folder, Class<T> messageClass, int pollingTime) {
        assert folder != null;
        assert messageClass != null;
        assert pollingTime > 0;

        this.folder = folder;
        this.messageClass = messageClass;
        this.pollingTime = pollingTime;
        //NOI18N
        this.messageFile = Paths.get(folder, "message.dat");
        //NOI18N
        this.boxMutex = new FileMutex(Paths.get(folder, "box.mtx"));
        //NOI18N
        this.messageMutex = new FileMutex(Paths.get(folder, "message.mtx"));

        if (!Files.exists(Paths.get(folder))) {
            //NOI18N
            throw new IllegalArgumentException(folder + " does not exist");
        }
    }

    public String getFolder() {
        return folder;
    }

    public boolean grab(Delegate<T> delegate)
            throws IOException {
        assert !boxMutex.isLocked();
        assert pollingThread == null;
        assert delegate != null;

        if (boxMutex.tryLock()) {
            this.delegate = delegate;
            this.pollingThread = new PollingThread<>(this);
            this.pollingThread.setDaemon(true);
            this.pollingThread.start();
        }

        return boxMutex.isLocked();
    }


    public void release() {
        assert boxMutex.isLocked();
        assert pollingThread != null;
        assert pollingThread.isAlive();

        pollingThread.interrupt();
        pollingThread = null;

        try {
            boxMutex.unlock();
        } catch (IOException x) {
            // Strange
            x.printStackTrace();
        }
    }


    public void sendMessage(T message) throws IOException, InterruptedException {
        assert !boxMutex.isLocked();
        assert !messageMutex.isLocked();

        final Path transientFile = Files.createTempFile(Paths.get(folder), null, null);
        Files.write(transientFile, serializeMessage(message));

        messageMutex.lock(100L * pollingTime);
        boolean retry;
        int accessDeniedCount = 0;
        do {
            if (!Files.exists(messageFile)) {
                try {
                    Files.move(transientFile, messageFile, StandardCopyOption.ATOMIC_MOVE);
                    retry = false;
                } catch (AccessDeniedException x) {
                    // Sometime on Windows, move is denied (?).
                    // So we retry a few times...
                    if (accessDeniedCount++ <= 10) {
                        retry = true;
                    } else {
                        throw x;
                    }
                }
            } else {
                retry = true;
            }
            if (retry) {
                Thread.sleep(NAP_TIME);
            }
        } while (retry);
        messageMutex.unlock();
    }

    public interface Delegate<T> {
        public void messageBoxDidGetMessage(T message);

        public void messageBoxDidCatchException(Exception x);
    }

    public Path getMessagePath() {
        return messageFile;
    }

    public Path getBoxMutexPath() {
        return boxMutex.getLockFile();
    }

    public Path getMessageMutexPath() {
        return messageMutex.getLockFile();
    }


    /*
     * Private
     */

    private byte[] serializeMessage(T message) throws IOException {
        final byte[] result;

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(message);
                result = bos.toByteArray();
            }
        }

        return result;
    }


    private T unserializeMessage(byte[] bytes) throws IOException {
        final T result;

        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream ois = new ObjectInputStream(bis)) {
                try {
                    result = messageClass.cast(ois.readObject());
                } catch (ClassNotFoundException x) {
                    // Strange
                    throw new IOException(x);
                }
            }
        }

        return result;
    }


    private static class PollingThread<T extends Serializable> extends Thread {

        private final MessageBox<T> messageBox;

        public PollingThread(MessageBox<T> messageBox) {
            super("MessageBox[" + messageBox.getFolder() + "]"); //NOI18N
            this.messageBox = messageBox;
        }

        @Override
        public void run() {

            try {
                do {
                    if (Files.exists(messageBox.messageFile)) {
                        try {
                            final byte[] messageBytes = Files.readAllBytes(messageBox.messageFile);
                            final T message = messageBox.unserializeMessage(messageBytes);
                            messageBox.delegate.messageBoxDidGetMessage(message);
                        } catch (IOException x) {
                            messageBox.delegate.messageBoxDidCatchException(x);
                        } finally {
                            try {
                                Files.delete(messageBox.messageFile);
                            } catch (IOException x) {
                                messageBox.delegate.messageBoxDidCatchException(x);
                            }
                        }
                        Thread.sleep(NAP_TIME);
                    } else {
                        Thread.sleep(messageBox.pollingTime);
                    }
                } while (true);

            } catch (InterruptedException x) {
            }
        }
    }
}