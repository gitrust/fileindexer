package org.gitrust.fileindexer.indexer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileIndexer {
    static Logger LOG = LogManager.getLogger(FileIndexer.class);

    private static final String INDEX_DIR = "c:/temp/lucene6index";

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: fileindexer <directory>");
            System.exit(1);
        }
        String filePath = args[0];
        LOG.info("Index path {}",filePath);
        IndexWriter writer = IndexWriterFactory.createWriter(INDEX_DIR);

        writer.deleteAll();

        Files.walk(Paths.get(filePath)).filter(Files::isRegularFile).forEach(path -> writeDocument(writer, path));

        writer.commit();
        writer.close();
    }

    private static void writeDocument(IndexWriter writer, Path path) {
        LOG.debug("Index path {}", path);
        File file = path.toFile();
        FileDocument fd = FileDocument.builder().absolutePath(file.getAbsolutePath()).fileName(file.getName()).fileSize(file.getTotalSpace()).build();
        Document doc = DocumentFactory.createDocument(fd);
        try {
            writer.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
