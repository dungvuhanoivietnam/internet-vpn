package com.example.wise_memory_optimizer.model.vpn;

import java.io.Serializable;

public class Country implements Serializable {

    private String code;
    private String name;
    private String flag;
    private boolean isExpand = false;

    public Country() {
    }

    public Country(String code, String name, String flag) {
        this.code = code;
        this.name = name;
        this.flag = flag;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getFlag() {
        return flag;
    }
}
