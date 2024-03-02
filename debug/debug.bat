set call_dir=%cd%
set home_dir=C:\Code\Ball-Game
set java_exec=%home_dir%\bin\jdk-20.0.1\bin\java
set java_args=-Dsun.java2d.uiScale=1.0 -Dsun.java2d.opengl=true
set pkg_dir=%home_dir%\pkg
set jar_path=%pkg_dir%\BALL_GAME.jar

cd %pkg_dir%
%java_exec% %java_args% -jar %jar_path% %*
cd %call_dir%
