package want.com.authtest.bean;

import java.util.List;

/**
 * Created by wisn on 2017/8/21.
 */

public class MoreIndexResponse {
    public List<MoreIndexResult> index;

    public List<MoreIndexResult> getIndex() {
        return index;
    }

    public void setIndex(List<MoreIndexResult> index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "MoreIndexResponse{" +
               "index=" + index.size() +
               '}';
    }
}
class MoreIndexResult{
    public String name;
    public String id;
    public String unit;
    public String code;

    @Override
    public String toString() {
        return "MoreIndexResult{" +
               "name='" + name + '\'' +
               ", id='" + id + '\'' +
               ", unit='" + unit + '\'' +
               ", code='" + code + '\'' +
               '}';
    }
}