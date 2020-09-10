package com.hfad.ival_revisited;
//import android.app.Fragment;
import androidx.fragment.app.Fragment;
//import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;

public class PositionFragment extends Fragment {
    private View view;
    private Button top1;
    private Button top2;
    Button top3;
    Button top4;
    Button top5;
    Button top6;
    Button center1;
    Button center2;
    Button center3;
    Button center4;
    Button right_center1;
    Button left_center1;
    Button left_center3;
    Button right_center3;
    Button far_left_center2;
    Button far_right_center2;
    Button left_center2;
    Button right_center2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_position, container, false);
        top1 = view.findViewById(R.id.shot_top_1);
        top2 = view.findViewById(R.id.shot_top_2);
        top3 = view.findViewById(R.id.shot_top_3);
        top4 = view.findViewById(R.id.shot_top_4);
        top5 = view.findViewById(R.id.shot_top_5);
        top6 = view.findViewById(R.id.shot_top_6);
        center4 = view.findViewById(R.id.shot_center_4);
        center3 = view.findViewById(R.id.shot_center_3);
        center2 = view.findViewById(R.id.shot_center_2);
        center1 = view.findViewById(R.id.shot_center_1);
        right_center1 = view.findViewById(R.id.shot_right_side_center_1);
        left_center1 = view.findViewById(R.id.shot_left_side_center_1);
        right_center2 = view.findViewById(R.id.shot_right_side_center_2);
        left_center2 = view.findViewById(R.id.shot_left_side_center_2);
        far_right_center2 = view.findViewById(R.id.shot_far_right_side_center_2);
        far_left_center2 = view.findViewById(R.id.shot_far_left_side_center_2);
        right_center3 = view.findViewById(R.id.shot_right_side_center_3);
        left_center3 = view.findViewById(R.id.shot_left_side_center_3);

        ((QuickstartActivity)getActivity()).positionView();

        top1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "left 3pt corner");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        top2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "left baseline midrange");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        top3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "left layup");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        top4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "right layup");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        top5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "right baseline midrange");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        top6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "right 3pt corner");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        center4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "center deep 3pt");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        center3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "center 3pt");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        center2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "free throw");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        center1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "form shot");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        right_center1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "right midrange wing");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        left_center1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "left midrange wing");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        right_center2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "right elbow");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        left_center2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "left elbow");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        far_right_center2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "right 3pt wing");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        far_left_center2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "left 3pt wing");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        right_center3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "right deep 3pt");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        left_center3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickstartFragment quickstartFragment = new QuickstartFragment();
                Bundle result = new Bundle();
                result.putString("position", "left deep 3pt");
                quickstartFragment.setArguments(result);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, quickstartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
