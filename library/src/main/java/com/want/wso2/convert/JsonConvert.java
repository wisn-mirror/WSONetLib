package com.want.wso2.convert;

import com.google.gson.stream.JsonReader;
import com.want.wso2.bean.BaseNetBean;
import com.want.wso2.bean.Fault;
import com.want.wso2.bean.SimpleResponse;
import com.want.wso2.utils.Convert;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by wisn on 2017/8/22.
 */

public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConvert() {
    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }
        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());
        if (rawType == String.class) {
            //noinspection unchecked
            return (T) body.string();
        } else if (rawType == JSONObject.class) {
            //noinspection unchecked
            return (T) new JSONObject(body.string());
        } else if (rawType == JSONArray.class) {
            //noinspection unchecked
            return (T) new JSONArray(body.string());
        } else {
            T t = Convert.fromJson(jsonReader, rawType);
            response.close();
            return t;
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());
        T t = Convert.fromJson(jsonReader, type);
        response.close();
        return t;
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());
        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
        if (rawType != BaseNetBean.class) {
            T t = Convert.fromJson(jsonReader, type);
            response.close();
            return t;
        } else {
            if (typeArgument == Void.class) {
                SimpleResponse simpleResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
                response.close();
                return (T) simpleResponse.toResponse();
            } else {
                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                BaseNetBean baseNetBean = Convert.fromJson(jsonReader, type);
                response.close();
                int code = baseNetBean.code;
                //这里的0是以下意思
                //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
                return (T) baseNetBean;
//                if (code == 0) {
//                    //noinspection unchecked
//                    return (T) baseNetBean;
//                } else if (code == 104) {
//                    throw new IllegalStateException("用户授权信息无效");
//                } else if (code == 105) {
//                    throw new IllegalStateException("用户收取信息已过期");
//                } else {
//                    //直接将服务端的错误信息抛出，onError中可以获取
//                    throw new IllegalStateException("错误代码：" + code + "，错误信息：" + baseNetBean.msg);
//                }
            }
        }
    }
}