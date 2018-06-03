package com.hayashi.android_geogence;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class Text {
    private Context context = null;
    private String filePath = null;

    public Text(String filePath, Context context) {
        this.filePath = filePath;
        this.context = context;
    }

    public void append(String text) {
        OutputStream out;
        try {
            out = this.context.openFileOutput(filePath,MODE_PRIVATE|MODE_APPEND);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
            writer.append(text); // 追記する
            writer.close();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    public String read() {
        InputStream in;
        String text = "";
        try {
            in = this.context.openFileInput(this.filePath);
            BufferedReader reader= new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String lineBuffer;
            while( (lineBuffer = reader.readLine()) != null ){
                text += lineBuffer + "\n";
            }
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return text;
    }

    public void delete() {
        // 参考
        // http://d.hatena.ne.jp/chiakisugimoto/20100311/1268312083
        context.deleteFile(filePath);
    }
}