package org.ekstep.genieservices.contentservice.contenttest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.RecommendedContentCriteria;
import org.ekstep.genieservices.commons.bean.RecommendedContentResult;
import org.ekstep.genieservices.commons.bean.RelatedContentCriteria;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.contentservice.collectiontest.AssertCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

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
        GenieServiceDBHelper.clearContentDBEntry();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void shouldImportContentHavingNoChild() {
        String ext = FileUtil.getFileExtension(CONTENT_FILEPATH);

        GenieResponse<Void> response = activity.importContent(false, CONTENT_FILEPATH, activity.getExternalFilesDir(null));
        Assert.assertTrue("true", response.getStatus());
        Assert.assertNull(response.getError());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        GenieServiceDBHelper.findContentDBEntry(CONTENT_ID);
        AssertCollection.verifyNoChildContentEntry(CONTENT_ID);

        //TODO : uncomment when telemetry events are implemented in the content service.
//        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportInitiated("GE_INTERACT");
//        AssertImportTelemetryEvent.verifyGeTransferIsLoggedForContentImport("GE_TRANSFER");
//        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportSuccess("GE_INTERACT");

    }

    @Test
    public void shouldImportContentHavingChild() {

        String ext = FileUtil.getFileExtension(CONTENT_WITH_CHILD_FILEPATH);

        GenieResponse<Void> genieResponse = activity.importContent(true, CONTENT_WITH_CHILD_FILEPATH, activity.getExternalFilesDir(null));
        Log.v(TAG, "genieresponse :: " + genieResponse.getStatus());

        Assert.assertTrue("true", genieResponse.getStatus());
        Assert.assertNull(genieResponse.getError());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        //TODO : uncomment when telemetry events are implemented in the content service.
//        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportInitiated("GE_INTERACT");
//        AssertImportTelemetryEvent.verifyGeTransferIsLoggedForContentImport("GE_TRANSFER");
//        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportSuccess("GE_INTERACT");

        GenieServiceDBHelper.findContentDBEntry(CONTENT_ID_WITH_CHILD);
        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);
    }

    @Test
    public void shouldGetContentDetails() {

        GenieResponse<Void> response = activity.importContent(false, CONTENT_FILEPATH, activity.getExternalFilesDir(null));
        Assert.assertTrue("true", response.getStatus());
        Assert.assertNull(response.getError());

        GenieResponse<Content> genieResponseDetails = activity.getContentDetails(CONTENT_ID);

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

        GenieResponse<Void> response = activity.importContent(true, CONTENT_WITH_CHILD_FILEPATH, activity.getExternalFilesDir(null));
        Assert.assertTrue("true", response.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);

        GenieResponse<Void> genieResponse = activity.importContent(false, CONTENT_FILEPATH, activity.getExternalFilesDir(null));
        Assert.assertTrue("true", genieResponse.getStatus());
        AssertCollection.verifyContentEntryAndVisibility(CONTENT_ID, VISIBILITY_DEFAULT);

        ContentCriteria.Builder contentCriteria = new ContentCriteria.Builder().contentTypes(new ContentType[]{ContentType.COLLECTION, ContentType.WORKSHEET});

        GenieResponse<List<Content>> genieGetLocalResponse = activity.getAllLocalContent(contentCriteria.build());
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
     * Note :: In the result getrecommended list comes as null.
     * Note :: Any other assertions.
     */
    @Test
    public void shouldGetAllRecommendedContent() {

        RecommendedContentCriteria.Builder contentCriteria = new RecommendedContentCriteria.Builder().language("en");

        GenieResponse<RecommendedContentResult> genieResponse = activity.getRecommendedContent(contentCriteria.build());
        Assert.assertNotNull(genieResponse.getResult());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals("ekstep.analytics.recommendations", genieResponse.getResult().getId());
    }

    /**
     * Note :: Any other assertions.
     */
    @Test
    public void shouldGetRelatedContent() {

        RelatedContentCriteria.Builder contentCriteria = new RelatedContentCriteria.Builder().contentId(CONTENT_ID);

        GenieResponse<RelatedContentResult> genieResponse = activity.getRelatedContent(contentCriteria.build());
        Assert.assertNotNull(genieResponse.getResult());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals("ekstep.analytics.recommendations", genieResponse.getResult().getId());
    }
}
