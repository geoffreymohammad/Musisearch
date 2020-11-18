package com.project.android.musisearch.ui.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
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
import com.project.android.musisearch.ui.login.LoginActivity;
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

public class Step2Activity extends BaseActivity {
    private Spinner spinnerGenre, spinnerInstrument1, spinnerInstrument2, spinnerInstrument3;
    ArrayList<String> genreName, instrumentName;
    ArrayList<Integer> genreId, instrumentId;
    int idGenre, idInstrument1, idInstrument2, idInstrument3;
    Button btnSave;
    int region = 2;
    private SharedPreferences sharedPreferencesGoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);
        setBaseLoadingProgress(this);
        sharedPreferencesGoogle = getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        spinnerGenre = findViewById(R.id.spinnerGenre);
        spinnerInstrument1 = findViewById(R.id.spinnerInstrument1);
        spinnerInstrument2 = findViewById(R.id.spinnerInstrument2);
        spinnerInstrument3 = findViewById(R.id.spinnerInstrument3);
        loadGenre();
    }

    private void save() {
        sendRequestArrayBasePost(getApplicationContext(), "/musisi/rest/insert", getParamsBody());
    }

    private JSONObject getParamsBody() {
        JSONObject jsonBody = new JSONObject();
        try {
            /*jsonBody.put("email", "Shozib@gmail.com");
            jsonBody.put("gender", "1");
            jsonBody.put("genre", "1");
            jsonBody.put("instrument1", "1");
            jsonBody.put("instrument2", "1");
            jsonBody.put("instrument3", "1");
            jsonBody.put("nama", "ffff");
            jsonBody.put("region", "1");
            jsonBody.put("telp", "1");
            jsonBody.put("tglLahir", "2019-01-27");*/

            if (getIntent().getExtras() != null) {
                jsonBody.put("email", getIntent().getStringExtra("email"));
                jsonBody.put("gender", getIntent().getIntExtra("gender", 0));
                jsonBody.put("genre", idGenre);
                jsonBody.put("instrument1", idInstrument1);
                jsonBody.put("instrument2", idInstrument2);
                jsonBody.put("instrument3", idInstrument3);
                jsonBody.put("nama", getIntent().getStringExtra("fullname"));
                jsonBody.put("region", region);
                jsonBody.put("telp", getIntent().getStringExtra("phoneNumber"));
                jsonBody.put("tglLahir", getIntent().getStringExtra("tglLahir"));
            }else {
                jsonBody.put("email", sharedPreferencesGoogle.getString("email", null));
                jsonBody.put("gender", 1);
                jsonBody.put("genre", idGenre);
                jsonBody.put("instrument1", idInstrument1);
                jsonBody.put("instrument2", idInstrument2);
                jsonBody.put("instrument3", idInstrument3);
                jsonBody.put("nama", sharedPreferencesGoogle.getString("fullname", null));
                jsonBody.put("region", region);
                jsonBody.put("telp", sharedPreferencesGoogle.getString("phoneNumber", null));
                jsonBody.put("tglLahir", null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonBody;
    }

    private void initSpinnerGenre() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item, genreName);
        spinnerGenre.setAdapter(adapter);
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idGenre = genreId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSpinnerInstrument() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item, instrumentName);
        spinnerInstrument1.setAdapter(adapter);
        spinnerInstrument1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idInstrument1 = instrumentId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerInstrument2.setAdapter(adapter);
        spinnerInstrument2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idInstrument2 = instrumentId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerInstrument3.setAdapter(adapter);
        spinnerInstrument3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idInstrument3 = instrumentId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void loadGenre() {
        sendBaseRequestArrayGetGenre(getApplicationContext(), "/genre/rest/all");
        sendBaseRequestArrayGetInstrument(getApplicationContext(), "/instrument/rest/all");
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_jakbar:
                if (checked)
                    region = 2;
                break;
            case R.id.radio_jaktim:
                if (checked)
                    region = 3;
                break;
            case R.id.radio_jakpus:
                if (checked)
                    region = 5;
                break;
            case R.id.radio_jakut:
                if (checked)
                    region = 4;
                break;
            case R.id.radio_jaksel:
                if (checked)
                    region = 1;
                break;
        }
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
                    JSONObject object  = jsonObj.getJSONObject("object");
                    int id  = object.getInt("id");
                    String nama = object.getString("nama");
                    String email = object.getString("email");
                    String imagePath = object.getString("imagePath");
                    String telp = object.getString("telp");
                    String tglLahir = object.getString("tglLahir");
                    Sessions.setupSessionLoginGoogle(this, id, nama, email, telp, tglLahir, imagePath);
                    SharedPreferences getPrefsSplash = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor e = getPrefsSplash.edit();
                    e.putBoolean(PREFS_LOGIN_STEP2, true);
                    e.apply();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
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

    //region API
    public void onResponseSuccessGenre(String response) {
        if ((response != null) && (!response.equals("null")) && (!response.equals("[]"))) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                genreName = new ArrayList<>();
                genreId = new ArrayList<>();
                if (jsonObj.getString("errorResult").equals("false")) {
                    JSONArray lstObject = jsonObj.getJSONArray("lstObject");
                    for (int i = 0; i < lstObject.length(); i++) {
                        final JSONObject c = lstObject.getJSONObject(i);
                        int id = c.getInt("id");
                        String nama = c.getString("nama");
                        genreName.add(nama);
                        genreId.add(id);
                    }
                }
                initSpinnerGenre();
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

    public void onResponseSuccessInstrument(String response) {
        if ((response != null) && (!response.equals("null")) && (!response.equals("[]"))) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                instrumentName = new ArrayList<>();
                instrumentId = new ArrayList<>();
                if (jsonObj.getString("errorResult").equals("false")) {
                    JSONArray lstObject = jsonObj.getJSONArray("lstObject");
                    for (int i = 0; i < lstObject.length(); i++) {
                        final JSONObject c = lstObject.getJSONObject(i);
                        int id = c.getInt("id");
                        String nama = c.getString("nama");
                        instrumentName.add(nama);
                        instrumentId.add(id);
                    }
                }
                initSpinnerInstrument();
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

    void sendBaseRequestArrayGetGenre(final Context context, final String Url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, WS_ADDRESS + END_POINT + Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseSuccessGenre(response);
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

    void sendBaseRequestArrayGetInstrument(final Context context, final String Url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, WS_ADDRESS + END_POINT + Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseSuccessInstrument(response);
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
    //endregion
}
