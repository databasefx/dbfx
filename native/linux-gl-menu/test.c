//
// Created by yangkui on 2020/10/19.
//
#include <stdio.h>
#include <gtk-3.0/gtk/gtk.h>


static void activate(GtkApplication *app, gpointer user_data) {
    GtkWidget *window;
    window = gtk_application_window_new(app);
    gtk_window_set_title(GTK_WINDOW (window), "Gtk Application");
    gtk_window_set_default_size(GTK_WINDOW (window), 500, 600);
    GtkWidget *hbox;
    GtkWidget *menubar;
    GtkWidget *fileMenu, *helpMenu;
    GtkWidget *fileMi, *helpMi;
    GtkWidget *quitMi;
    menubar = gtk_menu_bar_new();
    fileMenu = gtk_menu_new();
    helpMenu = gtk_menu_new();

    hbox = gtk_box_new(TRUE, 0);
    gtk_container_add(GTK_CONTAINER(window), hbox);

    fileMi = gtk_menu_item_new_with_label("File");
    helpMi = gtk_menu_item_new_with_label("Help");
    quitMi = gtk_menu_item_new_with_label("Quit");

    gtk_menu_item_set_submenu(GTK_MENU_ITEM(fileMi), fileMenu);
    gtk_menu_item_set_submenu(GTK_MENU_ITEM(helpMi), helpMenu);
    gtk_menu_shell_append(GTK_MENU_SHELL(fileMenu), quitMi);
    gtk_menu_shell_append(GTK_MENU_SHELL(menubar), fileMi);
    gtk_menu_shell_append(GTK_MENU_SHELL(menubar), helpMi);
    gtk_box_pack_start(GTK_BOX(hbox), menubar, FALSE, FALSE, 0);
    gtk_widget_show_all(window);
    gtk_main();
}

int main(int arg, char **argc) {
    GtkApplication *app;
    int status;
    app = gtk_application_new("cn.navigation.dbfx", G_APPLICATION_FLAGS_NONE);
    g_signal_connect (app, "activate", G_CALLBACK(activate), NULL);
    status = g_application_run(G_APPLICATION (app), arg, argc);
    g_object_unref(app);
    return status;
}