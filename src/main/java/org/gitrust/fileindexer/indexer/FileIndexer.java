package org.gitrust.fileindexer.indexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import java.util.ArrayList;
import java.util.List;


public class FileIndexer {

    private static final String INDEX_DIR = "c:/temp/lucene6index";

    public static void main(String[] args) throws Exception
    {
        IndexWriter writer = IndexWriterFactory.createWriter(INDEX_DIR);
        List<Document> documents = new ArrayList<>();

        FileDocument fd = FileDocument.builder().absolutePath("").fileName("").fileSize(0).build();
        Document document1 = DocumentFactory.createDocument(fd);
        documents.add(document1);

        //Let's clean everything first
        writer.deleteAll();

        writer.addDocuments(documents);
        writer.commit();
        writer.close();
    }
}
