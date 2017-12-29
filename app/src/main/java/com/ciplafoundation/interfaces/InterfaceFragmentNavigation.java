package com.ciplafoundation.interfaces;

import android.app.Fragment;

/**
 * Created by User-129-pc on 09-11-2017.
 */

public interface InterfaceFragmentNavigation {


        public void onForwardNavigation(Fragment fragment);

        public void onBackwardNavigation(String tag);

}