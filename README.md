## Docker简介

​	[Docker](https://docs.docker.com/) 是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的镜像中，然后发布到任何流行的 Linux或Windows操作系统的机器上，也可以实现虚拟化。容器是完全使用沙箱机制，相互之间不会有任何接口。

## Docker安装

#### 1、设置yum仓库

```shell
yum install -y yum-utils
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

#### 2、更新yum索引

```shell
yum makecache fast
```

#### 3、安装docker

```shell
yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

#### 4、启动docker

```shell
systemctl start docker
systemctl status docker
docker version
docker run hello-world
systemctl stop docker
```

#### 5、阿里镜像加速

（1）创建文件 /etc/docker/daemon.json

```json
{
	"registry-mirrors": ["https://oo94bfkd.mirror.aliyuncs.com"]
}
```


（2）重新加载docker配置

```shell
systemctl daemon-reload
systemctl restart docker
```

#### 6、数据存储目录

```
/var/lib/docker
/var/lib/containerd
```



## Docker操作

#### 1、镜像操作

```shell
docker images						查看镜像
docker search --limit 5 redis		  搜索镜像
docker pull redis:6.0.8				 下载镜像
docker system df					查看占用
docker rmi -f 						删除镜像
docker rmi -f $(docker images -q)	 删除所有镜像
```

仓库名和标签都是<none>的镜像为虚悬镜像,建议删除

#### 2、容器操作

```shell
docker run							启动新的容器
	--name="容器名称"
	-d 后台运行
	-i -t 交互模式,重新分配输入终端
	-P 随机端口映射
	-p 指定端口映射
	bash
	-itd 后台运行，ctrl+p+q后台运行
docker ps							列出运行中容器
	-a 包括历史容器
	-l 最近创建容器
	-q 静默,只显示编号
	-n 指定数量
docker start						启动停止的容器
docker restart						重启容器
docker stop							停止容器
docker kill			 				强制停止容器
docker rm  							删除容器
docker rm -f $(docker ps -a -q)		删除所有容器
docker ps -a -q | xargs docker rm
docker logs							查看控制台输出
	-f 追踪打印
	-n 指定数量
	-t 显示时间戳
docker top							查看容器内进程
dicker inspect						查看容器详情
docker exec							进入容器
	-i -t 交互模式
	bash
docker attach						进入容器
docker cp 容器ID:/home/a.txt /opt/	导出文件
docker export 容器ID > a.tar 		导出容器为文件
docker import 						导入文件为镜像
docker commit						直接导入镜像
docker commit -m='vim cmd add ok' -a='lain' 容器ID lain/name:1.2
```

#### 3、数据卷操作

```shell
--privileged=true			挂载权限，授予root权限
docker run -it --privileged=true -v /宿主机绝对路径目录:/容器内目录:rw 镜像名
rw 							可读可写，默认rw
docker run -it --privileged=true --volume-from 容器名称 镜像名
```



## Docker仓库

#### 1、阿里仓库

（1）从Registry中拉取镜像

```shell
docker pull registry.cn-hangzhou.aliyuncs.com/lain_rs/myubuntu:[镜像版本号]
```

（2）将镜像推送到Registry

```shell
docker login --username=13272734623 registry.cn-hangzhou.aliyuncs.com
docker tag [ImageId] registry.cn-hangzhou.aliyuncs.com/lain_rs/myubuntu:[镜像版本号]
docker push registry.cn-hangzhou.aliyuncs.com/lain_rs/myubuntu:[镜像版本号]
```

#### 2、私有仓库

（1）启动仓库

```shell
docker run -d -p 5000:5000 --privileged=true registry
```

（2）查看仓库数据

```shell
curl -X GET http://172.17.16.10:5000/v2/_catalog
```

（3）复制+改名镜像

```
docker tag myubuntu:1.0 172.17.16.10:5000/myubuntu:1.2
```

（4）修改/etc/docker/daemon.json，取消限制

```json
{
	"insecure-registries":["172.17.16.10:5000"]
}
```

（5）推送镜像到私服仓库

```shell
docker push 172.17.16.10:5000/myubuntu:1.0
```

（6）重新下载镜像

```shell
docker pull 172.17.16.10:5000/myubuntu:1.0
```



## 常规软件安装

#### 1、MySQL

```shell
docker pull mysql:5.7
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7
docker run -d -p 3306:3306 --privileged=true
	-v /opt/mysql/log:/var/log/mysql
	-v /opt/mysql/data:/var/lib/mysql
	-v /opt/mysql/conf:/etc/mysql/conf.d
	-e MYSQL_ROOT_PASSWORD=123456 
	--name mysql mysql:5.7
```

添加配置文件my.cnf

```shell
[client]
default-character-set=utf8
[mysqld]
character-set-server=utf8
```

#### 2、Redis

```shell
docker pull redis:6.0.8
docker run -d -p 6379:6379 redis:6.0.8
docker run -d -p 6379:6379 --privileged=true
	-v /opt/redis/redis.conf:/etc/redis/redis.conf
	-v /opt/redis/data:/data
	--name redis redis:6.0.8 redis-server /etc/redis/redis.conf
```

添加配置文件redis.conf

1. 运行外网访问
2. 关闭后台模式
3. 关闭保护模式

#### 3、Redis三主三从

```shell
docker run -d --name redis-node-1 --net host --privileged=true -v /opt/redis/share/redis-node-1:/data redis:6.0.8 --cluster-enabled yes --appendonly yes --port 6381
docker run -d --name redis-node-2 --net host --privileged=true -v /opt/redis/share/redis-node-2:/data redis:6.0.8 --cluster-enabled yes --appendonly yes --port 6382
docker run -d --name redis-node-3 --net host --privileged=true -v /opt/redis/share/redis-node-3:/data redis:6.0.8 --cluster-enabled yes --appendonly yes --port 6383
docker run -d --name redis-node-4 --net host --privileged=true -v /opt/redis/share/redis-node-4:/data redis:6.0.8 --cluster-enabled yes --appendonly yes --port 6384
docker run -d --name redis-node-5 --net host --privileged=true -v /opt/redis/share/redis-node-5:/data redis:6.0.8 --cluster-enabled yes --appendonly yes --port 6385
docker run -d --name redis-node-6 --net host --privileged=true -v /opt/redis/share/redis-node-6:/data redis:6.0.8 --cluster-enabled yes --appendonly yes --port 6386
```

（1）.进入redis容器执行

```shell
redis-cli --cluster create 192.168.119.128:6381 192.168.119.128:6382 192.168.119.128:6383 192.168.119.128:6384 192.168.119.128:6385 192.168.119.128:6386 --cluster-replicas 1
```

（2）查看集群状态

```shell
redis-cli -p 6381
cluster info
cluster nodes
redis-cli -p 6381 -c 		进入集群
```

（3）集群扩容

```shell
docker run -d --name redis-node-7 --net host --privileged=true -v /opt/redis/share/redis-node-7:/data redis:6.0.8 --cluster-enabled yes --appendonly yes --port 6387
docker run -d --name redis-node-8 --net host --privileged=true -v /opt/redis/share/redis-node-8:/data redis:6.0.8 --cluster-enabled yes --appendonly yes --port 6388
redis-cli --cluster add-node 192.168.119.128:6387 192.168.119.128:6381	加入集群
redis-cli --cluster check 192.168.119.128:6381							检查集群
redis-cli --cluster reshard 192.168.119.128:6381						重新分配哈希槽
redis-cli --cluster add-node 192.168.119.128:6388 192.168.119.128:6387 --cluster-slave --cluster-master-id 6387的ID
```

（4）集群缩容

```shell
redis-cli --cluster del-node 192.168.119.128:6388 6388的ID
redis-cli --cluster reshard 192.168.119.128:6381 					重新分配哈希槽
redis-cli --cluster del-node 192.168.119.128:6387 6387的ID
```



## Dockerfile

dockerfile同目录使用命令

```shell
docker build -t 新镜像名字：TAG .
```



## Docker网络

```shell
docker network ls
docker network create aa
docker network rm aa
docker network inspect id
--network host 使用主机的IP
--network bridge 重新分配一个IP
--network container:id 使用其他容器的IP
```



## Docker-compose

安装

```shell
curl -SL https://github.com/docker/compose/releases/download/v2.7.0/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
```

使用

```shell
docker-compose config -q 检查语法
docker-compose up -d     启动
docker-compose down      关闭
```

