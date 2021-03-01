package com.dev.grocerypricelist;

public class ModelStat {
    private String timestamp, state, district, market, commodity, variety,
            arrival_date, min_price, max_price, modal_price;

    public ModelStat() {
    }

    public ModelStat(String timestamp, String state, String district, String market, String commodity, String variety, String arrival_date, String min_price, String max_Price, String modal_price) {
        this.timestamp = timestamp;
        this.state = state;
        this.district = district;
        this.market = market;
        this.commodity = commodity;
        this.variety = variety;
        this.arrival_date = arrival_date;
        this.min_price = min_price;
        this.max_price = max_Price;
        this.modal_price = modal_price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getModal_price() {
        return modal_price;
    }

    public void setModal_price(String modal_price) {
        this.modal_price = modal_price;
    }
}
