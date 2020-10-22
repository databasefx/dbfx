package cn.navigational.dbfx.kit.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;

/**
 * Oss relative operation utils
 *
 * @author yangkui
 * @since 1.0
 */
public class OssUtils {
    private static final Logger LOG = LoggerFactory.getLogger(OssUtils.class);

    /**
     * Get current user home
     *
     * @return User home dir
     */
    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    /**
     * Get current os name
     *
     * @return Os name
     */
    public static String getOsName() {
        return System.getProperty("os.name");
    }

    /**
     * Call {@link Desktop#open(File)} open a file or direction.
     * <note>
     * Awt relation api must call in {@link SwingUtilities#invokeLater(Runnable)},otherwise
     * javafx application probably crash</p>
     * </note>
     *
     * @param path File or direction path
     * @throws Exception Execute error
     */
    public static void openDirOrFileUseFileSystem(String path) throws Exception {
        assert path != null;
        var file = new File(path);
        assert file.exists();
        SwingUtilities.invokeAndWait(() -> {
            var desktop = Desktop.getDesktop();
            assert desktop.isSupported(Desktop.Action.OPEN);
            try {
                desktop.open(file);
            } catch (IOException e) {
                LOG.error("Open direction or file happen error!", e);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Copy the target string to the system clipboard
     *
     * @param str Target string
     */
    public static void addStrToClipboard(String str) {
        var toolkit = Toolkit.getDefaultToolkit();
        var clipboard = toolkit.getSystemClipboard();
        clipboard.setContents(new StringSelection(str), null);
    }
}
