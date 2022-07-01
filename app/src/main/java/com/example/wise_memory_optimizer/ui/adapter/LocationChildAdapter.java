package com.example.wise_memory_optimizer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.custom.ExtTextView;
import com.example.wise_memory_optimizer.model.vpn.City;
import com.example.wise_memory_optimizer.model.vpn.Country;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LocationChildAdapter extends RecyclerView.Adapter<LocationChildAdapter.ViewHolder> {

    private Consumer<City> consumer;
    private Context context;
    private ArrayList<City> cities;
    private City dfCity;
    private Country country;

    public LocationChildAdapter(Context context, ArrayList<City> cities, City dfCity, Country country, Consumer<City> consumer) {
        this.consumer = consumer;
        this.context = context;
        this.cities = cities;
        this.dfCity = dfCity;
        this.country = country;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location_vpn_child, parent, false);
        return new LocationChildAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = cities.get(position);
        if (city == null)
            return;

        Glide.with(context).load(country.getFlag()).into(holder.ivFlag);
        holder.txtCountry.setText(country.getName());
        holder.txtCityName.setText("".equals(city.getName()) ? country.getName() : city.getName());
        holder.txtIpAddress.setText(city.getPing() + " " + context.getString(R.string.ms));
        holder.llContent.setOnClickListener(view -> consumer.accept(city));

        boolean isSelect = dfCity.getCode().contains(city.getCode());
        holder.ivSelect.setImageResource(isSelect ? R.drawable.ic_vpn_selected : 0);
        holder.ivSelect.setBackgroundResource(isSelect ? R.drawable.bg_cicrle_gray_656 : R.drawable.bg_cicrle_gray_45);
        holder.llContent.setBackgroundResource(isSelect ? R.drawable.bg_vpn_selected : R.drawable.bg_transparent_border);
    }

    @Override
    public int getItemCount() {
        return cities != null ? cities.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivFlag, ivPing, ivSelect;
        ExtTextView txtCountry, txtIpAddress, txtCityName, txtDot;
        LinearLayout llContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFlag = itemView.findViewById(R.id.ivFlag);
            ivPing = itemView.findViewById(R.id.ivPing);
            txtCountry = itemView.findViewById(R.id.txtCountry);
            txtIpAddress = itemView.findViewById(R.id.txtIpAddress);
            txtCityName = itemView.findViewById(R.id.txtCityName);
            txtDot = itemView.findViewById(R.id.txtDot);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            llContent = itemView.findViewById(R.id.llContent);
        }
    }
}
