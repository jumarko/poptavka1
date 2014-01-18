package com.eprovement.poptavka.rest.externalsource;

public class ExternalSourceDto {
    private String code;
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ExternalSourceDto{"
                + "code='" + code + '\''
                + ", url='" + url + '\''
                + '}';
    }
}
