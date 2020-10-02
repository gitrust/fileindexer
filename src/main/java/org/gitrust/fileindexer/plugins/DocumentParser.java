package org.gitrust.fileindexer.plugins;

import org.apache.lucene.document.Document;

import java.io.IOException;
import java.io.InputStream;

public interface DocumentParser {

    void readInputStream(InputStream inputStream, Document document) throws IOException;
}
