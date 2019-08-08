package com.pengsel.ws.rpc.bean;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JsonRPCRequest {
    String id;
    String jsonrpc="2.0";

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    String method;
    List<Object> params;

    public JsonRPCRequest(String method, List<Object>params) {
        this.method = method;
        this.params = params;
        this.id= UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
}
