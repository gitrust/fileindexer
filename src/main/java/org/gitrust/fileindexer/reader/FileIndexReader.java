package org.gitrust.fileindexer.reader;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.gitrust.fileindexer.indexer.FileDocument;

import java.io.IOException;

public class FileIndexReader {

    private IndexReader indexReader;

    public FileDocument getFileDocument(int docId) throws IOException {
        Document doc = this.indexReader.document(docId);
        if (doc == null) {
            return FileDocument.builder().build();
        }

        FileDocument fileDoc = FileDocument.builder()
                //.fileSize(doc.getField(FileDocument.FIELD_SIZE).numericValue().longValue())
                .absolutePath(doc.getField(FileDocument.FIELD_PATH).stringValue())
                .docId(docId)
                .fileName(doc.getField(FileDocument.FIELD_NAME).stringValue()).build();
        return fileDoc;
    }

    public static FileIndexReader createFromIndexSearcher(IndexSearcher indexSearcher) {
        FileIndexReader fileIndexReader = new FileIndexReader();
        fileIndexReader.indexReader = indexSearcher.getIndexReader();
        return fileIndexReader;
    }
}
