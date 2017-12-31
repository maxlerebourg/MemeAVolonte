package com.lerebourg.max.memeavolonte;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Max on 31/12/2017.
 */

public class Aide extends Fragment{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.aide, container, false);
        TextView text = view.findViewById(R.id.text);
        text.setText("Api faite par Max Lerebourg et Jules Baratier disponible à l'adresse 'https://lerebourg-baratier.hanotaux.fr/api' et les differentes routes disponible sur l'image.\n" +
                "Application fonctionnelle qui utilise plusieurs library externe tel que Picasso pour les images.\n" +
                "Le site est disponible à l'adresse 'https://lerebourg-baratier.hanotaux.fr'.");
        return view;
    }
}
