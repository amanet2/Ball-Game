(
echo #include ^<stdlib.h^>
echo #include ^<string.h^>
echo #include ^<unistd.h^>
echo int main^(int argc, char * argv^[^]^) {
echo     int argmalloc = 0;
echo     char * startstr = %1; // for editor
echo     int i;
echo     for^(i = 1; i ^< argc; i++^) {
echo         argmalloc += sizeof^(char^)*strlen^(argv^[i^]^);
echo         if^(i ^< argc-1^)
echo             argmalloc++; //for the space
echo     }
echo     char runstr^[sizeof^(char^)*strlen^(startstr^) + argmalloc^];
echo     strcpy^(runstr, startstr^);
echo     for^(i = 1; i ^< argc; i++^) {
echo         strcat^(runstr, argv^[i^]^);
echo         if^(i ^< argc-1^)
echo             strcat^(runstr, " "^);
echo     }
echo     chdir^("pkg"^);
echo     system^(runstr^);
echo     return 0;
echo }
)>"%2"