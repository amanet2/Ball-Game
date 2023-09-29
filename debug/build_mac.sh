home_dir=~/Code/Ball-Game
manifest_path=$home_dir/debug/MANIFEST.MF
out_dir=$home_dir/pkg
src_dir=$home_dir/src
java_dir=$home_dir/bin/jdk-18.jdk/Contents/Home

$java_dir/bin/javac -d $out_dir $src_dir/*.java
cd $out_dir
$java_dir/bin/jar cmf $manifest_path BALL_GAME.jar *.class
rm *.class
cd "$(cd $(dirname "$1");pwd)"