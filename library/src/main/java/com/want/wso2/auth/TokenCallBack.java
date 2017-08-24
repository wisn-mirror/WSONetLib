package com.want.wso2.auth;

import com.want.wso2.bean.Token;

/**
 * Created by wisn on 2017/8/24.
 */

public interface TokenCallBack {
    /**
     * Get access token stored in the app preferences.
     *
     * @param status  - Status code.
     * @param message - Success/Error message.
     * @param token   - Token retrieved.
     */
    void receiveAccessToken(int status, String message, Token token);

    /**
     * Get a new access token from the server.
     *
     * @param status  - Status code.
     * @param message - Success/Error message.
     * @param token   - Token retrieved.
     */
    void receiveNewAccessToken(int status, String message, Token token);
}
