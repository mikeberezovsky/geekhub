package com.michaelb.berezovskyhw2.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.michaelb.berezovskyhw2.R;

/**
 * Created by michaelb on 10/29/14.
 */
public class AnimationFragment extends Fragment implements AnimationListener {
    Animation animFadeIn, animZoomIn, animRotate, animMove;

    TextView txtView;

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation
        // check for fade in animation
        if (animation == animFadeIn) {
            //do nothing for now
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Animation is repeating
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Animation started
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        animFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        animZoomIn = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        animRotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        animMove = AnimationUtils.loadAnimation(getActivity(), R.anim.move);
        View view = inflater.inflate(R.layout.animation_fragment, container, false);
        txtView = (TextView) view.findViewById(R.id.animation_text_view);
        Button button = (Button) view.findViewById(R.id.btn_fade);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onAnimationButtonClick(v);
            }
        });
        button = (Button) view.findViewById(R.id.btn_zoom);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onAnimationButtonClick(v);
            }
        });
        button = (Button) view.findViewById(R.id.btn_rotate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onAnimationButtonClick(v);
            }
        });
        button = (Button) view.findViewById(R.id.btn_move);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onAnimationButtonClick(v);
            }
        });
        return view;
    }

    public void onAnimationButtonClick (View view) {
        Button sender = (Button) view;
        //String btnTag = sender.getTag().toString();
        switch (sender.getId()) {
            case R.id.btn_fade:
                txtView.startAnimation(animFadeIn);
                break;
            case R.id.btn_zoom:
                txtView.startAnimation(animZoomIn);
                break;
            case R.id.btn_rotate:
                txtView.startAnimation(animRotate);
                break;
            case R.id.btn_move:
                txtView.startAnimation(animMove);
                break;
            default:
                break;
        }
    }

}
