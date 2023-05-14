#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char * argv[]) {
    int argmalloc = 0;
    char * startstr = "start ..\\bin\\jdk-20.0.1\\bin\\javaw -Dsun.java2d.uiScale=1.0 -jar BALL_GAME.jar ";
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
    chdir("pkg");
    system(runstr);
    return 0;
}