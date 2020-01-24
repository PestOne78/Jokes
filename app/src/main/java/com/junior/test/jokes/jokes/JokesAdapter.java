package com.junior.test.jokes.jokes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.junior.test.jokes.R;

import java.util.ArrayList;


public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(View v) {
            super(v);
            this.textView = v.findViewById(R.id.text_joke);
        }
    }

    public JokesAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public JokesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.joke_card, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(mDataset.get(position));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}