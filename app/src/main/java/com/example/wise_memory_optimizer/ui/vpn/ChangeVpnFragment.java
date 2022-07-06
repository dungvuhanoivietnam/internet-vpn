package com.example.wise_memory_optimizer.ui.vpn;

import static android.app.Activity.RESULT_OK;

import static com.example.wise_memory_optimizer.utils.Const.ON_OFF_APP_CLONE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.wise_memory_optimizer.MainActivity;
import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.databinding.FragmentChangeVpnBinding;
import com.example.wise_memory_optimizer.model.vpn.City;
import com.example.wise_memory_optimizer.model.vpn.Country;
import com.example.wise_memory_optimizer.model.vpn.Server;
import com.example.wise_memory_optimizer.ui.dialog.DialogInformationVpn;
import com.example.wise_memory_optimizer.ui.dialog.DialogLoadingVpn;
import com.example.wise_memory_optimizer.utils.NavigationUtils;
import com.example.wise_memory_optimizer.utils.NetworkUtils;
import com.example.wise_memory_optimizer.utils.SharePrefrenceUtils;
import com.example.wise_memory_optimizer.utils.Utils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;
import de.blinkt.openvpn.core.VpnStatus;

public class ChangeVpnFragment extends Fragment {

    private FragmentChangeVpnBinding binding = null;
    private View view = null;
    private FirebaseDatabase database = null;
    private DatabaseReference databaseReference = null;
    private FirebaseStorage firebaseStorage = null;
    private StorageReference storageRef = null;
    private HashMap<String, Country> hmCountries = new HashMap<>();
    private City dfCity = new City();
    private Server server = new Server();
    private ChangeVpnViewModel viewModel;

    private OpenVPNThread vpnThread = new OpenVPNThread();
    private OpenVPNService vpnService = new OpenVPNService();
    boolean vpnStart = false;
    private DialogLoadingVpn dialogLoadingVpn = null;
    private DialogInformationVpn dialogInformationVpn = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChangeVpnBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(ChangeVpnViewModel.class);
        initViewModel();
        view = binding.getRoot();
        initView();
        initDialog();

        if (NetworkUtils.isConnectVpn() && server.getCountry().contains(viewModel.getServerCahce().getCountry())) {
            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                if (!NetworkUtils.isConnectVpn()) {
                    stopVpn();
                    prepareVpn();
                }else{
                    updateStatus(true);
                }
            } else {
                if (!dialogInformationVpn.isShowing()) {
                    dialogInformationVpn.show();
                    dialogInformationVpn.setState(DialogInformationVpn.TYPE_INFO.ERROR_NETWORK);
                }
            }
        }

        if (dfCity.getCode() != null) {
            initDefaultCountry();
            return view;
        }

        if (NetworkUtils.isNetworkAvailable(getActivity()) && !ON_OFF_APP_CLONE) {
            if (!dialogLoadingVpn.isShowing()) {
                dialogLoadingVpn.show();
                dialogLoadingVpn.loadingInfo();
                viewModel.getData(databaseReference, getContext(), o -> {
                    initViewModel();
                    initDefaultCountry();
                    if (dialogLoadingVpn.isShowing())
                        dialogLoadingVpn.dismiss();
                });
            }
        } else {
            if (!dialogInformationVpn.isShowing()) {
                dialogInformationVpn.show();
                dialogInformationVpn.setState(DialogInformationVpn.TYPE_INFO.ERROR_NETWORK);
            }
        }
        return view;
    }

    private void initViewModel() {
        dfCity = viewModel.getDfCity();
        hmCountries = viewModel.getHmCountries();
        if (dfCity != null) {
            server.setCountry(dfCity.getCountry());
            server.setOvpn(dfCity.getCode() + ".ovpn");
            server.setOvpnUserName(dfCity.getUsername());
            server.setOvpnUserPassword(dfCity.getPass());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getActivity());
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = database.getReference();
        storageRef = firebaseStorage.getReference();
        isServiceRunning();
        VpnStatus.initLogCache(getActivity().getCacheDir());
        initDialog();
    }

    private void initView() {
        binding.txtCountDown.setText("00 : 00 : 00");
        binding.llToolbar.toolbarTitle.setText(getContext().getString(R.string.vpn));
        binding.llToolbar.ivBack.setOnClickListener(view1 -> {
            NavigationUtils.Companion.back(binding.getRoot());
        });
        binding.ivStart.setOnClickListener(view1 -> {
            if (!Utils.Companion.checkDoubleClick())
                return;
            if (vpnStart) {
                stopVpn();
            } else {
                prepareVpn();
            }
        });

        binding.ctCountry.setOnClickListener(view1 -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_list_location, null);
        });
        updateStatus(false);
    }

    private void updateStatus(boolean isOn) {
        if (binding == null)
            return;
        binding.txtStatus.setText(getContext().getString(isOn ? R.string.on_vpn : R.string.off_vpn));
        Glide.with(getContext()).load(isOn ? R.drawable.ic_switch_on_vpn : R.drawable.ic_vpn_off).into(binding.ivStart);
    }

    private void initDialog() {
        dialogLoadingVpn = new DialogLoadingVpn(getActivity(), R.style.MaterialDialogSheet, o -> {
            if (o) {
                NavigationUtils.Companion.back(binding.getRoot());
            } else {
                stopVpn();
            }
            if (dialogLoadingVpn.isShowing())
                dialogLoadingVpn.dismiss();
        });
        dialogInformationVpn = new DialogInformationVpn(getContext(), R.style.MaterialDialogSheet, type -> {
            if (dialogInformationVpn.isShowing()) {
                dialogInformationVpn.dismiss();
            }
            if (type == DialogInformationVpn.TYPE_INFO.ERROR_VPN)
                stopVpn();
            if (type == DialogInformationVpn.TYPE_INFO.ERROR_NETWORK || type == DialogInformationVpn.TYPE_INFO.ERROR_SERVER) {
                NavigationUtils.Companion.back(binding.getRoot());
            }
        });
    }

    public void isServiceRunning() {
        setStatus(vpnService.getStatus());
    }

    public void setStatus(String connectionState) {
        if (connectionState != null)
            switch (connectionState) {
                case "DISCONNECTED":
                    vpnStart = false;
                    vpnService.setDefaultStatus();
                    if (dialogLoadingVpn != null && dialogLoadingVpn.isShowing())
                        dialogLoadingVpn.dismiss();
//                    if (dialogInformationVpn != null && !dialogInformationVpn.isShowing()) {
//                        dialogInformationVpn.show();
//                        dialogInformationVpn.setState(DialogInformationVpn.TYPE_INFO.ERROR_VPN);
//                    }
                    break;
                case "CONNECTED":
                    vpnStart = true;
                    if (dialogLoadingVpn != null && dialogLoadingVpn.isShowing())
                        dialogLoadingVpn.dismiss();
                    if (dialogInformationVpn != null && !dialogInformationVpn.isShowing()) {
                        dialogInformationVpn.show();
                        dialogInformationVpn.setState(DialogInformationVpn.TYPE_INFO.SUCCESS_VPN);
                    }
                    if (viewModel != null)
                        viewModel.setServerCahce(server);
                    updateStatus(true);
                    break;
                case "WAIT":
                    if (dialogLoadingVpn != null && dialogLoadingVpn.isShowing())
                        dialogLoadingVpn.setStatus(getContext().getString(R.string.waiting_for_server_connection));
                    break;
                case "AUTH":
                    if (dialogLoadingVpn != null && dialogLoadingVpn.isShowing())
                        dialogLoadingVpn.setStatus(getContext().getString(R.string.server_authenticating));
                    break;
                case "RECONNECTING":
                    if (dialogLoadingVpn != null && dialogLoadingVpn.isShowing())
                        dialogLoadingVpn.setStatus(getContext().getString(R.string.reconnecting));
                    break;
                case "NONETWORK":
                    if (dialogLoadingVpn != null && dialogLoadingVpn.isShowing())
                        dialogLoadingVpn.setStatus(getContext().getString(R.string.no_network_connection));
                    break;
            }

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                setStatus(intent.getStringExtra("state"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String duration = intent.getStringExtra("duration");
                String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");

                if (duration == null) duration = "00:00:00";
                if (lastPacketReceive == null) lastPacketReceive = "0";
                if (byteIn == null) byteIn = "";
                if (byteOut == null) byteOut = "";
                binding.txtCountDown.setText(duration);

                if (!"".equals(byteIn)) {
                    SharePrefrenceUtils.getInstance(getContext()).saveDown(dfCity.getCode(), byteIn);
                    binding.txtSpeedDownload.setText(byteIn + "");
                }
                if (!"".equals(byteOut)) {
                    binding.txtSpeedUpload.setText(byteOut + "");
                    SharePrefrenceUtils.getInstance(getContext()).saveUp(dfCity.getCode(), byteOut);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void initDefaultCountry() {
        if (dfCity == null)
            return;

        String name = dfCity.getName();
        Country country = hmCountries.get(dfCity.getCountry());
        if (country == null)
            return;
        Glide.with(getContext()).load(country.getFlag()).into(binding.ivFlag);
        binding.txtCountry.setText("".equals(name) ? country.getName() : name);
        binding.txtIpAddress.setText(getContext().getString(R.string.ip) + " " + dfCity.getIp());

        String speedDown = SharePrefrenceUtils.getInstance(getContext()).getDown(dfCity.getCode());
        if (!"".equals(speedDown)) {
            binding.txtSpeedDownload.setText(speedDown);
        } else {
            String ipFake = "220" + getContext().getString(R.string.speed_down_up);
            binding.txtSpeedDownload.setText(ipFake);
            SharePrefrenceUtils.getInstance(getContext()).saveDown(dfCity.getCode(), ipFake);
        }

        String speedUp = SharePrefrenceUtils.getInstance(getContext()).getup(dfCity.getCode());
        if (!"".equals(speedUp)) {
            binding.txtSpeedUpload.setText(speedUp);
        } else {
            String ipFake = "220" + getContext().getString(R.string.speed_down_up);
            binding.txtSpeedUpload.setText(ipFake);
            SharePrefrenceUtils.getInstance(getContext()).saveUp(dfCity.getCode(), ipFake);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));

        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            startVpn();
        } else {
//            showToast("Permission Deny !! ");
        }
    }

    /**
     * Start the VPN
     */
    private void startVpn() {
        // .ovpn file
        storageRef.child("ovpn").child(server.getOvpn()).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            try {
                OpenVpnApi.startVpn(getContext(), new String(bytes, StandardCharsets.UTF_8), server.getCountry(), server.getOvpnUserName(), server.getOvpnUserPassword(), MainActivity.class);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (dialogLoadingVpn != null && !dialogLoadingVpn.isShowing())
                dialogLoadingVpn.show();
            if (dialogLoadingVpn.isShowing())
                dialogLoadingVpn.setStatus(getContext().getString(R.string.connecting));
            vpnStart = true;
        });

    }

    /**
     * Prepare for vpn connect with required permission
     */
    private void prepareVpn() {
        if (!vpnStart) {
            if (getInternetStatus()) {
                Intent intent = VpnService.prepare(getContext());
                if (intent != null) {
                    startActivityForResult(intent, 1);
                } else startVpn();

            } else {

                // No internet connection available
//                showToast("you have no internet connection !!");
            }

        } else if (stopVpn()) {

            // VPN is stopped, show a Toast message.
//            showToast("Disconnect Successfully");
        }
    }

    public boolean getInternetStatus() {
        return NetworkUtils.isNetworkAvailable(getActivity());
    }

    public boolean stopVpn() {
        try {
            vpnThread.stop();
            vpnStart = false;
            updateStatus(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}
