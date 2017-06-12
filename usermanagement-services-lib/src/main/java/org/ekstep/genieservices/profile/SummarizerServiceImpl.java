package org.ekstep.genieservices.profile;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ISummarizerService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.CoRelation;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummary;
import org.ekstep.genieservices.commons.bean.LearnerContentSummaryDetails;
import org.ekstep.genieservices.commons.bean.SummaryRequest;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.profile.db.model.LearnerAssessmentDetailsModel;
import org.ekstep.genieservices.profile.db.model.LearnerAssessmentSummaryModel;
import org.ekstep.genieservices.profile.db.model.LearnerContentSummaryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class is the implementation of {@link ISummarizerService}
 */
public class SummarizerServiceImpl extends BaseService implements ISummarizerService {


    private static final String TAG = SummarizerServiceImpl.class.getSimpleName();

    public SummarizerServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<List<LearnerAssessmentSummary>> getSummary(SummaryRequest summaryRequest) {
        LearnerAssessmentSummaryModel learnerAssessmentSummaryModel = null;
        GenieResponse<List<LearnerAssessmentSummary>> response;
        String methodName = "getSummary@LearnerAssessmentsServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "2");

        if (summaryRequest.getUid() != null) {
            learnerAssessmentSummaryModel = LearnerAssessmentSummaryModel.findChildProgressSummary(mAppContext.getDBSession(), summaryRequest.getUid());
        } else if (summaryRequest.getContentId() != null) {
            learnerAssessmentSummaryModel = LearnerAssessmentSummaryModel.findContentProgressSummary(mAppContext.getDBSession(), summaryRequest.getContentId());
        }

        //if the assembleMap list size is 0 then their was some error
        if (learnerAssessmentSummaryModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROCESSING_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_SUMMARY, TAG);
            return response;
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(learnerAssessmentSummaryModel.getAssessmentMap());
        return response;
    }

    @Override
    public GenieResponse<List<LearnerAssessmentDetails>> getLearnerAssessmentDetails(SummaryRequest summaryRequest) {
        GenieResponse<List<LearnerAssessmentDetails>> response;
        String methodName = "getLearnerAssessmentDetails@LearnerAssessmentsServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "2");

        String filter = getFilterForLearnerAssessmentDetails(null, summaryRequest.getUid(), summaryRequest.getContentId(), null);

        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.find(mAppContext.getDBSession(), filter);
        if (learnerAssessmentDetailsModel == null) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROCESSING_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_FIND_SUMMARY, TAG);
            return response;
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        response.setResult(learnerAssessmentDetailsModel.getAllAssessments());
        return response;
    }

    private String getFilterForLearnerAssessmentDetails(String qid, String uid, String contentId, String hierarchyData) {
        String isQid = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_QID, qid);
        String isUid = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_UID, uid);
        String isContentId = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_CONTENT_ID, contentId);
        String isHData = String.format(Locale.US, "%s = '%s'", LearnerAssessmentsEntry.COLUMN_NAME_HIERARCHY_DATA, hierarchyData == null ? "" : hierarchyData);

        String filter;
        if (StringUtil.isNullOrEmpty(qid)) {
            filter = String.format(Locale.US, "where %s AND %s AND %s", isUid, isContentId, isHData);
        } else {
            filter = String.format(Locale.US, "where %s AND %s AND %s AND %s", isUid, isContentId, isHData, isQid);
        }
        return filter;
    }


    @Override
    public GenieResponse<Void> saveLearnerAssessmentDetails(Telemetry telemetry) {
        GenieResponse<Void> response;
        String methodName = "saveLearnerAssessmentDetails@LearnerAssessmentsServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "2");
        LearnerAssessmentDetails learnerAssessmentDetails = mapTelemtryToLearnerAssessmentData(telemetry);
        LearnerAssessmentDetailsModel learnerAssessmentDetailsModel = LearnerAssessmentDetailsModel.build(mAppContext.getDBSession(), learnerAssessmentDetails);

        String filter = getFilterForLearnerAssessmentDetails(learnerAssessmentDetails.getQid(), learnerAssessmentDetails.getUid(), learnerAssessmentDetails.getContentId(), learnerAssessmentDetails.getHierarchyData());

        //check if the learner assessment already exists
        if (LearnerAssessmentDetailsModel.find(mAppContext.getDBSession(), filter) == null) {
            learnerAssessmentDetailsModel.save();
        } else {
            learnerAssessmentDetailsModel.update();
        }

        if (learnerAssessmentDetailsModel.getInsertedId() == -1) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.PROCESSING_ERROR, ServiceConstants.ErrorMessage.UNABLE_TO_SAVE_LEARNER_ASSESSMENT, TAG);
            return response;
        }

        response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
        return response;
    }

    @Override
    public GenieResponse<Void> saveLearnerContentSummaryDetails(Telemetry telemetry) {
        GenieResponse<Void> response;
        String methodName = "saveLearnerContentSummaryDetails@LearnerAssessmentsServiceImpl";
        HashMap params = new HashMap();
        params.put("logLevel", "2");
        LearnerContentSummaryDetails learnerContentSummaryDetails = mapTelemtryToLearnerContentSummaryDeatils(telemetry);
        LearnerContentSummaryModel learnerContentSummaryModel;

        LearnerContentSummaryModel learnerContentSummaryModelInDB = LearnerContentSummaryModel.find(mAppContext.getDBSession(), learnerContentSummaryDetails.getUid(), learnerContentSummaryDetails.getContentId(),
                learnerContentSummaryDetails.getHierarchyData() == null ? "" : learnerContentSummaryDetails.getHierarchyData());

        //Check if the learner content summary already exists
        if (learnerContentSummaryModelInDB == null) {
            learnerContentSummaryDetails.setAvgts(learnerContentSummaryDetails.getTimespent());
            learnerContentSummaryDetails.setSessions(1);
            learnerContentSummaryDetails.setTotalts(learnerContentSummaryDetails.getTimespent());
            learnerContentSummaryDetails.setLastUpdated(learnerContentSummaryDetails.getTimestamp());
            //save with new details
            learnerContentSummaryModel = LearnerContentSummaryModel.build(mAppContext.getDBSession(), learnerContentSummaryDetails);
            learnerContentSummaryModel.save();

        } else {
            if (learnerContentSummaryModelInDB.getTimespent() != null) {
                learnerContentSummaryDetails.setSessions(learnerContentSummaryModelInDB.getSessions() + 1);
                learnerContentSummaryDetails.setTotalts(learnerContentSummaryModelInDB.getTotalts() + learnerContentSummaryModelInDB.getTimespent());
                learnerContentSummaryDetails.setAvgts(learnerContentSummaryModelInDB.getTotalts() / learnerContentSummaryModelInDB.getSessions());
                learnerContentSummaryDetails.setLastUpdated(learnerContentSummaryModelInDB.getTimestamp());
                //update with new details
                learnerContentSummaryModel = LearnerContentSummaryModel.build(mAppContext.getDBSession(), learnerContentSummaryDetails);
                learnerContentSummaryModel.update();
            }
        }

        return null;
    }

    private LearnerContentSummaryDetails mapTelemtryToLearnerContentSummaryDeatils(Telemetry telemetry) {
        LearnerContentSummaryDetails learnerContentSummaryDetails = new LearnerContentSummaryDetails();
        learnerContentSummaryDetails.setUid(telemetry.getUid());
        learnerContentSummaryDetails.setContentId(telemetry.getGdata().getId());
        Map<String, Object> eks = (Map<String, Object>) telemetry.getEData().get("eks");
        learnerContentSummaryDetails.setTimespent((Double) eks.get("length"));
        if ("2.0".equalsIgnoreCase(telemetry.getVer())) {
            learnerContentSummaryDetails.setTimestamp((Long) telemetry.getEts());
        } else {
            learnerContentSummaryDetails.setTimestamp(DateUtil.dateToEpoch(telemetry.getTs()));
        }

        if (telemetry.getCdata() != null) {
            List<String> idList = new ArrayList<>();
            for (CoRelation eachCdataValue : telemetry.getCdata()) {
                idList.add(eachCdataValue.getId());
            }
            learnerContentSummaryDetails.setHierarchyData(StringUtil.join(",", idList));
        }

        return learnerContentSummaryDetails;
    }

    private LearnerAssessmentDetails mapTelemtryToLearnerAssessmentData(Telemetry telemetry) {
        LearnerAssessmentDetails learnerAssessmentDetails = new LearnerAssessmentDetails();
        learnerAssessmentDetails.setUid(telemetry.getUid());
        learnerAssessmentDetails.setContentId(telemetry.getGdata().getId());
        Map<String, Object> eks = (Map<String, Object>) telemetry.getEData().get("eks");
        learnerAssessmentDetails.setQid((String) eks.get("qid"));
        learnerAssessmentDetails.setQindex((Double) eks.get("qindex"));
        String pass = (String) eks.get("pass");
        learnerAssessmentDetails.setCorrect(("Yes".equalsIgnoreCase(pass) ? 1 : 0));
        learnerAssessmentDetails.setScore((Double) eks.get("score"));
        learnerAssessmentDetails.setTimespent((Double) eks.get("length"));
        if ("2.0".equalsIgnoreCase(telemetry.getVer())) {
            learnerAssessmentDetails.setTimestamp((Long) telemetry.getEts());
            learnerAssessmentDetails.setRes(GsonUtil.toJson(eks.get("resvalues")));
        } else {
            learnerAssessmentDetails.setTimestamp(DateUtil.dateToEpoch(telemetry.getTs()));
            learnerAssessmentDetails.setRes(GsonUtil.toJson(eks.get("res")));
        }
        learnerAssessmentDetails.setQdesc((String) eks.get("qdesc"));
        learnerAssessmentDetails.setQtitle((String) eks.get("qtitle"));
        learnerAssessmentDetails.setMaxScore((Double) eks.get("maxscore"));

        if (telemetry.getCdata() != null) {
            List<String> idList = new ArrayList<>();
            for (CoRelation eachCdataValue : telemetry.getCdata()) {
                idList.add(eachCdataValue.getId());
            }
            learnerAssessmentDetails.setHierarchyData(StringUtil.join(",", idList));
        }
        return learnerAssessmentDetails;
    }


}
