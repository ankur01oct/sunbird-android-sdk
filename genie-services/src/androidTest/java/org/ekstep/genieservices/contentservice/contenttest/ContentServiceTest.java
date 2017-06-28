package org.ekstep.genieservices.contentservice.contenttest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RecommendedContentResult;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.contentservice.AssertImportTelemetryEvent;
import org.ekstep.genieservices.contentservice.collectiontest.AssertCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Sneha on 5/31/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ContentServiceTest extends GenieServiceTestBase {
    public static final String VISIBILITY_DEFAULT = "default";
    private static final String TAG = ContentServiceTest.class.getSimpleName();
    final String CONTENT_ID = "do_30013486";
    final String CONTENT_ID_WITH_CHILD = "do_30019820";
    private final String CONTENT_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Multiplication2.ecar";
    private final String CONTENT_WITH_CHILD_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        GenieServiceDBHelper.clearEcarEntryFromDB();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void shouldImportEcarHavingNoChild() {
        String ext = FileUtil.getFileExtension(CONTENT_FILEPATH);

//        ContentImportRequest.Builder importRequest = new ContentImportRequest.Builder().isChildContent(false).fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        EcarImportRequest.Builder importEcar = new EcarImportRequest.Builder().fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<Void> response = activity.importEcar(importEcar.build());
        Assert.assertTrue("true", response.getStatus());
        Assert.assertNull(response.getError());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        GenieServiceDBHelper.findEcarDBEntry(CONTENT_ID);
        AssertCollection.verifyNoChildContentEntry(CONTENT_ID);

        //TODO : uncomment when telemetry events are implemented in the content service.
        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportInitiated("GE_INTERACT");
        AssertImportTelemetryEvent.verifyGeTransferIsLoggedForContentImport("GE_TRANSFER");
        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportSuccess("GE_INTERACT");

    }

    @Test
    public void shouldImportEcarHavingChild() {

        String ext = FileUtil.getFileExtension(CONTENT_WITH_CHILD_FILEPATH);

//        ContentImportRequest.Builder importRequest = new ContentImportRequest.Builder().isChildContent().fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        EcarImportRequest.Builder contentImportRequest = new EcarImportRequest.Builder().fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<Void> genieResponse = activity.importEcar(contentImportRequest.build());
        Log.v(TAG, "genieresponse :: " + genieResponse.getStatus());

        Assert.assertTrue("true", genieResponse.getStatus());
        Assert.assertNull(genieResponse.getError());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        //TODO : uncomment when telemetry events are implemented in the content service.
        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportInitiated("GE_INTERACT");
        AssertImportTelemetryEvent.verifyGeTransferIsLoggedForContentImport("GE_TRANSFER");
        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportSuccess("GE_INTERACT");

        GenieServiceDBHelper.findEcarDBEntry(CONTENT_ID_WITH_CHILD);
        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);
    }

    @Test
    public void shouldGetEcarDetails() {

        GenieServiceDBHelper.clearEcarEntryFromDB();

//        ContentImportRequest.Builder importRequest = new ContentImportRequest.Builder().isChildContent(false).fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        EcarImportRequest.Builder ecarImportReuqest = new EcarImportRequest.Builder().fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<Void> response = activity.importEcar(ecarImportReuqest.build());
        Assert.assertTrue("true", response.getStatus());
        Assert.assertNull(response.getError());

//        ContentDetailsRequest.Builder detailsRequest = new ContentDetailsRequest.Builder().contentId(CONTENT_ID);

        ContentDetailsRequest.Builder contentDetailsRequest = new ContentDetailsRequest.Builder().forContent(CONTENT_ID);

        GenieResponse<Content> genieResponseDetails = activity.getContentDetails(contentDetailsRequest.build());
        Assert.assertNotNull(genieResponseDetails.getResult());
        Assert.assertTrue(genieResponseDetails.getResult().isAvailableLocally());
        Assert.assertEquals("worksheet", genieResponseDetails.getResult().getContentType());
        Assert.assertEquals(CONTENT_ID, genieResponseDetails.getResult().getIdentifier());
        Assert.assertEquals(1, genieResponseDetails.getResult().getReferenceCount());
    }

    /**
     * Import collection and import a content which is a part collection.
     * Content list size has to be 1.
     * As the content is a part of collection.
     */
    @Test
    public void _1shouldGetAllLocalContent() {

//        ContentImportRequest.Builder importRequest = new ContentImportRequest.Builder().isChildContent(true).fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<Void> response = activity.importEcar(ecarImportRequest.build());
        Assert.assertTrue("true", response.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);

//        ContentImportRequest.Builder importRequest1 = new ContentImportRequest.Builder().isChildContent(false).fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        EcarImportRequest.Builder contentImportRequest = new EcarImportRequest.Builder().fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<Void> genieResponse = activity.importEcar(contentImportRequest.build());
        Assert.assertTrue("true", genieResponse.getStatus());
        AssertCollection.verifyContentEntryAndVisibility(CONTENT_ID, VISIBILITY_DEFAULT);

//        ContentCriteria.Builder contentCriteria = new ContentCriteria.Builder().contentTypes(new ContentType[]{ContentType.COLLECTION, ContentType.WORKSHEET});

        ContentFilterCriteria.Builder contentFilterCriteria = new ContentFilterCriteria.Builder().contentTypes(new ContentType[]{ContentType.COLLECTION, ContentType.WORKSHEET});

        GenieResponse<List<Content>> genieGetLocalResponse = activity.getAllLocalContent(contentFilterCriteria.build());
        Assert.assertTrue(genieGetLocalResponse.getStatus());
        Assert.assertNotNull(genieGetLocalResponse.getResult());
        Assert.assertEquals(2, genieGetLocalResponse.getResult().size());

    }

    /**
     * When the ContentCriteria is not passed
     * Note :: Though collection is imported the result comes as null for get local contents.
     */
    @Test
    public void _2shouldGetAllLocalContent() {
        GenieResponse<List<Content>> genieResponse = activity.getAllLocalContent(null);
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());
    }

    /**
     * TODO :: Test case fails
     * Any other assertions
     * In the result get recommended list comes as null.
     */
    @Test
    public void shouldGetAllRecommendedContent() {

        RecommendedContentRequest.Builder contentRequest = new RecommendedContentRequest.Builder().language("en");

        GenieResponse<RecommendedContentResult> genieResponse = activity.getRecommendedContent(contentRequest.build());
//        Assert.assertNotNull(genieResponse.getResult());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals("ekstep.analytics.recommendations", genieResponse.getResult().getId());
    }

    /**
     * TODO : test case fails
     * Any other assertions
     * In the result get related content comes as null.
     */
    @Test
    public void shouldGetRelatedContent() {

        RelatedContentRequest.Builder contentRequest = new RelatedContentRequest.Builder().contentId(CONTENT_ID);

        GenieResponse<RelatedContentResult> genieResponse = activity.getRelatedContent(contentRequest.build());
//        Assert.assertNotNull(genieResponse.getResult());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals("ekstep.analytics.recommendations", genieResponse.getResult().getId());
    }

    @Test
    public void shouldDeleteContent() {

        EcarImportRequest.Builder importRequest = new EcarImportRequest.Builder()
                .fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<Void> response = activity.importEcar(importRequest.build());
        Assert.assertTrue("true", response.getStatus());

        ContentDeleteRequest.Builder detailsRequest = new ContentDeleteRequest.Builder().contentId(CONTENT_ID);

        GenieResponse genieResponse = activity.deleteContent(detailsRequest.build());
        Assert.assertTrue(genieResponse.getStatus());
        AssertCollection.verifyContentIsDeleted(CONTENT_ID, activity, CONTENT_FILEPATH);
    }

    @Test
    public void shouldSearchContent() {

//        ContentSearchCriteria.Builder searchCriteria = new ContentSearchCriteria.Builder().query("collection").limit(10);

        ContentSearchCriteria.Builder contentSearchCriteria = new ContentSearchCriteria.Builder().query("collection").limit(10);
        GenieResponse<ContentSearchResult> response = activity.searchContent(contentSearchCriteria.build());
        Assert.assertTrue(response.getStatus());
        Assert.assertNotNull(response.getResult().getContentDataList());
        Assert.assertEquals("collection", response.getResult().getRequest().get("query"));
        Map responseObj = GsonUtil.fromMap(response.getResult().getRequest(), Map.class);
        Assert.assertEquals(10.0, responseObj.get("limit"));
    }
}
