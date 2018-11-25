package com.sly.app.model.yunw.home;

public class MineAreaInfo {

    /**
     * \"Code\":\"22f0314d-e110-4ed9-9a97-f488ad78cc87\",
     \"Name\":\"B1\",
     \"TotalCount\":573,
     \"NormalCount\":0,
     \"RunRate\":0,
     \"CalcExceptionCount\":573,
     \"ErrorCount\":0
     */
    private String Code;
    private String Name;
    private int TotalCount;
    private int NormalCount;
    private double RunRate;
    private int CalcExceptionCount;
    private int ErrorCount;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public int getNormalCount() {
        return NormalCount;
    }

    public void setNormalCount(int normalCount) {
        NormalCount = normalCount;
    }

    public double getRunRate() {
        return RunRate;
    }

    public void setRunRate(double runRate) {
        RunRate = runRate;
    }

    public int getCalcExceptionCount() {
        return CalcExceptionCount;
    }

    public void setCalcExceptionCount(int calcExceptionCount) {
        CalcExceptionCount = calcExceptionCount;
    }

    public int getErrorCount() {
        return ErrorCount;
    }

    public void setErrorCount(int errorCount) {
        ErrorCount = errorCount;
    }
}
