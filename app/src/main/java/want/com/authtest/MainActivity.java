package want.com.authtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.want.wso2.auth.APIController;
import com.want.wso2.Constant;
import com.want.wso2.WSONet;
import com.want.wso2.bean.DeviceRequest;
import com.want.wso2.bean.DeviceResponse;
import com.want.wso2.bean.GetConfigResponse;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.bean.TokenRequest;
import com.want.wso2.bean.TokenResponse;
import com.want.wso2.callback.JsonCallback;
import com.want.wso2.request.PostRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import want.com.authtest.bean.UserT;

public class MainActivity extends Activity implements View.OnClickListener {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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
        String strUserName = userName.getText().toString();
        String strPassword = password.getText().toString();
        if (view == register) {
            APIController apiController = new APIController();
            String
                    applicationName =
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
//                WSONet.<BaseNetBean<UserTResponse>>post("http://10.0.86.120:8080/WSS/app/test")
//                        .tag("http://10.0.86.120:8080/WSS/app/test")
//                        .upJson(new Gson().toJson(user))
//                        .execute(new JsonCallback<BaseNetBean<UserTResponse>>() {
//
//                            @Override
//                            public void onSuccess(com.want.wso2.model.Response<BaseNetBean<UserTResponse>> response) {
//                                updateView("onResponse:"+response.body(), true);
//                            }
//{"machineId":11,"detailType":""}
//                            @Override
//                            public void onError(com.want.wso2.model.Response<BaseNetBean<UserTResponse>> response) {
//                                super.onError(response);
//                                updateView("onError:"+response.body(), true);
//                            }
//                        });
//                WSONet.<BaseNetBean<UserTResponse>>post("http://10.0.86.120:8080/WSS/app/test")
//                        .tag("http://10.0.86.120:8080/WSS/app/test")
//                        .upObjectToJson(user)
////                        .params("id",123432)
////                        .params("name","wisn")
////                        .params("password","nihao")
//                        .execute(new JsonCallback<BaseNetBean<UserTResponse>>() {
//
//                            @Override
//                            public void onSuccess(com.want.wso2.model.Response<BaseNetBean<UserTResponse>> response) {
//                                updateView("onResponse:"+response.body(), true);
//                            }
//
//                            @Override
//                            public void onError(com.want.wso2.model.Response<BaseNetBean<UserTResponse>> response) {
//                                super.onError(response);
//                                updateView("onError:"+response.body(), true);
//                            }
//                        });
                final PostRequest<String>
                        stringPostRequest =
                        WSONet.<String>post("http://10.0.35.10:9763/yunwang_backend/services/monitor/detail")
                                .tag("http://10.0.35.10:9763/yunwang_backend/services/monitor/detail")
                                .upJson("{\"machineids\":11}");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            SystemClock.sleep(100);
                            stringPostRequest.execute(new JsonCallback<String>(){

                                @Override
                                public void onSuccess(com.want.wso2.model.Response<String> response) {
                                    updateView("onResponse:"+response.body(), true);

                                }

                                @Override
                                public void onError(com.want.wso2.model.Response<String> response) {
                                    super.onError(response);
                                    updateView("onError:"+response.body(), true);

                                }
                            });
                            WSONet.getInstance().cancelTag("http://10.0.35.10:9763/yunwang_backend/services/monitor/detail");

                        }

                    }
                }).start();

//
                /*LinkedHashMap<String, List<String>>
                        urlParamsMap =
                        WSONet.getInstance().getCommonParams().urlParamsMap;
                Iterator<Map.Entry<String, List<String>>> iterator = urlParamsMap.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<String, List<String>> next = iterator.next();
                    updateView(next.getKey()+ next.getValue().size(),true);
                }*/
                /*WSONet.<UserTResponse2>post("http://10.0.86.120:8080/WSS/app/test")
                        .tag("http://10.0.86.120:8080/WSS/app/test")
                        .upJson(new Gson().toJson(user))
                        .execute(new JsonCallback<UserTResponse2>() {

                            @Override
                            public void onSuccess(com.want.wso2.model.Response<UserTResponse2> response) {
                                updateView("onResponse:"+response.body(), true);
                            }

                            @Override
                            public void onError(com.want.wso2.model.Response<UserTResponse2> response) {
                                super.onError(response);
                                updateView("onError:"+response.body(), true);
                            }
                        });*/
            } catch (Exception e) {
                e.printStackTrace();
            }

//                    .execute(new AbsCallback<UserTResponse>(){
//                        @Override
//                        public void onSuccess(com.want.wso.model.Response<UserTResponse> response) {
//
//                        }
//
//                        @Override
//                        public UserTResponse convertResponse(okhttp3.Response response) throws Throwable {
//                            return null;
//                        }
//                    });




            /*
            APIController apiController =new APIController();
            //String uid, String dateType, String roleid, int page
            MoreIndex moreIndex=new MoreIndex("20","day","1",1);
           apiController.excutePost("yunwang_backend/services/machinerank/moreindex",
                                     moreIndex,
                                     MoreIndexResponse.class,
                                     new NetListener<MoreIndexResponse>() {
                                         @Override
                                         public void onSuccess(MoreIndexResponse response,
                                                               String resonseStr) {
                                             updateView("onResponse:"+response+" "+resonseStr, true);

                                         }

                                         @Override
                                         public void onFailure(Throwable t, String resonseStr) {
                                             updateView("onFailure:"+t.getMessage(), true);

                                         }
                                     });*/

            //String uid, String dateType, String roleid, int page
            /*UserT user=new UserT(43241231,"wisn");
            apiController.excutePost("WSS/app/test",
                                     user,
                                     UserTResponse.class,
                                     new NetListener<UserTResponse>() {
                @Override
                public void onSuccess(UserTResponse response, int code, String msg, String resonseStr) {
                    updateView("onResponse:"+response+" "+resonseStr, true);
                }

                @Override
                public void onFailure(Throwable t, String resonseStr) {
                    updateView("onFailure:"+t.getMessage(), true);
                }
            });*/


            //新增测试接口
            /*try {
                User user = new User();
                user.setFirstname("wisn");
                user.setLastname("kmk");
                user.setEmailAddress("wuyishun_kmk@outlook.com");
                user.setRoles(roles);
                user.setUsername("adminaaa" + new Random().nextInt(100));
                user.setPassword("admin");
                CommonUtils.callSecured(context.getApplicationContext(),
                                        "/api/device-mgt/v1.0/users",
                                        Constants.HTTP_METHODS.POST, user.toJSON(), this,
                                        ConstantsApp.GETUSER);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
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
