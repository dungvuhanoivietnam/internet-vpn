package com.example.wise_memory_optimizer.model.vpn;

import java.io.Serializable;

public class City implements Serializable {

    private String country;
    private int df;
    private String name;
    private String code;
    private String username;
    private String pass;
    private String ip;
    private int ping;
    private int smart;

    public City() {
    }

    public City(String country, int df, String name, String code, String username, String pass, String ip, int ping, int smart) {
        this.country = country;
        this.df = df;
        this.name = name;
        this.code = code;
        this.username = username;
        this.pass = pass;
        this.ip = ip;
        this.ping = ping;
        this.smart = smart;
    }

    public int getPing() {
        return ping;
    }

    public int getSmart() {
        return smart;
    }

    public String getCountry() {
        return country;
    }

    public int getDf() {
        return df;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }

    public String getIp() {
        return ip;
    }
}
