package power.rpc.serialization;

import power.rpc.serialization.impl.DefaultJavaSerializer;
import power.rpc.serialization.impl.HessianSerializer;
import power.rpc.serialization.impl.JSONSerializer;
import power.rpc.serialization.impl.ProtoStuffSerializer;

/**
 * @Author: PowerYang
 * @Date: 2019/7/9 16:41
 */
public enum SerializeType  implements  SerializerGetter{
    /**
     * Java默认序列化
     */
    DefaultJava {
        @Override
        public Serializer getSerializer() {
            return new DefaultJavaSerializer();
        }
    },
    /**
     * Json序列化
     */
    JSON {
        @Override
        public Serializer getSerializer() {
            return new JSONSerializer();
        }
    },
    /**
     * Hessian序列化
     */
    Hessian {
        @Override
        public Serializer getSerializer() {
            return new HessianSerializer();
        }
    } ,
    /**
     * Protostuff序列化
     */
    ProtoStuff {
        @Override
        public Serializer getSerializer() {
            return new ProtoStuffSerializer();
        }
    }
}
