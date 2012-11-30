/* Group 2 
 * travelB - A user friendly travel planner
 * 
 * By:
 * Amabille Leal
 * Dominika Nowak
 * Alison Price
 * Michael Reid
 * 
 * Date: 30th November 2012
 * 
 * v 1.0
 * 
 * File Name: InternetStatus.java
 * Description:
 * 
 * The purpose of this class is to check if the device
 * has internet connectivity.
 */

package mobi.bwize.travelB;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetStatus {

    private static InternetStatus instance = new InternetStatus();
    static Context context;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static InternetStatus getInstance(Context ctx) {
        context = ctx;
        return instance;
    }

    // Check if the device is online or not
    public boolean isOnline(Context con) {
        try {
            connectivityManager = (ConnectivityManager) con
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connected = networkInfo != null && networkInfo.isAvailable() &&
                networkInfo.isConnected();
        return connected;
        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
        }
        return connected;
    }
}