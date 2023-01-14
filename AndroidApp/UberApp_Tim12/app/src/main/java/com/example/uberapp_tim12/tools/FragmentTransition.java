package com.example.uberapp_tim12.tools;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim12.R;

public class FragmentTransition {

    public static void passengerTo(Fragment newFragment, FragmentActivity activity)
    {
        passengerTo(newFragment, activity, true);
    }

    public static void passengerTo(Fragment newFragment, FragmentActivity activity, boolean addToBackstack)
    {
        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.passengerMainContent, newFragment);
        if(addToBackstack) transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void driverTo(Fragment newFragment, FragmentActivity activity)
    {
        passengerTo(newFragment, activity, true);
    }

    public static void driverTo(Fragment newFragment, FragmentActivity activity, boolean addToBackstack)
    {
        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.driverMainContent, newFragment);
        if(addToBackstack) transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void remove(Fragment fragment, FragmentActivity activity) // TODO izbaciti fragment parametar
    {
        activity.getSupportFragmentManager().popBackStack();
    }
}
