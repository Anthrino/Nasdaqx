package com.anthrino.nasdaqx;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Johns on 25-03-2017.
 * Stock object contains all attributes for a stockQuote
 */

class stockQuote implements Parcelable {
    public static final Creator<stockQuote> CREATOR = new Creator<stockQuote>() {
        @Override
        public stockQuote createFromParcel(Parcel in) {
            return new stockQuote(in);
        }

        @Override
        public stockQuote[] newArray(int size) {
            return new stockQuote[size];
        }
    };
    String comp_name, comp_symbol, comp_stockEx, comp_yearRange, comp_marketCap;
    double comp_ask, comp_bid, comp_open, comp_epsratio;

    public stockQuote(Parcel in) {
        comp_name = in.readString();
        comp_symbol = in.readString();
        comp_stockEx = in.readString();
        comp_yearRange = in.readString();
        comp_marketCap = in.readString();
        comp_ask = in.readDouble();
        comp_bid = in.readDouble();
        comp_open = in.readDouble();
        comp_epsratio = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.comp_name, this.comp_symbol, this.comp_stockEx, this.comp_yearRange, this.comp_marketCap});
        parcel.writeDoubleArray(new double[]{this.comp_ask, this.comp_bid, this.comp_open, this.comp_epsratio});
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getComp_symbol() {
        return comp_symbol;
    }

    public void setComp_symbol(String comp_symbol) {
        this.comp_symbol = comp_symbol;
    }

    public String getComp_stockEx() {
        return comp_stockEx;
    }

    public void setComp_stockEx(String comp_stockEx) {
        this.comp_stockEx = comp_stockEx;
    }

    public double getComp_epsratio() {
        return comp_epsratio;
    }

    public void setComp_epsratio(double comp_epsratio) {
        this.comp_epsratio = comp_epsratio;
    }

    public String getComp_yearRange() {
        return comp_yearRange;
    }

    public void setComp_yearRange(String comp_yearRange) {
        this.comp_yearRange = comp_yearRange;
    }

    public String getComp_marketCap() {
        return comp_marketCap;
    }

    public void setComp_marketCap(String comp_marketCap) {
        this.comp_marketCap = comp_marketCap;
    }

    public double getComp_ask() {
        return comp_ask;
    }

    public void setComp_ask(double comp_ask) {
        this.comp_ask = comp_ask;
    }

    public double getComp_bid() {
        return comp_bid;
    }

    public void setComp_bid(double comp_bid) {
        this.comp_bid = comp_bid;
    }

    public double getComp_open() {
        return comp_open;
    }

    public void setComp_open(double comp_open) {
        this.comp_open = comp_open;
    }


}
