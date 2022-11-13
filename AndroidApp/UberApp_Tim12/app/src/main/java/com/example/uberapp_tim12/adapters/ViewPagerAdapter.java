package com.example.uberapp_tim12.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.uberapp_tim12.fragments.ChatFragment;
import com.example.uberapp_tim12.fragments.NotificationsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentActivity fragmentActivity){super(fragmentActivity);}

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1)
            return new NotificationsFragment();
        return new ChatFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
