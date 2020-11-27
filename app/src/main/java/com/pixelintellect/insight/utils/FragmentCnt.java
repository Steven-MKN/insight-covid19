package com.pixelintellect.insight.utils;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class FragmentCnt{
    /**
     * A quick way of displaying a new fragment
     * @param toReplace the fragment container
     * @param fragment the instance of the new fragment being displayed
     * @param manager the current fragment manager
     * @param addBackStack add the fragment being replaced to the back stack?
     * @param isPrimary is the new fragment the primary fragment? if true, current back stack is deleted
     */
    public static void to(int toReplace, Fragment fragment, FragmentManager manager, boolean addBackStack, boolean isPrimary){

        if (isPrimary){

            // deletes all fragments in the back stack
            int backStackEntry = manager.getBackStackEntryCount();
            if (backStackEntry > 0) {
                for (int i = 0; i < backStackEntry; i++) {
                    manager.popBackStackImmediate();
                }
            }

            // removes fragments that exist in the fragment manager
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

    /**
     * A quick way of displaying a new fragment
     * @param toReplace the fragment container
     * @param fragment the instance of the new fragment being displayed
     * @param manager the current fragment manager
     * @param tag the tag of the new fragment
     * @param addBackStack add the fragment being replaced to the back stack?
     * @param isPrimary is the new fragment the primary fragment? if true, current back stack is deleted
     */
    public static void to(int toReplace, Fragment fragment, FragmentManager manager, String tag, boolean addBackStack, boolean isPrimary){

        if (isPrimary){

            // deletes all fragments in the back stack
            int backStackEntry = manager.getBackStackEntryCount();
            if (backStackEntry > 0) {
                for (int i = 0; i < backStackEntry; i++) {
                    manager.popBackStackImmediate();
                }
            }

            // removes fragments that exist in the fragment manager
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
