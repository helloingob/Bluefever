package com.helloingob.bluefever.util;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

public class WindowMaker {
    
    public static Window createWindow(String zul, String title, Map<String, ? extends Object> parameter, boolean isMaximizable) {
        Window window = new Window();
        window = (Window) Executions.createComponents(zul, null, parameter);
        window.setTitle(title);
        window.setPosition("center");
        window.setClosable(true);
        window.setBorder("normal");
        window.setMaximizable(isMaximizable);
        window.setSizable(isMaximizable);
        window.doModal();
        return window;
    }

}
