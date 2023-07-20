package cn.itcast.processdata

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.JedisPool

import java.util.Properties

object RedisClient {
  val prop = new Properties()
  // 加载配置文件
  prop.load(this.getClass.getClassLoader.getResourceAsStream("redis.properties"))

  val redisHost: String = prop.getProperty("jedis.host")
  val redisPort: String = prop.getProperty("jedis.port")
  val redisTimeout: String = prop.getProperty("jedis.max.wait.millis")

  lazy val pool = new JedisPool(
    new GenericObjectPoolConfig(),
    redisHost,redisPort.toInt,redisTimeout.toInt
  )

  lazy val hook = new Thread{
    override def run={
      println("Execute hook thread:" + this)
      pool.destroy()
    }
  }
}
