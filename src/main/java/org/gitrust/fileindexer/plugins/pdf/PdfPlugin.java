package org.gitrust.fileindexer.plugins.pdf;

import org.gitrust.fileindexer.plugins.CommonFields;
import org.gitrust.fileindexer.plugins.DocumentParser;
import org.gitrust.fileindexer.plugins.Plugin;

public class PdfPlugin implements Plugin {
    @Override
    public String getName() {
        return "PDF Plugin";
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{CommonFields.TEXT.getName()};
    }

    @Override
    public boolean supportsFileExtension(String extension) {
        return "pdf".equalsIgnoreCase(extension);
    }

    @Override
    public DocumentParser getDocumentParser() {
        return new PdfDocumentParser();
    }
}
