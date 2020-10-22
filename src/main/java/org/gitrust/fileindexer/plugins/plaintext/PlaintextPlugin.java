package org.gitrust.fileindexer.plugins.plaintext;

import org.gitrust.fileindexer.plugins.CommonFields;
import org.gitrust.fileindexer.plugins.DocumentParser;
import org.gitrust.fileindexer.plugins.Plugin;

public class PlaintextPlugin implements Plugin {
    @Override
    public String getName() {
        return "Plaintext Plugin";
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{CommonFields.TEXT.getName()};
    }

    @Override
    public boolean supportsFileExtension(String extension) {
        return "txt".equalsIgnoreCase(extension);
    }

    @Override
    public DocumentParser getDocumentParser() {
        return new PlaintextParser();
    }
}
