#include <stdlib.h>
#include <string.h>

int main(int argc, char * argv[]) {
    char * findstr = "/Ball-Game-Mac";
    char * replacestr = "/pkg/run_game.sh ";
    char * bashstr = "bash ";
    char * startstr = malloc(strlen(argv[0]) - strlen(findstr) + strlen(replacestr));
    char * ptr;

    strcpy(startstr, bashstr);
    strcat(startstr, argv[0]);

    ptr = strstr(startstr, findstr);
    if(ptr)
    {
        memmove(ptr + strlen(replacestr), ptr + strlen(findstr), strlen(ptr + strlen(findstr)) + 1);
        strncpy(ptr, replacestr, strlen(replacestr));
    }

    int argmalloc = 0;
    int i;
    for(i = 1; i < argc; i++) {
        argmalloc += sizeof(char)*strlen(argv[i]);
        if(i < argc-1)
            argmalloc++; //for the space
    }
    char runstr[sizeof(char)*strlen(startstr) + argmalloc];
    strcpy(runstr, startstr);
    free(startstr);
    for(i = 1; i < argc; i++) {
        strcat(runstr, argv[i]);
        if(i < argc-1)
            strcat(runstr, " ");
    }
    system(runstr);
//    free(runstr);
    return 0;
}
