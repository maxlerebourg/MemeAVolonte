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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Add extends Fragment {

    public static final String ADD = "ADD";

    private AddAd aa;

    public class AddAd extends BroadcastReceiver {
        //@Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("share", Context.MODE_PRIVATE);
            sharedPreferences
                    .edit()
                    .putInt("id", intent.getIntExtra("id",0))
                    .apply();

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentLayout, new Advert());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }
    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(aa);
        super.onDestroy();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.add, container, false);

        IntentFilter inF = new IntentFilter(ADD);
        aa = new Add.AddAd();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(aa, inF);

        final EditText title = (EditText) view.findViewById(R.id.title);
        final EditText url = (EditText) view.findViewById(R.id.url);
        final EditText alt = (EditText) view.findViewById(R.id.alt);
        final Button badd = (Button) view.findViewById(R.id.add);
        alt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                badd.performClick();
                return false;
            }
        });


        badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().length() == 0 || url.getText().length() == 0 || alt.getText().length() == 0){
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
                        json.put("user_id",1);
                        json.put("title",title.getText());

                        Log.e("Json",json.toString());
                        Download.startActionBaz(getContext(), "adverts", json.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        return view;
    }
}
