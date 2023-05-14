#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char * argv[]) {
    // 1. get the prefix of our current executable, save as prefix
    // 2. get this string to exec: cd {prefix}/pkg && {prefix}/bin/jdk-18.jdk/Contents/Home/bin/java -Dsun.java2d.uiScale=1.0 -jar {prefix}/pkg/BALL_GAME.jar
    char * findstr = "/ballmaster_mac";
    char * replacestr = "/bin/jdk-18.jdk/Contents/Home/bin/java -Dsun.java2d.uiScale=1.0 -jar BALL_GAME.jar "; //edit here for mapmaker
    char * startstr = malloc(strlen(argv[0]) - strlen(findstr) + strlen(replacestr));
    char * ptr;

    strcpy(startstr, argv[0]);
    ptr = strstr(startstr, findstr);
    if(ptr)
    {
        memmove(ptr + strlen(replacestr), ptr + strlen(findstr), strlen(ptr + strlen(findstr)) + 1);
        strncpy(ptr, replacestr, strlen(replacestr));
    }
    printf("foobar %s\n", startstr);

    char * cdreplacestr = "/pkg";
    char * cdstr = malloc(strlen("cd ") + strlen(argv[0]) - strlen(findstr) + strlen("/pkg"));
    char * cptr;

    strcpy(cdstr, "cd ");
    strcat(cdstr, argv[0]);
    cptr = strstr(cdstr, findstr);
    if(cptr) {
        memmove(cptr + strlen(cdreplacestr), cptr + strlen(findstr), strlen(cptr + strlen(findstr)) + 1);
        strncpy(cptr, cdreplacestr, strlen(cdreplacestr));
    }
    printf("foobar %s\n", cdstr);

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
    printf("%s\n", runstr);
    system("cd ~/Code/Ball-Game/pkg && ~/Code/Ball-Game/bin/jdk-18.jdk/Contents/Home/bin/java -Dsun.java2d.uiScale=1.0 -jar ~/Code/Ball-Game/pkg/BALL_GAME.jar");
//    free(runstr);
    return 0;
}