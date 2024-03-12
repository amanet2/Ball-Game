# build jar
build_script=~/Code/Ball-Game/debug/build_mac.sh
bash $build_script

# set variables for use after this point
home_dir=~/Code/Ball-Game
tmp_dir=$home_dir/tmp
out_dir=~/Code/pkg_ballmaster_mac
out_zip=~/Code/pkg_ballmaster_mac.zip
pkg_dir=$home_dir/pkg
java_dir=$home_dir/runtime
echo_c_script=$home_dir/debug/echo_c_mac.sh
echo_c_find_str="/ballmaster_mac"
echo_c_replace_str="/pkg && ../bin/jdk-18.jdk/Contents/Home/bin/java -Dsun.java2d.uiScale=1.0 -Dsun.java2d.opengl=true -jar BALL_GAME.jar "
echo_c_find_str_editor="/ballmaster_mac_editor"
echo_c_replace_str_editor="/pkg && ../bin/jdk-18.jdk/Contents/Home/bin/java -Dsun.java2d.uiScale=1.0 -Dsun.java2d.opengl=true -jar BALL_GAME.jar showmapmakerui 1 "
gcc_exe=gcc
game_c=$tmp_dir/ballmaster_mac.c
game_editor_c=$tmp_dir/ballmaster_mac_editor.c
game_exe=$tmp_dir/ballmaster_mac
game_editor_exe=$tmp_dir/ballmaster_mac_editor
readme_path=$home_dir/Readme.txt

# cleanup before starting
for FOLDER in $out_dir $tmp_dir; do rm -rf $FOLDER; done
rm -f $out_zip

# create C executables
mkdir $tmp_dir
bash $echo_c_script $echo_c_find_str $echo_c_replace_str $game_c
bash $echo_c_script $echo_c_find_str_editor $echo_c_replace_str_editor $game_editor_c
$gcc_exe -o $game_exe $game_c
$gcc_exe -o $game_editor_exe $game_editor_c

# copy files to output dir
mkdir $out_dir
for FOLDER in $java_dir $pkg_dir; do cp $FOLDER $out_dir; done # TODO: this corrupts java somehow
for FILE in $readme_path $game_exe $game_editor_exe; do cp $FILE $out_dir; done

# zip up finished output
zip -r $out_zip $out_dir

# clean up temp files
for FOLDER in $out_dir $tmp_dir; do rm -rf $FOLDER; done
