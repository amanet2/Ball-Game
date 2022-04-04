#include <stdlib.h>
#include <string.h>

int main(int argc, char * argv[]) {
    int argmalloc = 0;
    char * startstr = "start pkg/run_game.bat ";
    int i;
    for(i = 1; i < argc; i++) {
        argmalloc += sizeof(char)*strlen(argv[i]);
        if(i < argc-1)
            argmalloc++; //for the space
    }
    char runstr[sizeof(char)*strlen(startstr) + argmalloc];
    strcpy(runstr, startstr);
    for(i = 1; i < argc; i++) {
        strcat(runstr, argv[i]);
        if(i < argc-1)
            strcat(runstr, " ");
    }
    system(runstr);
    return 0;
}
