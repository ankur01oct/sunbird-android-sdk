package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * This is the interface with the required API to get feedback about the content.
 */
public interface IContentFeedbackService {

    /**
     * This api is used to save the feedback about content.
     *
     * @param contentFeedback {@link ContentFeedback}
     * @return On failing to fetch the data, the response will return status as FALSE with the following error.
     */
    GenieResponse<Void> sendFeedback(ContentFeedback contentFeedback);

    /**
     * This api is used to save the feedback about content.
     *
     * @param contentFeedback {@link ContentFeedback}
     * @param stageId         Stage id
     * @return On failing to fetch the data, the response will return status as FALSE with the following error.
     */
    GenieResponse<Void> sendFeedback(ContentFeedback contentFeedback, String stageId);

    /**
     * This api is used to get the feedback about a content.
     * <p>
     * <p>On successful fetching the data, the response will return status as TRUE and with result type as {@link ContentFeedback}, if content has any feedback then the result will not be null,
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     *
     * @param uid               User id
     * @param contentIdentifier Content identifier
     * @return {@link GenieResponse<ContentFeedback>}
     */
    GenieResponse<ContentFeedback> getFeedback(String uid, String contentIdentifier);

}