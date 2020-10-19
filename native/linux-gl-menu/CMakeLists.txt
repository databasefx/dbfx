cmake_minimum_required(VERSION 3.17)
project(dbm)
# 设置变量
set(CMAKE_C_STANDARD 99)
set(SOURCE_FILES linux_global_menu_wrapper.c)
set(GLIB_INCLUDE_DIRS /usr/include/glib-2.0 /usr/lib/x86_64-linux-gnu/glib-2.0/include)
set(GTK_INCLUDE_DIRS /usr/include/gtk-3.0/)
set(PANGO_INCLUDE_DIRS /usr/include/pango-1.0/)
set(HB_INCLUDE_DIRS /usr/include/harfbuzz/)
set(CAIRO_INCLUDE_DIRS /usr/include/cairo/)
MESSAGE(STATUS "Glib ${GLIB_INCLUDE_DIRS}")
include_directories(
        ${GTK_INCLUDE_DIRS}
        ${GLIB_INCLUDE_DIRS}
        ${PANGO_INCLUDE_DIRS}
        ${HB_INCLUDE_DIRS}
        ${CAIRO_INCLUDE_DIRS}
        /usr/include/gdk-pixbuf-2.0/
        /usr/include/atk-1.0/
)
add_executable(dbmexec main.c)
