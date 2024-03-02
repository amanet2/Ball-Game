home_dir=~/Code/Ball-Game
java_exec=$home_dir/bin/jdk-18.jdk/Contents/Home/bin/java
java_args="-Dsun.java2d.uiScale=1.0 -Dsun.java2d.opengl=true"
pkg_dir=$home_dir/pkg
jar_path=$pkg_dir/BALL_GAME.jar

cd $pkg_dir
$java_exec $java_args -jar $jar_path "$@"
