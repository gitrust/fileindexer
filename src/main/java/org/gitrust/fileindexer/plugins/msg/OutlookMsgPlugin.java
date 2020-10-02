package org.gitrust.fileindexer.plugins.msg;

import org.gitrust.fileindexer.plugins.CommonFields;
import org.gitrust.fileindexer.plugins.DocumentParser;
import org.gitrust.fileindexer.plugins.Plugin;

public class OutlookMsgPlugin implements Plugin {

    @Override
    public String getName() {
        return "Outlook msg plugin";
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{CommonFields.TEXT.getName(), "email_from", "email_fromname",
                "email_to", "email_toname", "email_subject", "email_date", "email_hasattachments"};
    }

    @Override
    public boolean supportsFileExtension(String extension) {
        return "msg".equalsIgnoreCase(extension);
    }


    @Override
    public DocumentParser getDocumentParser() {
        return new EmailMsgDocumentParser();
    }
}
