cd %~dp0..\pkg
set PATH=%~dp0..\bin\jdk-14.0.1\bin\;%~dp0..\bin\javafx-sdk-11.0.2\bin\
java --module-path %~dp0..\lib --add-modules=javafx.media --class-path %~dp0..\lib;%~dp0..\pkg -Dsun.java2d.uiScale=1 xMain %*
cd %~dp0
