package want.com.authtest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.want.wso2.WSONet;
import com.want.wso2.auth.Authenticator;
import com.want.wso2.auth.ChangePasswordCallBack;
import com.want.wso2.auth.IdentityProxy;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.bean.TokenResponse;
import com.want.wso2.callback.JsonCallback;
import com.want.wso2.callback.StringCallback;
import com.want.wso2.convert.StringConvert;
import com.want.wso2.db.UploadManager;
import com.want.wso2.download.Upload;
import com.want.wso2.interfaces.LoginExpireCallBack;
import com.want.wso2.interfaces.RegisterListener;
import com.want.wso2.model.HttpParams;
import com.want.wso2.model.Progress;
import com.want.wso2.model.Response;
import com.want.wso2.request.PostRequest;
import com.want.wso2.task.XExecutor;
import com.want.wso2.upload.UploadListener;
import com.want.wso2.upload.UploadTask;
import com.want.wso2.utils.Base64Utils;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    public static final String Result = "Result";
    public static final int GETTAG = 1;
    public static final int GETPREM = 2;
    private TextView mTestResult, getTAG, getPrem, register, getConfig, shareText,
            shareTextdelete, changePassword,
            loginOut, getStatus;
    private EditText url, userName, password, newpassword;
    private Context context;
    private static String[] SUBSCRIBED_API = new String[]{"device_management"};
    private static String[] roles = new String[]{"admin"};
    /* public final static String SCOPES = "default appm:read" +
                                         " perm:android:enroll perm:android:disenroll" +
                                         " perm:android:view-configuration perm:android:manage-configuration";
    */
//   public final static String SCOPES = "default perm:machinerank:machinerank perm:machinerank:view";
//   public final static String SCOPES = "default perm:svm:view";
    public final static String SCOPES = "perm:jpush:register perm:svm:view perm:users:credentials openid";
    public final static String
            supplySCOPES ="perm:jpush:register perm:svm:view perm:svm:checkLocation perm:users:credentials openid";
    private static String PERM = SCOPES;
    private ScrollView mScroll_info;
    private TextView mUsers;
    //    private String downloadUrl="http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0b8b552a1df0a8bc417a5afae3a26b2fb1342a909/com.qiyi.video.apk";
    private String downloadUrl = "https://nodejs.org/dist/v8.9.3/node-v8.9.3.pkg";
    private long mDownloadid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        mScroll_info = (ScrollView) findViewById(R.id.scroll_info);
        mUsers = (TextView) findViewById(R.id.users);
        mTestResult = (TextView) findViewById(R.id.testResult);
        changePassword = (TextView) findViewById(R.id.changePassword);
        register = (TextView) findViewById(R.id.register);
        getConfig = (TextView) findViewById(R.id.getConfig);
        getTAG = (TextView) findViewById(R.id.getTAG);
        getPrem = (TextView) findViewById(R.id.getPrem);
        getStatus = (TextView) findViewById(R.id.getStatus);
        shareText = (TextView) findViewById(R.id.shareText);
        shareTextdelete = (TextView) findViewById(R.id.shareTextdelete);
        loginOut = (TextView) findViewById(R.id.loginOut);
        url = (EditText) findViewById(R.id.url);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        newpassword = (EditText) findViewById(R.id.newpassword);
        getPrem.setOnClickListener(this);
        getTAG.setOnClickListener(this);
        register.setOnClickListener(this);
        getConfig.setOnClickListener(this);
        shareText.setOnClickListener(this);
        shareTextdelete.setOnClickListener(this);
        mUsers.setOnClickListener(this);
        loginOut.setOnClickListener(this);
        getStatus.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        IntentFilter filter = new IntentFilter(
                android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        this.registerReceiver(downloadReceiver, filter);
        WSONet.getInstance().setLoginExpireCallBack(new LoginExpireCallBack() {
            @Override
            public void LoginExpire(String tag, int code) {
                if ("changepassword".equals(tag)) {

                } else {

                }
                updateView("  重新登录  " + code, true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        IntentFilter filter = new IntentFilter(
                android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        this.registerReceiver(downloadReceiver, filter);
        super.onDestroy();
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long referenceId = intent.getLongExtra(
                    android.app.DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.d(TAG, " downloadReceiver  downloadReceiver:" + referenceId);
        }
    };

    @Override
    public void onClick(View view) {
        String Ip = url.getText().toString();
        String strUserName = userName.getText().toString();
        String strPassword = password.getText().toString();
        Log.e(TAG, strUserName + "  " + strPassword);
        if (view == register) {
            String applicationName =
//                    "yunwang_android_" + DeviceUtils.getDeviceName(context);
                    "yunwang_android_" + UUID.nameUUIDFromBytes("867516026498122".getBytes());
            RegistrationProfileRequest registrationProfileRequest = new RegistrationProfileRequest();
            registrationProfileRequest.setApplicationName(applicationName);
            registrationProfileRequest.setIsAllowedToAllDomains(false);
            registrationProfileRequest.setIsMappingAnExistingOAuthApp(false);
           // registrationProfileRequest.setTags(SUBSCRIBED_API);
            registrationProfileRequest.setTags(new String[]{"device_management"});
            registrationProfileRequest.setConsumerKey(null);
            registrationProfileRequest.setConsumerSecret(null);
            Authenticator.register(Ip + "/api-application-registration/register",
                                   Ip + "/token",
                                   registrationProfileRequest,
                                   strUserName,
                                   strPassword,
                                   supplySCOPES,
                                   new RegisterListener() {
                                       @Override
                                       public void onSuccess(RegisterResponse response,
                                                             TokenResponse tokenResponse,
                                                             int code) {
                                           updateView("onSuccess:code:" +
                                                      code +
                                                      " " +
                                                      response.toJSON() +
                                                      tokenResponse.toJSON(), true);
                                           tokenResponse.getScope();

                                       }

                                       @Override
                                       public void onFailure(String message, int code) {
                                           updateView("onFailure:code:" + code + " " + message, true);

                                       }

                                       @Override
                                       public void netWorkError(String msg) {
                                           updateView("netWorkError:" + msg, true);
                                       }
                                   });
        } else if (view == getConfig) {
            upload(Ip);
            /*WSONet.<String> get(Ip+"/api/machine/v1.0/settings")
                    .params("machineId",1)
                    .execute(new JsonCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.d(TAG, "onSuccess: 获取配置参数成功.");
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.d(TAG, "onError: 获取配置参数失败...");

                        }
                    });*/

           /* mDownloadid =
                    DownloadManagerTest.startDownload(this.getApplicationContext(), downloadUrl, "aaa.apk");
            Log.d(TAG, "   DownloadManagerTest.startDownload:" + mDownloadid);
*/
        } else if (view == loginOut) {
            Authenticator.loginOut(Ip + "/api-application-registration/unregister",
                                   "supply_android_" + com.want.wso2.utils.DeviceUtils.getDeviceId(this));
        } else if (view == changePassword) {
            Authenticator.changePassword(Ip + "/api/device-mgt/v1.0/users/credentials",
                                         password.getText().toString(),
                                         newpassword.getText().toString(),
                                         new ChangePasswordCallBack() {
                                             @Override
                                             public void onSuccess(int code, String response) {
                                                 updateView("onSuccess:" + response, true);

                                             }

                                             @Override
                                             public void onError(int code, String response) {
                                                 updateView("onError:" + response + code, true);
                                             }

                                             @Override
                                             public void netWorkError(String msg) {
                                                 updateView("netWorkError:" + msg, true);
                                             }
                                         });

        } else if (view == getStatus) {
            testDetails(Ip);
        } else if (view == mUsers) {
            Log.d(TAG, "  mDownloadid:" + mDownloadid);
            int
                    downloadStatus =
                    DownloadManagerTest.getDownloadStatus(this.getApplicationContext(), mDownloadid);
            Log.d(TAG, "  mDownloadid:" + mDownloadid + " downloadStatus:" + downloadStatus);

            String
                    downloadFile =
                    DownloadManagerTest.getDownloadFile(this.getApplicationContext(), mDownloadid);
            Log.d(TAG, "  mDownloadid:" + mDownloadid + " downloadFile:" + downloadFile);

//            updateView("isLogin:" + Authenticator.isLogin(), true);
        } else if (view == shareText) {


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

    public void testDetails(final String ip) {
        WSONet.<String>get(ip + "/api/supply/v1.0/supply/details")
                .tag("homepage")
                .params("factory_code", "93007736")
                .execute(new JsonCallback<String>() {
                    @Override
                    public void onSuccess(com.want.wso2.model.Response<String> response) {
                        if (response.body() != null) {
                            updateView("onSuccess:" + response.body().toString(), true);
                        }
                        //                            updateView("onSuccess:" + response.body().toString(), true);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                testDetails(ip);
                            }
                        }, 200);

                    }

                    @Override
                    public void onError(com.want.wso2.model.Response<String> response) {
                        super.onError(response);
                        updateView("onFailure:" + response.body(), true);
                        testDetails(ip);
                    }

                    @Override
                    public void netWorkError(String msg) {
                        super.netWorkError(msg);
                        updateView("netWorkError:" + msg, true);
                    }
                });
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

    public void upload(String ip) {
        try {
            Upload uploadTask = Upload.getInstance();
//            uploadTask.getThreadPool().setCorePoolSize(3);
//            //拿到断点的历史记录开始
//            List<Progress> all = UploadManager.getInstance().getAll();
//            uploadTask.restore(all);
            //开始所有的任务
            List<File> files=new ArrayList<>();
            files.add(new File("/sdcard/Download/components1.png"));
            files.add(new File("/sdcard/Download/components1.png"));
            files.add(new File("/sdcard/Download/components1.png"));
            files.add(new File("/sdcard/Download/components1.png"));
            files.add(new File("/sdcard/Download/components1.png"));
            files.add(new File("/sdcard/Download/components1.png"));
            String json="{\"name\":\"点位名称331\",\"location_source\":\"点位来源\",\"address\":\"省市区街道\",\"coordinate\":\"a,b\",\"location_type\":1,\"other_location_name\":\"其他点位名称\",\"other_location_score\":22.5,\"placement_position\":1,\"other_placement_position_name\":\"其他摆放位置名称\",\"other_placement_position_score\":22.5,\"location_people_flow\":1,\"other_location_people_flow\":\"其他点位人流量\",\"other_location_people_flow_score\":22.5,\"age_ratio\":1,\"machine_num_two\":1,\"machine_num_one\":1,\"machine_num_three\":1,\"indoor_or_outdoor\":\"室内/室外\",\"indoor_outdoor_note\":\"室内室外备注\",\"has_power\":\"电源\",\"power_note\":\"电源备注\",\"wifi_status\":\"4G网络状况\",\"install_and_logistics\":\"布机和商品配送的物流限制\",\"total_score\":22.5,\"overall_merit\":\"综评\",\"app_assessment\":\"app评估\"}";
            String encode = Base64Utils.encode(json.getBytes());
            PostRequest<String> postRequest =
                    WSONet.<String>post(ip + "/user/upload")
//                            .headers("Content-Type", "multipart/form-data")
                            .headers("Authorization", "Bearer 43214321432143214321432143214" )
                            .headers("Accept", "application/json")
                            .addFileParams("filekey",files)
                            .params("filekey1",new File("/sdcard/Download/components1.png"))
                            .params("filekey2",new File("/sdcard/Download/components2.png"))
                            .params("filekey3",new File("/sdcard/Download/components3.png"))
                            .params("filekey4",new File("/sdcard/Download/components4.png"))
                            .params("checkLocationInfoJson", encode )
                            .converter(new JsonCallback<String>() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    updateView("onSuccess:" + response.body().toString(), true);
                                }
                                @Override
                                public void onError(Response<String> response) {
                                    super.onError(response);
                                }
                            });
            UploadTask<String> task = Upload.request("upload", postRequest)//
                                            .priority(new Random().nextInt(100))//
                                            .extra1("额外的")//
                                            .save();//保存到任务中
            task.register(new UploadListener<String>("TAG") {
                @Override
                public void onStart(Progress progress) {
                    updateView("onStart:" + progress.currentSize, true);
                }
                @Override
                public void onProgress(Progress progress) {
                    updateView("onProgress:" + progress.currentSize, true);
                }
                @Override
                public void onError(Progress progress) {
                    updateView("onError:" + progress.currentSize, true);
                }
                @Override
                public void onFinish(String s, Progress progress) {
                    updateView("onFinish:" + progress.currentSize, true);
                }
                @Override
                public void onRemove(Progress progress) {
                    updateView("onRemove:" + progress.currentSize, true);
                }
            });
            //开始上传
            task.start();
//            /**
//             * 设置全局监听
//             */
//            uploadTask.addOnAllTaskEndListener(new XExecutor.OnAllTaskEndListener() {
//                @Override
//                public void onAllTaskEnd() {
//
//                }
//            });
//            /**
//             * 移除所有任务
//             */
//            Upload.getInstance().removeAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toast(final String message) {

       /* WSONet.<ConfigurationBean>post("")
                .tag("")
                .execute(new JsonCallback<ConfigurationBean>() {
                    @Override
                    public void onSuccess(Response<ConfigurationBean> response) {
                        ConfigurationBean body = response.body();
                        List<Configuration> configuration = body.configuration;
                    }

                    @Override
                    public void onError(Response<ConfigurationBean> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void netWorkError(String msg) {
                        super.netWorkError(msg);
                    }
                });*/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
