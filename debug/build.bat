set home_dir=C:\Code\Ball-Game
set debug_dir=%home_dir%\debug
set out_dir=%home_dir%\pkg
set src_dir=%home_dir%\src
set java_dir=%home_dir%\bin\jdk-20.0.1

%java_dir%\bin\javac -d %out_dir% %src_dir%\*.java
cd %out_dir%
%java_dir%\bin\jar cmf %debug_dir%\MANIFEST.MF BALL_GAME.jar *.class
del *.class
cd %~dp0