set PATH=%~dp0..\bin\jdk-14.0.1\bin\;%~dp0..\bin\javafx-sdk-11.02\bin\
javac --module-path %~dp0..\lib --add-modules=javafx.media -d %~dp0..\pkg %~dp0..\src\*.java
pause
