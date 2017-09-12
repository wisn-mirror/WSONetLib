package want.com.authtest.aaa;

import com.want.wso2.bean.Bean;

import java.util.List;

/**
 * Created by wisn on 2017/9/11.
 */

public class PaiHang {

    /**
     * uid : 20
     * page : 1
     * roleid : 1
     * dateType : month
     * times : ["2017-02"]
     */

    private String uid;
    private int page;
    private String roleid;
    private String dateType;
    private List<String> times;

    public String getUid() { return uid;}

    public void setUid(String uid) { this.uid = uid;}

    public int getPage() { return page;}

    public void setPage(int page) { this.page = page;}

    public String getRoleid() { return roleid;}

    public void setRoleid(String roleid) { this.roleid = roleid;}

    public String getDateType() { return dateType;}

    public void setDateType(String dateType) { this.dateType = dateType;}

    public List<String> getTimes() { return times;}

    public void setTimes(List<String> times) { this.times = times;}

    @Override
    public String toString() {
        return "PaiHang{" +
               "uid='" + uid + '\'' +
               ", page=" + page +
               ", roleid='" + roleid + '\'' +
               ", dateType='" + dateType + '\'' +
               ", times=" + times +
               '}';
    }
}
