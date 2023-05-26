out_dir="$(cd $(dirname "$1");pwd)"/../pkg
"$(cd $(dirname "$1");pwd)"/../bin/jdk-18.jdk/Contents/Home/bin/javac -d $out_dir "$(cd $(dirname "$1");pwd)"/../src/*.java
cd $out_dir
../bin/jdk-18.jdk/Contents/Home/bin/jar cmf ../debug/MANIFEST.MF BALL_GAME.jar *.class
rm *.class
cd "$(cd $(dirname "$1");pwd)"
