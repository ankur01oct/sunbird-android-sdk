package org.ekstep.genieservices.content;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.CourseBatchesRequest;
import org.ekstep.genieservices.commons.bean.EnrollCourseRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.bean.UpdateContentStateRequest;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.network.BatchDetailsAPI;
import org.ekstep.genieservices.content.network.CourseBatchesAPI;
import org.ekstep.genieservices.content.network.EnrolCourseAPI;
import org.ekstep.genieservices.content.network.EnrolledCoursesAPI;
import org.ekstep.genieservices.content.network.UpdateContentStateAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 9/3/18.
 *
 * @author anil
 */
public class CourseHandler {

    private static Map<String, String> getCustomHeaders(Session authSession) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Authenticated-User-Token", authSession.getAccessToken());
        return headers;
    }

    public static GenieResponse fetchEnrolledCoursesFromServer(AppContext appContext, Session sessionData, String userId) {
        EnrolledCoursesAPI enrolledCoursesAPI = new EnrolledCoursesAPI(appContext, getCustomHeaders(sessionData), userId);
        return enrolledCoursesAPI.get();
    }

    public static void refreshEnrolledCoursesFromServer(final AppContext appContext, final Session sessionData,
                                                        final String userId, final NoSqlModel enrolledCoursesInDB) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GenieResponse enrolledCoursesAPIResponse = fetchEnrolledCoursesFromServer(appContext, sessionData, userId);
                if (enrolledCoursesAPIResponse.getStatus()) {
                    String jsonResponse = enrolledCoursesAPIResponse.getResult().toString();
                    if (!StringUtil.isNullOrEmpty(jsonResponse)) {
                        enrolledCoursesInDB.setValue(jsonResponse);
                        enrolledCoursesInDB.update();
                    }
                }
            }
        }).start();
    }

    public static GenieResponse enrolCourseInServer(AppContext appContext, Session sessionData, EnrollCourseRequest enrollCourseRequest) {
        EnrolCourseAPI enrolCourseAPI = new EnrolCourseAPI(appContext, getCustomHeaders(sessionData),
                getEnrolCourseRequest(enrollCourseRequest));
        return enrolCourseAPI.post();
    }

    private static Map<String, Object> getEnrolCourseRequest(EnrollCourseRequest enrollCourseRequest) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", enrollCourseRequest.getUserId());
        requestMap.put("contentId", enrollCourseRequest.getContentId());
        requestMap.put("courseId", enrollCourseRequest.getCourseId());
        requestMap.put("batchId", enrollCourseRequest.getBatchId());
        return requestMap;
    }

    public static GenieResponse updateContentStateInServer(AppContext appContext, Session sessionData,
                                                           UpdateContentStateRequest updateContentStateRequest) {

        UpdateContentStateAPI updateContentStateAPI = new UpdateContentStateAPI(appContext, getCustomHeaders(sessionData),
                getUpdateContentStateRequest(updateContentStateRequest));
        return updateContentStateAPI.patch();
    }

    private static Map<String, Object> getUpdateContentStateRequest(UpdateContentStateRequest updateContentStateRequest) {
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("contentId", updateContentStateRequest.getContentId());
        contentMap.put("courseId", updateContentStateRequest.getCourseId());
        contentMap.put("batchId", updateContentStateRequest.getBatchId());
        contentMap.put("status", updateContentStateRequest.getStatus());
        contentMap.put("progress", updateContentStateRequest.getProgress());

        if (!StringUtil.isNullOrEmpty(updateContentStateRequest.getResult())) {
            contentMap.put("result", updateContentStateRequest.getResult());
        }
        if (!StringUtil.isNullOrEmpty(updateContentStateRequest.getGrade())) {
            contentMap.put("grade", updateContentStateRequest.getGrade());
        }
        if (!StringUtil.isNullOrEmpty(updateContentStateRequest.getScore())) {
            contentMap.put("score", updateContentStateRequest.getScore());
        }

        List<Map<String, Object>> contents = new ArrayList<>();
        contents.add(contentMap);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", updateContentStateRequest.getUserId());
        requestMap.put("contents", contents);
        return requestMap;
    }

    public static GenieResponse fetchCourseBatchesFromServer(AppContext appContext, Session sessionData,
                                                             CourseBatchesRequest courseBatchesRequest) {
        CourseBatchesAPI courseBatchesAPI = new CourseBatchesAPI(appContext, getCustomHeaders(sessionData),
                getCourseBatchesRequest(courseBatchesRequest));
        return courseBatchesAPI.post();
    }

    private static Map<String, Object> getCourseBatchesRequest(CourseBatchesRequest courseBatchesRequest) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("courseId", Arrays.asList(courseBatchesRequest.getCourseIds()));

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("filters", filterMap);
        return requestMap;
    }

    public static GenieResponse fetchBatchDetailsFromServer(AppContext appContext, Session sessionData,
                                                            String batchId) {
        BatchDetailsAPI batchDetailsAPI = new BatchDetailsAPI(appContext, getCustomHeaders(sessionData), batchId);
        return batchDetailsAPI.get();
    }
}
