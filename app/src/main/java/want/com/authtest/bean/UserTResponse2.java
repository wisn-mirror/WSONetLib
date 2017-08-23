package want.com.authtest.bean;

/**
 * Created by wisn on 2017/8/22.
 */

public class UserTResponse2 {
    public int code;
    public String msg;
    public UserTResponse data;

    @Override
    public String toString() {
        return "UserTResponse2{" +
               "code=" + code +
               ", msg='" + msg + '\'' +
               ", data=" + data +
               '}';
    }
}
