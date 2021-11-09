package com.example.myapp_plants;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
public class CustomAdapter extends BaseAdapter {
    Context c;
    List<Plants> plants;
    public CustomAdapter(Context c, List<Plants> spacecrafts) {
        this.c = c;
        this.plants = spacecrafts;
    }
    @Override
    public int getCount() {
        return plants.size();
    }

    @Override
    public Object getItem(int i) {
        return plants.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.model, viewGroup, false);
        }
        final Plants s = (Plants) this.getItem(i);
        ImageView img = (ImageView) view.findViewById(R.id.myImg);
        TextView myTxt = (TextView) view.findViewById(R.id.myTxt);
        myTxt.setText(s.getName());
        int resID = c.getResources().getIdentifier(s.getImage(),
                "drawable", c.getPackageName());
        if (resID != 0)
            img.setImageResource(resID);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(c, s.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(c, More_Plants.class);
                intent.putExtra("name", s.getName());
                c.startActivity(intent);
            }
        });
        return view;
    }
}
