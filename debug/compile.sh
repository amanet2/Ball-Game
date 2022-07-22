export PATH="$(cd $(dirname "$1");pwd)"/../bin/jdk-18.jdk/Contents/Home/bin/:"$(cd $(dirname "$1");pwd)"/../bin/javafx-sdk-11.0.2/lib/:${PATH}
javac --module-path "$(cd $(dirname "$1");pwd)"/../lib --add-modules=javafx.media -d "$(cd $(dirname "$1");pwd)"/../pkg "$(cd $(dirname "$1");pwd)"/../src/*.java
