package com.hfad.ival_revisited;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatsFragment extends Fragment {
    private View view;
    ArrayList<Position> positions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_stats, container, false);
        ((QuickstartActivity)getActivity()).positionView();

        RecyclerView rv = view.findViewById(R.id.rvPositions);
        positions = Position.createContactsList(20);
        PositionAdapter adapter = new PositionAdapter(positions);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }
}
