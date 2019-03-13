package org.gitrust.fileindexer.indexer;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileDocument {
    public static final String FIELD_SIZE ="fileSize";
    public static final String FIELD_NAME ="fileName";
    public static final String FIELD_PATH ="filePath";

    private long fileSize;
    private String fileName;
    private String absolutePath;
}
