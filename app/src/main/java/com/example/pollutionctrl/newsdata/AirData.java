package com.example.pollutionctrl.newsdata;

public class AirData {

    private String name;
    private int aqi;// index
    private int tp;// tmp
    private int pr;// pressure
    private int hm;//humidity
    private int ws;//wind speed
    private int wd;//wind direction
    private String main;//main pollutant

    public AirData(String name,int aqi, int tp, int pr, int hm, int ws, int wd, String main) {
        this.name = name;
        this.aqi = aqi;
        this.tp = tp;
        this.pr = pr;
        this.hm = hm;
        this.ws = ws;
        this.wd = wd;
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public int getTp() {
        return tp;
    }

    public int getPr() {
        return pr;
    }

    public int getHm() {
        return hm;
    }

    public int getWs() {
        return ws;
    }

    public int getWd() {
        return wd;
    }

    public String getMain() {
        return main;
    }

    public int getAqi() {
        return aqi;
    }

}
