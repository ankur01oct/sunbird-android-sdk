package org.ekstep.genieservices.commons;

import android.content.Context;

import org.ekstep.genieservices.Constants;
import org.ekstep.genieservices.commons.db.ServiceDbHelper;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.db.cache.PreferenceWrapper;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.download.AndroidDownloadManager;
import org.ekstep.genieservices.commons.network.AndroidHttpClient;
import org.ekstep.genieservices.commons.network.AndroidHttpClientFactory;
import org.ekstep.genieservices.commons.network.AndroidNetworkConnectivity;
import org.ekstep.genieservices.commons.network.IConnectionInfo;
import org.ekstep.genieservices.commons.network.IHttpClient;
import org.ekstep.genieservices.commons.network.IHttpClientFactory;
import org.ekstep.genieservices.commons.network.auth.BasicAuthenticator;

/**
 * Created on 18/4/17.
 */
public class AndroidAppContext extends AppContext<Context> {

    private IDBSession mDBSession;
    private IConnectionInfo mConnectionInfo;
    private IHttpClientFactory mHttpClientFactory;
    private IKeyValueStore mKeyValueOperation;
    private IDeviceInfo mDeviceInfo;
    private ILocationInfo mLocationInfo;
    private IParams mParams;
    private IDownloadManager mDownloadManager;

    private AndroidAppContext(Context context, String appPackage) {
        super(context, appPackage);
    }

    public static AppContext buildAppContext(Context context, String appPackage) {
        AndroidAppContext appContext = new AndroidAppContext(context, appPackage);
        appContext.setParams(new BuildParams(context, appPackage));
        appContext.setDBSession(ServiceDbHelper.getGSDBSession(appContext));
        appContext.setConnectionInfo(new AndroidNetworkConnectivity(appContext));
        appContext.setHttpClientFactory(new AndroidHttpClientFactory(appContext));
        appContext.setKeyValueStore(new PreferenceWrapper(context, Constants.SHARED_PREFERENCE_NAME));
        appContext.setDeviceInfo(new DeviceInfo(context));
        appContext.setLocationInfo(new LocationInfo(context));
        appContext.setDownloadManager(new AndroidDownloadManager(context));
        return appContext;
    }

    @Override
    public ILocationInfo getLocationInfo() {
        return mLocationInfo;
    }

    private void setLocationInfo(ILocationInfo mLocationInfo) {
        this.mLocationInfo = mLocationInfo;
    }

    @Override
    public IDBSession getDBSession() {
        return mDBSession;
    }

    @Override
    public Void setDBSession(IDBSession dbSession) {
        this.mDBSession = dbSession;
        return null;
    }

    @Override
    public IKeyValueStore getKeyValueStore() {
        return mKeyValueOperation;
    }

    private void setKeyValueStore(IKeyValueStore keyValueOperation) {
        this.mKeyValueOperation = keyValueOperation;
    }

    @Override
    public IConnectionInfo getConnectionInfo() {
        return mConnectionInfo;
    }

    private void setConnectionInfo(IConnectionInfo connectionInfo) {
        this.mConnectionInfo = connectionInfo;
    }

    @Override
    public IHttpClientFactory getHttpClientFactory() {
        return mHttpClientFactory;
    }

    private void setHttpClientFactory(IHttpClientFactory clientFactory) {
        this.mHttpClientFactory = clientFactory;
    }

    @Override
    public IParams getParams() {
        return mParams;
    }

    private void setParams(IParams params) {
        this.mParams = params;
    }

    @Override
    public IDeviceInfo getDeviceInfo() {
        return mDeviceInfo;
    }

    private void setDeviceInfo(IDeviceInfo deviceInfo) {
        this.mDeviceInfo = deviceInfo;
    }

    @Override
    public IDownloadManager getDownloadManager() {
        return mDownloadManager;
    }

    private void setDownloadManager(IDownloadManager downloadManager) {
        this.mDownloadManager = downloadManager;
    }

}
