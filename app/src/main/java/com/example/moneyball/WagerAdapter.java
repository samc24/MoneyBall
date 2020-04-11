package com.example.moneyball;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class WagerAdapter extends RecyclerView.Adapter<WagerAdapter.MyViewHolder>{
    private ArrayList<Wager> mDataset;
    private ItemClickListener mClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView header, group;
        public ImageView picture;
        public MyViewHolder(View v) {
            super(v);
            header = v.findViewById(R.id.header);
            picture = v.findViewById(R.id.picture);
            group = v.findViewById(R.id.group);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WagerAdapter(ArrayList<Wager> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WagerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.header.setText(mDataset.get(position).getHeading());
        holder.group.setText(mDataset.get(position).getGroup());
        holder.picture.setImageResource(mDataset.get(position).getPicture());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // convenience method for getting data at click position
    Wager getItem(int id) {
        return mDataset.get(id);
    }
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

