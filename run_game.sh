cd "$(cd $(dirname "$0");pwd)"/pkg
../bin/jdk-18.jdk/Contents/Home/bin/java -Dsun.java2d.uiScale=1.0 -jar BALL_GAME.jar "$@"
