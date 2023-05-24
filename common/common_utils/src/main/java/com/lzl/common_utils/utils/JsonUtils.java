package com.lzl.common_utils.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 对Jackson中的方法进行了简单封装
 *
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 将指定对象序列化为一个json字符串
     *
     * @param obj 指定对象
     * @return 返回一个json字符串
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    /**
     * 将指定json字符串解析为指定类型对象
     *
     * @param json   json字符串
     * @param tClass 指定类型
     * @return 返回一个指定类型对象
     */
    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * 将指定输入流解析为指定类型对象
     *
     * @param inputStream 输入流对象
     * @param tClass      指定类型
     * @return 返回一个指定类型对象
     */
    public static <T> T toBean(InputStream inputStream, Class<T> tClass) {
        try {
            return mapper.readValue(inputStream, tClass);
        } catch (IOException e) {
            logger.error("json解析出错：" + inputStream, e);
            return null;
        }
    }

    /**
     * 将指定json字符串解析为指定类型集合
     *
     * @param json   json字符串
     * @param eClass 指定元素类型
     * @return 返回一个指定类型集合
     */
    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * 将指定json字符串解析为指定键值对类型集合
     *
     * @param json   json字符串
     * @param kClass 指定键类型
     * @param vClass 指定值类型
     * @return 返回一个指定键值对类型集合
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * 将指定json字符串解析为一个复杂类型对象
     *
     * @param json json字符串
     * @param type 复杂类型
     * @return 返回一个复杂类型对象
     */
    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }
}