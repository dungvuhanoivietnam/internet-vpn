package com.example.wise_memory_optimizer.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.custom.ExtTextView;


public class DialogLoadingVpn extends Dialog {

    Consumer<Boolean> consumerClick;

    public DialogLoadingVpn(@NonNull Context context, int themeResId, Consumer<Boolean> consumerClick) {
        super(context, themeResId);
        this.consumerClick = consumerClick;
    }

    private ExtTextView txtStatus, txtContent;
    private boolean isInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.dialog_loading_vpn);

        txtStatus = findViewById(R.id.txtStatus);
        txtContent = findViewById(R.id.txtContent);
        findViewById(R.id.txtCancel).setOnClickListener(view -> {
            consumerClick.accept(isInfo);
        });
    }

    public void setStatus(String text) {
        txtStatus.setText(text);
        txtStatus.setVisibility(View.VISIBLE);
    }

    public void loadingInfo() {
        isInfo = true;
        txtContent.setText(getContext().getString(R.string.getting_vpn_information));
        txtStatus.setVisibility(View.GONE);
    }

    @Override
    public void dismiss() {
        isInfo = false;
        super.dismiss();
    }
}
