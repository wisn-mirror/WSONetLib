package want.com.authtest.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by wisn on 2017/8/22.
 */

public class UserTResponse {
    @Expose
    public  String  id;
    @Expose
    public  String  name;


    public UserTResponse() {
        super();
    }

    @Override
    public String toString() {
        return "UserTResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }

    public UserTResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
