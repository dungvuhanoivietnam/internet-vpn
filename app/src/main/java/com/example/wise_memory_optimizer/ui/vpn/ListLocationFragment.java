package com.example.wise_memory_optimizer.ui.vpn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.databinding.FragmentChangeLocationBinding;
import com.example.wise_memory_optimizer.model.vpn.City;
import com.example.wise_memory_optimizer.model.vpn.Country;
import com.example.wise_memory_optimizer.ui.adapter.LocationAdapter;
import com.example.wise_memory_optimizer.utils.NavigationUtils;
import com.example.wise_memory_optimizer.utils.SharePrefrenceUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class ListLocationFragment extends Fragment {

    private ArrayList<Country> countries = new ArrayList<>();
    private HashMap<String, ArrayList<City>> spCities = new HashMap<>();
    private LocationAdapter locationAdapter;
    private ChangeVpnViewModel changeVpnViewModel = null;

    private FragmentChangeLocationBinding binding = null;
    private View view = null;
    private City dfCity = new City();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChangeLocationBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        getViewModel();
        binding.llToolbar.toolbarTitle.setText(getContext().getString(R.string.location_vpn));
        binding.llToolbar.ivBack.setOnClickListener(view1 -> {
            NavigationUtils.Companion.back(binding.getRoot());
        });
        binding.rcvLocation.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        dfCity = changeVpnViewModel.getDfCity();
        for (City city : changeVpnViewModel.getCitySmarts()) {
            ArrayList<City> cities = spCities.get(city.getCountry());
            if (cities != null && cities.size() == 1 && city.getCode().contains(cities.get(0).getCode())) {
                for (Country country : countries) {
                    if (country.getCode().contains(cities.get(0).getCountry())) {
                        countries.remove(country);
                        break;
                    }
                }
            } else {
                ArrayList<City> mCities = spCities.get(city.getCountry());
                for (City mCity : mCities) {
                    if (mCity.getCode().contains(city.getCode())) {
                        spCities.get(city.getCountry()).remove(mCity);
                        break;
                    }
                }

            }
        }
        locationAdapter = new LocationAdapter(getContext(), changeVpnViewModel.getHmCountries(), countries, spCities, dfCity, changeVpnViewModel.getCitySmarts(), city -> {
            dfCity = city;
            binding.llToolbar.ivSuccess.setVisibility(!city.getCode().contains(changeVpnViewModel.getDfCity().getCode()) ? View.VISIBLE : View.GONE);
        });
        binding.rcvLocation.setAdapter(locationAdapter);
        binding.llToolbar.ivSuccess.setOnClickListener(view1 -> {
            changeVpnViewModel.setDfCity(dfCity);
            SharePrefrenceUtils.getInstance(getContext()).saveDfCity(dfCity.getCode());
            NavigationUtils.Companion.back(binding.getRoot());
        });
        return view;
    }

    private void getViewModel() {
        changeVpnViewModel = new ViewModelProvider(getActivity()).get(ChangeVpnViewModel.class);
        countries = changeVpnViewModel.getCountries();
        spCities = changeVpnViewModel.getSpCities();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
