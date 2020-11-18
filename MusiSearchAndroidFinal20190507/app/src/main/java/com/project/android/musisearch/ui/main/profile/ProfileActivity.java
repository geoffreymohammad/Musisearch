package com.project.android.musisearch.ui.main.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.project.android.musisearch.BaseActivity;
import com.project.android.musisearch.R;
import com.project.android.musisearch.ui.main.MainActivity;
import com.project.android.musisearch.utils.Sessions;

import org.json.JSONException;
import org.json.JSONObject;

import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_STEP2;

public class ProfileActivity extends BaseActivity {
    TextView textName,textGenre, textInstrument, textBio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setBaseLoadingProgress(this);
        textName = findViewById(R.id.textName);
        textGenre = findViewById(R.id.textGenre);
        textInstrument = findViewById(R.id.textInstrument);
        textBio = findViewById(R.id.textBio);
        loadData();
    }

    void loadData(){
        sendBaseRequestArrayGet(this, "/musisi/rest/all");
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
                    int instrument1 = object.getInt("instrument1");
                    String tglLahir = object.getString("tglLahir");
                    String biography = object.getString("biography");
                    textName.setText(nama);
                }
                if (progressDialog != null) {
                    statusBaseLoading(false, true);
                }
            } catch (JSONException e) {
                if (progressDialog != null) {
                    statusBaseLoading(false, true);
                }
            }
        }
    }

    @Override
    public void onResponseErrorString() {

    }
}
