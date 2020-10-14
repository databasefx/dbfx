package cn.navigational.dbfx;

import cn.navigational.dbfx.editor.EditorPlatform;
import cn.navigational.dbfx.utils.MessageBox;
import javafx.application.Application;
import javafx.application.Platform;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static cn.navigational.dbfx.editor.EditorPlatform.*;

public class AppPlatform {
    private static String messageBoxFolder;
    private static String applicationDataFolder;
    private static MessageBox<MessageBoxMessage> messageBox;


    public static synchronized String getApplicationDataFolder() {

        if (applicationDataFolder == null) {
            final String appName = "dbfx";

            if (IS_WINDOWS) {
                applicationDataFolder
                        = System.getenv("APPDATA") + "\\" + appName;
            } else if (IS_MAC) {
                applicationDataFolder
                        = System.getProperty("user.home") + "/Library/Application Support/" + appName;
            } else if (IS_LINUX) {
                applicationDataFolder
                        = System.getProperty("user.home") + "/." + appName;
            }
        }

        assert applicationDataFolder != null;

        return applicationDataFolder;
    }

    public static boolean requestStart(
            AppNotificationHandler notificationHandler, Application.Parameters parameters)
            throws IOException {
        if (EditorPlatform.isAssertionEnabled()) {
            // Development mode : we do not delegate to the existing instance
            notificationHandler.handleLaunch(parameters.getUnnamed());
            return true;
        } else {
            return requestStartGeneric(notificationHandler, parameters);
        }
    }

    private static synchronized boolean requestStartGeneric(
            AppNotificationHandler notificationHandler, Application.Parameters parameters)
            throws IOException {
        assert notificationHandler != null;
        assert parameters != null;
        assert messageBox == null;

        try {
            Files.createDirectories(Paths.get(getMessageBoxFolder()));
        } catch (FileAlreadyExistsException ignored) {
        }

        final boolean result;
        messageBox = new MessageBox<>(getMessageBoxFolder(), MessageBoxMessage.class, 1000);
        if (messageBox.grab(new MessageBoxDelegate(notificationHandler))) {
            notificationHandler.handleLaunch(parameters.getUnnamed());
            result = true;
        } else {
            result = false;
            final MessageBoxMessage params = new MessageBoxMessage(parameters.getUnnamed());
            try {
                messageBox.sendMessage(params);
            } catch (InterruptedException x) {
                throw new IOException(x);
            }
        }

        return result;
    }

    private static String getMessageBoxFolder() {
        if (messageBoxFolder == null) {
            messageBoxFolder = getApplicationDataFolder() + "/MB";
        }
        return messageBoxFolder;
    }

    public interface AppNotificationHandler {
        void handleLaunch(List<String> files);

        void handleOpenFilesAction(List<String> files);

        void handleMessageBoxFailure(Exception x);

        void handleQuitAction();
    }

    private static class MessageBoxMessage extends ArrayList<String> {
        static final long serialVersionUID = 10;

        public MessageBoxMessage(List<String> strings) {
            super(strings);
        }
    }

    private static class MessageBoxDelegate implements MessageBox.Delegate<MessageBoxMessage> {

        private final AppNotificationHandler eventHandler;

        public MessageBoxDelegate(AppNotificationHandler eventHandler) {
            assert eventHandler != null;
            this.eventHandler = eventHandler;
        }

        /*
         * MessageBox.Delegate
         */

        @Override
        public void messageBoxDidGetMessage(MessageBoxMessage message) {
            assert !Platform.isFxApplicationThread();
            Platform.runLater(() -> eventHandler.handleOpenFilesAction(message));
        }

        @Override
        public void messageBoxDidCatchException(Exception x) {
            assert !Platform.isFxApplicationThread();
            Platform.runLater(() -> eventHandler.handleMessageBoxFailure(x));
        }

    }
}
