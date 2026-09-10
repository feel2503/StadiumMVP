package com.thiscat.stadiumamp.system.common;

public class ColorUtils {

    private ColorUtils() {
    }

    public static String getColorValue(String color){
        if(color != null ){
            if(!color.startsWith("#"))
                color = "#" + color;
        }else{
            color = "#FFFFFF";
        }
        return color;
    }
}