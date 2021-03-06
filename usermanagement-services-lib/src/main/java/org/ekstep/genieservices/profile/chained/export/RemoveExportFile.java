package org.ekstep.genieservices.profile.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.importexport.bean.ExportProfileContext;

import java.io.File;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class RemoveExportFile implements IChainable<ProfileExportResponse, ExportProfileContext> {

    private static final String TAG = RemoveExportFile.class.getSimpleName();
    private IChainable<ProfileExportResponse, ExportProfileContext> nextLink;

    @Override
    public GenieResponse<ProfileExportResponse> execute(AppContext appContext, ExportProfileContext exportContext) {

        File file = new File(exportContext.getDestinationDBFilePath());
        file.delete();

        if (nextLink != null) {
            return nextLink.execute(appContext, exportContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export profile failed", TAG);
        }
    }

    @Override
    public IChainable<ProfileExportResponse, ExportProfileContext> then(IChainable<ProfileExportResponse, ExportProfileContext> link) {
        nextLink = link;
        return link;
    }
}
