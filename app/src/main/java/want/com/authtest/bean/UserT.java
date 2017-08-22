package want.com.authtest.bean;

/**
 * Created by wisn on 2017/8/22.
 */

public class UserT {

    public  long id;
    public  String token;
    public  String  name;
    public  String password;

    public UserT() {
        super();
    }

    public UserT(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserT(long id, String token, String name) {
        this.id = id;
        this.token = token;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserT{" +
               "id=" + id +
               ", token='" + token + '\'' +
               ", name='" + name + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
