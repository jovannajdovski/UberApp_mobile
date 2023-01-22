package com.example.uberapp_tim12.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.controller.RideController;
import com.example.uberapp_tim12.holder.FavouriteRideHolder;
import com.example.uberapp_tim12.model.FavouriteRide;
import com.example.uberapp_tim12.security.LoggedUser;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteRidesAdapter extends RecyclerView.Adapter<FavouriteRideHolder>{
    Context context;
    List<FavouriteRide> items;

    public FavouriteRidesAdapter(Context context, List<FavouriteRide> items) {
        this.context = context;
        this.items = items;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public FavouriteRideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouriteRideHolder(LayoutInflater.from(context).inflate(R.layout.favourite_ride_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteRideHolder holder, int position) {
        holder.title.setText(items.get(position).getFavoriteName());
        holder.departure.setText(items.get(position).getLocations().get(0).getDeparture().getAddress());
        holder.destination.setText(items.get(position).getLocations().get(0).getDestination().getAddress());
        if (items.get(position).getPassengers().size() == 1)
            holder.passengers.setText(R.string.only_you);
        else
            holder.passengers.setText("You and " + items.get(position).getPassengers().size() +
                    " others");

        holder.dropDownButton.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.dropDownButton);
            popup.inflate(R.menu.favourite_route_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "Remove":
                        showDeleteConfirmationDialog(holder, items.get(holder.getBindingAdapterPosition()).getId(), view);
                        break;
                }
                return false;
            });
            popup.show();

        });
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private void showDeleteConfirmationDialog(FavouriteRideHolder holder, Integer id, View view) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Remove favourite route")
                .setMessage("Are you sure you want to remove chosen route?")
                .setPositiveButton(context.getResources().getString(R.string.confirm),
                        (dialogInterface, i) -> {
                            deleteItem(holder, id, view);
                        })
                .setNegativeButton(context.getResources().getString(R.string.cancel),
                        (dialogInterface, i) -> {})
                .show();
    }

    private void deleteItem(FavouriteRideHolder holder, Integer itemId, View view) {
        RideController controller = ControllerUtils.retrofit.create(RideController.class);
        Call<String> call = controller.deleteFavoriteForPassenger(itemId, LoggedUser.getTokenWithBearer());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 204) {
                    items.remove(holder.getBindingAdapterPosition());
                    notifyItemRemoved(holder.getBindingAdapterPosition());
                    showMessage(view, "Favorite route is deleted successfully.");
                } else {
                    showMessage(view, "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showMessage(view, "Something went wrong!");
            }
        });
    }

    private void showMessage(View view, String message) {
        Toast toast = new Toast(view.getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.show();
    }
}
