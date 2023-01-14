package com.example.uberapp_tim12.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.holder.FavouriteRideHolder;
import com.example.uberapp_tim12.model.FavouriteRide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class FavouriteRidesAdapter extends RecyclerView.Adapter<FavouriteRideHolder>{
    Context context;
    List<FavouriteRide> items;

    public FavouriteRidesAdapter(Context context, List<FavouriteRide> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public FavouriteRideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouriteRideHolder(LayoutInflater.from(context).inflate(R.layout.favourite_ride_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteRideHolder holder, int position) {
        holder.departure.setText(items.get(position).getDeparture());
        holder.destination.setText(items.get(position).getDestination());
        holder.passengers.setText("Only you");

        holder.dropDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.dropDownButton);
                popup.inflate(R.menu.favourite_route_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()) {
                            case "Remove":
                                new MaterialAlertDialogBuilder(context)
                                        .setTitle("Remove favourite route")
                                        .setMessage("Are you sure you want to remove chosen route?")
                                        .setPositiveButton(context.getResources().getString(R.string.confirm),
                                                (dialogInterface, i) -> {

                                        })
                                        .setNegativeButton(context.getResources().getString(R.string.cancel),
                                                (dialogInterface, i) -> {

                                        })
                                        .show();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private void showRemoveDialog() {

    }

    private void showRenameDialog() {

    }
}
