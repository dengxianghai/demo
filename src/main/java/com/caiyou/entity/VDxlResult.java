package com.caiyou.entity;

/**
 * 多相流结果实体类
 */
public class VDxlResult {

    private double depth;//井深
    private double pressure; //压力
    private double temperature;//温度
    private double liquidFlowRate;//液体流速
    private double gasFlowRate;//气体流速
    private double liquidHoldupRate;//持液率
    private double gasCompressibility;//气体压缩系数
    private String flow;//流态

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getLiquidFlowRate() {
        return liquidFlowRate;
    }

    public void setLiquidFlowRate(double liquidFlowRate) {
        this.liquidFlowRate = liquidFlowRate;
    }

    public double getGasFlowRate() {
        return gasFlowRate;
    }

    public void setGasFlowRate(double gasFlowRate) {
        this.gasFlowRate = gasFlowRate;
    }

    public double getLiquidHoldupRate() {
        return liquidHoldupRate;
    }

    public void setLiquidHoldupRate(double liquidHoldupRate) {
        this.liquidHoldupRate = liquidHoldupRate;
    }

    public double getGasCompressibility() {
        return gasCompressibility;
    }

    public void setGasCompressibility(double gasCompressibility) {
        this.gasCompressibility = gasCompressibility;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }
}
