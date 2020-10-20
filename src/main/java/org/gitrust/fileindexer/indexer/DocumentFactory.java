package org.gitrust.fileindexer.indexer;

import org.apache.lucene.document.*;

public class DocumentFactory {

    /**
     * Create a lucence Document with initial file fields
     *
     * @param fileDocument
     * @return
     */
    public static Document createDocument(FileDocument fileDocument) {
        Document document = new Document();
        document.add(new StringField(FileDocument.FIELD_PATH, fileDocument.getAbsolutePath(), Field.Store.YES));
        document.add(new TextField(FileDocument.FIELD_NAME, fileDocument.getFileName(), Field.Store.YES));
        document.add(new StoredField(FileDocument.FIELD_SIZE, fileDocument.getFileSize()));
        document.add(new StringField(FileDocument.FIELD_MD5_HEX, fileDocument.getMd5Hex(), Field.Store.YES));
        return document;
    }
}
