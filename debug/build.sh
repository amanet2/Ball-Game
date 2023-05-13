"$(cd $(dirname "$1");pwd)"/../bin/jdk-18.jdk/Contents/Home/bin/javac --module-path "$(cd $(dirname "$1");pwd)"/../bin/javafx-sdk-18/lib --add-modules=javafx.media -d "$(cd $(dirname "$1");pwd)"/../pkg "$(cd $(dirname "$1");pwd)"/../src/*.java
cd "$(cd $(dirname "$1");pwd)"/../pkg
"$(cd $(dirname "$1");pwd)"/../bin/jdk-18.jdk/Contents/Home/bin/jar cmf "$(cd $(dirname "$1");pwd)"/../debug/MANIFEST.MF "$(cd $(dirname "$1");pwd)"/../pkg/BALL_GAME.jar *.class
rm "$(cd $(dirname "$1");pwd)"/../pkg/*.class
cd "$(cd $(dirname "$1");pwd)"
