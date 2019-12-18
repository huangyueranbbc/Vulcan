modules="Vulcan_Server Vulcan_Runner"

version=$(cat version.txt)

dateversion=`date +%Y%m%d`

# git版本号
vcs_latest_id=`git rev-parse HEAD`
vcs_latest_short_id=`git rev-parse --short HEAD`

# 添加版本号到版本信息文件
for i in $modules;
do
    if [ -e $i/src/main/resources/version_*.txt ];then
      rm -f $i/src/main/resources/version_*.txt
    fi
    echo version: $version datetime: `date '+%Y-%m-%d %H:%M:%S'` vcs-version: $vcs_latest_id > $i/src/main/resources/version_info.txt
done

# 打包
mvn clean package -Dmaven.test.skip=true

# 打包结果放入指定的目录
rm -rf build
mkdir build

for i in $modules;
do
    if [ -f "$i/target/$i.jar" ];then
       mkdir build/$i
       cp $i/target/$i.jar build/$i/
       cp readme.md build/$i/
       cp release-notes.md build/$i/
       tar -cvf build/$i-$vcs_latest_short_id-$version.zip build/$i/*
    fi
done

cp -r bin build/

cp -r conf build/

# 删除版本号
for i in $modules;
do
    if [ -e $i/src/main/resources/version_*.txt ];then
      rm -f $i/src/main/resources/version_*.txt
    fi
done
