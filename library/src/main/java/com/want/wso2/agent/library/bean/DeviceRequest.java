package com.want.wso2.agent.library.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wisn on 2017/8/9.
 */

public class DeviceRequest extends Bean {
    public String description;
    public String deviceIdentifier;
    public String name;
    public List<Property> properties;
    public EnrolmentInfo enrolmentInfo;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Property> getProperties() {
        properties =new ArrayList<>();
        properties.add(new Property("SERIAL","AQ5TPZHUHY6LGQNF"));
        properties.add(new Property("IMEI",this.description));
        properties.add(new Property("IMSI",this.description));
        properties.add(new Property("MAC","58:44:98:4c:39:e9"));
        properties.add(new Property("DEVICE_MODEL","MI 6"));
        properties.add(new Property("VENDOR","Xiaomi"));
        properties.add(new Property("OS_VERSION","5.0.2"));
        properties.add(new Property("OS_BUILD_DATE","1498901814000"));
        properties.add(new Property("DEVICE_NAME","867516026498102"));
        properties.add(new Property("LATITUDE","31.188046"));
        properties.add(new Property("LONGITUDE","31.188046"));
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public EnrolmentInfo getEnrolmentInfo() {
        return enrolmentInfo;
    }

    public void setEnrolmentInfo(String owner,String ownership) {
        this.enrolmentInfo = new EnrolmentInfo(owner,ownership);
    }


    @Override
    public String toString() {
        return "DeviceRequest{" +
               "description='" + description + '\'' +
               ", deviceIdentifier='" + deviceIdentifier + '\'' +
               ", name='" + name + '\'' +
               ", properties=" + properties +
               '}';
    }
}
class Property{
    public String name;
    public String value;

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Property{" +
               "name='" + name + '\'' +
               ", value='" + value + '\'' +
               '}';
    }
}
 class EnrolmentInfo{
    public String owner;
    public String ownership;

     public EnrolmentInfo(String owner, String ownership) {
         this.owner = owner;
         this.ownership = ownership;
     }

     public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    @Override
    public String toString() {
        return "EnrolmentInfo{" +
               "owner='" + owner + '\'' +
               ", ownership='" + ownership + '\'' +
               '}';
    }
}
