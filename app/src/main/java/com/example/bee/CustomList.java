package com.example.bee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<Offer> {
    private ArrayList<Offer> offers;
    private Context context;

    public CustomList(Context context, ArrayList<Offer> offers){
        super(context,0,offers);
        this.offers = offers;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,@NonNull ViewGroup parent){
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.offer_info, parent, false);
        }
        Offer moffer = offers.get(position);

        TextView fromText = view.findViewById(R.id.fromText);
        TextView toText = view.findViewById(R.id.toText);
        TextView fare = view.findViewById(R.id.offerFare);

        String start = "From:" + moffer.getStartingPoint();
        String to = "To:" + moffer.getEndPoint();
        String cost = "$" + moffer.getFare();
        fromText.setText(start);
        toText.setText(to);
        fare.setText(cost);

        return view;

    }

}
