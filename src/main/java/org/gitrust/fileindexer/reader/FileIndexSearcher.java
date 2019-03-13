package org.gitrust.fileindexer.reader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.gitrust.fileindexer.indexer.FileDocument;

public class FileIndexSearcher {

    private final IndexSearcher searcher;

    public FileIndexSearcher(IndexSearcher searcher) {
        this.searcher = searcher;
    }

    public TopDocs searchByFileName(String fileName) throws Exception {
        QueryParser qp = new QueryParser(FileDocument.FIELD_NAME, new StandardAnalyzer());
        Query idQuery = qp.parse(fileName);
        TopDocs hits = this.searcher.search(idQuery, 10);
        return hits;
    }
}
