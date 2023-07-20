package cn.itcast.util;
import java.util.Arrays;
public class UiBean {
    private String [] produceId;
    private String [] produceSumPrice;

    public UiBean(){}

    public UiBean(String[] produceId, String[] produceSumPrice){
        this.produceId = produceId;
        this.produceSumPrice = produceSumPrice;
    }
    public String[] getProduceId(){
        return produceId;
    }
    public void setProduceId(String[] produceId){
        this.produceId = produceId;
    }
    public String[] getProduceSumPrice(){
        return produceSumPrice;
    }

    public void setProduceSumPrice(String[] produceSumPrice){
        this.produceSumPrice = produceSumPrice;
    }

    @Override
    public String toString(){
        return "UiBean{" +
                "produceId=" + Arrays.toString(produceId) +
                ", produceSumPrice=" + Arrays.toString(produceSumPrice) +
                "}";
    }
}
