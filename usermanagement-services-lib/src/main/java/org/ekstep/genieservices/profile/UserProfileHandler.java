package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileVisibilityRequest;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.network.ProfileVisibilityAPI;
import org.ekstep.genieservices.profile.network.TenantInfoAPI;
import org.ekstep.genieservices.profile.network.UserProfileDetailsAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 5/3/18.
 *
 * @author anil
 */
public class UserProfileHandler {

    public static GenieResponse fetchUserProfileDetailsFromServer(AppContext appContext, String userId, String fields) {
        UserProfileDetailsAPI userProfileDetailsAPI = new UserProfileDetailsAPI(appContext, userId, fields);
        return userProfileDetailsAPI.get();
    }

    public static void refreshUserProfileDetailsFromServer(final AppContext appContext, final String userId, final String fields, final NoSqlModel userProfileInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse userProfileDetailsAPIResponse = fetchUserProfileDetailsFromServer(appContext, userId, fields);
                if (userProfileDetailsAPIResponse.getStatus()) {
                    String jsonResponse = userProfileDetailsAPIResponse.getResult().toString();
                    if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                        userProfileInDB.setValue(jsonResponse);
                        userProfileInDB.update();
                    }
                }
            }
        }).start();
    }

    public static GenieResponse fetchTenantInfoFromServer(AppContext appContext, String slug) {
        TenantInfoAPI tenantInfoAPI = new TenantInfoAPI(appContext, slug);
        return tenantInfoAPI.get();
    }

    public static void refreshTenantInfoFromServer(final AppContext appContext, final String slug, final NoSqlModel tenantInfoInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse tenantInfoAPIResponse = fetchTenantInfoFromServer(appContext, slug);
                if (tenantInfoAPIResponse.getStatus()) {
                    String jsonResponse = tenantInfoAPIResponse.getResult().toString();
                    if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                        tenantInfoInDB.setValue(jsonResponse);
                        tenantInfoInDB.update();
                    }
                }
            }
        }).start();
    }

    public static GenieResponse setProfileVisibilityDetailsInServer(AppContext appContext, ProfileVisibilityRequest profileVisibilityRequest) {
        ProfileVisibilityAPI profileVisibilityAPI = new ProfileVisibilityAPI(appContext, getProfileVisibilityRequest(profileVisibilityRequest));
        return profileVisibilityAPI.post();
    }

    private static Map<String, Object> getProfileVisibilityRequest(ProfileVisibilityRequest profileVisibilityRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", profileVisibilityRequest.getUserId());

        if (profileVisibilityRequest.getPrivateFields() != null) {
            requestMap.put("private", profileVisibilityRequest.getPrivateFields());
        }

        if (profileVisibilityRequest.getPublicFields() != null) {
            requestMap.put("public", profileVisibilityRequest.getPublicFields());
        }

        return requestMap;
    }
}
