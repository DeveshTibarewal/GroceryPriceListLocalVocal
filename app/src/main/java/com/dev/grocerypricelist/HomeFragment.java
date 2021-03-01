package com.dev.grocerypricelist;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    private static final String BASE_URL = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?";
    private static final String API_KEY = "579b464db66ec23bdd0000016d25c6e33eef45af53becb479510d1de";
    private static final String FORMAT_DESC = "&format=json";
    private static final String LIMIT_STRING = "&limit=";
    private static final String STATS_URL = BASE_URL + "api-key=" + API_KEY + FORMAT_DESC;
    public static int total;

    //context for fragments
    Context context;

    //UI Views
    private ProgressBar progressBar;
    private TextView titleTv, sourceTv, createdTv, updatedTv;

    public HomeFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //init UI Views
        progressBar = view.findViewById(R.id.progressBar);
        titleTv = view.findViewById(R.id.titleTv);
        sourceTv = view.findViewById(R.id.sourceTv);
        createdTv = view.findViewById(R.id.createdTv);
        updatedTv = view.findViewById(R.id.updatedTv);

        progressBar.setVisibility(View.INVISIBLE);

        loadHomeData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHomeData();
    }

    private void loadHomeData() {
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
        try {
            //since we know, our response is in JSON Object so converting it to an Object
            JSONObject jsonObject = new JSONObject(response);

            //JSONObject globalJo = jsonObject.getJSONObject("field");
            String title = jsonObject.getString("title");
            String source = jsonObject.getString("source");
            String created = jsonObject.getString("created");
            String updated = jsonObject.getString("updated");

            //CHangeing Date format
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(created + "000"));
            String formattedDate = DateFormat.format("dd MMMM yyyy", calendar).toString();
            createdTv.setText(formattedDate);

            calendar.setTimeInMillis(Long.parseLong(updated + "000"));
            formattedDate = DateFormat.format("dd MMMM yyyy", calendar).toString();
            updatedTv.setText(formattedDate);

            //set Data
            titleTv.setText(title);
            sourceTv.setText(source);

            //hide Progress
            progressBar.setVisibility(View.GONE);

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}