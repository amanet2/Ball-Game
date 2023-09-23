set home_dir=C:\Code\Ball-Game
set out_dir=C:\Code\pkg_ballmaster
set debug_dir=%home_dir%\debug
set pkg_dir=%home_dir%\pkg
set src_dir=%home_dir%\src
set java_prefix=bin\jdk-20.0.1
set java_dir=%home_dir%\%java_prefix%

REM build jar
%java_dir%\bin\javac -d %pkg_dir% %src_dir%\*.java
cd %pkg_dir%
%java_dir%\bin\jar cmf %debug_dir%\MANIFEST.MF BALL_GAME.jar *.class
del *.class
cd %~dp0

REM create archive
if exist %out_dir% rmdir /s /q %out_dir%
mkdir %out_dir%
xcopy /y /e /i %java_dir% %out_dir%\%java_prefix%
xcopy /y /e /i %pkg_dir% %out_dir%\pkg
xcopy /y %home_dir%\Readme.txt %out_dir%
xcopy /y %home_dir%\ballmaster.exe %out_dir%
xcopy /y %home_dir%\ballmaster_editor.exe %out_dir%
if exist %out_dir%.zip del %out_dir%.zip
powershell.exe Compress-Archive -Path %out_dir%\* -DestinationPath %out_dir%.zip