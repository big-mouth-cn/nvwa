package org.bigmouth.nvwa.sap;


public enum ContentType {

    TLV((byte) 0, "application/octet-stream"), JSON((byte) 1, "application/json;charset=UTF-8"), KV((byte) 2,
            "application/x-www-form-urlencoded");

    private final byte innerCode;
    private final String httpHeader;

    ContentType(byte innerCode, String httpHeader) {
        this.innerCode = innerCode;
        this.httpHeader = httpHeader;
    }

    public byte code() {
        return innerCode;
    }

    public String httpHeader() {
        return httpHeader;
    }

    public static ContentType forCode(byte innerCode) {
        if (0 == innerCode) {
            return TLV;
        }
        else if (1 == innerCode) {
            return JSON;
        }
        else if (2 == innerCode) {
            return KV;
        }
        else {
            throw new IllegalArgumentException("innerCode");
        }
    }
}
