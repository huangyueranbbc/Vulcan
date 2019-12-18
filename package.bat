@rem TODO windows环境还未测试 需要请自行调试

set modules=Vulcan_Server,Vulcan_Runner

set /P version=<version.txt

@rem git版本号
set vcs_latest_id=`git rev-parse HEAD`
set vcs_latest_short_id=`git rev-parse --short HEAD`

set dateversion=%date:~0,4%%date:~5,2%%date:~8,2%
set baseDir=%~dp0

@rem 删除版本号文件
for %%i in (%modules%) do if exist %%i\src\main\resources\version_*.txt del /s /q %%i\src\main\resources\version_*.txt
@rem 添加版本号到版本信息文件
for %%i in (%modules%) do echo version: %version% datetime: %date% %time% vcs-version: %vcs_latest_id% >  %%i\src\main\resources\version_info.txt

@rem 执行maven打包
call mvn clean package -Dmaven.test.skip=true

@rem 打包结果放入指定的目录
rd /s /q build
md build

for %%i in (%modules%);
do
    if exist %%i\target\%%i.jar (
        md build\%%i
        copy %%i\target\%%i.jar build\%%i\
        copy release-notes.md build\%%i\
        copy readme.md build\%%i\
        7z.exe a -tzip build\%%i-%vcs_latest_short_id%-%version%.zip %baseDir%build\%%i\*
    )

copy bin\ build\

copy conf\ build\

@rem 删除版本号
for %%i in (%modules%) do if exist %%i\src\main\resources\version_*.txt del /s /q %%i\src\main\resources\version_*.txt
