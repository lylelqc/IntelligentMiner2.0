package com.sly.app.model.sly;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CalReturnBean implements Parcelable {

    private String Currency ;
    private String StartingDifficulty;
    private String GrosspRofit;
    private String GrosspIncome;
    private String GrosspEnergy;
    private String GrosspMillCost;
    private String PriceOfEveryT;
    private String ROI;
    private String TheDailyIncomeT;
    private String TheDailyIncome;
    private String TheDailyEnergy;
    private String TheDailyRofit;
    private String BackToTheDays;
    private String MiningDays;
    private String MostDays;
    private List<DataBean> list;
    public void setCurrency (String Currency ) {
        this.Currency  = Currency ;
    }
    public String getCurrency () {
        return Currency ;
    }

    public void setStartingDifficulty(String StartingDifficulty) {
        this.StartingDifficulty = StartingDifficulty;
    }
    public String getStartingDifficulty() {
        return StartingDifficulty;
    }

    public void setGrosspRofit(String GrosspRofit) {
        this.GrosspRofit = GrosspRofit;
    }
    public String getGrosspRofit() {
        return GrosspRofit;
    }

    public void setGrosspIncome(String GrosspIncome) {
        this.GrosspIncome = GrosspIncome;
    }
    public String getGrosspIncome() {
        return GrosspIncome;
    }

    public void setGrosspEnergy(String GrosspEnergy) {
        this.GrosspEnergy = GrosspEnergy;
    }
    public String getGrosspEnergy() {
        return GrosspEnergy;
    }

    public void setGrosspMillCost(String GrosspMillCost) {
        this.GrosspMillCost = GrosspMillCost;
    }
    public String getGrosspMillCost() {
        return GrosspMillCost;
    }

    public void setPriceOfEveryT(String PriceOfEveryT) {
        this.PriceOfEveryT = PriceOfEveryT;
    }
    public String getPriceOfEveryT() {
        return PriceOfEveryT;
    }

    public void setROI(String ROI) {
        this.ROI = ROI;
    }
    public String getROI() {
        return ROI;
    }

    public void setTheDailyIncomeT(String TheDailyIncomeT) {
        this.TheDailyIncomeT = TheDailyIncomeT;
    }
    public String getTheDailyIncomeT() {
        return TheDailyIncomeT;
    }

    public void setTheDailyIncome(String TheDailyIncome) {
        this.TheDailyIncome = TheDailyIncome;
    }
    public String getTheDailyIncome() {
        return TheDailyIncome;
    }

    public void setTheDailyEnergy(String TheDailyEnergy) {
        this.TheDailyEnergy = TheDailyEnergy;
    }
    public String getTheDailyEnergy() {
        return TheDailyEnergy;
    }

    public void setTheDailyRofit(String TheDailyRofit) {
        this.TheDailyRofit = TheDailyRofit;
    }
    public String getTheDailyRofit() {
        return TheDailyRofit;
    }

    public void setBackToTheDays(String BackToTheDays) {
        this.BackToTheDays = BackToTheDays;
    }
    public String getBackToTheDays() {
        return BackToTheDays;
    }

    public void setMiningDays(String MiningDays) {
        this.MiningDays = MiningDays;
    }
    public String getMiningDays() {
        return MiningDays;
    }

    public void setMostDays(String MostDays) {
        this.MostDays = MostDays;
    }
    public String getMostDays() {
        return MostDays;
    }

    public void setList(List<DataBean> list) {
        this.list = list;
    }
    public List<DataBean> getList() {
        return list;
    }

    public class DataBean implements Parcelable {

        private String DateBegin;
        private String EndTime;
        private String strBegin;
        private String strEnd;
        private double SoCalculateForce;
        private long Diff;
        private double Income;
        private double BtcIncome;
        private double Energy;
        private double BtcEnergy;
        private double Rofit;
        private double BtcRofit;
        private double GrosspRofit;
        private double BtcGrosspRofit;
        private double BackToTheSchedule;
        public void setDateBegin(String DateBegin) {
            this.DateBegin = DateBegin;
        }
        public String getDateBegin() {
            return DateBegin;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }
        public String getEndTime() {
            return EndTime;
        }

        public void setStrBegin(String strBegin) {
            this.strBegin = strBegin;
        }
        public String getStrBegin() {
            return strBegin;
        }

        public void setStrEnd(String strEnd) {
            this.strEnd = strEnd;
        }
        public String getStrEnd() {
            return strEnd;
        }

        public void setSoCalculateForce(double SoCalculateForce) {
            this.SoCalculateForce = SoCalculateForce;
        }
        public double getSoCalculateForce() {
            return SoCalculateForce;
        }

        public void setDiff(long Diff) {
            this.Diff = Diff;
        }
        public long getDiff() {
            return Diff;
        }

        public void setIncome(double Income) {
            this.Income = Income;
        }
        public double getIncome() {
            return Income;
        }

        public void setBtcIncome(double BtcIncome) {
            this.BtcIncome = BtcIncome;
        }
        public double getBtcIncome() {
            return BtcIncome;
        }

        public void setEnergy(double Energy) {
            this.Energy = Energy;
        }
        public double getEnergy() {
            return Energy;
        }

        public void setBtcEnergy(double BtcEnergy) {
            this.BtcEnergy = BtcEnergy;
        }
        public double getBtcEnergy() {
            return BtcEnergy;
        }

        public void setRofit(double Rofit) {
            this.Rofit = Rofit;
        }
        public double getRofit() {
            return Rofit;
        }

        public void setBtcRofit(double BtcRofit) {
            this.BtcRofit = BtcRofit;
        }
        public double getBtcRofit() {
            return BtcRofit;
        }

        public void setGrosspRofit(double GrosspRofit) {
            this.GrosspRofit = GrosspRofit;
        }
        public double getGrosspRofit() {
            return GrosspRofit;
        }

        public void setBtcGrosspRofit(double BtcGrosspRofit) {
            this.BtcGrosspRofit = BtcGrosspRofit;
        }
        public double getBtcGrosspRofit() {
            return BtcGrosspRofit;
        }

        public void setBackToTheSchedule(double BackToTheSchedule) {
            this.BackToTheSchedule = BackToTheSchedule;
        }
        public double getBackToTheSchedule() {
            return BackToTheSchedule;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.DateBegin);
            dest.writeString(this.EndTime);
            dest.writeString(this.strBegin);
            dest.writeString(this.strEnd);
            dest.writeDouble(this.SoCalculateForce);
            dest.writeLong(this.Diff);
            dest.writeDouble(this.Income);
            dest.writeDouble(this.BtcIncome);
            dest.writeDouble(this.Energy);
            dest.writeDouble(this.BtcEnergy);
            dest.writeDouble(this.Rofit);
            dest.writeDouble(this.BtcRofit);
            dest.writeDouble(this.GrosspRofit);
            dest.writeDouble(this.BtcGrosspRofit);
            dest.writeDouble(this.BackToTheSchedule);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.DateBegin = in.readString();
            this.EndTime = in.readString();
            this.strBegin = in.readString();
            this.strEnd = in.readString();
            this.SoCalculateForce = in.readDouble();
            this.Diff = in.readLong();
            this.Income = in.readDouble();
            this.BtcIncome = in.readDouble();
            this.Energy = in.readDouble();
            this.BtcEnergy = in.readDouble();
            this.Rofit = in.readDouble();
            this.BtcRofit = in.readDouble();
            this.GrosspRofit = in.readDouble();
            this.BtcGrosspRofit = in.readDouble();
            this.BackToTheSchedule = in.readDouble();
        }

        public final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Currency);
        dest.writeString(this.StartingDifficulty);
        dest.writeString(this.GrosspRofit);
        dest.writeString(this.GrosspIncome);
        dest.writeString(this.GrosspEnergy);
        dest.writeString(this.GrosspMillCost);
        dest.writeString(this.PriceOfEveryT);
        dest.writeString(this.ROI);
        dest.writeString(this.TheDailyIncomeT);
        dest.writeString(this.TheDailyIncome);
        dest.writeString(this.TheDailyEnergy);
        dest.writeString(this.TheDailyRofit);
        dest.writeString(this.BackToTheDays);
        dest.writeString(this.MiningDays);
        dest.writeString(this.MostDays);
        dest.writeList(this.list);
    }

    public CalReturnBean() {
    }

    protected CalReturnBean(Parcel in) {
        this.Currency = in.readString();
        this.StartingDifficulty = in.readString();
        this.GrosspRofit = in.readString();
        this.GrosspIncome = in.readString();
        this.GrosspEnergy = in.readString();
        this.GrosspMillCost = in.readString();
        this.PriceOfEveryT = in.readString();
        this.ROI = in.readString();
        this.TheDailyIncomeT = in.readString();
        this.TheDailyIncome = in.readString();
        this.TheDailyEnergy = in.readString();
        this.TheDailyRofit = in.readString();
        this.BackToTheDays = in.readString();
        this.MiningDays = in.readString();
        this.MostDays = in.readString();
        this.list = new ArrayList<DataBean>();
        in.readList(this.list, DataBean.class.getClassLoader());
    }

    public static final Creator<CalReturnBean> CREATOR = new Creator<CalReturnBean>() {
        @Override
        public CalReturnBean createFromParcel(Parcel source) {
            return new CalReturnBean(source);
        }

        @Override
        public CalReturnBean[] newArray(int size) {
            return new CalReturnBean[size];
        }
    };
}
