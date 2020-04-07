package com.example.smsassistantapp;

import android.widget.Button;

public class SentCard {
    private String line1;
    private String label;
    private String line2;
    private Button billbtn;

    public SentCard(String line1,String label, String line2, Button billbtn) {
        this.line1 = line1;
        this.label = label;
        this.line2 = line2;
        this.billbtn = billbtn;
    }

    public String getLine1() {
        return line1;
    }

    public String getLabel() { return label; }

    public String getLine2() {
        return line2;
    }

    public Button getBillbtn() {return  billbtn;}
}
