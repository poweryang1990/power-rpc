package power.rpc.serialization;

/**
 * @Author: PowerYang
 * @Date: 2019/7/9 16:40
 */
public interface Serializer {
    /**
     * 序列化
     * @param obj 序列化对象
     * @param <T> 序列化对象原始类型
     * @return 字节数组
     */
    <T> byte[] serialize(T obj);


    /**
     * 反序列化
     * @param data 序列化字节数组
     * @param clazz 原始类型的类对象
     * @param <T> 原始类型
     * @return
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}
