package org.gitrust.fileindexer.indexer;

import org.apache.lucene.document.*;

public class DocumentFactory {

    public static Document createDocument(FileDocument fileDocument) {
        Document document = new Document();
        document.add(new StringField(FileDocument.FIELD_PATH, fileDocument.getAbsolutePath(), Field.Store.YES));
        document.add(new TextField(FileDocument.FIELD_NAME, fileDocument.getFileName(), Field.Store.YES));
        document.add(new LongPoint(FileDocument.FIELD_SIZE, fileDocument.getFileSize()));
        return document;
    }
}
