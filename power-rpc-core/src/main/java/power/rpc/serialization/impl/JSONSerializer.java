package power.rpc.serialization.impl;

import com.alibaba.fastjson.JSONObject;
import power.rpc.serialization.Serializer;

/**
 * JSON 序列化
 * @Author: PowerYang
 * @Date: 2019/7/9 16:45
 */
public class JSONSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        return JSONObject.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSONObject.parseObject(data,clazz);
    }
}
