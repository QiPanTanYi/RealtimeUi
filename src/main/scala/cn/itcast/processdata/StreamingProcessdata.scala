package cn.itcast.processdata

import redis.clients.jedis.Jedis
import kafka.serializer.StringDecoder
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object StreamingProcessdata {
  // 每件商品总销售额
  val orderTotalKey = "bussiness::order::total"
  // 总销售额
  val totalKey = "bussiness::order::all"
  // Redis数据库
  val dbIndex = 0

  def main(args: Array[String]): Unit = {
    // 1.创建SparkConf对象
    val sparkCont: SparkConf = new SparkConf()
      .setAppName("KafkaStreamingTest")
      .setMaster("local[4]")

    // 2.创建SparkContext对象
    val sc = new SparkContext(sparkCont)
    sc.setLogLevel("WARN")

    // 3.构建StreamingContext对象
    val ssc = new StreamingContext(sc, Seconds(3))

    // 4.消息的偏移量会被写入到checkpoint中,并设置Kafka参数
    ssc.checkpoint("./spark-receiver")
    val kafkaParams = Map("bootstrap.servers" -> "hadoop01:9092,hadoop02:9092,hadoop03:9092",
    "group.id" -> "spark-receiver")

    // 5.指定Topic相关信息
    val topics = Set("itcast_order")

    // 6.通过kafkaUile.createDirectStream,利用低级api接受kafka数据
    val kafkaDstream: InputDStream[(String,String)] =
      KafkaUtils.createDirectStream
        [String, String,StringDecoder,StringDecoder](ssc, kafkaParams, topics)

    // 7.获取Kafka中Topic数据，并解析JSON格式数据
    val events: DStream[JSONObject] = kafkaDstream.flatMap(line => Some(JSON.parseObject(line._2)))

    // 按照productID进行分组统计个数和总价格
    val orders: DStream[(String, Int, Long)] = events.map(x => (x.getString("productId"), x.getLong("productPrice"))).groupByKey()
      .map(x => (x._1,x._2.size, x._2.reduceLeft(_+_)))


    orders.foreachRDD(x =>
    x.foreachPartition(patition =>
    patition.foreach(x => {
      println("productId="
      + x._1 + "count=" + x._2 + " productPricrice=" + x._3)
      //获取Redis连接资源
      val jedis:Jedis = RedisClient.pool.getResource()
      // 指定数据库
      jedis.select(dbIndex)
      // 每个商品销售额累加
      jedis.hincrBy(orderTotalKey, x._1,x._3)
      // 总销售额累加
      jedis.incrBy(totalKey, x._3)
      RedisClient.pool.returnResource(jedis)
    })))
    ssc.start()
    ssc.awaitTermination()
  }
}
