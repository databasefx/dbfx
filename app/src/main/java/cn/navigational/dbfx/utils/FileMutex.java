package cn.navigational.dbfx.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class implements a mutex using FileLock.
 * Two processes which want to be in mutual exclusion should:
 * 1) create an instance of FileMutex using the same file
 * 2) call FileMutex.lock() or FileMutex.tryLock()
 */
class FileMutex {

    private final Path lockFile;
    private RandomAccessFile lockRAF;
    private FileLock lock;

    public FileMutex(Path lockFile) {
        assert lockFile != null;
        this.lockFile = lockFile;
    }

    public Path getLockFile() {
        return lockFile;
    }

    public void lock(long timeout) throws IOException {
        assert lockRAF == null;
        assert lock == null;

        createFileChannel();
        assert lockRAF != null;
        final Timer timer = new Timer();
        timer.schedule(new InterruptTask(), timeout);
        lock = lockRAF.getChannel().lock();
        timer.cancel();
        assert lock != null;
    }

    public boolean tryLock() throws IOException {
        assert lockRAF == null;
        assert lock == null;

        createFileChannel();
        assert lockRAF != null;
        lock = lockRAF.getChannel().tryLock();
        if (lock == null) {
            lockRAF.close();
            lockRAF = null;
        }

        return lock != null;
    }

    public void unlock() throws IOException {
        assert lockRAF != null;
        assert lock != null;
        assert lock.channel() == lockRAF.getChannel();

        lock.release();
        lock = null;
        lockRAF.close();
        lockRAF = null;
    }

    public boolean isLocked() {
        return lock != null;
    }


    /*
     * Private
     */

    private void createFileChannel() throws IOException {
        try {
            Files.createFile(lockFile);
        } catch (FileAlreadyExistsException x) {
            // Someone else already created it
        }
        lockRAF = new RandomAccessFile(lockFile.toFile(), "rw");
    }

    private static class InterruptTask extends TimerTask {
        @Override
        public void run() {
            Thread.currentThread().interrupt();
        }
    }
}