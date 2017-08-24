token sdk
初始化在Application中：

        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");
        try {
            WSONet.getInstance()
                  .init(this)
                 .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .addCommonHeaders(headers)                                         //设置全局公共头
                .addCommonParams(params);                                          //设置全局公共参数
        } catch (Exception e) {
            e.printStackTrace();
        }

注册：

            /**
             *
             *
             * @param registerUrl
             * @param tokenUrl
             * @param registrationProfileRequest
             * @param userName
             * @param password
             * @param scope
             * @param registerListener
             */
        Authenticator.register(String registerUrl,
                                final String tokenUrl,
                                RegistrationProfileRequest registrationProfileRequest,
                                final String userName,
                                final String password,
                                final String scope,
                                final RegisterListener registerListener)

  请求：

                WSONet.<BaseNetBean<UserTResponse>>post("http://10.0.86.120:8080/WSS/app/test")
                                                   .tag("tag")
                                                   .upObjectToJson(user)
                                                   .params("id",123432)
                                                   .params("name","wisn")
                                                   .params("password","nihao")
                                                   .execute(new JsonCallback<BaseNetBean<UserTResponse>>() {
                                                       @Override
                                                       public void onSuccess(com.want.wso2.model.Response<BaseNetBean<UserTResponse>> response) {
                                                       }

                                                       @Override
                                                       public void onError(com.want.wso2.model.Response<BaseNetBean<UserTResponse>> response) {
                                                           super.onError(response);
                                                       }
                                                   });
//设置下载目录

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