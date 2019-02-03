package com.helloingob.bluefever;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.helloingob.bluefever.Settings;
import com.helloingob.bluefever.util.NotificationManager;

public class LogController extends GenericForwardComposer<Component> {

    private static final long serialVersionUID = -3956104202269583384L;
    private Listbox lbxDebugLog;
    private Listbox lbxErrorLog;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();
    }

    private void init() {
        loadLog(lbxDebugLog, Settings.Files.DEBUG_LOG_FILENAME);
        loadLog(lbxErrorLog, Settings.Files.ERROR_LOG_FILENAME);
    }

    private void loadLog(Listbox listbox, String filename) {
        listbox.getItems().clear();
        List<String> logLines = new ArrayList<String>();
        try {
            logLines = FileUtils.readLines(new File(filename));
        } catch (IOException e) {
            //NotificationManager.showNotification(filename + " not found!");
        }
        Collections.reverse(logLines);
        for (String logLine : logLines) {
            Listitem listitem = new Listitem();
            listitem.setHeight("100px");
            Listcell listcellLine = new Listcell();
            Label labelLine = new Label(logLine);
            labelLine.setStyle("font-size: 30px;");
            listcellLine.appendChild(labelLine);
            listitem.appendChild(listcellLine);
            listbox.appendChild(listitem);
        }
    }

    public void onClick$btnRefresh() {
        loadLog(lbxDebugLog, Settings.Files.DEBUG_LOG_FILENAME);
        loadLog(lbxErrorLog, Settings.Files.ERROR_LOG_FILENAME);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:SS");
        LocalDateTime dateTime = LocalDateTime.now();
        NotificationManager.showInfo("Refreshed @" + dateTime.format(formatter), NotificationManager.CENTER);
    }

    public void onClick$btnClearError() {
        File file = new File(Settings.Files.ERROR_LOG_FILENAME);
        if (file.exists()) {
            file.delete();
        }
        loadLog(lbxErrorLog, Settings.Files.ERROR_LOG_FILENAME);
    }

}
