package com.dev.grocerypricelist;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StatsFragment extends Fragment {

    private static final String BASE_URL = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?";
    private static final String API_KEY = "579b464db66ec23bdd0000016d25c6e33eef45af53becb479510d1de";
    private static final String FORMAT_DESC = "&format=json";
    private static final String LIMIT_STRING = "&limit=";
    private static final String STATS_URL = BASE_URL + "api-key=" + API_KEY + FORMAT_DESC;
    private int total;
    //context
    Context context;
    ArrayList<ModelStat> statArrayList;
    AdapterStat adapterStat;

    //UI Views
    private ProgressBar progressBar;
    private EditText searchEt;
    private ImageButton sortBtn;
    private RecyclerView statsRv;

    public StatsFragment() {
        //Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        //init UI Views
        progressBar = view.findViewById(R.id.progressBar);
        searchEt = view.findViewById(R.id.searchEt);
        sortBtn = view.findViewById(R.id.sortBtn);
        statsRv = view.findViewById(R.id.statsRv);

        progressBar.setVisibility(View.INVISIBLE);

        loadStatsData();

        //search
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterStat.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //popup menu to show sorting options
        PopupMenu popupMenu = new PopupMenu(context, sortBtn);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Modal Price Ascending");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Modal Price Descending");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Arrival Date Ascending");
        popupMenu.getMenu().add(Menu.NONE, 3, 3, "Arrival Date Descending");
        popupMenu.setOnMenuItemClickListener(item -> {
            //handle items click
            int id = item.getItemId();
            if (id == 0) {
                Collections.sort(statArrayList, new SortStatModalPriceAsc());
                adapterStat.notifyDataSetChanged();
            } else if (id == 1) {
                Collections.sort(statArrayList, new SortStatModalPriceDesc());
                adapterStat.notifyDataSetChanged();
            } else if (id == 2) {
                Collections.sort(statArrayList, new SortStatDateAsc());
                adapterStat.notifyDataSetChanged();
            } else if (id == 3) {
                Collections.sort(statArrayList, new SortStatDateDesc());
                adapterStat.notifyDataSetChanged();
            }
            return false;
        });

        sortBtn.setOnClickListener(v -> popupMenu.show());

        onResume();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatsData();
        loadStatsData();
    }

    private void loadStatsData() {
        //Show Progress
        progressBar.setVisibility(View.VISIBLE);

        //JSON String Request For Header
        StringRequest stringRequestTotal = new StringRequest(Request.Method.GET, STATS_URL,
                response -> {
                    try {
                        total = Integer.parseInt(new JSONObject(response).getString("total"));
                    } catch (Exception e) {
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    //some error occurred, hide progress, show error message
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        //JSON String Request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, STATS_URL + LIMIT_STRING + total,
                response -> {
                    //Response received, handle response
                    handleJsonRequest(response);
                },
                error -> {
                    //some error occurred, hide progress, show error message
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                });
        //add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequestTotal);
        requestQueue.add(stringRequest);
    }

    private void handleJsonRequest(String response) {
        statArrayList = new ArrayList<>();
        statArrayList.clear();

        try {
            //We have json object as response
            JSONObject jsonObject = new JSONObject(response);
            //and then we have array of records
            JSONArray jsonArray = jsonObject.getJSONArray("records");

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/MM/yyyy hh:mm a");
            Gson gson = gsonBuilder.create();

            //Start getting data
            for (int index = 0; index < jsonArray.length(); index++) {
                ModelStat modelStat = gson.fromJson(jsonArray.getJSONObject(index).toString(), ModelStat.class);
                statArrayList.add(modelStat);
            }

            //setup Adapter
            adapterStat = new AdapterStat(context, statArrayList);
            //set adapter to recyclerVIew
            statsRv.setAdapter(adapterStat);

            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*Sort Modal price in ascending order*/
    public class SortStatModalPriceAsc implements Comparator<ModelStat> {
        @Override
        public int compare(ModelStat left, ModelStat right) {
            return Float.compare(Float.parseFloat(left.getModal_price()), Float.parseFloat(right.getModal_price()));
        }
    }

    /*Sort Modal price in ascending order*/
    public class SortStatModalPriceDesc implements Comparator<ModelStat> {
        @Override
        public int compare(ModelStat left, ModelStat right) {
            return Float.compare(Float.parseFloat(right.getModal_price()), Float.parseFloat(left.getModal_price()));
        }
    }

    /*Sort Modal price in ascending order*/
    public class SortStatDateAsc implements Comparator<ModelStat> {
        @Override
        public int compare(ModelStat left, ModelStat right) {
            return Long.compare(Long.parseLong(left.getTimestamp() + "000"),
                    Long.parseLong(right.getTimestamp() + "000"));
        }
    }

    /*Sort Modal price in ascending order*/
    public class SortStatDateDesc implements Comparator<ModelStat> {
        @Override
        public int compare(ModelStat left, ModelStat right) {
            return Long.compare(Long.parseLong(right.getTimestamp() + "000"),
                    Long.parseLong(left.getTimestamp() + "000"));
        }
    }
}