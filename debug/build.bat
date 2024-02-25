set call_dir=%cd%
set home_dir=C:\Code\Ball-Game
set manifest_path=%home_dir%\debug\MANIFEST.MF
set out_dir=%home_dir%\pkg
set tmp_dir=%home_dir%\tmp
set java_src=%home_dir%\src\*.java
set java_bin=%home_dir%\bin\jdk-20.0.1\bin

REM create temp directory and compile java class files
if exist %tmp_dir% rmdir /s /q %tmp_dir%
mkdir %tmp_dir%
%java_bin%\javac -d %tmp_dir% %java_src%

REM generate jar file
%java_bin%\jar cmf %manifest_path% %out_dir%\BALL_GAME.jar -C %tmp_dir% .

REM cleanup temp files
rmdir /s /q %tmp_dir%
