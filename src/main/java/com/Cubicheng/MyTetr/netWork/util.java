package com.Cubicheng.MyTetr.netWork;

import java.net.URL;
import java.net.URLConnection;

public interface util {
    public static boolean isNetworkConnected() {
        try {
            URL url = new URL("http://www.baidu.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
