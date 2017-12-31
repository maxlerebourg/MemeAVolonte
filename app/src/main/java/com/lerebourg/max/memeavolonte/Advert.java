package com.lerebourg.max.memeavolonte;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifTextView;

public class Advert extends Fragment {

    public static final String ADVERT = "ADVERT";
    public static final String DELETE = "DELETE";

    private String id;
    private JSONObject obj;
    private UpdateAdvert ua;
    private UpdateDelete ud;
    private LinearLayout adv;
    private ImageView img;
    private GifImageView gif;
    private TextView text,date;
    private int nb = 0;
    public JSONObject getFromFile(String param) {
        try {
            InputStream is = new FileInputStream(getContext().getCacheDir() + "/" + param + ".json");
            Log.d("UAdvert", getContext().getCacheDir() + "/" + param + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            return new JSONObject(text);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public class UpdateAdvert extends BroadcastReceiver {
        //@Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                obj = getFromFile("advert");
                Log.d("Images", obj.toString());
                try {

                    text.setText(obj.getString("title"));
                    date.setText(obj.getString("date").substring(0,10));
                    adv.bringToFront();
                    adv.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(obj.getJSONArray("image").getJSONObject(nb).getString("url")).fit().centerInside().into(img);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (nb+1<obj.getJSONArray("image").length()){
                                    nb++;
                                }else{
                                    nb=0;
                                }
                                Picasso.with(getContext()).load(obj.getJSONArray("image").getJSONObject(nb).getString("url")).fit().centerInside().into(img);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class UpdateDelete extends BroadcastReceiver {
        //@Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentLayout, new Adverts());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        }
    }


    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(ua);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(ud);
        super.onDestroy();
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ua = new UpdateAdvert();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(ua, new IntentFilter(ADVERT));
        ud = new UpdateDelete();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(ud, new IntentFilter(DELETE));
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.advert, container, false);
        SharedPreferences sp = getContext().getSharedPreferences("share",Context.MODE_PRIVATE);
        id = String.valueOf(sp.getInt("id",0));
        Download.startActionFoo(getContext(), "advert", id);

        gif = view.findViewById(R.id.gif);
        gif.bringToFront();

        adv = view.findViewById(R.id.adv);
        adv.setVisibility(View.INVISIBLE);
        img = view.findViewById(R.id.img);
        date = view.findViewById(R.id.date);
        text = view.findViewById(R.id.text);
        Button back = view.findViewById(R.id.back);
        Button sup = view.findViewById(R.id.delete);
        Button update = view.findViewById(R.id.update);

        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Download.startActionRac(getContext(), "advert", id);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentLayout, new Adverts());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Update();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("share", Context.MODE_PRIVATE);
                sharedPreferences
                        .edit()
                        .putString("sid", id)
                        .apply();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });
        return view;
    }
}
