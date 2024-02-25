set call_dir=%cd%
set home_dir=C:\Code\Ball-Game
set manifest_path=%home_dir%\debug\MANIFEST.MF
set out_dir=%home_dir%\pkg
set src_dir=%home_dir%\src
set java_dir=%home_dir%\bin\jdk-20.0.1

%java_dir%\bin\javac -d %out_dir% %src_dir%\*.java
cd %out_dir%
%java_dir%\bin\jar cmf %manifest_path% BALL_GAME.jar *.class
del *.class
cd %call_dir%