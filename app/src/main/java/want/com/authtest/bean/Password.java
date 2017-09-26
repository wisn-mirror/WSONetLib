package want.com.authtest.bean;

import com.want.wso2.bean.Bean;

/**
 * Created by wisn on 2017/9/26.
 */

public class Password extends Bean{
    /**
     * oldPassword : aa
     * newPassword : ddd
     */

    private String oldPassword;
    private String newPassword;

    public Password(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() { return oldPassword;}

    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword;}

    public String getNewPassword() { return newPassword;}

    public void setNewPassword(String newPassword) { this.newPassword = newPassword;}
}
