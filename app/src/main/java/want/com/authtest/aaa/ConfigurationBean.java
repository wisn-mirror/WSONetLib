package want.com.authtest.aaa;

import com.want.wso2.bean.Bean;

import java.util.List;

/**
 * Created by wisn on 2017/9/8.
 */

public class ConfigurationBean extends Bean{
    public List<Configuration> configuration;
    @Override
    public String toString() {
        return super.toString()+"ConfigurationBean{" +
               "configuration=" + configuration +
               '}';
    }
}
