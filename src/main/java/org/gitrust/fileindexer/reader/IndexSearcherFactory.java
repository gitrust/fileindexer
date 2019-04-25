package org.gitrust.fileindexer.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class IndexSearcherFactory {
    private static Logger LOG = LogManager.getLogger(IndexSearcherFactory.class);

    public static FileIndexSearcher createFileIndexSearcher(String indexPath) throws IOException {
        LOG.info("Create index searcher for path {}", indexPath);
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        IndexReader reader = DirectoryReader.open(dir);
        return new FileIndexSearcher(new IndexSearcher(reader));
    }
}
