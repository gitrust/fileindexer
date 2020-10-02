package org.gitrust.fileindexer.plugins;

import org.gitrust.fileindexer.plugins.msg.OutlookMsgPlugin;
import org.gitrust.fileindexer.plugins.pdf.PdfPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PluginRegistry {

    private static PluginRegistry INSTANCE = new PluginRegistry();
    private List<Plugin> plugins = new CopyOnWriteArrayList<>();
    private List<String> fieldNames = new ArrayList<>();

    static {
        INSTANCE.plugins.add(new OutlookMsgPlugin());
        INSTANCE.plugins.add(new PdfPlugin());

        for (Plugin p : INSTANCE.plugins) {
            INSTANCE.fieldNames.addAll(Arrays.asList(p.getFieldNames()));
        }
    }

    public List<Plugin> getPlugins() {
        return Collections.unmodifiableList(this.plugins);
    }

    /**
     * @param extension file extension (e.g. 'mp3')
     * @return Plugin or null
     */
    public Plugin getPluginByFileExtension(String extension) {
        return plugins.stream().filter(plugin -> plugin.supportsFileExtension(extension)).findFirst().orElse(null);
    }

    public static PluginRegistry instance() {
        return INSTANCE;
    }

    /**
     *
     * @return all registered field names
     */
    public List<String> getRegisteredFieldNames() {
        return this.fieldNames;
    }

}
