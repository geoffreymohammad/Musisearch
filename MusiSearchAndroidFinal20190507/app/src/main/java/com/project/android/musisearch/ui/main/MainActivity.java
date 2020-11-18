package com.project.android.musisearch.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.android.musisearch.BaseActivity;
import com.project.android.musisearch.R;
import com.project.android.musisearch.data.StaticConfig;
import com.project.android.musisearch.ui.login.LoginActivity;
import com.project.android.musisearch.ui.main.ChatMain.MainChatActivity;
import com.project.android.musisearch.ui.main.chat.FragmentChat;
import com.project.android.musisearch.ui.main.groupBand.FragmentBand;
import com.project.android.musisearch.ui.main.profile.FragmentProfile;
import com.project.android.musisearch.ui.main.search.FragmentSearch;
import com.project.android.musisearch.ui.main.status.FragmentStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.project.android.musisearch.utils.AppConstant.END_POINT;
import static com.project.android.musisearch.utils.AppConstant.WS_ADDRESS;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static String TAG = "MainActivity";
    private Toolbar toolbarMain;
    private TextView textLogo;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBaseLoadingProgress(this);
        toolbarMain = findViewById(R.id.toolbarMain);
        textLogo = findViewById(R.id.textLogo);
        setSupportActionBar(toolbarMain);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        updateToolbar("Musisearch", true);
        //loading the default fragment
        loadFragment(new FragmentStatus());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        initFirebase();
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onResponseSuccessString(String response) {
        Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show();
        if (progressDialog != null) {
            statusBaseLoading(false, true);
        }
    }

    private void initFirebase() {
        //Khoi tao thanh phan de dang nhap, dang ky
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    StaticConfig.UID = user.getUid();
                } else {
                    MainActivity.this.finish();
                    // User is signed in
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onResponseErrorString() {
    }

    public void updateToolbar(String title, boolean isAction){
        Toolbar.LayoutParams params = (Toolbar.LayoutParams) textLogo.getLayoutParams();
        if (isAction){
            params.setMarginStart(getActionBarSize());
        }else {
            params.setMarginStart(0);
        }
        textLogo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textLogo.setLayoutParams(params);
        textLogo.setText(title);
    }

    public int getActionBarSize() {
        TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.action_home:
                fragment = new FragmentStatus();
                break;
            /*case R.id.action_band:
                fragment = new FragmentBand();
                break;*/
            case R.id.action_search:
                fragment = new FragmentSearch();
                break;
            case R.id.action_chat:
                Intent i =  new Intent(this, MainChatActivity.class);
                startActivity(i);
                break;
            case R.id.action_profile:
                fragment = new FragmentProfile();
                break;
        }

        return loadFragment(fragment);
    }
}
