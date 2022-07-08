package com.example.wise_memory_optimizer.custom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aemerse.onboard.OnboardAdvanced;
import com.aemerse.onboard.OnboardFragment;
import com.aemerse.onboard.ScreenUtils;
import com.example.wise_memory_optimizer.MainActivity;
import com.example.wise_memory_optimizer.MyContextWrapper;
import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.ui.battery.PreferenceUtil;
import com.example.wise_memory_optimizer.ui.menu.ui.LanguageFragment;

public class MyCustomOnboarder extends OnboardAdvanced {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.transparentStatusAndNavigation(this);
        addSlide(OnboardFragment.newInstance(getString(R.string.onboard_title_1), getString(R.string.onboard_des_1),
                R.drawable.bg_guide_ip_1,
                R.drawable.bg_guide,
                getResources().getColor(R.color.color_4B5CBF),
                getResources().getColor(R.color.white), 0, 0, R.drawable.bg_guide));
        addSlide(OnboardFragment.newInstance(getString(R.string.onboard_title_2), getString(R.string.onboard_des_2),
                R.drawable.bg_guide_ip_2,
                R.drawable.bg_guide,
                getResources().getColor(R.color.color_4B5CBF),
                getResources().getColor(R.color.white), 0, 0, R.drawable.bg_guide));
        addSlide(OnboardFragment.newInstance(getString(R.string.onboard_title_3), getString(R.string.onboard_des_3),
                R.drawable.bg_guide_ip_3,
                R.drawable.bg_guide,
                getResources().getColor(R.color.color_4B5CBF),
                getResources().getColor(R.color.white), 0, 0, R.drawable.bg_guide));
        String language = PreferenceUtil.getString(getBaseContext(), PreferenceUtil.SETTING_ENGLISH, "");
        if ("".equals(language))
            addFragment(new LanguageFragment(true));
//        getSupportFragmentManager().beginTransaction().add(R.id.background, new LanguageFragment());
    }

    public void removeFragLang() {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        String language = PreferenceUtil.getString(newBase, PreferenceUtil.SETTING_ENGLISH, "");
        super.attachBaseContext(MyContextWrapper.wrap(newBase, language));
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        Bundle args = new Bundle();
        args.putBoolean("onboarding", true);
        intent.putExtras(args);
        PreferenceUtil.saveBoolean(getApplicationContext(), PreferenceUtil.OPEN_APP_FIRST_TIME, false);
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
        PreferenceUtil.saveBoolean(getApplicationContext(), PreferenceUtil.OPEN_APP_FIRST_TIME, false);
        startActivity(intent);
        finish();
    }
}
