package cn.navigational.dbfx.controls.tree

import cn.navigational.dbfx.view.HomeView
import javafx.scene.control.TreeView

class CustomTreeView : TreeView<String>() {
    private var homeView: HomeView? = null


    fun getHomeView(): HomeView? {
        return homeView
    }
}