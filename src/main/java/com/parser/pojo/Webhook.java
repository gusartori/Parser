package com.parser.pojo;

public class Webhook {
    private String url;
    private int code;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Webhook(String url, int code) {

        this.url = url;
        this.code = code;
    }

    public Webhook() {
    }
}
