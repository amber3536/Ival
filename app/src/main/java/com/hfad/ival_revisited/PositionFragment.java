package com.hfad.ival_revisited;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class PositionFragment extends Fragment {
    View view;
    Button bottom1;
    Button bottom2;
    Button bottom3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_position, container, false);
        bottom1 = view.findViewById(R.id.shot_bottom_1);
        bottom2 = view.findViewById(R.id.shot_bottom_2);
        bottom3 = view.findViewById(R.id.shot_bottom_3);

        bottom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "bottom 1", Toast.LENGTH_SHORT).show();
            }
        });

        bottom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "bottom 2", Toast.LENGTH_SHORT).show();
            }
        });

        bottom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "bottom 3", Toast.LENGTH_SHORT).show();
            }
        });
// get the reference of Button
//        firstButton = (Button) view.findViewById(R.id.firstButton);
//// perform setOnClickListener on first Button
//        firstButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//// display a message by using a Toast
//                Toast.makeText(getActivity(), "First Fragment", Toast.LENGTH_LONG).show();
//            }
//        });
        return view;
    }
}
