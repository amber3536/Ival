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
    Button leftBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_position, container, false);
        leftBtn = view.findViewById(R.id.shot_bottom_3);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();
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
