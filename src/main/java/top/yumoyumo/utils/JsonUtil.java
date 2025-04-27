package top.yumoyumo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import top.yumoyumo.enums.ErrorEnum;
import top.yumoyumo.exception.LocalRunTimeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static Gson gson;
    private static ObjectMapper objectMapper;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(int.class, new IntTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntTypeAdapter())
                .setDateFormat("MMM d, yyyy, h:mm:ss a")
                .disableHtmlEscaping()
                .create();
        objectMapper = new ObjectMapper();
    }

    public static <T> T parseObject(String data, Class<T> clazz) {
        return gson.fromJson(data, clazz);
    }



    public static <T> T parseObjectJackson(String data, Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (JsonProcessingException e) {
            throw new LocalRunTimeException(ErrorEnum.COMMON_ERROR);
        }
    }

    public static <T> T parseObject(JsonObject jsonObject, TypeToken<T> typeToken) {
        return gson.fromJson(jsonObject, typeToken.getType());
    }

    public static <T> T parseObject(String data, TypeToken<T> typeToken) {
        return gson.fromJson(data, typeToken.getType());
    }


    public static <T> T parseObject(String data, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(data, typeReference);
        } catch (JsonProcessingException e) {
            throw new LocalRunTimeException(ErrorEnum.COMMON_ERROR);
        }
    }

    public static JsonObject parseObject(String data) {
        return gson.fromJson(data, JsonObject.class);
    }

    public static JsonArray parseArray(String data) {
        return gson.fromJson(data, JsonArray.class);
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static String toJackson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new LocalRunTimeException(ErrorEnum.COMMON_ERROR);
        }
    }

    public static <T> List<T> castToList(String data, Class<T> clazz) {
        JsonArray jsonArray = JsonUtil.parseArray(data);
        List<T> list = new ArrayList<T>();
        for (JsonElement jsonElement : jsonArray) {
            list.add(gson.fromJson(jsonElement, clazz));
        }
        return list;
    }

  
    //类型不统一 ，有的是 "" 有的是Number
    static class IntTypeAdapter extends TypeAdapter<Number> {

        @Override
        public void write(JsonWriter out, Number value)
                throws IOException {
            out.value(value);
        }

        @Override
        public Number read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String result = in.nextString();
                if ("".equals(result)) {
                    return null;
                }
                return Integer.parseInt(result);
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }
    }
}

