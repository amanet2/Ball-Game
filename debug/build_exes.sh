cd "$(cd $(dirname "$1");pwd)"/..
gcc Ball-Game-Mac.c -o Ball-Game-Mac
gcc Ball-Game-Mapmaker-Mac.c -o Ball-Game-Mapmaker-Mac
cd "$(cd $(dirname "$1");pwd)"
