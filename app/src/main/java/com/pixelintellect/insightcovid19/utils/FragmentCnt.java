package com.pixelintellect.insightcovid19.utils;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class FragmentCnt{
    public static void to(int toReplace, Fragment fragment, FragmentManager manager, boolean addBackStack, boolean isPrimary){

        if (isPrimary){
            int backStackEntry = manager.getBackStackEntryCount();
            if (backStackEntry > 0) {
                for (int i = 0; i < backStackEntry; i++) {
                    manager.popBackStackImmediate();
                }
            }

            if (manager.getFragments() != null && manager.getFragments().size() > 0) {
                for (int i = 0; i < manager.getFragments().size(); i++) {
                    Fragment mFragment = manager.getFragments().get(i);
                    if (mFragment != null) {
                        manager.beginTransaction().remove(mFragment).commit();
                    }
                }
            }
        }
        FragmentTransaction transaction = manager.beginTransaction();

        if (addBackStack) {
            transaction.addToBackStack(null);
        }
        if (isPrimary){

            transaction.setPrimaryNavigationFragment(fragment);

        }
        transaction.replace(toReplace, fragment)
                .commit();

    }

    public static void to(int toReplace, Fragment fragment, FragmentManager manager, String tag, boolean addBackStack, boolean isPrimary){

        if (isPrimary){
            int backStackEntry = manager.getBackStackEntryCount();
            if (backStackEntry > 0) {
                for (int i = 0; i < backStackEntry; i++) {
                    manager.popBackStackImmediate();
                }
            }

            if (manager.getFragments() != null && manager.getFragments().size() > 0) {
                for (int i = 0; i < manager.getFragments().size(); i++) {
                    Fragment mFragment = manager.getFragments().get(i);
                    if (mFragment != null) {
                        manager.beginTransaction().remove(mFragment).commit();
                    }
                }
            }
        }
        FragmentTransaction transaction = manager.beginTransaction();

        if (addBackStack) {
            transaction.addToBackStack(null);
        }
        if (isPrimary){

            transaction.setPrimaryNavigationFragment(fragment);

        }
        transaction.replace(toReplace, fragment, tag)
                .commit();

    }
}
