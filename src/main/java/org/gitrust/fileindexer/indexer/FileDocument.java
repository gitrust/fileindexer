package org.gitrust.fileindexer.indexer;

import lombok.*;

@Builder
@Getter
@ToString
public class FileDocument {
    public static final String FIELD_SIZE ="fileSize";
    public static final String FIELD_NAME ="fileName";
    public static final String FIELD_PATH ="filePath";

    private long fileSize;
    private String fileName;
    private String absolutePath;
    private int docId;
}
