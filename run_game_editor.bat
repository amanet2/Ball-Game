@echo off
cd %~dp0pkg
start ..\bin\jdk-20.0.1\bin\javaw -Dsun.java2d.uiScale=1.0 -jar BALL_GAME.jar %* showmapmakerui 1
