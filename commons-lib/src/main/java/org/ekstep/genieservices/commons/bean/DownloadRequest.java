package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.DateUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public class DownloadRequest implements Serializable {

    private long downloadId = -1;
    private String identifier;
    private String downloadUrl;
    private String name;
    private String mimeType;
    private String destinationFolder;
    private long createdAt;
    private boolean isChildContent;
    private String processorClass;
    private List<CoRelation> coRelation;

    public DownloadRequest(String identifier, String downloadUrl, String mimeType, String destinationFolder, boolean isChildContent) {
        this.identifier = identifier;
        this.downloadUrl = downloadUrl;
        this.mimeType = mimeType;
        this.destinationFolder = destinationFolder;
        this.isChildContent = isChildContent;
        this.createdAt = DateUtil.getEpochTime();
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public boolean isChildContent() {
        return isChildContent;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public String getProcessorClass() {
        return processorClass;
    }

    public void setProcessorClass(String processorClass) {
        this.processorClass = processorClass;
    }

    public List<CoRelation> getCoRelation() {
        return coRelation;
    }

    public void setCoRelation(List<CoRelation> coRelation) {
        this.coRelation = coRelation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadRequest that = (DownloadRequest) o;
        return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }
}
