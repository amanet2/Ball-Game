home_dir=~/Code/Ball-Game
manifest_path=$home_dir/debug/MANIFEST.MF
tmp_dir=$home_dir/tmp
java_src=$home_dir/src/
java_bin=$home_dir/runtime/jdk-18.jdk/Contents/Home/bin
jar_path=$home_dir/pkg/BALL_GAME.jar

# clean up existing files
rm -rf $tmp_dir
rm -f $jar_path

# create temp directory
mkdir $tmp_dir
$java_bin/javac -d $tmp_dir $java_src/*.java # need the filename pattern here

# generate jar file
$java_bin/jar cmf $manifest_path $jar_path -C $tmp_dir .

# cleanup tmp files
rm -r $tmp_dir
