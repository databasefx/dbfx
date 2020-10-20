cmake_minimum_required(VERSION 3.17)
project(dbm)

find_package(PkgConfig REQUIRED)
pkg_check_modules(GTK3 REQUIRED gtk+-3.0)
include_directories(${GTK3_INCLUDE_DIRS})
link_directories(${GTK3_LIBRARY_DIRS})
list(APPEND FC_DEP_LIBS ${GTK3_LIBRARIES})

add_executable(dbmexec test.c)
target_link_libraries(dbmexec ${FC_DEP_LIBS})
