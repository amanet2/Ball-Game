cd "$(cd $(dirname "$0");pwd)"
export PATH="$(cd $(dirname "$0");pwd)"/../bin/jdk-18.jdk/Contents/Home/bin/:"$(cd $(dirname "$0");pwd)"/../bin/javafx-sdk-18/lib/:${PATH}
java -Dsun.java2d.uiScale=1.0 -jar --module-path "$(cd $(dirname "$0");pwd)"/../bin/javafx-sdk-18/lib --add-modules=javafx.media BALL_GAME.jar "$@"
exit