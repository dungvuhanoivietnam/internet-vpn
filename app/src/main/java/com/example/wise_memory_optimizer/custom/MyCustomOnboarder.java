package com.example.wise_memory_optimizer.custom;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aemerse.onboard.OnboardAdvanced;
import com.aemerse.onboard.OnboardFragment;
import com.example.wise_memory_optimizer.MainActivity;
import com.example.wise_memory_optimizer.R;

public class MyCustomOnboarder extends OnboardAdvanced {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(OnboardFragment.newInstance("Internet and VPN\n solution for you", "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint.",
                R.drawable.bg_guide_ip_1,
                R.drawable.bg_guide,
                getResources().getColor(R.color.color_4B5CBF),
                getResources().getColor(R.color.white),0,0,R.drawable.bg_guide));
        addSlide(OnboardFragment.newInstance("Internet and VPN\n solution for you", "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint.",
                R.drawable.bg_guide_ip_2,
                R.drawable.bg_guide,
                getResources().getColor(R.color.color_4B5CBF),
                getResources().getColor(R.color.white),0,0,R.drawable.bg_guide));
        addSlide(OnboardFragment.newInstance("Internet and VPN\n solution for you", "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint.",
                R.drawable.bg_guide_ip_3,
                R.drawable.bg_guide,
                getResources().getColor(R.color.color_4B5CBF),
                getResources().getColor(R.color.white),0,0,R.drawable.bg_guide));

    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        Bundle args = new Bundle();
        args.putBoolean("onboarding", true);
        intent.putExtras(args);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        Bundle args = new Bundle();
        args.putBoolean("onboarding", true);
        intent.putExtras(args);
        startActivity(intent);
        finish();
    }
}
