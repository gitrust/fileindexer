package org.gitrust.fileindexer.plugins.msg;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;

import org.apache.logging.log4j.util.Strings;
import org.apache.lucene.document.*;
import org.gitrust.fileindexer.plugins.CommonFields;
import org.gitrust.fileindexer.plugins.DocumentParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

class EmailMsgDocumentParser implements DocumentParser {

    @Override
    public void readInputStream(InputStream inputStream, Document document) throws IOException {
        MsgParser msgParser = new MsgParser();
        Message message = msgParser.parseMsg(inputStream);

        String fromEmail = message.getFromEmail();
        String fromName = message.getFromName();
        String to = message.getToEmail();
        String toName = message.getToName();
        String subject = message.getSubject();
        String bodyText = message.getBodyText();
        Date emailDate = message.getDate();
        Boolean hasAttachments = message.getAttachments() != null && !message.getAttachments().isEmpty();

        storeString("email_from", fromEmail, document);
        storeString("email_fromname", fromName, document);
        storeString("email_to", to, document);
        storeString("email_toname", toName, document);
        storeString(CommonFields.SUBJECT.getName(), subject, document);
        storeString("email_hasattachments", Boolean.toString(hasAttachments), document);

        if (emailDate != null)
            storeString("email_date", DateTools.dateToString(emailDate, DateTools.Resolution.SECOND), document);

        if (Strings.isNotBlank(bodyText))
            document.add(new TextField(CommonFields.TEXT.getName(), bodyText, Field.Store.NO));

    }

    private void storeString(String name, String value, Document document) {
        if (Strings.isNotBlank(value))
            document.add(new StringField(name, value, Field.Store.YES));
    }
}
