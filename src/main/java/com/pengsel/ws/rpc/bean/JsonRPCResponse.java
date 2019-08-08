package com.pengsel.ws.rpc.bean;


public class JsonRPCResponse {
    String id;
    String jsonrpc="2.0";
    Error error;
    Object result;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public static class Error{
        Integer code;
        String message;

        public Error(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
