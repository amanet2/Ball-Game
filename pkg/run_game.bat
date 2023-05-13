@echo off
start %~dp0..\bin\jdk-20.0.1\bin\javaw -Dsun.java2d.uiScale=1.0 -jar %~dp0BALL_GAME.jar %*
