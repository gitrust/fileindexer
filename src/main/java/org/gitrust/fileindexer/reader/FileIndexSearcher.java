package org.gitrust.fileindexer.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.gitrust.fileindexer.indexer.FileDocument;

import java.io.IOException;

public class FileIndexSearcher {
    private static Logger LOG = LogManager.getLogger(FileIndexSearcher.class);
    private static final int NUM_HITS = 5;
    private final IndexSearcher searcher;

    public FileIndexSearcher(IndexSearcher searcher) {
        this.searcher = searcher;
    }

    public TopDocs searchByFileName(String fileName) throws ParseException, IOException {
        LOG.trace("Search by filename " + fileName);
        QueryParser qp = new QueryParser(FileDocument.FIELD_NAME, new StandardAnalyzer());
        Query idQuery = qp.parse(fileName);
        return this.searcher.search(idQuery, NUM_HITS);
    }

    public TopDocs searchText(String query) throws ParseException, IOException {
        LOG.debug("Execute query {}", query);

        // FIXME
        String[] searchableFields = {"email_from", "email_body", "email_"};
        MultiFieldQueryParser qp = new MultiFieldQueryParser(searchableFields, new StandardAnalyzer());
        Query idQuery = qp.parse(query);
        return this.searcher.search(idQuery, NUM_HITS);
    }

    public IndexReader getIndexReader() {
        return this.searcher.getIndexReader();
    }


}
