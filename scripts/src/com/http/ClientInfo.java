package com.http;

import org.json.JSONObject;

public class ClientInfo {
    public static final String GET = "GET";
    public static final String POST = "POST";
    String type;
    String url;
    JSONObject head;
    String cookie;
    JSONObject entity;
    HttpCallback callback;

    public static String getGET() {
        return GET;
    }

    public static String getPOST() {
        return POST;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getHead() {
        return head;
    }

    public void setHead(JSONObject head) {
        this.head = head;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public JSONObject getEntity() {
        return entity;
    }

    public void setEntity(JSONObject entity) {
        this.entity = entity;
    }

    public HttpCallback getCallback() {
        return callback;
    }

    public void setCallback(HttpCallback callback) {
        this.callback = callback;
    }
}
