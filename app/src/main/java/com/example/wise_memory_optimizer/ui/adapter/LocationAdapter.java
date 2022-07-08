package com.example.wise_memory_optimizer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.custom.ExtTextView;
import com.example.wise_memory_optimizer.model.vpn.City;
import com.example.wise_memory_optimizer.model.vpn.Country;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Country> countries;
    private HashMap<String, ArrayList<City>> spCities;
    private City dfCity;
    private Consumer<City> consumer;
    private ArrayList<City> smartCities;
    private HashMap<String, Country> stringCountryHashMap;

    private int typeTitle = 1, typeItemSmart = 2, typeItemAllServer = 3;

    public LocationAdapter(Context context, HashMap<String, Country> stringCountryHashMap, ArrayList<Country> countries, HashMap<String, ArrayList<City>> spCities, City city, ArrayList<City> smartCities, Consumer<City> consumer) {
        this.context = context;
        this.consumer = consumer;
        this.countries = countries;
        this.spCities = spCities;
        this.dfCity = city;
        this.smartCities = smartCities;
        this.stringCountryHashMap = stringCountryHashMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        if (viewType == typeTitle) {
            layout = R.layout.item_title_list_location;
        } else {
            layout = R.layout.item_location_vpn;
        }
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new LocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.getItemViewType() == typeTitle) {
            holder.txtTitleTime.setText(context.getString(position == 0 && smartCities != null && smartCities.size() > 0 ? R.string.smart_location : R.string.all_server));
            return;
        }

        ArrayList<City> cities = new ArrayList<>();
        Country country;
        if (holder.getItemViewType() == typeItemSmart) {
            cities.add(smartCities.get(position - 1));
            country = stringCountryHashMap.get(smartCities.get(position - 1).getCountry());
        } else {
            country = countries.get(position - 1 - (smartCities.size() > 0 ? smartCities.size() + 1 : 0));
            cities = spCities.get(country.getCode());
        }

        Glide.with(context).load(country.getFlag()).into(holder.ivFlag);
        holder.txtCountry.setText(country.getName());
        if (cities == null)
            return;
        boolean onlyCity = cities.size() == 1;
        if (!onlyCity) {
            holder.txtCityName.setText(String.format(context.getString(R.string.total_location), cities.size() + ""));
            holder.ivSelect.setImageResource(R.drawable.ic_vpn_expand);
            LocationChildAdapter locationChildAdapter = new LocationChildAdapter(context, cities, dfCity, country, b -> {
                dfCity = b;
                consumer.accept(dfCity);
                notifyDataSetChanged();
            });

            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            holder.recyclerView.setAdapter(locationChildAdapter);
            holder.recyclerView.setVisibility(country.isExpand() ? View.VISIBLE : View.GONE);
        } else {
            boolean isSelect = dfCity.getCode().contains(cities.get(0).getCode());
            holder.txtIpAddress.setText(cities.get(0).getPing() + " " + context.getString(R.string.ms));
            holder.txtCityName.setText("".equals(cities.get(0).getName()) ? country.getName() : cities.get(0).getName());
            holder.ivSelect.setImageResource(isSelect ? R.drawable.ic_vpn_selected : 0);
            holder.ivSelect.setBackgroundResource(isSelect ? R.drawable.bg_cicrle_gray_656 : R.drawable.bg_cicrle_gray_45);
            holder.llContent.setBackgroundResource(isSelect ? R.drawable.bg_vpn_selected : R.drawable.bg_transparent_border);
        }
        holder.txtDot.setVisibility(onlyCity ? View.VISIBLE : View.GONE);
        holder.ivPing.setVisibility(onlyCity ? View.VISIBLE : View.GONE);
        ArrayList<City> finalCities = cities;
        holder.llContent.setOnClickListener(view -> {
            if (onlyCity) {
                dfCity = finalCities.get(0);
                consumer.accept(dfCity);
            } else {
                int mPosition = position - 1 - (smartCities.size() > 0 ? smartCities.size() + 1 : 0);
                countries.get(mPosition).setExpand(!countries.get(mPosition).isExpand());
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (smartCities != null && smartCities.size() > 0 && position < smartCities.size() + 1) {
            if (position == 0)
                return 1;
            else if (position < smartCities.size() + 1) {
                return 2;
            }
        }
        if (countries != null && countries.size() > 0) {
            int totalSmart = smartCities.size();
            if (totalSmart > 0) {
                if (position == totalSmart + 1)
                    return 1;
                else
                    return 3;
            } else {
                if (position == 0)
                    return 1;
                else
                    return 3;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int total = 0;
        if (smartCities != null && smartCities.size() > 0) {
            total += (smartCities.size() + 1);
        }
        if (countries != null && countries.size() > 0)
            total += (countries.size() + 1);
        return total;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivFlag, ivPing, ivSelect;
        ExtTextView txtCountry, txtIpAddress, txtCityName, txtDot, txtTitleTime;
        LinearLayout llContent;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFlag = itemView.findViewById(R.id.ivFlag);
            ivPing = itemView.findViewById(R.id.ivPing);
            txtCountry = itemView.findViewById(R.id.txtCountry);
            txtIpAddress = itemView.findViewById(R.id.txtIpAddress);
            txtCityName = itemView.findViewById(R.id.txtCityName);
            txtDot = itemView.findViewById(R.id.txtDot);
            txtTitleTime = itemView.findViewById(R.id.txtTitleTime);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            llContent = itemView.findViewById(R.id.llContent);
            recyclerView = itemView.findViewById(R.id.rcvChild);
        }
    }
}
