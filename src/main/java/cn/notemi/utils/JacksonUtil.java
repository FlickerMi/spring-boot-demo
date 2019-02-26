package cn.notemi.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

/**
 * Title：JacksonUtil
 * Description：
 *
 * @author Flicker
 * @create 2017/8/17 0017 18:48
 **/
public class JacksonUtil {

    private static ObjectMapper objectMapper;
    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象
     *
     * @param jsonStr
     * @param valueType
     * @return
     */
    public static <T> T readValue(String jsonStr, Class<T> valueType) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json数组转List
     * @param jsonStr
     * @param valueTypeRef
     * @return
     */
    public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把JavaBean转换为json字符串
     * @param object
     * @return
     */
    public static String toJSon(Object object) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
