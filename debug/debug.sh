cd "$(cd $(dirname "$1");pwd)"/../pkg
export PATH="$(cd $(dirname "$1");pwd)"/../bin/jdk-18.jdk/Contents/Home/bin/:"$(cd $(dirname "$1");pwd)"/../bin/javafx-sdk-18/lib/:${PATH}
java --module-path "$(cd $(dirname "$1");pwd)"/../lib --add-modules=javafx.media --class-path "$(cd $(dirname "$1");pwd)"/../lib:"$(cd $(dirname "$1");pwd)"/../pkg -Dsun.java2d.dpiaware=false xMain "$@"
#java --class-path "$(cd $(dirname "$1");pwd)"/../lib:"$(cd $(dirname "$1");pwd)"/../pkg  -Dsun.java2d.uiScale=1 xMain "$@"
cd "$(cd $(dirname "$1");pwd)"
