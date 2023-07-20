package cn.itcast;

import cn.itcast.createorder.PaymentInfo;
import org.junit.Test;

public class TestDemo {
    @Test
    public void testPaymentInfo(){
        PaymentInfo paymentInfo = new PaymentInfo();
        String orderInfo = paymentInfo.random();
        System.out.println(orderInfo);
    }

}
