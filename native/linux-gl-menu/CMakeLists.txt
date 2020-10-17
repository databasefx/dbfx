cmake_minimum_required(VERSION 3.17)
project(dbm)

set(CMAKE_C_STANDARD 99)
set(SOURCE_FILES linux_global_menu_wrapper.c)

add_executable(dbmexec test.c)