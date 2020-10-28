package cn.navigational.dbfx.controls.tree

import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.controls.tree.cell.NTreeCell
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.utils.AlertUtils
import javafx.collections.ListChangeListener
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CustomTreeView private constructor() : TreeView<String>() {

    init {
        isShowRoot = false
        styleClass.add("l-navigator")
        this.setCellFactory { NTreeCell() }
        SQLClientManager.manager.getDbInfo().forEach(this::createClientTree)
        SQLClientManager.manager.getDbInfo().addListener(ListChangeListener {
            while (it.next()) {
                //Listener add operation
                if (it.wasAdded()) {
                    it.addedSubList.forEach(this::createClientTree)
                }
                //Listener remove operation
                if (it.wasRemoved()) {
                    it.removed.forEach(this::deleteClientTree)
                }
            }
        })
    }

    /**
     *
     * Create a new client tree
     * @param it Database info
     */
    private fun createClientTree(it: DbInfo) {
        val item = DatabaseTreeItem(it)
        item.text = it.name
        if (this.root == null) {
            this.root = TreeItem()
        }
        this.root.children.add(item.treeItem)
    }

    /**
     * Delete a client tree
     *
     * @param it Database info
     */
    private fun deleteClientTree(it: DbInfo) {
        for (child in root.children) {
            val item = (child as AbstractBaseTreeItem.InnerTreeItem).control as DatabaseTreeItem
            if (it.uuid == item.getUuId()) {
                item.delConnect()
                break
            }
        }
    }

    fun updateConnection(dbInfo: DbInfo) {
        val list = root.children.filter {
            val item = ((it as AbstractBaseTreeItem.InnerTreeItem).control) as DatabaseTreeItem
            item.getUuId() == dbInfo.uuid
        }
        if (list.isEmpty()) {
            return
        }
        val item = ((list[0] as AbstractBaseTreeItem.InnerTreeItem).control) as DatabaseTreeItem
        item.update(dbInfo)
        if (!item.conStatus()) {
            return
        }
        val result = AlertUtils.showSimpleConfirmDialog("当前连接已改变,是否重新连接?")
        if (!result) {
            return
        }
        //Start reconnection
        item.reConnect()
    }

    companion object {
        val customTreeView: CustomTreeView by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { CustomTreeView() }
    }
}