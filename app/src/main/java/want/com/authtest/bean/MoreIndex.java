package want.com.authtest.bean;

/**
 * Created by wisn on 2017/8/21.
 */

public class MoreIndex {
    /*{
        "uid": "20",
            "dateType":"day",
            "page": 1,
            "roleid":"1",
            "times": [ "2017-08-18"]
    }*/
    public String uid;
    public String dateType;
    public String roleid;
    public int page;
    public String[] times={"2017-08-18"};

    public MoreIndex() {
    }

    public MoreIndex(String uid, String dateType, String roleid, int page) {
        this.uid = uid;
        this.dateType = dateType;
        this.roleid = roleid;
        this.page = page;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String[] getTimes() {
        return times;
    }

    public void setTimes(String[] times) {
        this.times = times;
    }
}
