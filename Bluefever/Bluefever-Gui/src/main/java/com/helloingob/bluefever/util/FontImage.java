package com.helloingob.bluefever.util;

import org.zkoss.zul.Span;

public class FontImage extends Span {
    private static final long serialVersionUID = -5118231180504684877L;
    private String iconSclass = ""; // to compare images
    private String colorStyle = "";
    private String sizeStyle = "";
    private String cursorStyle = "";

    public FontImage() {
        super();
    }

    public FontImage(String iconSclass) {
        super();
        setIconSclass(iconSclass);
    }

    public FontImage(String iconSclass, int size) {
        super();
        setIconSclass(iconSclass);
        setSize(size);
    }

    public void setIconSclass(String iconSclass) {
        this.iconSclass = iconSclass;
        this.setSclass(iconSclass);
    }

    public String getIconSclass() {
        return iconSclass;
    }

    public void setColor(String hexColorCode) {
        if (!hexColorCode.startsWith("#")) {
            hexColorCode = "#" + hexColorCode;
        }
        colorStyle = "color: " + hexColorCode + ";";
        refreshStyle();
    }

    public void setSize(int size) {
        sizeStyle = "font-size: " + size + "px;";
        refreshStyle();
    }

    public void setCursor(String string) {
        cursorStyle = "cursor: " + string + ";";
        refreshStyle();
    }

    private void refreshStyle() {
        this.setStyle(colorStyle + sizeStyle + cursorStyle);
    }

}
