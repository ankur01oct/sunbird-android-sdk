package org.ekstep.genieservices.auth;

import org.ekstep.genieservices.auth.network.AuthAPI;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.enums.JWTokenType;
import org.ekstep.genieservices.commons.network.NetworkConstants;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.JWTUtil;

import java.util.Map;

/**
 * Created on 18/7/17.
 */
public class AuthHandler {

    public static void resetAuthToken(AppContext appContext) {
        String mobileDeviceConsumerBearerToken = generateMobileDeviceConsumerBearerToken(appContext);
        if (mobileDeviceConsumerBearerToken != null) {
            appContext.getKeyValueStore().putString(NetworkConstants.API_BEARER_TOKEN, mobileDeviceConsumerBearerToken);
        }
    }

    private static String generateMobileDeviceConsumerBearerToken(AppContext appContext) {
        String mobileAppConsumerKey = appContext.getParams().getString(IParams.Key.MOBILE_APP_KEY);
        String mobileAppConsumerSecret = appContext.getParams().getString(IParams.Key.MOBILE_APP_SECRET);
        String mobileDeviceConsumerKey = appContext.getParams().getString(IParams.Key.PRODUCER_ID) + "-" + appContext.getDeviceInfo().getDeviceID();
        String mobileDeviceConsumerSecret = getMobileDeviceConsumerSecret(appContext, mobileDeviceConsumerKey, mobileAppConsumerKey, mobileAppConsumerSecret);
        String mobileDeviceConsumerBearerToken = null;
        if (mobileDeviceConsumerSecret != null) {
            mobileDeviceConsumerBearerToken = JWTUtil.createJWToken(mobileDeviceConsumerKey, mobileDeviceConsumerSecret, JWTokenType.HS256);
        }
        return mobileDeviceConsumerBearerToken;
    }

    private static String getMobileDeviceConsumerSecret(AppContext appContext, String mobileDeviceConsumerKey, String mobileAppConsumerKey, String mobileAppConsumerSecret) {
        String mobileAppConsumerBearerToken = JWTUtil.createJWToken(mobileAppConsumerKey, mobileAppConsumerSecret, JWTokenType.HS256);
        AuthAPI authAPI = new AuthAPI(appContext, mobileDeviceConsumerKey, mobileAppConsumerBearerToken);
        GenieResponse response = authAPI.post();
        String deviceSecret = null;
        if (response.getStatus()) {
            String body = response.getResult().toString();
            Map responseMap = GsonUtil.fromJson(body, Map.class);
            Map result = (Map) responseMap.get("result");
            if (result != null) {
                Object keyObj = result.get("secret");
                deviceSecret = keyObj == null ? null : keyObj.toString();
            }
        }
        return deviceSecret;
    }

}
