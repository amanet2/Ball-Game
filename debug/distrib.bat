REM run build process to generate jar
set build_script=C:\Code\Ball-Game\debug\build.bat
call %build_script%

REM we have to set our variables here or they will be overwritten by previously called script
set home_dir=C:\Code\Ball-Game
set tmp_dir=%home_dir%\tmp
set out_dir=C:\Code\pkg_ballmaster
set out_zip=C:\Code\pkg_ballmaster.zip
set pkg_prefix=pkg
set pkg_dir=%home_dir%\%pkg_prefix%
set java_prefix=bin\jdk-20.0.1
set java_dir=%home_dir%\%java_prefix%
set gcc_exe=C:\mingw64\bin\gcc.exe
set game_c=%tmp_dir%\ballmaster.c
set game_editor_c=%tmp_dir%\ballmaster_editor.c
set game_exe=%tmp_dir%\ballmaster.exe
set game_editor_exe=%tmp_dir%\ballmaster_editor.exe
set readme_path=%home_dir%\Readme.txt

REM cleanup existing files
(for %%f in (%out_dir% %tmp_dir%) do (if exist %%f rmdir /s /q %%f))
(for %%f in (%out_zip% %game_c% %game_editor_c% %game_exe% %game_editor_exe%) do (if exist %%f del %%f))

REM create executables
mkdir %tmp_dir%
set echo_c_script=%home_dir%\debug\echo_c.bat
set echo_c_start_str="start ..\\bin\\jdk-20.0.1\\bin\\javaw -Dsun.java2d.uiScale=1.0 -Dsun.java2d.opengl=true -jar BALL_GAME.jar "
set echo_c_file="%game_c%"
call %echo_c_script%
set echo_c_start_str="start ..\\bin\\jdk-20.0.1\\bin\\javaw -Dsun.java2d.uiScale=1.0 -Dsun.java2d.opengl=true -jar BALL_GAME.jar showmapmakerui 1 "
set echo_c_file="%game_editor_c%"
call %echo_c_script%
%gcc_exe% -o %game_exe% %game_c%
%gcc_exe% -o %game_editor_exe% %game_editor_c%

REM copy files to output dir
mkdir %out_dir%
xcopy /y /e /i %java_dir% %out_dir%\%java_prefix%
xcopy /y /e /i %pkg_dir% %out_dir%\%pkg_prefix%
(for %%f in (%readme_path% %game_exe% %game_editor_exe%) do (xcopy /y %%f %out_dir%))

REM zip up output
powershell.exe Compress-Archive -Path %out_dir%\* -DestinationPath %out_zip%

REM cleanup temp files
(for %%f in (%out_dir% %tmp_dir%) do (if exist %%f rmdir /s /q %%f))
(for %%f in (%game_c% %game_editor_c% %game_exe% %game_editor_exe%) do (if exist %%f del %%f))
