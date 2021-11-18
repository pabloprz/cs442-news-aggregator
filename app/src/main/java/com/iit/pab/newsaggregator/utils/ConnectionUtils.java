package com.iit.pab.newsaggregator.utils;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtils {

    public static boolean hasNetworkConnection(Activity activity) {
        ConnectivityManager connectivityManager =
                activity.getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
