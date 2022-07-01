package com.example.wise_memory_optimizer.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.custom.ExtTextView;


public class DialogInformationVpn extends Dialog {

    public enum TYPE_INFO {
        ERROR_NETWORK, ERROR_VPN, SUCCESS_VPN, ERROR_SERVER
    }

    private Consumer<TYPE_INFO> consumerClick;
    private TYPE_INFO state;

    public DialogInformationVpn(@NonNull Context context, int themeResId, Consumer<TYPE_INFO> consumerClick) {
        super(context, themeResId);
        this.consumerClick = consumerClick;
    }

    ExtTextView txtContent;
    ImageView ivContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.dialog_info_vpn);

        txtContent = findViewById(R.id.txtContent);
        ivContent = findViewById(R.id.ivContent);
        findViewById(R.id.txtCancel).setOnClickListener(view -> {
            consumerClick.accept(state);
        });
    }

    public void setState(TYPE_INFO state) {
        int text = 0;
        int mDrawable = 0;
        switch (state) {
            case ERROR_VPN:
                text = R.string.error_connect_vpn;
                mDrawable = R.drawable.ic_connect_vpn_error;
                break;
            case SUCCESS_VPN:
                text = R.string.success_connect_vpn;
                mDrawable = R.drawable.ic_connect_vpn_success;
                break;
            case ERROR_NETWORK:
                text = R.string.error_network_vpn;
                mDrawable = R.drawable.ic_error_network;
                break;
            case ERROR_SERVER:
                text = R.string.error_load_server;
                mDrawable = R.drawable.ic_error_loading_server;
                break;

        }
        this.state = state;
        txtContent.setText(getContext().getText(text));
        ivContent.setImageResource(mDrawable);
    }

}
