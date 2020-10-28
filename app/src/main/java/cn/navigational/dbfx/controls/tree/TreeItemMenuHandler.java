package cn.navigational.dbfx.controls.tree;

import javafx.event.ActionEvent;

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
     * @param event  {@link ActionEvent} event source
     * @param action Some action
     */
    void onAction(ActionEvent event, MenuAction action);
}
