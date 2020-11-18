package com.project.android.musisearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.android.musisearch.utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static com.project.android.musisearch.utils.AppConstant.END_POINT;
import static com.project.android.musisearch.utils.AppConstant.WS_ADDRESS;


public abstract class BaseActivity extends AppCompatActivity {
    public ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private TextView textViewPercent, textViewCount;
    private int totalCount = 0;

    public void setBaseLoadingProgress(Context context) {
        progressDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading...");
    }

    public void setBaseLoadingProgressPercentage(Context context, ProgressBar progressBar, TextView textViewPercent, TextView textViewCount) {
        this.progressBar = progressBar;
        this.textViewPercent = textViewPercent;
        this.textViewCount = textViewCount;
    }

    public void statusBaseLoading(boolean show, boolean statusAPI) {

        if ((show) && (statusAPI)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else if ((!show) && (statusAPI)) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    public void sendBaseRequestArrayGet(final Context context, final String Url) {

        if (progressDialog != null) {
            statusBaseLoading(true, true);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WS_ADDRESS + END_POINT + Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseSuccessString(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog != null) {
                            statusBaseLoading(false, true);
                        }
                        String _toastMessage;
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            int _statusCode = error.networkResponse.statusCode;
                            switch (_statusCode) {
                                case HttpURLConnection.HTTP_NOT_FOUND:
                                    _toastMessage = "404 : Data tidak ditemukan";
                                    break;
                                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                                    _toastMessage = "500 : Kegagalan server, error input dsb";
                                    break;
                                case HttpURLConnection.HTTP_UNAUTHORIZED:
                                    _toastMessage = "401 : Authentifikasi gagal";
                                    break;
                                default:
                                    _toastMessage = _statusCode + " : Gagal Memuat Data";
                                    break;
                            }
                        } else {
                            _toastMessage = "200 : Gagal Memuat Data";
                        }
                        Toast.makeText(context, _toastMessage, Toast.LENGTH_LONG).show();
                        onResponseErrorString();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("User-Agent", "Android-Musisearch");
                params.put("User-Api-Id", "Android-X");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                //int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void sendBaseRequestArrayGetWhitoutLoading(final Context context, final String Url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, WS_ADDRESS + END_POINT + Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseSuccessString(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String _toastMessage;
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            int _statusCode = error.networkResponse.statusCode;
                            switch (_statusCode) {
                                case HttpURLConnection.HTTP_NOT_FOUND:
                                    _toastMessage = "404 : Data tidak ditemukan";
                                    break;
                                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                                    _toastMessage = "500 : Kegagalan server, error input dsb";
                                    break;
                                case HttpURLConnection.HTTP_UNAUTHORIZED:
                                    _toastMessage = "401 : Authentifikasi gagal";
                                    break;
                                default:
                                    _toastMessage = _statusCode + " : Gagal Memuat Data";
                                    break;
                            }
                        } else {
                            _toastMessage = "200 : Gagal Memuat Data";
                        }
                        Toast.makeText(context, _toastMessage, Toast.LENGTH_LONG).show();
                        onResponseErrorString();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("User-Agent", "Android-Musisearch");
                params.put("User-Api-Id", "Android-X");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void sendRequestArrayBasePost(final Context context, String Url, final JSONObject jsonObject) {
        if (progressDialog != null) {
            statusBaseLoading(true, true);
        }


        final String mRequestBody = jsonObject.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WS_ADDRESS + END_POINT + Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseSuccessString(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog != null) {
                            statusBaseLoading(false, true);
                        }
                        String _toastMessage = "";
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            int _statusCode = error.networkResponse.statusCode;
                            switch (_statusCode) {
                                case HttpURLConnection.HTTP_NOT_FOUND:
                                    _toastMessage = "404 : Data tidak ditemukan";
                                    break;
                                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                                    _toastMessage = "500 : Kegagalan server, error input dsb";
                                    break;
                                case HttpURLConnection.HTTP_UNAUTHORIZED:
                                    _toastMessage = "401 : Authentifikasi gagal";
                                    break;
                                default:
                                    _toastMessage = _statusCode + " : Gagal Memuat Data";
                                    break;
                            }
                        } else {
                            _toastMessage = "200 : Gagal Memuat Data";
                        }

                        Toast.makeText(context, _toastMessage, Toast.LENGTH_LONG).show();
                        onResponseErrorString();
                    }
                })
        {
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                for (Map.Entry<String, String> e : paramPost.entrySet()) {
                    params.put(e.getKey(), e.getValue());
                }
                return params;
            }*/


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("User-Agent", "Android-Musisearch");
                params.put("User-Api-Id", "Android-X");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(this != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        /*RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);*/
    }

    private String readBody(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);

        StringBuilder sb = new StringBuilder();
        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
            sb.append(nextLine);
        }

        return sb.toString();
    }

    public abstract void onResponseSuccessString(String response);

    public abstract void onResponseErrorString();

}
