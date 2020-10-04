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
    private final int NUM_POS = 19;
    ArrayList<Position> positions;
    String positionNames[] = new String[] { "Total accuracy", "right layup",
            "left layup", "form shot", "free throw", "right elbow", "left elbow",
            "right midrange wing", "left midrange wing", "right baseline midrange",
            "left baseline midrange", "center 3pt", "right 3pt wing", "left 3pt wing",
            "right 3pt center", "left 3pt center", "center deep 3pt", "right deep 3pt", "left deep 3pt" };
    int positionAccuracy[] = new int[NUM_POS];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_stats, container, false);
        ((QuickstartActivity)getActivity()).positionView();

        RecyclerView rv = view.findViewById(R.id.rvPositions);
        int makeCount[] = new int[NUM_POS];
        int missCount[] = new int[NUM_POS];

//        positionAccuracy[0] = ((QuickstartActivity)getActivity()).returnPositionAccuracy("Total accuracy");
//        makeCount[0] = ((QuickstartActivity)getActivity()).getMakeCount();
//        missCount[0] = ((QuickstartActivity)getActivity()).getMissCount();

        for (int i = 0; i < positionNames.length; i++) {
            positionAccuracy[i] = ((QuickstartActivity)getActivity()).returnPositionAccuracy(positionNames[i]);
            makeCount[i] = ((QuickstartActivity)getActivity()).getMakeCount();
            missCount[i] = ((QuickstartActivity)getActivity()).getMissCount();
        }
        positions = Position.createContactsList(positionNames, positionAccuracy, makeCount, missCount);

        PositionAdapter adapter = new PositionAdapter(positions);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }
}
