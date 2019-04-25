package org.gitrust.fileindexer.indexer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class FileDocument {
    public static final String FIELD_SIZE = "fileSize";
    public static final String FIELD_NAME = "fileName";
    public static final String FIELD_PATH = "filePath";
    public static final String FIELD_MD5_HEX = "fileMd5Hex";

    private long fileSize;
    private String fileName;
    private String absolutePath;
    private int docId;
    private String md5Hex;
}
