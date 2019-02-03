package com.helloingob.bluefever.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;

public class NotificationManager {
    public static String LEFT = "start_center";
    public static String RIGHT = "end_center";
    public static String CENTER = "top_center";
    public static String BEFORE_CENTER = "before_center";
    public static String AFTER_CENTER = "after_center";

    public static void showInfo(String msg, Component component, String position) {
        showNotification(msg, Clients.NOTIFICATION_TYPE_INFO, component, position);
    }

    public static void showInfo(String msg, String position) {
        showNotification(msg, Clients.NOTIFICATION_TYPE_INFO, null, position);
    }

    public static void showWarning(String msg, Component component, String position) {
        showNotification(msg, Clients.NOTIFICATION_TYPE_WARNING, component, position);
    }

    public static void showWarning(String msg, String position) {
        showNotification(msg, Clients.NOTIFICATION_TYPE_WARNING, null, position);
    }

    private static void showNotification(String msg, String type, Component component, String position) {
        Clients.showNotification(msg, type, component, position, 5000);
    }

    public static void showNotification(String msg) {
        Clients.showNotification(msg);
    }
}
