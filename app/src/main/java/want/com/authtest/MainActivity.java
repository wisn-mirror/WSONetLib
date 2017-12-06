package want.com.authtest;

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
import com.want.wso2.auth.Authenticator;
import com.want.wso2.auth.ChangePasswordCallBack;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.bean.TokenResponse;
import com.want.wso2.callback.JsonCallback;
import com.want.wso2.convert.StringConvert;
import com.want.wso2.db.DownloadManager;
import com.want.wso2.db.UploadManager;
import com.want.wso2.download.Download;
import com.want.wso2.download.DownloadListener;
import com.want.wso2.download.Upload;
import com.want.wso2.interfaces.LoginExpireCallBack;
import com.want.wso2.interfaces.RegisterListener;
import com.want.wso2.model.Progress;
import com.want.wso2.request.GetRequest;
import com.want.wso2.request.PostRequest;
import com.want.wso2.task.XExecutor;
import com.want.wso2.upload.UploadListener;
import com.want.wso2.upload.UploadTask;

import java.io.File;
import java.util.List;
import java.util.Random;

import want.com.authtest.aaa.ConfigurationBean;

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
            supplySCOPES =
            "default appm:read perm:supply:view perm:supply:add perm:supply:delete perm:supply:update";
    private static String PERM = SCOPES;
    private ScrollView mScroll_info;
    private TextView mUsers;

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
    public void onClick(View view) {
        String Ip = url.getText().toString();
        String strUserName = userName.getText().toString();
        String strPassword = password.getText().toString();
        Log.e(TAG, strUserName + "  " + strPassword);
        if (view == register) {
            String applicationName =
                    "supply_android_" + DeviceUtils.getDeviceName(context);
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
                                       public void onFailure(String resonseStr, int code) {
                                           updateView("onFailure:code:" + code + " " + resonseStr, true);

                                       }

                                       @Override
                                       public void netWorkError(String msg) {
                                           updateView("netWorkError:" + msg, true);
                                       }
                                   });
        } else if (view == getConfig) {
            WSONet.<ConfigurationBean>get(
                    Ip + "/api/device-mgt/android/v1.0/configuration")
                    .execute(new JsonCallback<ConfigurationBean>() {
                        /**
                         * 请求成功，具体code在JsonCallback中定制
                         * @param response
                         */
                        @Override
                        public void onSuccess(com.want.wso2.model.Response<ConfigurationBean> response) {
                            if (response.body().fault != null) {
                                ConfigurationBean body = response.body();
                                updateView("onSuccess:" + response.body().fault.toString(), true);
                            } else {
                                updateView("onSuccess:" + response.body().toString(), true);
                            }
                        }

                        /**
                         * 请求错误，网络切换
                         * @param response
                         */
                        @Override
                        public void onError(com.want.wso2.model.Response<ConfigurationBean> response) {
                            super.onError(response);
                            updateView("onFailure:" + response.body(), true);

                        }

                        /**
                         * 无网络 在发起网络请求之前，检查网络，无网络回调
                         * @param msg
                         */
                        @Override
                        public void netWorkError(String msg) {
                            super.netWorkError(msg);
                            updateView("netWorkError:" + msg, true);
                        }
                    });

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
            final String urls = Ip + "/api/svm/v1.0/supply/homepage";
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    for (int i = 0; i < 10; i++) {
                    WSONet.<String>get(urls)
                            .tag("homepage")
                            .params("factory_code", "12456789")
                            .execute(new JsonCallback<String>() {
                                @Override
                                public void onSuccess(com.want.wso2.model.Response<String> response) {
                                    if (response.body() != null) {
                                        updateView("onSuccess:" + response.body().toString(), true);
                                    }
                                    //                            updateView("onSuccess:" + response.body().toString(), true);

                                }

                                @Override
                                public void onError(com.want.wso2.model.Response<String> response) {
                                    super.onError(response);
                                    updateView("onFailure:" + response.body(), true);

                                }

                                @Override
                                public void netWorkError(String msg) {
                                    super.netWorkError(msg);
                                    updateView("netWorkError:" + msg, true);
                                }
                            });
//                    }
                }
            }).start();
           /*final String urls= Ip + "/api/svm/v1.0/index/home";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        WSONet.<String>get(urls)
                                .tag("changepassword")
                                .execute(new JsonCallback<String>() {
                                    @Override
                                    public void onSuccess(com.want.wso2.model.Response<String> response) {
                                        if (response.body() != null) {
                                            updateView("onSuccess:" + response.body().toString(), true);
                                        }
                                        //                            updateView("onSuccess:" + response.body().toString(), true);

                                    }

                                    @Override
                                    public void onError(com.want.wso2.model.Response<String> response) {
                                        super.onError(response);
                                        updateView("onFailure:" + response.body(), true);

                                    }

                                    @Override
                                    public void netWorkError(String msg) {
                                        super.netWorkError(msg);
                                        updateView("netWorkError:" + msg, true);
                                    }
                                });
                    }
                }
            }).start();*/

/*
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
                    });*/

        } else if (view == mUsers) {
            updateView("isLogin:" + Authenticator.isLogin(), true);

            /*WSONet.<IndexPage>post(
                    Ip + "/api/yunwang/v1.0/machinerank/indexpage")
                    .upJson("{\"uid\":\"20\",\"roleid\":1}")
                    .execute(new JsonCallback<IndexPage>() {
                        @Override
                        public void onSuccess(com.want.wso2.model.Response<IndexPage> response) {
                            if (response.body().fault != null) {
                                updateView("onSuccess:" + response.body().fault.toString(), true);
                            }
                            updateView("onSuccess:" + response.body().toString(), true);

                        }

                        @Override
                        public void onError(com.want.wso2.model.Response<IndexPage> response) {
                            super.onError(response);
                            updateView("onFailure:" + response.body(), true);

                        }
                    });*/

        } else if (view == shareText) {
            download();
            /*String result = mTestResult.getText().toString();
            if (!TextUtils.isEmpty(result)) {
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT, result);
                intent1.setType("text/plain");
                startActivity(Intent.createChooser(intent1, "share"));
            } else {
                toast("结果为空！");
            }*/
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

    public void download() {
        try {
            Upload uploadTask = Upload.getInstance();
            uploadTask.getThreadPool().setCorePoolSize(3);
            //拿到断点的历史记录开始
            List<Progress> all = UploadManager.getInstance().getAll();
            uploadTask.restore(all);
            //开始所有的任务
            uploadTask.startAll();
            PostRequest<String> postRequest = WSONet.<String>post("url")//
                                                                        .headers("aaa", "111")//
                                                                        .params("bbb", "222")//
                                                                        .params("fileKey",
                                                                        new File("file path"))//
                                                                        .converter(new StringConvert());

            UploadTask<String> task = Upload.request("file path", postRequest)//
                                           .priority(new Random().nextInt(100))//
                                           .extra1("额外的")//
                                           .save();//保存到任务中
            /**
             * task注册上传监听
             */
            task.register(new UploadListener<String>("TAG") {
                @Override
                public void onStart(Progress progress) {
                }
                @Override
                public void onProgress(Progress progress) {
                }
                @Override
                public void onError(Progress progress) {
                }
                @Override
                public void onFinish(String s, Progress progress) {
                }
                @Override
                public void onRemove(Progress progress) {
                }
            });
            //开始上传
            task.start();
            /**
             * 设置全局监听
             */
            uploadTask.addOnAllTaskEndListener(new XExecutor.OnAllTaskEndListener() {
                @Override
                public void onAllTaskEnd() {

                }
            });
            /**
             * 移除所有任务
             */
            Upload.getInstance().removeAll();
            //设置下载目录
            Download download = Download.getInstance();

            download.setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaa/");
            //自定义设置下载线程池
            download.getThreadPool().setCorePoolSize(3);
            //获取断点下载的进度
            List<Progress> progressList = DownloadManager.getInstance().getAll();
            //加载所有没有下载完成的任务
            download.restore(progressList);
            //开始所有的下载任务
            download.startAll();
            GetRequest<File>
                    request =
                    WSONet.<File>get(
                            "http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0b8b552a1df0a8bc417a5afae3a26b2fb1342a909/com.qiyi.video.apk")
                            .headers("aaa", "111")
                            .params("bbb", "222");
            Download.request("Tag", request)
                    .save()
                    .register(new DownloadListener("Tag") {
                        /**
                         * 开始下载监听
                         * @param progress
                         */
                        @Override
                        public void onStart(Progress progress) {
                            Log.d(TAG,
                                  "onStart  currentSize:" +
                                  progress.currentSize +
                                  "totalSize:" +
                                  progress.totalSize);
                        }

                        /**
                         * 下载进度监听
                         * @param progress
                         */
                        @Override
                        public void onProgress(Progress progress) {
                            Log.d(TAG,
                                  "onProgress  currentSize:" +
                                  progress.currentSize +
                                  "totalSize:" +
                                  progress.totalSize);
                        }

                        /**
                         * 下载错误监听
                         * @param progress
                         */
                        @Override
                        public void onError(Progress progress) {
                            Log.d(TAG,
                                  "onError  currentSize:" +
                                  progress.currentSize +
                                  "totalSize:" +
                                  progress.totalSize);
                        }

                        /**
                         * 下载完成监听
                         * @param file
                         * @param progress
                         */
                        @Override
                        public void onFinish(File file, Progress progress) {
                            Log.d(TAG,
                                  "onFinish  currentSize:" +
                                  progress.currentSize +
                                  "totalSize:" +
                                  progress.totalSize);
                        }

                        /**
                         * 移除下载监听
                         * @param progress
                         */
                        @Override
                        public void onRemove(Progress progress) {
                            Log.d(TAG,
                                  "onRemove  currentSize:" +
                                  progress.currentSize +
                                  "totalSize:" +
                                  progress.totalSize);
                        }
                    })
                    .start();
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
