package org.gitrust.fileindexer;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.gitrust.fileindexer.domain.Command;
import org.gitrust.fileindexer.domain.CommandExecutor;
import org.gitrust.fileindexer.domain.CommandParser;
import org.gitrust.fileindexer.indexer.FileDocument;
import org.gitrust.fileindexer.plugins.PluginRegistry;
import org.gitrust.fileindexer.reader.FileIndexReader;
import org.gitrust.fileindexer.reader.FileIndexSearcher;
import org.gitrust.fileindexer.reader.IndexSearcherFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Main application to query files in a given Lucene Index.
 */
public class Main {

    private static Logger LOG = LogManager.getLogger(Main.class);
    private final String luceneIndexPath;
    private FileIndexSearcher searcher;
    private FileIndexReader indexReader;


    public static void main(String[] argv) {
        System.out.println("Starting query app...");
        CommandLineParser parser = new DefaultParser();
        Options opts = createAppOptions();
        CommandLine cmd;
        try {
            cmd = parser.parse(opts, argv);
        } catch (ParseException e) {
            System.out.println("Could not parse app arguments! " + e.getMessage());
            System.exit(1);
            return;
        }

        if (!cmd.hasOption("i")) {
            System.out.println("Usage: ");
            System.exit(0);
        }

        Main app = new Main(cmd.getOptionValue("i"));
        try {
            app.setup();
        } catch (IOException e) {
            System.out.println("Could not setup lucene index: " + e.getMessage());
            System.exit(1);
        }
        app.listen();
    }

    public Main(String luceneIndexPath) {
        this.luceneIndexPath = luceneIndexPath;
    }

    private void setup() throws IOException {
        this.searcher = IndexSearcherFactory.createFileIndexSearcher(this.luceneIndexPath);
        this.indexReader = FileIndexReader.createFileIndexReader(this.searcher.getIndexReader());
    }

    private void listen() {
        CommandExecutor commander = new Commander(this.searcher, System.out);
        CommandParser cmdParser = new CommandParser();
       

        try ( Scanner scanner = new Scanner(System.in)){
            System.out.println("Known field names: " + PluginRegistry.instance().getRegisteredFieldNames());
            while (true) {
                System.out.print("> Input search query: ");
                long then = System.currentTimeMillis();
                String userInput = scanner.nextLine();
                long now = System.currentTimeMillis();
                Command cmd = cmdParser.parseCommand(userInput);
                commander.executeCommand(cmd);
            }
        } catch (IllegalStateException e) {
            LOG.debug("Could not execute search");
        }
    }

    private static Options createAppOptions() {
        Options options = new Options();
        options.addOption("h", false, "This help");
        options.addOption("i", true, "Lucene Indexer directory");

        return options;
    }

    private class Commander implements CommandExecutor {
        // TODO move command implementation to appropriate package
        private final FileIndexSearcher searcher;
        private final PrintStream output;

        Commander(FileIndexSearcher searcher, PrintStream output) {
            this.searcher = searcher;
            this.output = output;
        }

        @Override
        public void executeCommand(Command cmd) {
            if ("exit".equals(cmd.getCommand())) {
                System.exit(0);
            } else if ("q".equals(cmd.getCommand())) {
                try {
                    TopDocs result = searcher.searchText(cmd.getFirstArgument());
                    this.printSearchResults(result);
                } catch (org.apache.lucene.queryparser.classic.ParseException e) {
                    printError(e);
                } catch (IOException e) {
                    printError(e);
                }
            }
        }

        private void printSearchResults(TopDocs result) {
            this.output.println("Total Hits: " + result.totalHits);
            ScoreDoc[] docs = result.scoreDocs;
            for (ScoreDoc doc : docs) {
                try {
                    FileDocument fd = indexReader.getFileDocument(doc.doc);
                    this.output.println("docId: " + fd.getDocId() + ", file: " + fd);
                } catch (IOException e) {
                    printError(e);
                }

            }
        }

        private void printError(Exception e) {
            this.output.println(e.getMessage());
        }
    }


}
