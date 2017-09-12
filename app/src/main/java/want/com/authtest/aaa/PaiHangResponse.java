package want.com.authtest.aaa;

import com.want.wso2.bean.Bean;

import java.util.List;

/**
 * Created by wisn on 2017/9/11.
 */

public class PaiHangResponse extends Bean {
    /**
     * indexnum : 8
     * todaySales : 3410.0
     * todaySalesVolume : 1037
     * singleAverage : 148.2608695652174
     * cashPayment : 331.0
     * index : [{"name":"销售额","id":"1","unit":"元","code":"xiaoshoue"},{"name":"销售量","id":"2","unit":"件","code":"xiaoshouliang"},{"name":"现金支付","id":"3","unit":"元","code":"xianjinzhifu"}]
     * records : [{"first":"3410.0","second":"1037","third":"331.0"},{"machineName":"神旺售货机","location":"如旺公司3F","code":"93002952","first":"848.5","second":"210","third":"0.0"},{"machineName":"久保田老王售货机2金山","location":"红中路点位","code":"25501002","first":"663.25","second":"186","third":"27.5"},{"machineName":"久保田售货机3华新镇北青公路","location":"一点","code":"25501003","first":"525.0","second":"155","third":"75.0"},{"machineName":"上海分五楼","location":"如旺公司3F","code":"93002908","first":"375.0","second":"121","third":"83.5"},{"machineName":"久保田老王售货机4红中3楼","location":"金山点位","code":"25501004","first":"373.5","second":"150","third":"42.5"},{"machineName":"如旺3F售货机-杨飞测试","location":"红中路点位","code":"12456789","first":"205.5","second":"81","third":"0.0"},{"machineName":"旺旺总部B1","location":"一点","code":"93002951","first":"120.5","second":"42","third":"0.0"},{"machineName":"久保田老王售货机5红中2楼","location":"金山点位","code":"25501005","first":"96.75","second":"34","third":"41.5"},{"machineName":"孵化中心门口","location":"南京旺旺总厂食堂","code":"93002911","first":"79.0","second":"23","third":"0.0"},{"machineName":"旺旺总部A3","location":"金山点位","code":"93004679D","first":"64.0","second":"20","third":"2.0"}]
     */

    private int indexnum;
    private String todaySales;
    private String todaySalesVolume;
    private String singleAverage;
    private String cashPayment;
    private List<IndexBean> index;
    private List<RecordsBean> records;

    public int getIndexnum() { return indexnum;}

    public void setIndexnum(int indexnum) { this.indexnum = indexnum;}

    public String getTodaySales() { return todaySales;}

    public void setTodaySales(String todaySales) { this.todaySales = todaySales;}

    public String getTodaySalesVolume() { return todaySalesVolume;}

    public void setTodaySalesVolume(String todaySalesVolume) { this.todaySalesVolume = todaySalesVolume;}

    public String getSingleAverage() { return singleAverage;}

    public void setSingleAverage(String singleAverage) { this.singleAverage = singleAverage;}

    public String getCashPayment() { return cashPayment;}

    public void setCashPayment(String cashPayment) { this.cashPayment = cashPayment;}

    public List<IndexBean> getIndex() { return index;}

    public void setIndex(List<IndexBean> index) { this.index = index;}

    public List<RecordsBean> getRecords() { return records;}

    public void setRecords(List<RecordsBean> records) { this.records = records;}

    public static class IndexBean {
        /**
         * name : 销售额
         * id : 1
         * unit : 元
         * code : xiaoshoue
         */

        private String name;
        private String id;
        private String unit;
        private String code;

        public String getName() { return name;}

        public void setName(String name) { this.name = name;}

        public String getId() { return id;}

        public void setId(String id) { this.id = id;}

        public String getUnit() { return unit;}

        public void setUnit(String unit) { this.unit = unit;}

        public String getCode() { return code;}

        public void setCode(String code) { this.code = code;}
    }

    public static class RecordsBean {
        /**
         * first : 3410.0
         * second : 1037
         * third : 331.0
         * machineName : 神旺售货机
         * location : 如旺公司3F
         * code : 93002952
         */

        private String first;
        private String second;
        private String third;
        private String machineName;
        private String location;
        private String code;

        public String getFirst() { return first;}

        public void setFirst(String first) { this.first = first;}

        public String getSecond() { return second;}

        public void setSecond(String second) { this.second = second;}

        public String getThird() { return third;}

        public void setThird(String third) { this.third = third;}

        public String getMachineName() { return machineName;}

        public void setMachineName(String machineName) { this.machineName = machineName;}

        public String getLocation() { return location;}

        public void setLocation(String location) { this.location = location;}

        public String getCode() { return code;}

        public void setCode(String code) { this.code = code;}
    }

    @Override
    public String toString() {
        return "PaiHangResponse{" +
               "indexnum=" + indexnum +
               ", todaySales='" + todaySales + '\'' +
               ", todaySalesVolume='" + todaySalesVolume + '\'' +
               ", singleAverage='" + singleAverage + '\'' +
               ", cashPayment='" + cashPayment + '\'' +
               ", index=" + index +
               ", records=" + records +
               '}';
    }
}
