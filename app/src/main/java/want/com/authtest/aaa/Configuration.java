package want.com.authtest.aaa;

/**
 * Created by wisn on 2017/9/8.
 */

public class Configuration {
    public String name;
    public String contentType;
    public String value;

    @Override
    public String toString() {
        return "Configuration{" +
               "name='" + name + '\'' +
               ", contentType='" + contentType + '\'' +
               ", value='" + value + '\'' +
               '}';
    }
}
