package com.project.android.musisearch.ui.main.groupBand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.project.android.musisearch.BaseActivity;
import com.project.android.musisearch.R;
import com.project.android.musisearch.adapter.AdapterMember;
import com.project.android.musisearch.model.GroupBand;
import com.project.android.musisearch.model.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_GOOGLE;

public class MemberBandActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<Member> singleItem;
    private AdapterMember adapterMember;
    private SharedPreferences sharedPreferencesGoogle;
    protected Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_band);
        setBaseLoadingProgress(this);
        sharedPreferencesGoogle = getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerview);
        singleItem = new ArrayList<>();
        adapterMember = new AdapterMember(this, singleItem);
        initVerticalRecycler();

    }
    void loadMember() {
        singleItem = new ArrayList<>();
        adapterMember = new AdapterMember(this, singleItem);
        handler = new Handler();
        initVerticalRecycler();
        String params = "inId=" + getIntent().getIntExtra("id",0);
        sendBaseRequestArrayGet(this, "/memberband/rest/all?" + params);
    }
    private void initVerticalRecycler() {
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapterMember);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.actionAdd:
                Intent i = new Intent(this, AddBandActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public void onResponseSuccessString(String response) {
        if ((response != null) && (!response.equals("null")) && (!response.equals("[]"))) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                if (jsonObj.getString("errorResult").equals("true")) {
                    if (progressDialog != null) {
                        statusBaseLoading(false, true);
                    }
                } else {
                    JSONArray lstObject = jsonObj.getJSONArray("lstObject");
                    for (int i = 0; i < lstObject.length(); i++) {
                        JSONObject c = lstObject.getJSONObject(i);
                        final int id = c.getInt("id");
                        final int musisi = c.getInt("musisi");
                        final String namaMusisi  = c.getString("namaMusisi");
                        final int createdBy = c.getInt("createdBy");
                        final String namaBand  = c.getString("namaBand");
                        final int groupBand = c.getInt("groupBand");

                        singleItem.add(new Member(id, musisi, groupBand, createdBy, namaMusisi, namaBand));
                    }
                    adapterMember.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (progressDialog != null) {
            statusBaseLoading(false, true);
        }
    }

    @Override
    public void onResponseErrorString() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
    public void onResume() {
        super.onResume();
        loadMember();
    }
}
