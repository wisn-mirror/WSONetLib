package com.want.wso2.bean;

import java.util.Iterator;
import java.util.List;

/**
 * Created by wisn on 2017/8/9.
 */

public class GetConfigResponse extends Bean{
    public List<Config> configuration;

    public List<Config> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(List<Config> configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder();
        if(configuration!=null){
            Iterator<Config> iterator = configuration.iterator();
            while(iterator.hasNext()){
                Config next = iterator.next();
                result.append(next.toString());
            }
        }
        return result.toString();
    }
}
class Config{
    public String name;
    public String contentType;
    public String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Config{" +
               "name='" + name + '\'' +
               ", contentType='" + contentType + '\'' +
               ", value='" + value + '\'' +
               '}';
    }
}
