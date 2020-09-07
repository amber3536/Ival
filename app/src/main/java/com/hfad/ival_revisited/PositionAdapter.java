package com.hfad.ival_revisited;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.ViewHolder> {
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }

    private List<Position> mPositions;
    public PositionAdapter(List<Position> positions) {
        mPositions = positions;
    }

@Override
public PositionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    // Inflate the custom layout
    View contactView = inflater.inflate(R.layout.item_position, parent, false);

    // Return a new holder instance
    ViewHolder viewHolder = new ViewHolder(contactView);
    return viewHolder;
}

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PositionAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
//        Contact contact = mContacts.get(position);
//
//        // Set item views based on your views and data model
 //       TextView textView = holder.nameTextView;
//        textView.setText(contact.getName());
        Position positions = mPositions.get(position);
        TextView tv = holder.positionTV;
        TextView tv2 = holder.percentTV;
        tv.setText(positions.getPosition());
        Log.i("positions", "onBindViewHolder: " + positions.getPercentage());
        tv2.setText(String.valueOf(positions.getPercentage()));
//        TextView tv2 = holder.positionTV;
//        tv2.setText(positions.getPercentage());


    }

    @Override
    public int getItemCount() {
       // return mContacts.size();
        return mPositions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView positionTV;
        public TextView percentTV;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            positionTV = (TextView) itemView.findViewById(R.id.stats_position);
            percentTV = itemView.findViewById(R.id.stats_percentage);

        }
    }



}
