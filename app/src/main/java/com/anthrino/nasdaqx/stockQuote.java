package com.anthrino.nasdaqx;

import android.graphics.Bitmap;
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
    String comp_name;
    String comp_symbol;
    String comp_stockEx;
    String comp_yearRange;
    String comp_marketCap;
    Bitmap comp_logo;
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
        parcel.writeString(this.comp_name);
        parcel.writeString(this.comp_symbol);
        parcel.writeString(this.comp_stockEx);
        parcel.writeString(this.comp_yearRange);
        parcel.writeString(this.comp_marketCap);
        parcel.writeDouble(this.comp_ask);
        parcel.writeDouble(this.comp_bid);
        parcel.writeDouble(this.comp_open);
        parcel.writeDouble(this.comp_epsratio);
    }

    public Bitmap getComp_logo() {
        return comp_logo;
    }

    public void setComp_logo(Bitmap comp_logo) {
        this.comp_logo = comp_logo;
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
