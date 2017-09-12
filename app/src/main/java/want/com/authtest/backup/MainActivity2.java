package want.com.authtest.backup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.want.wso2.WSONet;
import com.want.wso2.auth.TokenStore;
import com.want.wso2.bean.DeviceRequest;
import com.want.wso2.bean.DeviceResponse;
import com.want.wso2.bean.GetConfigResponse;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.bean.TokenRequest;
import com.want.wso2.bean.TokenResponse;
import com.want.wso2.db.DownloadManager;
import com.want.wso2.download.Download;
import com.want.wso2.download.DownloadListener;
import com.want.wso2.model.Progress;
import com.want.wso2.request.GetRequest;
import com.want.wso2.utils.Constant;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import want.com.authtest.ChrooseItemActivity;
import want.com.authtest.DeviceUtils;
import want.com.authtest.R;
import want.com.authtest.bean.UserT;

public class MainActivity2 extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    public static final String Result = "Result";
    public static final int GETTAG = 1;
    public static final int GETPREM = 2;
    private TextView mTestResult, getTAG, getPrem, register, getToken, refreshToken, getConfig, shareText,
            shareTextdelete,
            loginOut, getStatus;
    private EditText url, userName, password;
    private Context context;
    private static String[] SUBSCRIBED_API = new String[]{"android"};
    private static String[] roles = new String[]{"admin"};
    public final static String SCOPES = "default appm:read" +
                                        " perm:android:enroll perm:android:disenroll" +
                                        " perm:android:view-configuration perm:android:manage-configuration";
    private static String PERM = SCOPES;
    private ScrollView mScroll_info;
    private TextView mUsers;
    private RegisterResponse mRegisterResponse;
    private TokenResponse mTokenBody;
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
        getToken = (TextView) findViewById(R.id.getToken);
        refreshToken = (TextView) findViewById(R.id.refreshToken);
        getConfig = (TextView) findViewById(R.id.getConfig);
        shareText = (TextView) findViewById(R.id.shareText);
        getTAG = (TextView) findViewById(R.id.getTAG);
        getPrem = (TextView) findViewById(R.id.getPrem);
        getStatus = (TextView) findViewById(R.id.getStatus);
        shareTextdelete = (TextView) findViewById(R.id.shareTextdelete);
        loginOut = (TextView) findViewById(R.id.loginOut);
        url = (EditText) findViewById(R.id.url);
        mTokenUrl = mTokenStore.getTokenUrl();
        if(!TextUtils.isEmpty(mTokenUrl)){
            url.setText(mTokenUrl);
        }
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        getPrem.setOnClickListener(this);
        getTAG.setOnClickListener(this);
        register.setOnClickListener(this);
        getToken.setOnClickListener(this);
        getConfig.setOnClickListener(this);
        shareText.setOnClickListener(this);
        shareTextdelete.setOnClickListener(this);
        mUsers.setOnClickListener(this);
        loginOut.setOnClickListener(this);
        getStatus.setOnClickListener(this);
        refreshToken.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        Network.Config.Url=url.getText().toString();
        mTokenStore.saveTokenUrl(url.getText().toString());
        String strUserName = userName.getText().toString();
        String strPassword = password.getText().toString();
        if (view == register) {
            APIController apiController = new APIController();
            String applicationName =
                    Constant.API_APPLICATION_NAME_PREFIX + DeviceUtils.getDeviceName(context);
            RegistrationProfileRequest registrationProfileRequest = new RegistrationProfileRequest();
            registrationProfileRequest.setApplicationName(applicationName);
            registrationProfileRequest.setIsAllowedToAllDomains(false);
            registrationProfileRequest.setIsMappingAnExistingOAuthApp(false);
            registrationProfileRequest.setTags(SUBSCRIBED_API);
            apiController.register(registrationProfileRequest,
                                   strUserName,
                                   strPassword,
                                   new Callback<RegisterResponse>() {
                                       @Override
                                       public void onResponse(Call<RegisterResponse> call,
                                                              Response<RegisterResponse> response) {
                                           mRegisterResponse = response.body();
                                           updateView("onResponse:" + mRegisterResponse.toJSON(), true);
                                       }

                                       @Override
                                       public void onFailure(Call<RegisterResponse> call, Throwable t) {
                                           updateView("onFailure:" + t.getMessage(), true);

                                       }
                                   });
        } else if (view == getToken) {
            // initializeIDPLib(null, strUserName, strPassword, PERM);
            if (mRegisterResponse == null) {
                toast(" please register");
                return;
            }
            APIController apiController = new APIController();
            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setUsername(strUserName);
            tokenRequest.setPassword(strPassword);
            tokenRequest.setClientID(mRegisterResponse.getClient_id());
            tokenRequest.setClientSecret(mRegisterResponse.getClient_secret());
            tokenRequest.setScope(PERM);
            apiController.getToken(tokenRequest, new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    mTokenBody = response.body();
                    updateView("onResponse:" + mTokenBody.toJSON(), true);
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    updateView("onFailure:" + t.getMessage(), true);
                }
            });

        } else if (view == refreshToken) {
            if (mRegisterResponse == null && mTokenBody == null) {
                toast(" please gettoken");
                return;
            }
            APIController apiController = new APIController();
            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setUsername(strUserName);
            tokenRequest.setPassword(strPassword);
            tokenRequest.setClientID(mRegisterResponse.getClient_id());
            tokenRequest.setClientSecret(mRegisterResponse.getClient_secret());
            tokenRequest.setScope(PERM);
            apiController.refreshToken(tokenRequest,
                                       mTokenBody.getRefresh_token(),
                                       new Callback<TokenResponse>() {

                                           @Override
                                           public void onResponse(Call<TokenResponse> call,
                                                                  Response<TokenResponse> response) {
                                               mTokenBody = response.body();
                                               updateView("onResponse:" + mTokenBody.toJSON(), true);

                                           }

                                           @Override
                                           public void onFailure(Call<TokenResponse> call, Throwable t) {
                                               updateView("onFailure:" + t.getMessage(), true);

                                           }
                                       });
        } else if (view == getConfig) {
            if (mTokenBody == null) {
                toast(" please gettoken");
                return;
            }
            APIController apiController = new APIController();
            apiController.getConfig(mTokenBody.getAccess_token(), new Callback<GetConfigResponse>() {
                @Override
                public void onResponse(Call<GetConfigResponse> call, Response<GetConfigResponse> response) {
                    GetConfigResponse getConfigResponse = response.body();
                    updateView("onResponse:" + getConfigResponse.toJSON(), true);
                }

                @Override
                public void onFailure(Call<GetConfigResponse> call, Throwable t) {
                    updateView("onFailure:" + t.getMessage(), true);
                }
            });
        } else if (view == loginOut) {
            if (mTokenBody == null) {
                toast(" please gettoken");
                return;
            }
            APIController apiController = new APIController();
            String
                    applicationName =
                    Constant.API_APPLICATION_NAME_PREFIX + DeviceUtils.getDeviceName(context);
            apiController.loginOut(mTokenBody.getAccess_token(), applicationName, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    updateView("onResponse:" + response.body(), true);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    updateView("onFailure:" + t.getMessage(), true);
                }
            });
        } else if (view == getStatus) {
            if (mTokenBody == null) {
                toast(" please gettoken");
                return;
            }
            APIController apiController = new APIController();
            DeviceRequest device = new DeviceRequest();
            String imei = DeviceUtils.getDeviceName(context);
            device.setDescription(imei);
            device.setDeviceIdentifier(imei);
            device.setName(imei);
            device.setEnrolmentInfo("admin", "BYOD");
            device.getProperties();
            Log.e(TAG, device.toJSON());
            apiController.upDeviceInfo(device, mTokenBody.getAccess_token(), new Callback<DeviceResponse>() {
                @Override
                public void onResponse(Call<DeviceResponse> call, Response<DeviceResponse> response) {
                    updateView("onResponse:" + response.body(), true);
                }

                @Override
                public void onFailure(Call<DeviceResponse> call, Throwable t) {
                    updateView("onFailure:" + t.getMessage(), true);
                }
            });
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
        } else if (view == mUsers) {
            UserT user = new UserT(43241231, "wisn");
            try {
                Download.getInstance().setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaa/");
                Download.getInstance().getThreadPool().setCorePoolSize(3);
                List<Progress> progressList = DownloadManager.getInstance().getAll();
                Download.restore(progressList);
                GetRequest<File> request = WSONet.<File>get("http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0b8b552a1df0a8bc417a5afae3a26b2fb1342a909/com.qiyi.video.apk").headers("aaa", "111").params("bbb", "222");
                Download.request("Tag", request)
                        .save()//
                        .register(new DownloadListener("Tag") {
                            @Override
                            public void onStart(Progress progress) {
                                Log.d(TAG,"onStart  currentSize:"+progress.currentSize +"totalSize:"+progress.totalSize);
                            }

                            @Override
                            public void onProgress(Progress progress) {
                                Log.d(TAG,"onProgress  currentSize:"+progress.currentSize +"totalSize:"+progress.totalSize);
                            }

                            @Override
                            public void onError(Progress progress) {
                                Log.d(TAG,"onError  currentSize:"+progress.currentSize +"totalSize:"+progress.totalSize);
                            }

                            @Override
                            public void onFinish(File file, Progress progress) {
                                Log.d(TAG,"onFinish  currentSize:"+progress.currentSize +"totalSize:"+progress.totalSize);
                            }

                            @Override
                            public void onRemove(Progress progress) {
                                Log.d(TAG,"onRemove  currentSize:"+progress.currentSize +"totalSize:"+progress.totalSize);
                            }
                        })
                        .start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

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
