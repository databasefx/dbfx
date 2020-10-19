#include <stdio.h>
#include <string.h>
#include <stdlib.h>


void cmake_file(char **data, int max_txt_line, int max_row_line) {
    FILE *file = fopen("CMakeLists.txt", "r+");
    if (file == NULL) {
        printf("%s", "CMakeList.txt不存在!");
        return;
    }
    char buffer[1024];
    int row = 0;
    while (fgets(buffer, 1024, file) != NULL) {
        data[row] = (char *) malloc(max_txt_line * sizeof(char *));
        strcpy(data[row], buffer);
        row++;
    }
}


