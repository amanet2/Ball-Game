home_dir=~/Code/Ball-Game
java_exec=$home_dir/bin/jdk-18.jdk/Contents/Home/bin/java
pkg_dir=$home_dir/pkg

cd $pkg_dir
$java_exec -Dsun.java2d.uiScale=1.0 -Dsun.java2d.opengl=true -jar $pkg_dir/BALL_GAME.jar "$@"
cd "$(cd $(dirname "$1");pwd)"