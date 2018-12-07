package com.sly.app.model.sly;

public class CalerDefult {

    /**
     * Currency : 43155.06
     * difficulty : 6389316883512
     */
//    Currency	string	当前币价
//    difficulty	string	当前周期难度

    private String Currency;
    private long difficulty;

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public long getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(long difficulty) {
        this.difficulty = difficulty;
    }
}
