//
// Created by yangkui on 2020/10/19.
//
#include <stdio.h>
#include "test.h"

int main(int arg, char **argc) {
    char *txt[500];
    cmake_file(txt, 500, 1024);
    int row = 0;
    char *str;
    while ((str = *txt + row) != NULL && row < 500) {
        printf("%s", str);
        row++;
    }
    printf("%s", "Success read cmalelist.txt file.");
    return 0;
}