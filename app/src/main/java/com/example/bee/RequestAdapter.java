package com.example.bee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RequestAdapter extends ArrayAdapter<Request> {
    private ArrayList<Request> requests;
    private Context context;

    public RequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
        this.requests = requests;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.request, parent, false);
        }

        Request request = requests.get(position);
        TextView start, end, price;
        start = v.findViewById(R.id.start);
        end = v.findViewById(R.id.end);
        price = v.findViewById(R.id.price);
        start.setText(request.getOrigin());
        end.setText(request.getDest());
        String cost = String.valueOf(request.getCost());
        price.setText("$ " + cost);
        return v;
    }

}
