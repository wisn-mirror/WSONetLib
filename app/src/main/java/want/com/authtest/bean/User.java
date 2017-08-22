package want.com.authtest.bean;

import java.util.Arrays;

/**
 * Created by wisn on 2017/8/1.
 */

public class User {
    private String username;
    private String firstname;
    private String lastname;
    private String emailAddress;
    private String password;
    private String[] roles;

    public User() {
    }

    public User(String username,
                String firstname,
                String lastname,
                String emailAddress,
                String password, String[] roles) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", firstname='" + firstname + '\'' +
               ", lastname='" + lastname + '\'' +
               ", emailAddress='" + emailAddress + '\'' +
               ", password='" + password + '\'' +
               ", roles=" + Arrays.toString(roles) +
               '}';
    }
    public String toJSON() {
//        return CommonUtils.toJSON(this);
        return  "";
    }
}
