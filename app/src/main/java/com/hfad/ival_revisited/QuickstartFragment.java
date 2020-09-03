package com.hfad.ival_revisited;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class QuickstartFragment extends Fragment {
    private View view;
    private TextView positionTxt;
    private String position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_quickstart, container, false);
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
