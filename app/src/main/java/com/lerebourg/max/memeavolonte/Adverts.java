package com.lerebourg.max.memeavolonte;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Adverts extends Fragment {

    public static final String ADVERTS = "ADVERTS";

    private JSONArray list_obj;
    private boolean fini = false;
    private UpdateAdverts ua;
    private ArrayList<Meme> item = new ArrayList<>();
    private GridView gridview;

    public JSONArray getFromFile(String param) {
        try {
            InputStream is = new FileInputStream(getContext().getCacheDir() + "/" + param + ".json");
            Log.d("UAdverts", getActivity().getCacheDir() + "/" + param + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            return new JSONArray(text);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public class UpdateAdverts extends BroadcastReceiver {
        //@Override
        public void onReceive(Context context, Intent intent) {
            item.clear();
            if (null != intent) {
                JSONArray list_obj = getFromFile("adverts");
                Log.d("Images", list_obj.toString());
                try {
                    for (int i = list_obj.length()-1; i >= 0; i--) {
                        String title = list_obj.getJSONObject(i).getString("title");
                        String date = list_obj.getJSONObject(i).getString("date").substring(0,10);
                        String url = list_obj.getJSONObject(i).getJSONArray("image").getJSONObject(0).getString("url");
                        int id = list_obj.getJSONObject(i).getInt("id");
                        item.add(new Meme(title, url, id, date));
                    }
                    gridview.setAdapter(new ImageAdapter(getActivity(), item));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(ua);
        super.onDestroy();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.adverts, container, false);
        Download.startActionFoo(getContext(), "adverts", "");
        IntentFilter inF = new IntentFilter(ADVERTS);
        ua = new UpdateAdverts();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(ua, inF);

        gridview = (GridView) view.findViewById(R.id.gridview);
        return view;
    }
}
