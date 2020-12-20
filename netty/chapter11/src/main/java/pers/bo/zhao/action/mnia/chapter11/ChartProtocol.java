package pers.bo.zhao.action.mnia.chapter11;

import java.nio.charset.StandardCharsets;

public class ChartProtocol {
    private int length;

    private byte[] content;

    public String getContentStr() {
        return new String(content, StandardCharsets.UTF_8);
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
