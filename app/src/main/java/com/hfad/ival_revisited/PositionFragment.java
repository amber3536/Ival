package com.hfad.ival_revisited;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
    Button bottom4;
    Button bottom5;
    Button bottom6;
    Button center1;
    Button center2;
    Button center3;
    Button center4;
    Button left_center4;
    Button right_center4;
    Button left_center3;
    Button right_center3;
    Button far_left_center3;
    Button far_right_center3;
    Button left_center2;
    Button right_center2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_position, container, false);
        bottom1 = view.findViewById(R.id.shot_bottom_1);
        bottom2 = view.findViewById(R.id.shot_bottom_2);
        bottom3 = view.findViewById(R.id.shot_bottom_3);
        bottom4 = view.findViewById(R.id.shot_bottom_4);
        bottom5 = view.findViewById(R.id.shot_bottom_5);
        bottom6 = view.findViewById(R.id.shot_bottom_6);
        center1 = view.findViewById(R.id.shot_center_1);
        center2 = view.findViewById(R.id.shot_center_2);
        center3 = view.findViewById(R.id.shot_center_3);
        center4 = view.findViewById(R.id.shot_center_4);
        left_center4 = view.findViewById(R.id.shot_left_side_center_4);
        right_center4 = view.findViewById(R.id.shot_right_side_center_4);
        left_center3 = view.findViewById(R.id.shot_left_side_center_3);
        right_center3 = view.findViewById(R.id.shot_right_side_center_3);
        far_left_center3 = view.findViewById(R.id.shot_far_left_side_center_3);
        far_right_center3 = view.findViewById(R.id.shot_far_right_side_center_3);
        left_center2 = view.findViewById(R.id.shot_left_side_center_2);
        right_center2 = view.findViewById(R.id.shot_right_side_center_2);

        bottom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               // Toast.makeText(getActivity(), "bottom 1", Toast.LENGTH_SHORT).show();
//                FragmentManager fm = getFragmentManager();
//// create a FragmentTransaction to begin the transaction and replace the Fragment
//                FragmentTransaction fragmentTransaction = fm.beginTransaction();
//// replace the FrameLayout with new Fragment
//                fragmentTransaction.replace(this, R.id.frameLayout);
//                fragmentTransaction.commit();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.frameLayout, new QuickstartFragment());
                transaction.addToBackStack(null);

// Commit the transaction
                transaction.commit();
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

        bottom4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "bottom 4", Toast.LENGTH_SHORT).show();
            }
        });

        bottom5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "bottom 5", Toast.LENGTH_SHORT).show();
            }
        });

        bottom6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "bottom 6", Toast.LENGTH_SHORT).show();
            }
        });

        center1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "center 1", Toast.LENGTH_SHORT).show();
            }
        });

        center2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "center 2", Toast.LENGTH_SHORT).show();
            }
        });

        center3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "center 3", Toast.LENGTH_SHORT).show();
            }
        });

        center4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "center 4", Toast.LENGTH_SHORT).show();
            }
        });

        left_center4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "left center 4", Toast.LENGTH_SHORT).show();
            }
        });

        right_center4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "right center 4", Toast.LENGTH_SHORT).show();
            }
        });

        left_center3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "left center 3", Toast.LENGTH_SHORT).show();
            }
        });

        right_center3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "right center 3", Toast.LENGTH_SHORT).show();
            }
        });

        far_left_center3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "far left center 3", Toast.LENGTH_SHORT).show();
            }
        });

        far_right_center3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "far right center 3", Toast.LENGTH_SHORT).show();
            }
        });

        left_center2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "left center 2", Toast.LENGTH_SHORT).show();
            }
        });

        right_center2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "right center 2", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
