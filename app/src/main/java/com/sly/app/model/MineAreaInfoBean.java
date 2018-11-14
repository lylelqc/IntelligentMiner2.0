package com.sly.app.model;

public class MineAreaInfoBean {

    /**
     *
     * "Code":"001cfcd1-5bb9-49d2-bd20-0ecfb9b8b408",
     * "Name":"b3_5",
     * "TotalCount":0,
     * "NormalCount":0,
     * "RunRate":0,
     * "CalcExceptionCount":0,
     * "ErrorCount":0
     * **/

    private String Code;
    private String Name;
    private int TotalCount;
    private int NormalCount;
    private int RunRate;
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

    public int getRunRate() {
        return RunRate;
    }

    public void setRunRate(int runRate) {
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
