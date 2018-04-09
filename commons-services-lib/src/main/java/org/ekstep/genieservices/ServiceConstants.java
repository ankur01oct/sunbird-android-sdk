package org.ekstep.genieservices;

/**
 * Created on 19/4/17.
 *
 * @author swayangjit
 */
public interface ServiceConstants {

    String SUCCESS_RESPONSE = "successful";
    String FAILED_RESPONSE = "failed";
    String VALIDATION_ERROR = "VALIDATION_ERROR";

    String DOWNLOAD_QUEUE = "download_queue";

    /* Key Constants for all the key value store */
    String KEY_USER_SESSION = "session";

    String VERSION = "version";
    String EXPORT_ID = "export_id";
    String DID = "did";
    String EVENTS_COUNT = "events_count";
    String PROFILES_COUNT = "profiles_count";
    String EXPORT_TYPES = "types";
    String EXPORT_TYPE_TELEMETRY = "telemetry";
    String EXPORT_TYPE_PROFILE = "userprofile";
    String FILE_SIZE = "FILE_SIZE";
    String FILE_TYPE = "FILE_TYPE";
    String CONTENT_ITEMS_COUNT_KEY = "content_count";
    String MASTER_DATA_TYPE_GENIE_CONFIG = "genieConfig";

    interface Params {
        String PROFILE_CONFIG = "PROFILE_CONFIG";
        String PLAYER_CONFIG = "PLAYER_CONFIG";
    }

    interface BundleKey {
        //ContentPlayer

        String BUNDLE_KEY_DOWNLOAD_REQUEST = "download_request";

    }

    interface FileExtension {
        String CONTENT = "ecar";
        String PROFILE = "epar";
        String TELEMETRY = "gsa";
        String APK = "apk";
    }

    interface PreferenceKey {
        //Sync service pref keys
        String LAST_SYNC_TIME = "lastSyncTime";

        //Partner
        String KEY_ACTIVE_PARTNER_ID = "partner.activeid";
        String SHARED_PREF_SESSION_KEY = "partnersessionid";
        String SHARED_PREF_PARTNER_SET_EPOCH = "partnerSET";

        //Download
        String KEY_DOWNLOAD_STATUS = "download_status";

        //Scan Storage
        String KEY_LAST_MODIFIED = "last_modified";
    }

    interface Partner {
        String KEY_PARTNER_ID = "partnerid";
    }

    interface Event {
        String ERROR_INVALID_EVENT = "Invalid event";
        String ERROR_INVALID_JSON = "Invalid Json";
    }

    interface APIExecutionMode {
        String MODE_WIFI = "WIFI";
        String MODE_MDATA = "MDATA";
        //        String MODE_LOCAL = "LOCAL";
        String MODE_NO_NETWORK = "";
    }

    interface Telemetry {
        String CONTENT_IMPORT_INITIATED_SUB_TYPE = "ContentImport-Initiated";
        String CONTENT_IMPORT_SUCCESS_SUB_TYPE = "ContentImport-Success";
        String CONTENT_IMPORT_STAGE_ID = "ImportContent";
        String CONTENT_DOWNLOAD_INITIATE = "ContentDownload-Initiate";
        String CONTENT_DOWNLOAD_SUCCESS = "ContentDownload-Success";
        String CONTENT_DOWNLOAD_CANCEL = "ContentDownload-Cancel";
        String CONTENT_DETAIL = "ContentDetail";

        String CONTENT_PLAYER = "ContentPlayer";
        String SESSION = "Session";
        String OBJECT_TYPE_CONTENT = "Content";
        String DEFAULT_ENVIRONMENT = "app";
        String SDK_ENVIRONMENT = "sdk";
    }

    interface ErrorCode {
        String PROCESSING_ERROR = "PROCESSING_ERROR";
        String DATA_NOT_FOUND_ERROR = "NO_DATA_FOUND";
        String VALIDATION_ERROR = "VALIDATION_ERROR";
        String INVALID_PROFILE = "INVALID_PROFILE";
        String PROFILE_NOT_FOUND = "PROFILE_NOT_FOUND";
        String INVALID_USER = "INVALID_USER";
        String EXPORT_FAILED = "EXPORT_FAILED";
        String IMPORT_FAILED = "IMPORT_FAILED";
        String MOVE_FAILED = "MOVE_FAILED";
        String SWITCH_FAILED = "SWITCH_FAILED";

        //Partner
        String UNREGISTERED_PARTNER = "UNREGISTERED_PARTNER";
        String ENCRYPTION_FAILURE = "ENCRYPTION_FAILURE";
        String MISSING_PARTNER_ID = "MISSING_PARTNER_ID";
        String MISSING_PUBLIC_KEY = "MISSING_PUBLIC_KEY";
        String INVALID_RSA_PUBLIC_KEY = "INVALID_RSA_PUBLIC_KEY";

        //Content
        String NO_DATA_FOUND = "NO_DATA_FOUND";
        String INVALID_FILE = "INVALID_FILE";
        String ECAR_NOT_FOUND = "ECAR_NOT_FOUND";

        // Notification
        String ADD_FAILED = "ADD_FAILED";
        String DELETE_FAILED = "DELETE_FAILED";

        // Authentication
        String TOKEN_GENERATION_FAILED = "TOKEN_GENERATION_FAILED";

        // KeyValueStore
        String KEY_NOT_FOUND = "KEY_NOT_FOUND";

        String AUTH_SESSION = "AUTH_SESSION";

        //Framework
        String NO_CHANNEL_DETAILS_FOUND = "NO_CHANNEL_DETAILS_FOUND";
        String NO_FRAMEWORK_DETAILS_FOUND = "NO_FRAMEWORK_DETAILS_FOUND";

        //Announcement
        String UPDATE_ANNOUNCEMENT_FAILED = "UPDATE_ANNOUNCEMENT_FAILED";
    }

    interface ErrorMessage {
        String UNABLE_TO_FIND_PROFILE = "Unable to find profile";
        String UNABLE_TO_FIND_SUMMARY = "Unable to find summary";
        String NO_USER_WITH_SPECIFIED_ID = "There is no user with specified id exists";

        String MANDATORY_FIELD_CONTENT_IDENTIFIER = "Content identifier could not be null or empty.";
        String NO_CONTENT_LISTING_DATA = "No data found.";

        String UNABLE_TO_CREATE_PROFILE = "Unable to create profile";
        String INVALID_PROFILE = "Invalid profile";
        String UNABLE_TO_UPDATE_PROFILE = "Unable to update profile";
        String UNABLE_TO_DELETE_PROFILE = "Unable to delete profile";
        String UNABLE_TO_SET_CURRENT_USER = "Unable to set current user";

        //Config
        String UNABLE_TO_FIND_MASTER_DATA = "Unable to find master data.";
        String UNABLE_TO_FIND_RESOURCE_BUNDLE = "Unable to find resource bundle.";
        String UNABLE_TO_FIND_ORDINALS = "Unable to find ordinals.";

        //Tag
        String TAG_NAME_SHOULD_NOT_BE_EMPTY = "Tag name can't be null or empty.";
        String UNABLE_TO_FIND_TAG = "Tag name not found.";

        //Sync
        String UNABLE_TO_SYNC = "Sync Failed.";

        //Telemetry
        String UNABLE_TO_SAVE_EVENT = "Not able to save event";

        //Import
        String IMPORT_PROFILE_FAILED = "Import profile failed.";
        String IMPORT_TELEMETRY_FAILED = "Import telemetry failed.";
        String FILE_DOES_NOT_EXIST = "Content import failed, file doesn't exist.";
        String UNSUPPORTED_FILE = "Content import failed, unsupported file extension";
        String CONTENT_NOT_FOUND = "No content found for identifier = ";
        String NO_CONTENT_TO_EXPORT = "Nothing to export!";

        String FAILED_TO_DELETE_NOTIFICATION = "Failed to delete notification";
        String FAILED_TO_ADD_UPDATE_NOTIFICATION = "Failed to add/update  notification";

        //Authentication
        String FAILED_TO_GENERATE_TOKEN = "Failed to generate the bearer token";

        //KeyStore Value
        String UNABLE_TO_FIND_KEY = "Unable to find key";

        String NOT_WRITABLE = "Destination folder is not writable.";
        String NO_CONTENT_TO_MOVE = "Nothing to move.";

        String USER_NOT_SIGN_IN = "User is not sign in.";

        //Framework
        String UNABLE_TO_FIND_CHANNEL_DETAILS = "Unable to find channel details";
        String UNABLE_TO_FIND_FRAMEWORK_DETAILS = "Unable to find framework details";
        String UNABLE_TO_UPDATE_ANNOUNCEMENT = "Unable to update announcement";
    }

    interface SuccessMessage {
        String SCAN_SUCCESS_NO_CHANGES = "Scan success no changes available";
        String SCAN_SUCCESS_WITH_CHANGES = "Scan success and changes available";
    }
}
