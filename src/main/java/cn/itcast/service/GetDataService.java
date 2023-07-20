package cn.itcast.service;

import cn.itcast.util.JedisUtil;
import cn.itcast.util.UiBean;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Map;

@Service
public class GetDataService {
    // 获取Jedis对象
    Jedis jedis = JedisUtil.getJedis();
    public String getData(){
        // 获取Redis数据库中键为bussiness::order::total的数据
        Map<String,String > testData = jedis.hgetAll("bussiness::order::total");

        String [] produceId = new String[10];
        String [] produceSumPrice = new String[10];
        int i = 0;
        // 封装数据
        for (Map.Entry<String,String> entry : testData.entrySet()){
            produceId[i] = entry.getKey();
            produceSumPrice[i] = entry.getValue();
            i++;
        }
        UiBean ub = new UiBean();
        ub.setProduceSumPrice(produceSumPrice);
        ub.setProduceId(produceId);
        // 将ub对象转换为json格式的字符串
        return JSONObject.toJSONString(ub);
    }
}
