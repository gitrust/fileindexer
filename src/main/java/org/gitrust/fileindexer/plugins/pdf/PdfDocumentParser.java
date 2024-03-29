package org.gitrust.fileindexer.plugins.pdf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.apache.lucene.document.*;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.gitrust.fileindexer.plugins.CommonFields;
import org.gitrust.fileindexer.plugins.DocumentParser;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class PdfDocumentParser implements DocumentParser {
    private static Logger LOG = LogManager.getLogger(PdfDocumentParser.class);

    @Override
    public void readInputStream(InputStream inputStream, Document document) throws IOException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        AutoDetectParser parser = new AutoDetectParser();
        ParseContext parseContext = new ParseContext();

        try {
            parser.parse(inputStream, handler, metadata, parseContext);

            String text = handler.toString().replaceAll("\n|\r|\t", " ");
            String title = metadata.get(TikaCoreProperties.TITLE);
            Integer pages = Integer.valueOf(metadata.get("xmpTPg:NPages"));

            if (Strings.isNotBlank(text))
                document.add(new TextField(CommonFields.TEXT.getName(), text, Field.Store.NO));

            if (title != null)
                document.add(new StringField(CommonFields.TITLE.getName(), title, Field.Store.NO));

            if (pages != null)
                document.add(new StoredField("pdf_pages", pages));

        } catch (SAXException | TikaException e) {
            LOG.warn("Cannot read pdf input", e);
        }
    }
}
