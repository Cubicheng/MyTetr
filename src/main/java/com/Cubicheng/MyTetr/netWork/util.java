package com.Cubicheng.MyTetr.netWork;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

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

    public static Text get_text(GridPane gridPane, int targetColumn, int targetRow) {
        Text retrievedText = null;
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == targetRow && GridPane.getColumnIndex(node) == targetColumn && node instanceof Text) {
                retrievedText = (Text) node;
                break;
            }
        }
        return retrievedText;
    }
}
