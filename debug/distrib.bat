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
set java_prefix=runtime\jdk-23
set java_dir=%home_dir%\%java_prefix%
set echo_c_script=%home_dir%\debug\echo_c.bat
set echo_c_start_str="start ..\\runtime\\jdk-23\\bin\\javaw -Dsun.java2d.uiScale=1.0 -Dsun.java2d.opengl=true -jar BALL_GAME.jar "
set echo_c_start_str_mapmaker="start ..\\runtime\\jdk-23\\bin\\javaw -Dsun.java2d.uiScale=1.0 -Dsun.java2d.opengl=true -jar BALL_GAME.jar showmapmakerui 1 "
set echo_c_outfile=%tmp_dir%\ballmaster.c
set echo_c_outfile_mapmaker=%tmp_dir%\ballmaster_editor.c
set gcc_exe=C:\mingw64\bin\gcc.exe
set game_exe=%tmp_dir%\ballmaster.exe
set game_editor_exe=%tmp_dir%\ballmaster_editor.exe
set readme_path=%home_dir%\Readme.txt

REM cleanup existing files
(for %%f in (%out_dir% %tmp_dir%) do (if exist %%f rmdir /s /q %%f))
if exist %out_zip% del %out_zip%

REM create executables
mkdir %tmp_dir%
call %echo_c_script% %echo_c_start_str% %echo_c_outfile%
call %echo_c_script% %echo_c_start_str_mapmaker% %echo_c_outfile_mapmaker%
%gcc_exe% -o %game_exe% %echo_c_outfile%
%gcc_exe% -o %game_editor_exe% %echo_c_outfile_mapmaker%

REM copy files to output dir
mkdir %out_dir%
xcopy /y /e /i /Q %java_dir% %out_dir%\%java_prefix%
xcopy /y /e /i /Q %pkg_dir% %out_dir%\%pkg_prefix%
(for %%f in (%readme_path% %game_exe% %game_editor_exe%) do (xcopy /y %%f %out_dir%))

REM zip up output
powershell.exe Compress-Archive -Path %out_dir%\* -DestinationPath %out_zip%

REM cleanup temp files
(for %%f in (%out_dir% %tmp_dir%) do (if exist %%f rmdir /s /q %%f))
