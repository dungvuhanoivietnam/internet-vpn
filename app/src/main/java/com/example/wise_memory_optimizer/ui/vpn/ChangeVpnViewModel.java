package com.example.wise_memory_optimizer.ui.vpn;

import android.content.Context;
import android.util.Log;

import androidx.core.util.Consumer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.wise_memory_optimizer.model.vpn.City;
import com.example.wise_memory_optimizer.model.vpn.Country;
import com.example.wise_memory_optimizer.model.vpn.Server;
import com.example.wise_memory_optimizer.utils.SharePrefrenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangeVpnViewModel extends ViewModel {

    private final SavedStateHandle state;
    private City dfCity = new City();
    private ArrayList<Country> countries = new ArrayList<>();
    private HashMap<String, ArrayList<City>> spCities = new HashMap<>();
    private HashMap<String, Country> hmCountries = new HashMap<>();
    private Server server = new Server();
    private ArrayList<City> citySmarts = new ArrayList<>();
    private ArrayList<City> cities = new ArrayList<>();
    private Server serverCahce = new Server();

    public ChangeVpnViewModel(SavedStateHandle state) {
        this.state = state;
    }

    public City getDfCity() {
        return dfCity;
    }

    public void setDfCity(City dfCity) {
        this.dfCity = dfCity;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public HashMap<String, ArrayList<City>> getSpCities() {
        return spCities;
    }

    public HashMap<String, Country> getHmCountries() {
        return hmCountries;
    }

    public ArrayList<City> getCitySmarts() {
        return citySmarts;
    }

    public void getData(DatabaseReference databaseReference, Context context, Consumer consumer) {
        databaseReference.child("country").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                hmCountries = new HashMap<>();
                countries = new ArrayList<>();

                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    Country country = dataSnapshot.getValue(Country.class);
                    hmCountries.put(country.getCode(), country);
                    countries.add(country);
                }
            }
        });

        databaseReference.child("city").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                String codeDf = SharePrefrenceUtils.getInstance(context).getDfCity();
                citySmarts = new ArrayList<>();
                dfCity = new City();
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    City city = dataSnapshot.getValue(City.class);

                    if (city == null)
                        continue;

                    cities.add(city);
                    if (dfCity.getCode() == null && city.getDf() == 1 && "".equals(codeDf)) {
                        dfCity = city;
                    }
                    if (dfCity.getCode() == null && !"".equals(codeDf) && codeDf.contains(city.getCode())) {
                        dfCity = city;
                    }
                    if (city.getSmart() == 1) {
                        citySmarts.add(city);
                    }
                }

                if (dfCity != null && dfCity.getCode() != null) {
                    server.setCountry(dfCity.getCountry());
                    server.setOvpn(dfCity.getCode() + ".ovpn");
                    server.setOvpnUserName(dfCity.getUsername());
                    server.setOvpnUserPassword(dfCity.getPass());
                    consumer.accept(new Object());
                }

                spCities = new HashMap<>();
                for (Country country : countries) {
                    ArrayList<City> cities = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        City city = dataSnapshot.getValue(City.class);
                        if (city.getCountry().contains(country.getCode())) {
                            cities.add(city);
                        }
                    }
                    spCities.put(country.getCode(), cities);
                }
            }
        });
    }

    public Server getServerCahce() {
        return serverCahce;
    }

    public void setServerCahce(Server serverCahce) {
        this.serverCahce = serverCahce;
    }

    public ArrayList<City> getCities() {
        return cities;
    }
}
