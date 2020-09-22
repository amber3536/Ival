package com.hfad.ival_revisited;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.ViewHolder> {

    private Context context;
    private List<Position> mPositions;
    public PositionAdapter(List<Position> positions) {
        mPositions = positions;
    }

    @Override
    public PositionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_position, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        
        
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PositionAdapter.ViewHolder holder, final int position) {
        // Get the data model based on position
//        Contact contact = mContacts.get(position);
//
//        // Set item views based on your views and data model
 //       TextView textView = holder.nameTextView;
//        textView.setText(contact.getName());
        Position positions = mPositions.get(position);
        final Button positionBtn = holder.positionTV;
        TextView tv2 = holder.percentTV;
        TextView tv3 = holder.missedMadeTV;
        positionBtn.setText(positions.getPosition());
        Log.i("positions", "onBindViewHolder: " + positions.getPercentage());
        String str = context.getResources().getString(R.string.accuracy_txt, positions.getPercentage());
        tv2.setText(str);
        String str2 = context.getResources().getString(R.string.stats_missed_made, positions.getMade(), positions.getMissed());
        tv3.setText(str2);
        
        positionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Position positions = mPositions.get(position);
                Log.i("idk", "onClick: " + positions.getPosition());
                ((QuickstartActivity)context).loadFragment(new StatsDetailFragment(), "position", positions.getPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
       // return mContacts.size();
        return mPositions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public Button positionTV;
        public TextView percentTV;
        public TextView missedMadeTV;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            positionTV = itemView.findViewById(R.id.stats_position);
            percentTV = itemView.findViewById(R.id.stats_percentage);
            missedMadeTV = itemView.findViewById(R.id.missed_made_TV);

        }
    }



}
