export PATH="$(cd $(dirname "$1");pwd)"/../bin/jdk-18.jdk/Contents/Home/bin/:"$(cd $(dirname "$1");pwd)"/../bin/javafx-sdk-18/lib/:${PATH}
javac --module-path "$(cd $(dirname "$1");pwd)"/../bin/javafx-sdk-18/lib --add-modules=javafx.media -d "$(cd $(dirname "$1");pwd)"/../pkg "$(cd $(dirname "$1");pwd)"/../src/*.java
cd "$(cd $(dirname "$1");pwd)"/../pkg
jar cmf MANIFEST.MF BALL_GAME.jar *.class
rm *.class
cd "$(cd $(dirname "$1");pwd)"
