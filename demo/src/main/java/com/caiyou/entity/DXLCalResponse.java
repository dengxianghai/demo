package com.caiyou.entity;

import java.util.LinkedList;

/**
 * DXLCal返回参数
 */
public class DXLCalResponse {


    private LinkedList<VDxlResult> vDxlResults;


    public LinkedList<VDxlResult> getvDxlResults() {
        return vDxlResults;
    }

    public void setvDxlResults(LinkedList<VDxlResult> vDxlResults) {
        this.vDxlResults = vDxlResults;
    }



}
