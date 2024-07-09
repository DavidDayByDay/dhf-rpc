package com.better.serializer;

import com.better.exceptions.RpcException;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;

@Slf4j
public class JSONSerializer implements Serializer{
    private final Gson gson;

    public JSONSerializer() {
        gson = new Gson().newBuilder()
                .registerTypeAdapter(Class.class, new JsonClassAdapter())
                .create();
    }

    @Override
    public <T> byte[] serialize(T obj) {
        byte[] data = gson.toJson(obj).getBytes();
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        String json = new String(data);
        return gson.fromJson(json, clazz);
    }


    /**
     * adapter用于支持JSON对Class类型的序列化
     */
    static class JsonClassAdapter implements JsonSerializer<Class<?>> , JsonDeserializer<Class<?>> {
        @Override
        public Class<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            String name = jsonElement.getAsString();
            try {
                Class<?> aClass = Class.forName(name);
                return aClass;
            } catch (ClassNotFoundException e) {
                log.debug("ClassNotFoundException when deserialize in JSON for jsonElement: {}",jsonElement, e);
                throw new RpcException(e.getMessage());
            }
        }


        @Override
        public JsonElement serialize(Class<?> aClass, Type type, JsonSerializationContext context) {
            String name = aClass.getName();
            return new JsonPrimitive(name);
        }
    }
}
