package want.com.authtest.aaa;

import com.want.wso2.bean.Bean;

/**
 * Created by wisn on 2017/9/12.
 */

public class IndexPage extends Bean {

    /**
     * todaySales : 0.0
     * todaySalesAccount : 0
     * userName : allen
     * companyName : 如旺久保田售货机
     * swapAccount : 3
     * machineAccount : 23xxx
     * messageAccount : 0
     */

    private String todaySales;
    private String todaySalesAccount;
    private String userName;
    private String companyName;
    private String swapAccount;
    private String machineAccount;
    private String messageAccount;

    public String getTodaySales() { return todaySales;}

    public void setTodaySales(String todaySales) { this.todaySales = todaySales;}

    public String getTodaySalesAccount() { return todaySalesAccount;}

    public void setTodaySalesAccount(String todaySalesAccount) { this.todaySalesAccount = todaySalesAccount;}

    public String getUserName() { return userName;}

    public void setUserName(String userName) { this.userName = userName;}

    public String getCompanyName() { return companyName;}

    public void setCompanyName(String companyName) { this.companyName = companyName;}

    public String getSwapAccount() { return swapAccount;}

    public void setSwapAccount(String swapAccount) { this.swapAccount = swapAccount;}

    public String getMachineAccount() { return machineAccount;}

    public void setMachineAccount(String machineAccount) { this.machineAccount = machineAccount;}

    public String getMessageAccount() { return messageAccount;}

    public void setMessageAccount(String messageAccount) { this.messageAccount = messageAccount;}

    @Override
    public String toString() {
        super.toString();
        return "IndexPage{" +
               "todaySales='" + todaySales + '\'' +
               ", todaySalesAccount='" + todaySalesAccount + '\'' +
               ", userName='" + userName + '\'' +
               ", companyName='" + companyName + '\'' +
               ", swapAccount='" + swapAccount + '\'' +
               ", machineAccount='" + machineAccount + '\'' +
               ", messageAccount='" + messageAccount + '\'' +
               '}';
    }
}
