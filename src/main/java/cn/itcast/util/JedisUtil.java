package cn.itcast.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;

/**
 * Redis Java API 操作工具类
 * 主要提供Java操作Redis的对象Jedis，类似数据库连接池
 */
public class JedisUtil {
    private JedisUtil(){}
    private static JedisPool jedisPool;
    static {
        Properties prop = new Properties();
        try{
            prop.load(JedisUtil.class.getClassLoader().getResourceAsStream("redis.properties"));
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            // jedis连接池中最大连接个数
            poolConfig.setMaxTotal(Integer.valueOf(prop.getProperty("jedis.max.total")));
            // jedis连接池中最大的空闲连接个数
            poolConfig.setMaxIdle(Integer.valueOf(prop.getProperty("jedis.max.idle")));
            // jedis连接池中最小的空闲连接个数
            poolConfig.setMinIdle(Integer.valueOf(prop.getProperty("jedis.min.idle")));
            // jedis连接池最大等待连接时间ms值
            poolConfig.setMaxWaitMillis(Long.valueOf(prop.getProperty("jedis.max.wait.millis")));

            // 表示jedis的服务器主机名
            String host = prop.getProperty("jedis.host");
            int port = Integer.valueOf(prop.getProperty("jedis.port"));

            jedisPool = new JedisPool(poolConfig, host, port, 10000);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //  提供了jedis的对象
    public static Jedis getJedis(){return jedisPool.getResource();}

    // 资源释放
    public static void returnJedis(Jedis jedis){jedis.close();}
}
