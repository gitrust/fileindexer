package org.gitrust.fileindexer.plugins.plaintext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.gitrust.fileindexer.plugins.CommonFields;
import org.gitrust.fileindexer.plugins.DocumentParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class PlaintextParser implements DocumentParser {
    private static Logger LOG = LogManager.getLogger(PlaintextParser.class);

    @Override
    public void readInputStream(InputStream inputStream, Document document) throws IOException {
        Reader reader = new InputStreamReader(inputStream);
        document.add(new TextField(CommonFields.TEXT.getName(), reader));
    }
}
