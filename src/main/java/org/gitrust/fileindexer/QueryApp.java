package org.gitrust.fileindexer;

import org.apache.commons.cli.*;
import org.apache.lucene.search.IndexSearcher;
import org.gitrust.fileindexer.reader.FileIndexSearcher;
import org.gitrust.fileindexer.reader.IndexSearcherFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Main application to query files in a given Lucene Index.
 */
public class QueryApp {

    private final String luceneIndexPath;
    private FileIndexSearcher searcher;

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

        QueryApp app = new QueryApp(cmd.getOptionValue("i"));
        try {
            app.setup();
        } catch (IOException e) {
            System.out.println("Could not setup lucene index: " + e.getMessage());
            System.exit(1);
        }
        app.listenAndSearch();
    }

    public QueryApp(String luceneIndexPath) {
        this.luceneIndexPath = luceneIndexPath;
    }

    private void setup() throws IOException {
        IndexSearcher indexSearcher = IndexSearcherFactory.createSearcher(this.luceneIndexPath);
        this.searcher = new FileIndexSearcher(indexSearcher);
    }

    private void listenAndSearch() {
        CommandParser cmd = new CommandParser(System.out,this.searcher);
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("> Input search query: ");
                long then = System.currentTimeMillis();
                String userInput = scanner.nextLine();
                long now = System.currentTimeMillis();
                cmd.execCmd(userInput);
            }
        } catch(IllegalStateException  e) {
            System.out.println("");
        }
    }

    private static Options createAppOptions() {
        Options options = new Options();
        options.addOption("h", false, "This help");
        options.addOption("i", true, "Lucene Indexer directory");

        return options;
    }

    class CommandParser {
        private final PrintStream printStream;
        private final FileIndexSearcher searcher;

        CommandParser(PrintStream printStream, FileIndexSearcher searcher) {

            this.printStream = printStream;
            this.searcher = searcher;
        }

        public void execCmd(String command) {
            this.printStream.println("Command: "+command);
            if ("exit".equals(command)) {
                System.exit(0);
            }
        }
    }
}
