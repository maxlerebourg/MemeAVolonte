package com.lerebourg.max.memeavolonte;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Max on 31/12/2017.
 */

public class Update extends Fragment {
    public static final String ADVERT = "ADVERT";
    public static final String UPDATE = "UPDATE";
    private EditText title, url, alt;

    private UpdateAd ua;
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
    public class UpdateAd extends BroadcastReceiver {
        //@Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("share", Context.MODE_PRIVATE);
            sharedPreferences
                    .edit()
                    .putInt("id", intent.getIntExtra("id", 0))
                    .apply();

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentLayout, new Advert());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(ua);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.add, container, false);

        ua = new UpdateAd();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(ua, new IntentFilter(UPDATE));

        title = (EditText) view.findViewById(R.id.title);
        url = (EditText) view.findViewById(R.id.url);
        alt = (EditText) view.findViewById(R.id.alt);
        Button badd = (Button) view.findViewById(R.id.add);
        badd.setText("Modifier le même");
        JSONObject obj = getFromFile("advert");
        Log.d("Update", obj.toString());
        try {

            title.setText(obj.getString("title"));
            url.setText(obj.getJSONArray("image").getJSONObject(0).getString("url"));
            alt.setText(obj.getJSONArray("image").getJSONObject(0).getString("alt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().length() == 0 || url.getText().length() == 0 || alt.getText().length() == 0) {
                    Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONArray imgs = new JSONArray();
                        JSONObject img = new JSONObject();
                        img.put("url", url.getText());
                        img.put("alt", alt.getText());
                        imgs.put(img);
                        JSONObject json = new JSONObject();
                        json.put("image", imgs);
                        json.put("user_id", 1);
                        json.put("title", title.getText());

                        Log.e("Json", json.toString());
                        Download.startActionToz(getContext(), "advert", json.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        return view;
    }
}
