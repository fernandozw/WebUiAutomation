echo "开始关闭进程:"
project_path=/usr/local/zhuwei/javaWorkspace
project_name=WebUiAutomation
jar_name=WebUiAutomation
port='9998'
pid=`netstat -anp|grep $port|awk '{printf $7}'|cut -d/ -f1`
if test -z $pid
then
    echo -e "\033[42m当前端口${port}没有被占用!\033[0m"
else
    echo -e "\033[47;31m当前端口${port}被进程${pid}占用!\033[0m"
    kill -9 ${pid}
 fi
 cd $project_path
 rm -rf $project_name
 git clone ssh://git@139.224.169.199:26662/zhuwei/webuiautomation.git
 cd $project_name
 mvn clean package
 cd target
 nohup java -jar ${jar_name}.jar > /dev/null 2>&1 &
 echo -e "\033[42;31m项目启动完成!\033[0m"