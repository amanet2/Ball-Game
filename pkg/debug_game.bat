@echo off
cd %~dp0
set PATH=%~dp0..\bin\jdk-18.0.2.1\bin\;%~dp0..\bin\javafx-sdk-11.0.2\bin\
java -Dsun.java2d.uiScale=1.0 -jar --module-path ..\lib --add-modules=javafx.media BALL_GAME.jar %*
exit
