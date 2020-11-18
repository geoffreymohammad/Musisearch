package com.project.android.musisearch.ui.main.groupBand;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

public class AddBandActivity extends BaseActivity {
    private SharedPreferences sharedPreferencesGoogle;
    Button btnSave;
    TextInputLayout textInputNama;
    private EditText editNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_band);
        setBaseLoadingProgress(this);
        sharedPreferencesGoogle = getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);
        editNama = findViewById(R.id.editNama);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                    save();
            }
        });
    }

    private void save() {
        sendRequestArrayBasePost(getApplicationContext(), "/groupband/rest/insert", getParamsBody());
    }

    private JSONObject getParamsBody() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("musisi", sharedPreferencesGoogle.getInt("id", 0));
            jsonBody.put("nama", editNama.getText().toString());
            jsonBody.put("createdBy", sharedPreferencesGoogle.getInt("id", 0));
            jsonBody.put("modifiedBy", sharedPreferencesGoogle.getInt("id", 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonBody;
    }

    private boolean validate() {
        textInputNama = findViewById(R.id.textInputNama);

        boolean valid = true;
        String nama = editNama.getText().toString();
        if (nama.isEmpty()) {
            textInputNama.setErrorEnabled(true);
            textInputNama.setError("Please fill out this field");
            valid = false;
        } else {
            textInputNama.setError(null);
            textInputNama.setErrorEnabled(false);
        }

        return valid;
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
                } else {
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
}
