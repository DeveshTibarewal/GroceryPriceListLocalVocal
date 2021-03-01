package com.dev.grocerypricelist;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdapterStat extends RecyclerView.Adapter<AdapterStat.HolderStat> implements Filterable {

    private final Context context;
    public ArrayList<ModelStat> statArrayList, filterList;
    private FilterStat filter;

    public AdapterStat(Context context, ArrayList<ModelStat> statArrayList) {
        this.context = context;
        this.statArrayList = statArrayList;
        this.filterList = statArrayList;
    }

    @NonNull
    @Override
    public HolderStat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate layout row_stat.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_stats, parent, false);

        return new HolderStat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderStat holder, int position) {
        //get Data
        ModelStat modelStat = statArrayList.get(position);
        String timestamp = modelStat.getTimestamp();
        String state = modelStat.getState();
        String district = modelStat.getDistrict();
        String market = modelStat.getMarket();
        String commodity = modelStat.getCommodity();
        String variety = modelStat.getVariety();
        String arrivalDate = modelStat.getArrival_date();
        String minPrice = modelStat.getMin_price();
        String maxPrice = modelStat.getMax_price();
        String modalPrice = modelStat.getModal_price();

        //Date Format
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp + "000"));
        String formattedDate = DateFormat.format("E, dd MMM yyyy", calendar).toString();
        holder.arrivalDataTv.setText("" + formattedDate);


        //set data
        holder.commodityTv.setText(commodity);
        holder.varietyTv.setText(variety);
        holder.marketTv.setText(market);
        holder.districtTv.setText(district);
        holder.stateTv.setText(state);
        holder.minPriceTv.setText("₹" + minPrice);
        holder.maxPriceTv.setText("₹" + maxPrice);
        holder.modalPriceTv.setText("₹" + modalPrice);
    }

    @Override
    public int getItemCount() {
        return statArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterStat(this, filterList);
        }
        return filter;
    }

    //View Holder Class
    class HolderStat extends RecyclerView.ViewHolder {

        //UI View
        private final TextView commodityTv;
        private final TextView arrivalDataTv;
        private final TextView varietyTv;
        private final TextView marketTv;
        private final TextView districtTv;
        private final TextView stateTv;
        private final TextView minPriceTv;
        private final TextView maxPriceTv;
        private final TextView modalPriceTv;

        public HolderStat(@NonNull View itemView) {
            super(itemView);

            commodityTv = itemView.findViewById(R.id.commodityTv);
            arrivalDataTv = itemView.findViewById(R.id.arrivalDataTv);
            varietyTv = itemView.findViewById(R.id.varietyTv);
            marketTv = itemView.findViewById(R.id.marketTv);
            districtTv = itemView.findViewById(R.id.districtTv);
            stateTv = itemView.findViewById(R.id.stateTv);
            minPriceTv = itemView.findViewById(R.id.minPriceTv);
            maxPriceTv = itemView.findViewById(R.id.maxPriceTv);
            modalPriceTv = itemView.findViewById(R.id.modalPriceTv);
        }
    }

}
