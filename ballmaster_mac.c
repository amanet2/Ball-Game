#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char * argv[]) {
    char * findstr = "/ballmaster_mac"; // for regular
//    char * findstr = "/ballmaster_mac_editor"; // for editor
    char * cdreplacestr = "/pkg && ../bin/jdk-18.jdk/Contents/Home/bin/java -Dsun.java2d.uiScale=1.0 -jar BALL_GAME.jar "; // for regular
//    char * cdreplacestr = "/pkg && ../bin/jdk-18.jdk/Contents/Home/bin/java -Dsun.java2d.uiScale=1.0 -jar BALL_GAME.jar showmapmakerui 1 "; // for editor
    char * cdstr = malloc(strlen("cd ") + strlen(argv[0]) - strlen(findstr) + strlen("/pkg"));
    char * cptr;

    strcpy(cdstr, "cd ");
    strcat(cdstr, argv[0]);
    cptr = strstr(cdstr, findstr);
    if(cptr) {
        memmove(cptr + strlen(cdreplacestr), cptr + strlen(findstr), strlen(cptr + strlen(findstr)) + 1);
        strncpy(cptr, cdreplacestr, strlen(cdreplacestr));
    }

    int argmalloc = 0;
    int i;
    for(i = 1; i < argc; i++) {
        argmalloc += sizeof(char)*strlen(argv[i]);
        if(i < argc-1)
            argmalloc++; //for the space
    }
    char runstr[sizeof(char)*strlen(cdstr) + argmalloc];
    strcpy(runstr, cdstr);
    free(cdstr);
    for(i = 1; i < argc; i++) {
        strcat(runstr, argv[i]);
        if(i < argc-1)
            strcat(runstr, " ");
    }
//    printf("runstr %s\n", runstr);
    system(runstr);
    return 0;
}