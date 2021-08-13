package com.caiyou.WenDuYaLi;

import com.caiyou.entity.GasData;
import com.caiyou.entity.RecTube;
import com.caiyou.entity.VDxlResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TempPreCalTest {

    public static void main(String[] args) throws Exception {
        GasData gasData=new GasData();
        gasData.setSCFS(0);
        gasData.setYLJZXS(1);
        gasData.setRg(0.5620);
        gasData.setRo(0.84);
        gasData.setRw(1.15);
        gasData.setDwtd(3);
        gasData.setPstart(0.1);
        gasData.setTpc(-85.6285);
        gasData.setPpc(4.5742);


        List<RecTube> list=new ArrayList<>();
        RecTube r=new RecTube(3000,90,62,73.03,127,117.81,200,0.015);
        list.add(r);

        System.out.println("");
        LinkedList<VDxlResult> v=TempPreCal.CalWenDuYaLi(0,2,10,0.2,5,298.15,50,false,gasData,list);


        for(int i=0;i<v.size();i++){
            System.out.println(v.get(i).getDepth()+",   "+
                    v.get(i).getPressure()+",   "+
                    v.get(i).getTemperature()+",   "+
                    v.get(i).getLiquidFlowRate()+
                    ",   "+v.get(i).getGasFlowRate()+",   "
                    +v.get(i).getLiquidHoldupRate()+",   "+
                    v.get(i).getFlow());

        }
    }
}
