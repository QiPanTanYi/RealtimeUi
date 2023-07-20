package cn.itcast.createorder;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class PaymentInfoProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        // 1.指定Kafka集群的主机名和端口号
        props.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");
        // 2.指定等待所有副本节点的答应
        props.put("acks","all");
        // 3.指定消息发送最大尝试次数
        props.put("retries",0);
        // 4.指定一批消息处理大小
        props.put("batch.size",16384);
        // 5.指定请求延时
        props.put("linger.ms",1);
        // 6.指定缓冲区内存大小
        props.put("buffer.memory",33554432);
        // 7.设置key序列化
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        // 8.设置value序列化
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String,String> kafkaProducer = new KafkaProducer<String, String>(props);
        PaymentInfo pay = new PaymentInfo();
        while (true) {
            // 9.生产数据
            String message = pay.random();
            kafkaProducer.send(new ProducerRecord<String, String>("itcast_order", message));
            System.out.println("数据已发送到Kafka：" + message);
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }
}
