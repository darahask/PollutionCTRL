package com.example.pollutionctrl.qdata;

public class QData {

    private String mainQ;
    private String subQ;
    private String ef;

    public QData() {
    }

    public QData(String mainQ, String subQ, String ef) {
        this.mainQ = mainQ;
        this.subQ = subQ;
        this.ef = ef;
    }

    public String getMainQ() {
        return mainQ;
    }

    public String getSubQ() {
        return subQ;
    }

    public String getEf() {
        return ef;
    }
}
