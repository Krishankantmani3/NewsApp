package com.bibhas.newsapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class EntertainmentFragment extends Fragment {
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    private ProgressDialog progressDialog;
    List<HeadlineModel> headlineModelList;
    private static final String URL="http://newsapi.org/v2/top-headlines?country=in&category=entertainment&apiKey=cbd46bd6a4f54fe69d0cb261dbe1a878";

    public EntertainmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_entertainment, container, false);
        recyclerView=rootview.findViewById(R.id.headRecyclerView);
        headlineModelList=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

       progressDialog=new ProgressDialog(getContext(),R.style.ProgressColor);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        loadData();
        return rootview;
    }
    private void loadData() {
        //progressDialog.setMessage("Loading data...");
        //progressDialog.show();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray=response.getJSONArray("articles");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String title=jsonObject.getString("title");
                        String desc=jsonObject.getString("description");
                        String url=jsonObject.getString("url");
                        String urlToImage=jsonObject.getString("urlToImage");
                        String publishedAt=jsonObject.getString("publishedAt");
                        JSONObject source=jsonObject.getJSONObject("source");
                        String name=source.getString("name");

                        // for formatting time and date //

                        String year=publishedAt.substring(0,4);
                        String month=publishedAt.substring(5,7);
                        String date=publishedAt.substring(8,10);
                        String hour=publishedAt.substring(11,13);

                        //String hour="11";
                        String min=publishedAt.substring(14,16);
                        //Log.v("XXXXXX",min);
                        String updatedDate=date.concat("-").concat(month).concat("-").concat(year).concat("  ");
                        String print="";
                        int convertHour=Integer.parseInt(hour);
                        int convertMin=Integer.parseInt(min);
                        if (convertHour==12) {
                            convertHour=12;
                            print="PM";
                        }
                        else if (convertHour>11&&convertMin>0){
                            convertHour=convertHour-12;
                            print="PM";
                        }else {
                            print="AM";
                        }
                        String newHour=String.valueOf(convertHour);
                        String updatedTime=updatedDate.concat(newHour).concat(":").concat(min).concat(" ").concat(print);

                        //lastUpatedOn.setText(updatedTime);
//                        Log.v("AAAAAAAAAA",updatedTime);
                        // for formatting time and date //

                        myAdapter=new MyAdapter(getContext(),headlineModelList);
                        HeadlineModel headlineModel=new HeadlineModel(name,title,desc,url,urlToImage,updatedTime);
                        headlineModelList.add(headlineModel);
                        recyclerView.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);

    }

}
