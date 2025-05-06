set home_dir=C:\Code\Ball-Game
set manifest_path=%home_dir%\debug\MANIFEST.MF
set tmp_dir=%home_dir%\tmp
set java_src=%home_dir%\src\*.java
set java_bin=%home_dir%\runtime\jdk-23.0.2\bin
set jar_path=%home_dir%\pkg\BALL_GAME.jar

REM clean up any existing files
if exist %tmp_dir% rmdir /s /q %tmp_dir%
if exist %jar_path% del %jar_path%

REM create temp directory and compile java class files
mkdir %tmp_dir%
%java_bin%\javac -d %tmp_dir% %java_src%

REM generate jar file
REM note that the -C file for full path to .class files only works with the value "."
%java_bin%\jar cmf %manifest_path% %jar_path% -C %tmp_dir% .

REM cleanup temp files
rmdir /s /q %tmp_dir%
