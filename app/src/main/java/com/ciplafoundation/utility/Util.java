package com.ciplafoundation.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ciplafoundation.R;
import com.ciplafoundation.interfaces.InterfaceDialogCallback;
import com.ciplafoundation.model.AcceptedProposal;
import com.ciplafoundation.model.PendingProposal;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.model.UserDivision;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static String USERCLASS = "USERCLASS";
    private static String USERDIVISIONLIST = "USERDIVISIONLIST";
    private static String ACCEPTEDPROPOSALLIST = "ACCEPTEDPROPOSALLIST";
    private static String PENDINGPROPOSALLIST = "PENDINGPROPOSALLIST";

  static   Context mContext;

    public static void showDialog(Activity activity, String title, String msg,
                                  final InterfaceDialogCallback interfaceDialogCallback) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (interfaceDialogCallback != null)
                            interfaceDialogCallback.onClickAlertPositiveButton();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public static boolean checkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }


    public static String getDeviceID(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = manager.getDeviceId();
        return deviceId = (deviceId == null ? "000" : deviceId);
    }

    // Saving UserClass details
    public static void saveUserClass(final Context mContext, UserClass userClass) {
        SharedPreferences pwdPrefs = mContext.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pwdPrefs.edit();
        try {
            prefsEditor.putString(USERCLASS, ObjectSerializer.serialize(userClass));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }
    // Fetching UserClass details
    public static UserClass fetchUserClass(final Context mContext) {
        SharedPreferences pwdPrefs = mContext.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        UserClass userClass = null;
        String serializeOrg = pwdPrefs.getString(USERCLASS, null);
        try {
            if (serializeOrg != null) {
                userClass = (UserClass) ObjectSerializer.deserialize(serializeOrg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return userClass;
    }

    // Saving UserDivision details
    public static void saveUserDivision(final Context mContext, ArrayList<UserDivision> userDivisionList) {
        SharedPreferences pwdPrefs = mContext.getSharedPreferences(Constants.USER_DIVISION, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pwdPrefs.edit();
        try {
            prefsEditor.putString(USERDIVISIONLIST, ObjectSerializer.serialize(userDivisionList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }

    // Fetching UserDivision details
    public static ArrayList<UserDivision> fetchUserDivision(final Context mContext) {
        SharedPreferences pwdPrefs = mContext.getSharedPreferences(Constants.USER_DIVISION, Context.MODE_PRIVATE);
        ArrayList<UserDivision> arrUserDivision = null;
        String serializeOrg = pwdPrefs.getString(USERDIVISIONLIST, null);
        try {
            if (serializeOrg != null) {
                arrUserDivision = (ArrayList<UserDivision>) ObjectSerializer.deserialize(serializeOrg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return arrUserDivision;
    }

    // Saving Accepted Proposal details
    public static void saveAcceptedProposal(final Context mContext, ArrayList<AcceptedProposal> acceptedProposalList) {
        SharedPreferences pwdPrefs = mContext.getSharedPreferences(Constants.ACCEPTED_PROPOSAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pwdPrefs.edit();
        try {
            prefsEditor.putString(ACCEPTEDPROPOSALLIST, ObjectSerializer.serialize(acceptedProposalList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }

    // Fetching Pending Proposal details
    public static ArrayList<AcceptedProposal> fetchAcceptedProposal(final Context mContext) {
        SharedPreferences pwdPrefs = mContext.getSharedPreferences(Constants.ACCEPTED_PROPOSAL, Context.MODE_PRIVATE);
        ArrayList<AcceptedProposal> arrAcceptedProposal = null;
        String serializeOrg = pwdPrefs.getString(ACCEPTEDPROPOSALLIST, null);
        try {
            if (serializeOrg != null) {
                arrAcceptedProposal = (ArrayList<AcceptedProposal>) ObjectSerializer.deserialize(serializeOrg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return arrAcceptedProposal;
    }

    // Saving PendingProposal details
    public static void savePendingProposal(final Context mContext, ArrayList<PendingProposal> userDivisionList) {
        SharedPreferences pwdPrefs = mContext.getSharedPreferences(Constants.PENDING_PROPOSAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pwdPrefs.edit();
        try {
            prefsEditor.putString(PENDINGPROPOSALLIST, ObjectSerializer.serialize(userDivisionList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }

    // Fetching UserDivision details
    public static ArrayList<PendingProposal> fetchPendingProposal(final Context mContext) {
        SharedPreferences pwdPrefs = mContext.getSharedPreferences(Constants.PENDING_PROPOSAL, Context.MODE_PRIVATE);
        ArrayList<PendingProposal> arrPendingProposal = null;
        String serializeOrg = pwdPrefs.getString(PENDINGPROPOSALLIST, null);
        try {
            if (serializeOrg != null) {
                arrPendingProposal = (ArrayList<PendingProposal>) ObjectSerializer.deserialize(serializeOrg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return arrPendingProposal;
    }

    public static void buildAlertMessageNoGps(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(
                "Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        context.startActivity(new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showMessageWithOk(final Context mContext, final String message) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(R.string.app_name);

                alert.setMessage(message);
                alert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                alert.show();
            }
        });
    }

    public static void showCallBackMessageWithOk(final Context mContext,
                                                 final String message, final AlertDialogCallBack callBack) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(
                        mContext);
                alert.setTitle(R.string.app_name);
                alert.setCancelable(false);
                alert.setMessage(message);
                alert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                callBack.onSubmit();
                            }
                        });
                alert.show();
            }
        });
    }

    public static void showCallBackMessage(final Context mContext,
                                           final String message, final AlertDialogCallBack callBack) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(
                        mContext);
                alert.setTitle(R.string.app_name);
                alert.setCancelable(false);
                alert.setMessage(message);
                alert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // alert.cancel();
                                callBack.onSubmit();
                            }
                        });
                alert.show();
            }
        });
    }

    public static void showCallBackMessageWithOkCancel(final Context mContext,
                                                       final String message, final AlertDialogCallBack callBack) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(
                        mContext);
                alert.setTitle(R.string.app_name);
                alert.setCancelable(false);
                alert.setMessage(message);
                alert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                callBack.onSubmit();
                            }
                        });
                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                callBack.onCancel();
                            }
                        });
                alert.show();
            }
        });
    }

    public static void showMessageWithOkFocus(final Context mContext,
                                              final String message, final ScrollView mScrollView, final View mView) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(R.string.app_name);

        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.scrollTo(0, mView.getBottom());
                    }
                });
            }
        });
        alert.show();
    }

    public static String getBase64StringFromBitmap(Bitmap mBitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        return (Base64.encodeToString(ba, Base64.DEFAULT));
    }

    public static Bitmap getBitmapBase64FromString(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return (decodedByte);
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {

            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it

        return dir.delete();
    }


    public static String ReadFromfile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    public static AlertDialog showSettingsAlert(final Context applicationContext,
                                                AlertDialog systemAlertDialog) {
        //Log.v("calling showSettingsAlert()", "true");

        AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
        builder.setTitle("GPS Disabled");
        //builder.setIcon(R.drawable.warning);
        builder.setCancelable(false);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // mContext.startActivity(new
                // Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // <-- Newly added line
                applicationContext.startActivity(viewIntent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        systemAlertDialog = builder.create();
        systemAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        systemAlertDialog.show();
        return systemAlertDialog;
    }

    // Saving Registration details
    public static void isAllDataFetched(final Context mContext, boolean done) {
        SharedPreferences emsPrefs = mContext.getSharedPreferences(
                Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = emsPrefs.edit();
        prefsEditor.putBoolean(Constants.PREF_ALLDATA_NAME, done);
        prefsEditor.commit();
    }

    // if Registration is done
    public static boolean fetchIfAllDataFetched(final Context mContext) {
        SharedPreferences emsPrefs = mContext.getSharedPreferences(
                Constants.PREF_NAME, Context.MODE_PRIVATE);
        return emsPrefs.getBoolean(Constants.PREF_ALLDATA_NAME, false);
    }

    /**
     * Method to return custom font
     *
     * @return Typeface
     */
    public static Typeface changeFont(Context context, String font) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
                font);
        return typeFace;

    }

    //animation slide up and slide down /

    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        //a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
       a.setDuration(600);
        v.startAnimation(a);
    }
    ///colapes/
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        //a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(600);
        v.startAnimation(a);
    }
    public static void showErrorDialog(final Context context, final String message) {
        try {
            ((Activity) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            try {
                ((FragmentActivity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e2) {
                try {
                    ((Activity) context.getApplicationContext()).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e3) {
                    Toast.makeText(context.getApplicationContext(), message,Toast.LENGTH_LONG).show();

                }
            }

        }
    }


    public  static boolean isConnectionPossible() {

        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }



}
