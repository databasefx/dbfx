package cn.navigational.dbfx.controls.tree;

/**
 * {@link AbstractBaseTreeItem} menu handler
 *
 * @author yangkui
 * @since 1.0
 */
public interface TreeItemMenuHandler {
    /**
     * {@link javafx.scene.control.TreeItem} current support menu
     */
    enum MenuAction {
        /**
         * FLUSH
         */
        FLUSH,
        /**
         * Open terminal
         */
        OPEN_TERMINAL,
        /**
         * Edit connect
         */
        EDIT_CONNECT,
        /**
         * Open connection
         */
        OPEN_CONNECT,
        /**
         * Delete a connect
         */
        DELETE_CONNECT,
        /**
         * Discount connect
         */
        DISCOUNT_CONNECT
    }

    /**
     * When any {@link javafx.scene.control.MenuItem} has clicked callback that method.
     *
     * @param action Some action
     */
    void onAction(MenuAction action);
}
