set call_dir=%cd%
set home_dir=C:\Code\Ball-Game
set java_exec=%home_dir%\bin\jdk-20.0.1\bin\java
set pkg_dir=%home_dir%\pkg

cd %pkg_dir%
%java_exec% -Dsun.java2d.uiScale=1.0 -Dsun.java2d.opengl=true -jar %pkg_dir%\BALL_GAME.jar %*
cd %call_dir%
