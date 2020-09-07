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
    String positionNames[] = new String[] {
            "left layup", "right layup", "free throw" };
    int positionAccuracy[] = new int[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_stats, container, false);
        ((QuickstartActivity)getActivity()).positionView();

        RecyclerView rv = view.findViewById(R.id.rvPositions);
        int makeCount[] = new int[3];
        int missCount[] = new int[3];

        for (int i = 0; i < positionNames.length; i++) {
            positionAccuracy[i] = ((QuickstartActivity)getActivity()).returnPositionAccuracy(positionNames[i]);
            makeCount[i] = ((QuickstartActivity)getActivity()).getMakeCount();
            missCount[i] = ((QuickstartActivity)getActivity()).getMissCount();
        }
        positions = Position.createContactsList(positionNames, positionAccuracy);

        PositionAdapter adapter = new PositionAdapter(positions);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }
}
