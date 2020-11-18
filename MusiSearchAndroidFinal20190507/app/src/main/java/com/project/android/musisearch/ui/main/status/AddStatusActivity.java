package com.project.android.musisearch.ui.main.status;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.android.musisearch.BaseActivity;
import com.project.android.musisearch.R;
import com.project.android.musisearch.ui.main.MainActivity;
import com.project.android.musisearch.utils.Sessions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.project.android.musisearch.utils.AppConstant.END_POINT;
import static com.project.android.musisearch.utils.AppConstant.WS_ADDRESS;
import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_GOOGLE;
import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_STEP2;

public class AddStatusActivity extends BaseActivity {
    private SharedPreferences sharedPreferencesGoogle;
    Button btnSave;
    private Spinner spinnerInitial;
    private EditText editWrite;
    int idInitial;
    ArrayList<String> initialName;
    ArrayList<Integer> initialId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_status);
        setBaseLoadingProgress(this);
        sharedPreferencesGoogle = getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);
        editWrite = findViewById(R.id.editWrite);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        spinnerInitial = findViewById(R.id.spinnerInitial);
        loadInitial();
    }

    private void save() {
        sendRequestArrayBasePost(getApplicationContext(), "/status/rest/insert", getParamsBody());
    }

    void loadInitial() {
        sendBaseRequestArrayGetInitial(getApplicationContext(), "/initialStatus/rest/all");
    }

    private void initSpinnerStatus() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item, initialName);
        spinnerInitial.setAdapter(adapter);
        spinnerInitial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idInitial = initialId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private JSONObject getParamsBody() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("initialStatus", idInitial);
            jsonBody.put("musisi", sharedPreferencesGoogle.getInt("id", 0));
            jsonBody.put("nama", editWrite.getText().toString());
            jsonBody.put("createdBy", sharedPreferencesGoogle.getInt("id", 0));
            jsonBody.put("modifiedBy", sharedPreferencesGoogle.getInt("id", 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonBody;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void snackBar(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    public void onResponseSuccessString(String response) {
        if ((response != null) && (!response.equals("null")) && (!response.equals("[]"))) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                if (jsonObj.getString("errorResult").equals("false")) {
                    onBackPressed();
                }else {
                    snackBar("Error while save data");
                }
                if (progressDialog != null) {
                    statusBaseLoading(false, true);
                }
            } catch (JSONException e) {
                if (progressDialog != null) {
                    statusBaseLoading(false, true);
                }
                snackBar("Error while loading data");
            }
        }
    }

    @Override
    public void onResponseErrorString() {

    }

    public void onResponseSuccessInitial(String response) {
        if ((response != null) && (!response.equals("null")) && (!response.equals("[]"))) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                initialName = new ArrayList<>();
                initialId = new ArrayList<>();
                if (jsonObj.getString("errorResult").equals("false")) {
                    JSONArray lstObject = jsonObj.getJSONArray("lstObject");
                    for (int i = 0; i < lstObject.length(); i++) {
                        final JSONObject c = lstObject.getJSONObject(i);
                        int id = c.getInt("id");
                        String nama = c.getString("nama");
                        initialName.add(nama);
                        initialId.add(id);
                    }
                }
                initSpinnerStatus();
                if (progressDialog != null) {
                    statusBaseLoading(false, true);
                }
            } catch (JSONException e) {
                if (progressDialog != null) {
                    statusBaseLoading(false, true);
                }
                snackBar("Error while loading data");
            }
        }
    }

    void sendBaseRequestArrayGetInitial(final Context context, final String Url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, WS_ADDRESS + END_POINT + Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseSuccessInitial(response);
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


}
