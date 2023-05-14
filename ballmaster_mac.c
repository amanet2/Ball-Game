#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char * argv[]) {
    char * findstr = "/ballmaster_mac";
    char * replacestr = "/bin/jdk-18.jdk/Contents/Home/bin/java -cp pkg -Dsun.java2d.uiScale=1.0 -jar BALL_GAME.jar "; //edit here for mapmaker
    char * startstr = malloc(strlen(argv[0]) - strlen(findstr) + strlen(replacestr));
    char * ptr;

    strcpy(startstr, "");
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
//    printf("%s", runstr);
    system("cd ~/Code/Ball-Game/pkg && ~/Code/Ball-Game/bin/jdk-18.jdk/Contents/Home/bin/java -Dsun.java2d.uiScale=1.0 -jar ~/Code/Ball-Game/pkg/BALL_GAME.jar");
//    free(runstr);
    return 0;
}