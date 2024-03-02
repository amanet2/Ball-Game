cat <<EOF > $echo_c_file
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char * argv[]) {
    char * findstr = "$echo_c_find_str"; // for regular
    char * cdreplacestr = "$echo_c_replace_str";
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
EOF