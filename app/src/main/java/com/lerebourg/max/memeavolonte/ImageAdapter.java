package com.lerebourg.max.memeavolonte;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by Max on 30/12/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private List<Meme> listData;
    private LayoutInflater layoutInflater;
    private Activity mContext;

    public ImageAdapter(Activity c, ArrayList<Meme> list) {
        mContext = c;
        listData = list;
        layoutInflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return listData.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item, null);
            holder = new ViewHolder(convertView);
            holder.getTextView().setText(listData.get(position).getTitle());
            Picasso.with( holder.getImageView().getContext()).load(listData.get(position).getImageUrl()).resize(500,500).into(holder.getImageView());

            convertView.setTag(holder);

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new Advert();

                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("share", Context.MODE_PRIVATE);
                    sharedPreferences
                            .edit()
                            .putInt("id", listData.get(position).getId())
                            .apply();

                    Log.e("Click", position + " " + listData.get(position).getId());

                    FragmentTransaction ft = ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLayout, fragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();

                }
            });
        } else {
            convertView.getTag();
        }

        return convertView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            imageView = (ImageView) v.findViewById(R.id.imageView);
        }
        TextView getTextView() { return textView; }
        ImageView getImageView() {
            return imageView;
        }
    }

}