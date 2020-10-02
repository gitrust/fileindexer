package org.gitrust.fileindexer.reader;

import org.apache.lucene.index.*;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.store.SimpleFSLockFactory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class IndexInfoPrinter {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("No arguments specified");
            System.exit(0);
            return;
        }


        String path = args[0];
        System.out.println("Print index information for " + path);
        IndexReader reader = DirectoryReader.open(
                new SimpleFSDirectory(new File(path).toPath(), SimpleFSLockFactory.INSTANCE));
        final Fields fields = MultiFields.getFields(reader);
        final Iterator<String> iterator = fields.iterator();

        while (iterator.hasNext()) {
            final String field = iterator.next();
            final Terms terms = MultiFields.getTerms(reader, field);
            final TermsEnum it = terms.iterator();
            BytesRef term = it.next();
            while (term != null) {
                System.out.println("Field: " + field);
                System.out.println(term.utf8ToString());
                term = it.next();
            }
        }

        System.exit(0);
    }
}
