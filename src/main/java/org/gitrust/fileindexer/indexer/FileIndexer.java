package org.gitrust.fileindexer.indexer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;


public class FileIndexer {
    private static Logger LOG = LogManager.getLogger(FileIndexer.class);

    private static final String INDEX_DIR = "c:/temp/lucene6index";
    private final String indexerPath;
    private final String filesPath;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: fileindexer <indexerpath> <directory>");
            System.exit(1);
        }
        String indexerPath = args[0];
        String filePath = args[1];
        LOG.info("Use indexer path {}", indexerPath);
        LOG.info("Index directory {}", filePath);

        FileIndexer fileIndexer = new FileIndexer(indexerPath, filePath);
        fileIndexer.generateIndex();
    }

    FileIndexer(String indexerPath, String filesPath) {
        this.indexerPath = indexerPath;
        this.filesPath = filesPath;
    }

    public void generateIndex() throws IOException {
        IndexWriter indexWriter = IndexWriterFactory.createWriter(INDEX_DIR);
        indexWriter.deleteAll();

        try {
            walkFileTree(indexWriter);
        } finally {
            indexWriter.commit();
            indexWriter.close();
        }
    }

    private void walkFileTree(IndexWriter writer) throws IOException {
        Files.walkFileTree(Paths.get(this.filesPath), new HashSet<FileVisitOption>(Arrays.asList(FileVisitOption.FOLLOW_LINKS)),
                Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        writeDocument(writer, file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
                        LOG.debug("Skip subtree {}", file);
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }
                });
    }

    private void writeDocument(IndexWriter writer, Path path) {
        LOG.trace("Index path {}", path);
        File file = path.toFile();
        FileDocument fd = FileDocument.builder().absolutePath(file.getAbsolutePath()).fileName(file.getName()).fileSize(file.length()).build();
        Document doc = DocumentFactory.createDocument(fd);
        try {
            writer.addDocument(doc);
        } catch (IOException e) {
            LOG.warn("Could not index document {}", fd);
        }
    }
}
