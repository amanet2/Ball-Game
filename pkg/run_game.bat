@echo off
cd %~dp0
start %~dp0..\bin\jdk-20.0.1\bin\javaw -Dsun.java2d.uiScale=1.0 -jar --module-path %~dp0..\bin\javafx-sdk-11.0.2\lib --add-modules=javafx.media BALL_GAME.jar %*
exit
