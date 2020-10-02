package org.gitrust.fileindexer.indexer;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.gitrust.fileindexer.plugins.Plugin;
import org.gitrust.fileindexer.plugins.PluginRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;


public class FileIndexer {
    private static Logger LOG = LogManager.getLogger(FileIndexer.class);

    private PluginRegistry pluginRegistry = PluginRegistry.instance();
    //private static final String INDEX_DIR = "c:/temp/lucene6index";
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
        LOG.info("Registered plugins: {}", PluginRegistry.instance().getPlugins().stream().map(plugin -> plugin.getName()).collect(Collectors.toList()));

        FileIndexer fileIndexer = new FileIndexer(indexerPath, filePath);
        fileIndexer.startIndexing();
    }

    FileIndexer(String indexerPath, String filesPath) {
        this.indexerPath = indexerPath;
        this.filesPath = filesPath;
    }

    public void startIndexing() throws IOException {
        LOG.info("Generate index...");
        IndexWriter indexWriter = IndexWriterFactory.createWriter(this.indexerPath);
        indexWriter.deleteAll();

        try {
            walkFileTree(indexWriter);
            LOG.info("Documents in index: {}", indexWriter.getDocStats().numDocs);
        } finally {
            indexWriter.commit();
            indexWriter.close();
        }
    }

    private void walkFileTree(IndexWriter writer) throws IOException {
        Files.walkFileTree(Paths.get(this.filesPath), new HashSet<>(Arrays.asList(FileVisitOption.FOLLOW_LINKS)),
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

    private void writeDocument(IndexWriter writer, Path filePath) {
        LOG.debug("Index path {}", filePath);
        File file = filePath.toFile();
        FileDocument.FileDocumentBuilder builder = FileDocument.builder().absolutePath(file.getAbsolutePath()).fileName(file.getName()).fileSize(file.length());
        Optional<String> md5Hex = md5Hex(filePath);
        if (md5Hex.isPresent()) {
            builder.md5Hex(md5Hex.get());
        }

        FileDocument fd = builder.build();
        Document doc = DocumentFactory.createDocument(fd);

        // additional parser plugins
        triggerParserPlugins(file, fd, doc);

        try {
            writer.addDocument(doc);
        } catch (IOException e) {
            LOG.warn("Could not index document {}", fd);
        }
    }

    private void triggerParserPlugins(File file, FileDocument fd, Document doc) {
        String extension = FilenameUtils.getExtension(fd.getFileName());
        Plugin plugin = this.pluginRegistry.getPluginByFileExtension(extension);
        if (plugin != null) {
            try (FileInputStream fis = new FileInputStream(file)) {
                LOG.debug("Read file {}", file);
                plugin.getDocumentParser().readInputStream(fis, doc);
            } catch (IOException e) {
                LOG.warn("Could not read file {}", file, e);
            }
        }
    }

    private Optional<String> md5Hex(Path path) {
        try (InputStream is = Files.newInputStream(path)) {
            return Optional.ofNullable(DigestUtils.md5Hex(is));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
