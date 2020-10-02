package org.gitrust.fileindexer.plugins;

public interface Plugin {

    String getName();

    String[] getFieldNames();

    boolean supportsFileExtension(String extension);

    DocumentParser getDocumentParser();

}
