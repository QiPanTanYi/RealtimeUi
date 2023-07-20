# 实时交易数据计算统计可视化

### 题要

​        模拟消费者生产商品订单数据，使用kafka接收数据，Spark Streaming计算并统计数据，数据库采用了Redis，同时使用了WebSocket传输数据到前端，采用JSP及Tomcat服务器搭建可视化页面，以Echarts/JQuery辅助页面开发。



### 配置要求

这里只是我个人的开发环境：

本地配置：JDK1.8、Tomcat10.0.22、Scala 2.11.8

集群环境（CentOS 6.8）：Hadoop 2.7.4、Zookeeper 3.4.10、Spark 2.3.2、Kafka 2.11、Redis 3.2.8

![p1](.\Document\p1.png)



### 部署前置工作

如果你也想运行此仓库的代码，别急别急，一步步来，首先是本地IDEA的部署：

1.安装Smart Tomcat插件（Tomcat 源码包需要到官网下载 [Apache Tomcat®](https://tomcat.apache.org/ )）

![p2](.\Document\p2.png)

2.配置Tomcat服务

![p3](.\Document\p3.png)



### 运行步骤

1.优先启动Hadoop，Zookeeper，Redis。

2.在Kafka文件目录下，启动命令：

> bin/kafka-server-start.sh config/server.properties        # 启动Kafka服务，需要在三台集群机上都启动

> kafka-topics.sh --create \                           # 创建Topic，以下内容只需要在一台机子运行即可
>
> --topic itcast_order \
> --partitions 3 \
> --replication-factor 2 \
> --zookeeper hadoop01:2181,hadoop02:2181,hadoop03:2181

> kafka-console-consumer.sh \                                                     # 监听数据
>
> --from-beginning --topic itcast_order \
> --bootstrap-server hadoop01:9092,hadoop02:9092,hadoop03:9092

3.运行 PaymentInfoProducer.java (模拟生产数据)

![p4](.\Document\p4.png)

回到Kafka中，可以看到以下信息：

![p5](.\Document\p5.png)

4.运行StreamingProcessdata.scala（计算并统计数据）

![p6](.\Document\p6.png)

5.启动本地Tomcat（提供Web环境）

![p7](.\Document\p7.png)

![p7](.\Document\p8.png)



### 可视化结果

根据Tomcat日志提供的本地Web地址，在浏览器中输入 “ http://locathost:8080/RealtimeUi/index ”  , 可以看到对应的实时运算统计结果：

![p9](.\Document\p9.png)

