package want.com.authtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.want.wso2.WSONet;
import com.want.wso2.auth.Authenticator;
import com.want.wso2.auth.Network;
import com.want.wso2.auth.TokenStore;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.callback.JsonCallback;
import com.want.wso2.interfaces.RegisterListener;
import com.want.wso2.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import want.com.authtest.aaa.ConfigurationBean;
import want.com.authtest.aaa.PaiHang;
import want.com.authtest.aaa.PaiHangResponse;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    public static final String Result = "Result";
    public static final int GETTAG = 1;
    public static final int GETPREM = 2;
    private TextView mTestResult, getTAG, getPrem, register, getConfig, shareText,
            shareTextdelete,
            loginOut, getStatus;
    private EditText url, userName, password;
    private Context context;
    private static String[] SUBSCRIBED_API = new String[]{"android"};
    private static String[] roles = new String[]{"admin"};
   /* public final static String SCOPES = "default appm:read" +
                                        " perm:android:enroll perm:android:disenroll" +
                                        " perm:android:view-configuration perm:android:manage-configuration";
   */
   public final static String SCOPES = "default perm:machinerank:machinerank perm:machinerank:view";
    private static String PERM = SCOPES;
    private ScrollView mScroll_info;
    private TextView mUsers;
    private TokenStore mTokenStore;
    private String mTokenUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mTokenStore = new TokenStore(this);

//        initConfig();
        setContentView(R.layout.activity_main);
        mScroll_info = (ScrollView) findViewById(R.id.scroll_info);
        mUsers = (TextView) findViewById(R.id.users);
        mTestResult = (TextView) findViewById(R.id.testResult);
        register = (TextView) findViewById(R.id.register);
        getConfig = (TextView) findViewById(R.id.getConfig);
        shareText = (TextView) findViewById(R.id.shareText);
        getTAG = (TextView) findViewById(R.id.getTAG);
        getPrem = (TextView) findViewById(R.id.getPrem);
        getStatus = (TextView) findViewById(R.id.getStatus);
        shareTextdelete = (TextView) findViewById(R.id.shareTextdelete);
        loginOut = (TextView) findViewById(R.id.loginOut);
        url = (EditText) findViewById(R.id.url);
        mTokenUrl = mTokenStore.getTokenUrl();
        if (!TextUtils.isEmpty(mTokenUrl)) {
            url.setText(mTokenUrl);
        }
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        getPrem.setOnClickListener(this);
        getTAG.setOnClickListener(this);
        register.setOnClickListener(this);
        getConfig.setOnClickListener(this);
        shareText.setOnClickListener(this);
        shareTextdelete.setOnClickListener(this);
        mUsers.setOnClickListener(this);
        loginOut.setOnClickListener(this);
        getStatus.setOnClickListener(this);

    }

    String Ip = "http://10.0.35.158:8280";

    @Override
    public void onClick(View view) {
        Network.Config.Url = url.getText().toString();
        mTokenStore.saveTokenUrl(url.getText().toString());
        String strUserName = userName.getText().toString();
        String strPassword = password.getText().toString();
        Log.e(TAG,strUserName+"  "+strPassword);
        if (view == register) {
            String applicationName =
                    Constant.API_APPLICATION_NAME_PREFIX + DeviceUtils.getDeviceName(context);
            RegistrationProfileRequest registrationProfileRequest = new RegistrationProfileRequest();
            registrationProfileRequest.setApplicationName(applicationName);
            registrationProfileRequest.setIsAllowedToAllDomains(false);
            registrationProfileRequest.setIsMappingAnExistingOAuthApp(false);
            registrationProfileRequest.setTags(SUBSCRIBED_API);
            registrationProfileRequest.setTags(new String[]{"device_management"});
            registrationProfileRequest.setConsumerKey(null);
            registrationProfileRequest.setConsumerSecret(null);
            Authenticator.register(Ip + "/api-application-registration/register",
                                   Ip + "/token",
                                   registrationProfileRequest,
                                   strUserName,
                                   strPassword,
                                   SCOPES,
                                   new RegisterListener() {
                                       @Override
                                       public void onSuccess(RegisterResponse response, int code) {
                                           updateView("onSuccess:" + response.toJSON(), true);

                                       }

                                       @Override
                                       public void onFailure(String resonseStr, int code) {
                                           updateView("onFailure:" + resonseStr, true);

                                       }
                                   });
        } else if (view == getConfig) {
            WSONet.<ConfigurationBean>get(
                    Ip + "/api/device-mgt/android/v1.0/configuration")
                    .execute(new JsonCallback<ConfigurationBean>() {
                        @Override
                        public void onSuccess(com.want.wso2.model.Response<ConfigurationBean> response) {
                            if (response.body().fault != null) {

                                updateView("onSuccess:" + response.body().fault.toString(), true);
                            } else {
                                updateView("onSuccess:" + response.body().toString(), true);
                            }
                        }

                        @Override
                        public void onError(com.want.wso2.model.Response<ConfigurationBean> response) {
                            super.onError(response);
                            updateView("onFailure:" + response.body(), true);

                        }
                    });

        } else if (view == loginOut) {


        } else if (view == getStatus) {
            PaiHang paiHang = new PaiHang();
            paiHang.setUid("20");
            paiHang.setPage(1);
            paiHang.setRoleid("1");
            paiHang.setDateType("month");
            List<String> time = new ArrayList<String>();
            time.add("2017-02");
            paiHang.setTimes(time);
            WSONet.<PaiHangResponse>post(
                    Ip + "/api/yunwang/v1.0/machinerank/machinerank")
                    .upObjectToJson(paiHang)
                    .execute(new JsonCallback<PaiHangResponse>() {
                        @Override
                        public void onSuccess(com.want.wso2.model.Response<PaiHangResponse> response) {
                            if (response.body().fault != null) {
                                updateView("onSuccess:" + response.body().fault.toString(), true);
                            }
                            updateView("onSuccess:" + response.body().toString(), true);

                        }

                        @Override
                        public void onError(com.want.wso2.model.Response<PaiHangResponse> response) {
                            super.onError(response);
                            updateView("onFailure:" + response.body(), true);

                        }
                    });

        } else if (view == mUsers) {

        } else if (view == shareText) {
            String result = mTestResult.getText().toString();
            if (!TextUtils.isEmpty(result)) {
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT, result);
                intent1.setType("text/plain");
                startActivity(Intent.createChooser(intent1, "share"));
            } else {
                toast("结果为空！");
            }
        } else if (view == shareTextdelete) {
            mTestResult.setText("");
        } else if (view == getTAG) {
            Intent intent = new Intent(this, ChrooseItemActivity.class);
            intent.putExtra(ChrooseItemActivity.ChrooseTypeIsTAG, true);
            startActivityForResult(intent, GETTAG);
        } else if (view == getPrem) {
            Intent intent = new Intent(this, ChrooseItemActivity.class);
            intent.putExtra(ChrooseItemActivity.ChrooseTypeIsTAG, false);
            startActivityForResult(intent, GETPREM);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        toast(data.getStringExtra(Result));
        String result = data.getStringExtra(Result);
        if (!TextUtils.isEmpty(result)) {
            if (resultCode == GETTAG) {
                this.updateView("Tag:" + result, true);
                SUBSCRIBED_API = result.split(":");
            } else {
                this.updateView("Perm:" + result, true);
                PERM = result;
            }
        }
    }

    public void updateView(final String msg, final boolean isAppend) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isAppend) {
                    mTestResult.append(msg + "\n");
                } else {
                    mTestResult.setText(msg);
                }
                mScroll_info.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
            }
        });
    }

    public void toast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
