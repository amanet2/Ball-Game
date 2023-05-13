cd %~dp0..\pkg
%~dp0..\bin\jdk-18.0.2.1\bin\java -Dsun.java2d.uiScale=1.0 -jar --module-path %~dp0..\bin\javafx-sdk-11.0.2\lib --add-modules=javafx.media BALL_GAME.jar %*
