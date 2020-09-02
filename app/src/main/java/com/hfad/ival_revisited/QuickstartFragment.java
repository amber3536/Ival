package com.hfad.ival_revisited;

//import android.app.Fragment;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class QuickstartFragment extends Fragment {
    View view;
    TextView positionTxt;
    String position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quickstart, container, false);
       // tv.findViewById(R.id.position_accuracy);
        positionTxt = view.findViewById(R.id.position_txt);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position = bundle.getString("position", "");
            Log.i("here", "onCreateView: " + position);
        }

        ((QuickstartActivity) getActivity()).setPositionName(position);
        ((QuickstartActivity) getActivity()).showView();
        String str = getResources().getString(R.string.you_have_selected, position);
        positionTxt.setText(str);

        return view;
    }



}
