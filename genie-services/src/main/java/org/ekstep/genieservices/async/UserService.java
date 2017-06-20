package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessFilterCriteria;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportRequest;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileExportRequest;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.importexport.FileExporter;
import org.ekstep.genieservices.importexport.FileImporter;

import java.util.List;

/**
 * This class provides all the required APIs to perform necessary operations related to Users on a separate thread
 */
public class UserService {

    private IUserService userService;
    private FileImporter fileImporter;
    private FileExporter fileExporter;

    public UserService(GenieService genieService) {
        this.userService = genieService.getUserService();
        this.fileImporter = genieService.getFileImporter();
        this.fileExporter = genieService.getFileExporter();
    }

    /**
     * This api is used to create a new user with specific {@link Profile}.
     * <p>
     * <p>On Successful creation of new profile, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to delete a user, the response will have status as FALSE with the following error:
     * <p>FAILED - createProfile
     *
     * @param profile         - {@link Profile}
     * @param responseHandler - {@link IResponseHandler<Profile>}
     */
    public void createUserProfile(final Profile profile, IResponseHandler<Profile> responseHandler) {
        new AsyncHandler<Profile>(responseHandler).execute(new IPerformable<Profile>() {
            @Override
            public GenieResponse<Profile> perform() {
                return userService.createUserProfile(profile);
            }
        });
    }

    /**
     * This api is used to get all user profiles.
     * <p>
     * <p>On Successful creation of new profile, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to get all user profiles, the response will have status as FALSE
     * <p>
     *
     * @param responseHandler {@link IResponseHandler<List<Profile>>}
     */
    public void getAllUserProfile(IResponseHandler<List<Profile>> responseHandler) {
        new AsyncHandler<List<Profile>>(responseHandler).execute(new IPerformable<List<Profile>>() {
            @Override
            public GenieResponse<List<Profile>> perform() {
                return userService.getAllUserProfile();
            }
        });
    }

    /**
     * This api is used to delete a existing user with a specific uid
     * <p>
     * <p>
     * <p>On successful deletion of a user, the response will return status as TRUE and with result set as Profile related data
     * <p>
     * <p>On failing to create new profile, the response will have status as FALSE with the following error:
     * <p>FAILED
     *
     * @param uid
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void deleteUser(final String uid, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return userService.deleteUser(uid);
            }
        });
    }

    /**
     * This api sets the specific uid passed to it as active current user.
     * <p>
     * <p>
     * <p>On successful setting a user active, the response will return status as TRUE and with successful message
     * <p>
     * <p>On failing to set the uid to current user, the response will have status as FALSE with the following error:
     * <p>INVALID_USER
     *
     * @param uid
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void setCurrentUser(final String uid, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return userService.setCurrentUser(uid);
            }
        });
    }

    /**
     * This api gets the current active user.
     * <p>
     * <p>On successful fetching the active user, the response will return status as TRUE and with the active profile set in result.
     * <p>
     * <p>Their would be no failure case with this api, as it would by default had set anonymous user as active.
     *
     * @param responseHandler - {@link IResponseHandler<Profile>}
     */
    public void getCurrentUser(IResponseHandler<Profile> responseHandler) {
        new AsyncHandler<Profile>(responseHandler).execute(new IPerformable<Profile>() {
            @Override
            public GenieResponse<Profile> perform() {
                return userService.getCurrentUser();
            }
        });
    }

    /**
     * This api gets the current active user session.
     * <p>
     * <p>On successful fetching the active user session, the response will return status as TRUE and with the active user session set in result.
     * <p>
     * <p>Their would be no failure case with this api, as it would by default had set anonymous user session as active.
     *
     * @param responseHandler - {@link IResponseHandler<UserSession>}
     */
    public void getCurrentUserSession(IResponseHandler<UserSession> responseHandler) {
        new AsyncHandler<UserSession>(responseHandler).execute(new IPerformable<UserSession>() {
            @Override
            public GenieResponse<UserSession> perform() {
                return userService.getCurrentUserSession();
            }
        });
    }

    /**
     * This api gets the anonymous user the one if exists or a new anonymous user will be created.
     * <p>
     * <p>On successful fetching the anonymous user, the response will return status as TRUE and with the profile of anonymous user set in result.
     * <p>
     * <p>Their would be no failure case with this api, as it would get anonymous user if exists or a new one will be created.
     *
     * @param responseHandler - {@link IResponseHandler<Profile>}
     */
    public void getAnonymousUser(IResponseHandler<Profile> responseHandler) {
        new AsyncHandler<Profile>(responseHandler).execute(new IPerformable<Profile>() {
            @Override
            public GenieResponse<Profile> perform() {
                return userService.getAnonymousUser();
            }
        });
    }

    /**
     * This api gets the anonymous user from getAnonymousUser() api and sets it to current active user.
     * <p>
     * <p>On successful setting the anonymous user as active, the response will return status as TRUE and with the uid of anonymous user set in result.
     * <p>
     * <p>Their would be no failure case with this api, as it would get anonymous user if exists or a new one will be created and set to active user.
     *
     * @param responseHandler - {@link IResponseHandler<String>}
     */
    public void setAnonymousUser(IResponseHandler<String> responseHandler) {
        new AsyncHandler<String>(responseHandler).execute(new IPerformable<String>() {
            @Override
            public GenieResponse<String> perform() {
                return userService.setAnonymousUser();
            }
        });
    }

    /**
     * This api updates the specific profile that is possed to it.
     * <p>
     * <p>On successful updating the profile, the response will return status as TRUE and with the updated profile set in result.
     * <p>
     * <p>
     * <p>On failing to update the profile, the response will have status as FALSE with one of the following errors:
     * <p>INVALID_PROFILE
     * <p>VALIDATION_ERROR
     *
     * @param profile         - {@link Profile}
     * @param responseHandler - {@link IResponseHandler<Profile>}
     */
    public void updateUserProfile(final Profile profile, IResponseHandler<Profile> responseHandler) {
        new AsyncHandler<Profile>(responseHandler).execute(new IPerformable<Profile>() {
            @Override
            public GenieResponse<Profile> perform() {
                return userService.updateUserProfile(profile);
            }
        });
    }

    /**
     * This api is used to the set the state of learner.
     * <p>
     * <p> On successful setting the learner state, the response will return status as TRUE
     * <p>
     * <p> On failing to set the learner state, the response will have status as FALSE with the following error:
     * <p>PROFILE_NOT_FOUND
     *
     * @param contentAccess   - {@link ContentAccess}
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void addContentAccess(final ContentAccess contentAccess, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return userService.addContentAccess(contentAccess);
            }
        });
    }

    /**
     * This api gives the status each content of being accessed.
     * <p>
     * <p>Response status always be True, with all the contents access state set in result
     *
     * @param criteria        - {@link ContentAccessFilterCriteria}
     * @param responseHandler - {@link IResponseHandler<List<ContentAccess>>}
     */
    public void getAllContentAccess(final ContentAccessFilterCriteria criteria, IResponseHandler<List<ContentAccess>> responseHandler) {
        new AsyncHandler<List<ContentAccess>>(responseHandler).execute(new IPerformable<List<ContentAccess>>() {
            @Override
            public GenieResponse<List<ContentAccess>> perform() {
                return userService.getAllContentAccess(criteria);
            }
        });
    }

    /**
     * This api is used to import the profile.
     * <p>
     * <p> On successful importing the profile, the response will return status as TRUE.
     * <p>
     * <p>On failing to importing the profile, the response will return status as FALSE and the error be the following:
     * <p>IMPORT_FAILED
     *
     * @param importRequest   - {@link ImportRequest}
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void importProfile(final ImportRequest importRequest, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return fileImporter.importProfile(importRequest, userService);
            }
        });
    }

    /**
     * This api is used to export the profile.
     * <p>
     * <p> On successful exporting the telemetry, the response will return status as TRUE.
     * <p>
     * <p>On failing to exporting the telemetry, the response will return status as FALSE and the error be the following:
     * <p>EXPORT_FAILED
     *
     * @param profileExportRequest - {@link ProfileExportRequest}
     * @param responseHandler      - {@link IResponseHandler<ProfileExportResponse>}
     */
    public void exportProfile(final ProfileExportRequest profileExportRequest, IResponseHandler<ProfileExportResponse> responseHandler) {
        new AsyncHandler<ProfileExportResponse>(responseHandler).execute(new IPerformable<ProfileExportResponse>() {
            @Override
            public GenieResponse<ProfileExportResponse> perform() {
                return fileExporter.exportProfile(profileExportRequest, userService);
            }
        });
    }

}
