@echo off
cd %~dp0
set PATH=%~dp0..\bin\jdk-14.0.1\bin\;%~dp0..\bin\javafx-sdk-11.0.2\bin\
start javaw -Dsun.java2d.uiScale=1.0 -jar --module-path ..\lib --add-modules=javafx.media BALL_GAME.jar %*
exit
