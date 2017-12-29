package com.ciplafoundation.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ciplafoundation.R;
import com.ciplafoundation.interfaces.MultipartPostCallback;
import com.ciplafoundation.model.FileDetails;
import com.ciplafoundation.utility.ServerResponseCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import android.util.Log;

@SuppressLint("ShowToast")
public class VolleyTaskManager extends ServiceConnector {
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private String TAG = "";
    private String tag_json_obj = "jobj_req";
    private boolean isToShowDialog = true, isToHideDialog = true;

    public VolleyTaskManager(Context context) {
        mContext = context;

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading...");

        TAG = mContext.getClass().getSimpleName();
        //Log.d("tag", TAG);
    }

    public void showProgressDialog() {
        if (!mProgressDialog.isShowing())
            try {
                mProgressDialog.show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    public void hideProgressDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    // Making json object request

    private void makeJsonObjReq(int method, String url, final Map<String, String> paramsMap) {

        if (isToShowDialog) {
            showProgressDialog();
        }

         Log.v("JSONObject", new JSONObject(paramsMap).toString());
        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, new JSONObject(paramsMap),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, response.toString());
                        // msgResponse.setText(response.toString());
                        if (isToHideDialog) {
                            hideProgressDialog();
                        }
                        // TODO On getting successful result:
                        ((ServerResponseCallback) mContext).onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                // VolleyLog.d(TAG, "Error: " + error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //Log.d("error ocurred","TimeoutError");
                    Toast.makeText(mContext, mContext.getString(R.string.response_timeout), Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //Log.d("error ocurred","AuthFailureError");
                    Toast.makeText(mContext, mContext.getString(R.string.auth_failure), Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Log.d("error ocurred","ServerError");
                    Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Log.d("error ocurred","NetworkError");
                    Toast.makeText(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //Log.d("error ocurred","ParseError");
                    Toast.makeText(mContext, mContext.getString(R.string.parse_error), Toast.LENGTH_LONG).show();
                }

                ((ServerResponseCallback) mContext).onError();
            }
        }) {

            // Passing some request headers

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

           /* @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Androidhive");
				params.put("email", "abc@androidhive.info");
				params.put("pass", "password123");
                return paramsMap;
            }*/

        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(mContext);
        // Adding request to request queue
        queue.add(jsonObjReq);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

   /* //Method to Call Web service through volley
    public  void  makeJsonObjReq(int method, String url, final Map<String, String> paramsMap)
    {
        RequestQueue queue = Volley.newRequestQueue(mContext);

        if (isToShowDialog) {
            showProgressDialog();
        }

        Log.v("JSONObject", new JSONObject(paramsMap).toString());

        StringRequest jsObjRequest = new StringRequest (Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, response.toString());
                // msgResponse.setText(response.toString());
                if (isToHideDialog) {
                    hideProgressDialog();
                }
                // TODO On getting successful result:
                try {
                    ((ServerResponseCallback) mContext).onSuccess(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //Log.d("error ocurred","TimeoutError");
                    Toast.makeText(mContext, mContext.getString(R.string.response_timeout), Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //Log.d("error ocurred","AuthFailureError");
                    Toast.makeText(mContext, mContext.getString(R.string.auth_failure), Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Log.d("error ocurred","ServerError");
                    Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Log.d("error ocurred","NetworkError");
                    Toast.makeText(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //Log.d("error ocurred","ParseError");
                    Toast.makeText(mContext, mContext.getString(R.string.parse_error), Toast.LENGTH_LONG).show();
                }

                ((ServerResponseCallback) mContext).onError();
            }
        })
        {
            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    return new JSONObject(paramsMap).toString().getBytes();
                } catch (OutOfMemoryError e) {

                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                String headers = "Content-Type=application/json";
                //headers.put("Content-Type","application/json");
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                // Log.e("mStatusCode", " "+mStatusCode);
                return super.parseNetworkResponse(response);
            }
        }
                ;
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);
    }*/

    // Making json object request

    private void makeJsonObjReq(int method, String url, final Map<String, String> paramsMap, final ServerResponseCallback serverResponseCallback) {

        if (isToShowDialog) {
            showProgressDialog();
        }

        // Log.v("JSONObject", new JSONObject(paramsMap).toString());
        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, new JSONObject(paramsMap),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, response.toString());
                        //   msgResponse.setText(response.toString());
                        if (isToHideDialog) {
                            hideProgressDialog();
                        }
                        // TODO On getting successful result:
                        if (serverResponseCallback != null)
                            serverResponseCallback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                // VolleyLog.d(TAG, "Error: " + error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //Log.d("error ocurred","TimeoutError");
                    Toast.makeText(mContext, mContext.getString(R.string.response_timeout), Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //Log.d("error ocurred","AuthFailureError");
                    Toast.makeText(mContext, mContext.getString(R.string.auth_failure), Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Log.d("error ocurred","ServerError");
                    Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Log.d("error ocurred","NetworkError");
                    Toast.makeText(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //Log.d("error ocurred","ParseError");
                    Toast.makeText(mContext, mContext.getString(R.string.parse_error), Toast.LENGTH_LONG).show();
                }

                // TODO On getting successful result:
                if (serverResponseCallback != null)
                    serverResponseCallback.onError();
            }
        }) {

            // Passing some request headers

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

           /* @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Androidhive");
				params.put("email", "abc@androidhive.info");
				params.put("pass", "password123");
                return paramsMap;
            }*/

        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(mContext);
        // Adding request to request queue
        queue.add(jsonObjReq);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    /**
     * Service method calling for Login -->
     **/

    public void doLogin(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/logins/post";
        int method = Method.POST;

        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }

    /**
     * Service method calling for Login First-->
     **/

    public void doLoginFirst(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/loginFirst/post";
        int method = Method.POST;

        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }

    /**
     * Service method calling for Login First-->
     **/

    public void doLoginSecond(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/loginSecond/post";
        int method = Method.POST;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
        // callPostWithJsonWebtask(url, paramsMap,method);
    }


    /**
     * Service method calling for Role -->
     **/

    public void doGetUserRole(String params, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/userrole/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    /**
     * Service method calling for User Division -->
     **/

    public void doGetUserDivision(String params, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/userdivision/get?" + params;
        int method = Method.GET;

        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetProposalList(String params, boolean isToHideDialog) {
        //this.isToShowDialog = isToHideDialog;
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "proposal/lists/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetProjectList(String params, boolean isToHideDialog) {
        //this.isToShowDialog = isToHideDialog;
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "project/lists/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetMadicalList(String params, boolean isToHideDialog) {
        //this.isToShowDialog = isToHideDialog;
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "medicalproposal/lists/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }





    public void doGetAddEventBasic(String params, boolean isToHideDialog) {
        //this.isToShowDialog = isToHideDialog;
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "event/add/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetEditEventDetails(String params, boolean isToHideDialog) {
        //this.isToShowDialog = isToHideDialog;
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "event/edit/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doSaveEvent(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "event/add/post";
        int method = Method.POST;

        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }

    public void doSaveEditedEvent(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "event/edit/post";
        int method = Method.POST;

        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }

    public void doGetBlock(String params, boolean isToHideDialog) {
        //this.isToShowDialog = isToHideDialog;
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "event/blocks/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetVillage(String params, boolean isToHideDialog) {
        //this.isToShowDialog = isToHideDialog;
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "event/villages/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doSaveImage(Activity activity, Map<String, String> map, ArrayList<FileDetails> fileList, MultipartPostCallback multipartPostCallback) {
        //this.isToShowDialog = isToHideDialog;
        MultipartPostRequest request = new MultipartPostRequest(activity, getBaseURL() + "event/eventImage/post",
                map, fileList, "IMAGES");
        request.mListener = multipartPostCallback;
        request.execute();
    }

    public void doGetEventList(String params, boolean isToHideDialog) {
        //this.isToShowDialog = isToHideDialog;
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "event/lists/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetProposalDetails(String params, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "proposal/view/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }


    public void doGetMadicalDetails(String params, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "medicalproposal/view/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }




    public void doGetEventDetails(String params, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "event/view/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetProjectDetails(String params, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "project/view/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetInitiated(String paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "proposal/userListsDivision/get?" + paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);
    }

    public void doGetTreeList(String params, boolean isToHideDialog) {

        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "tree/lists/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetSearchTreeList(String params, boolean isToHideDialog) {

        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "tree/searchlevellist/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }


    /*
    *  For reset password change service calling method
     *  */

    public void doGetForget(String params, boolean isToHideDialog) {

        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/forgetPassword/get?" + params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

   /*
    *  For reset password change for difference user Roll  service calling method
     *  */

    public void doReset(String paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/sentforgetPasswordemail/get?" + paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>());

    }

    public void doGetInitiatedByFillte(String paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "proposal/lists/get?" + paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>());

    }
    public void doGetInitiatedByProjectFillte(String paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "project/lists/get?" + paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>());

    }

    public void doGetInitiatedByMedicalFillte(String paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "medicalproposal/lists/get?" + paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>());

    }

   /*
    *  For reset user role change for difference user Roll  service calling method
     *  */

    public void doResetRole(String paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/userrole/get?" + paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);

    }

    public void doSwitchRoleFirst(String paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/checkRolePassword/get?" + paramsMap;
        //  String url = getBaseURL() + "login/loginSecond/post";
        // String url = getBaseURL() + "login/userrole/get?"+paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);

    }

    public void doSwitchRoleSecond(HashMap<String, String> paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/loginSecond/post";
        int method = Method.POST;
        //Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap, serverResponseCallback);
        // callPostWithJsonWebtask(url, paramsMap,method);
    }

    public void GetUserList(String url, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;

        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);

    }


    public void doSiteRoleSelect(String paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/loginas/get?" + paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);

    }

    public void doLogout(String paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/logout/get?" + paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);

    }

    public void doApprovealProposalReview(HashMap<String, String> paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "proposal/pafApproval/post";
        int method = Method.POST;
        Log.i("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap, serverResponseCallback);

    }

    public void doApprovealProjectReview(HashMap<String, String> paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "project/projectApproval/post";
        int method = Method.POST;
        Log.i("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap, serverResponseCallback);

    }



    public void doApprovealDeviationReview(HashMap<String, String> paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "deviation/pdaApproval/post";
        int method = Method.POST;
        Log.i("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap, serverResponseCallback);

    }





    public void doApprovealprojectPanReview(HashMap<String, String> paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "pan/ppaApproval/post";
        int method = Method.POST;
        Log.i("url", url);
        System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap, serverResponseCallback);

    }

    public void doMonitoringDitails(String paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL()+"project/projectMonitoring/get?"+paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);

    }



    public void doDeviationDitails(String paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL()+"project/projectDeviation/get?"+paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);

    }



    public void doPanDitails(String paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL()+"project/projectPan/get?"+paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);

    }

    public void doclouserDitails(String paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL()+"project/projectCloser/get?"+paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);

    }

    public void mouDitails(String paramsMap, boolean isToHideDialog, ServerResponseCallback serverResponseCallback) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL()+"project/projectMou/get?"+paramsMap;
        int method = Method.GET;
        Log.i("url", url);
        makeJsonObjReq(method, url, new HashMap<String, String>(), serverResponseCallback);

    }

    /****
     * Making String Req method
     */
/*
    private void makeStringReq(int method, String url, final Map<String, String> paramsMap final ServerResponseCallback serverResponseCallback) {
        showProgressDialog();
        Log.v("JSONObject", new JSONObject(paramsMap).toString());
        StringRequest jsonObjReq = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String string = response.replace("\\u000d", "");
                string = string.replace("\\u000a", "");
                string = string.replace("\\", "");
                string = string.replace(" ", "");
                string = string.substring(1, string.length() - 1);
                if (isToHideDialog) {
                    hideProgressDialog();
                }
                if (serverResponseCallback != null)
                    serverResponseCallback.onSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.d("error ocurred", "TimeoutError");
                } else if (error instanceof AuthFailureError) {
                    Log.d("error ocurred", "AuthFailureError");
                    Toast.makeText(mContext, mContext.getString(R.string.auth_failure), Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Log.d("error ocurred", "ServerError");
                    Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Log.d("error ocurred", "NetworkError");
                    Toast.makeText(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Log.d("error ocurred", "ParseError");
                    Toast.makeText(mContext, mContext.getString(R.string.parse_error), Toast.LENGTH_LONG).show();
                }
                error.printStackTrace();
                ((ServerResponseCallback) mContext).onError();
            }
        }) {
            *//**
             * Passing some request headers
             * *//*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Ocp-Apim-Subscription-Key", "");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return paramsMap;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }*/


}
