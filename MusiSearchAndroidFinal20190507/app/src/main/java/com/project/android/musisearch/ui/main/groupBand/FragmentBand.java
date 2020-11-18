package com.project.android.musisearch.ui.main.groupBand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.project.android.musisearch.BaseFragment;
import com.project.android.musisearch.R;
import com.project.android.musisearch.adapter.AdapterBand;
import com.project.android.musisearch.model.GroupBand;
import com.project.android.musisearch.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_GOOGLE;

public class FragmentBand extends BaseFragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<GroupBand> singleItem;
    private AdapterBand adapterBand;
    protected Handler handler;
    private SharedPreferences sharedPreferencesGoogle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_band, container, false);
        setBaseLoadingProgress(getActivity());
        sharedPreferencesGoogle = getActivity().getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setEnabled(false);
        recyclerView = view.findViewById(R.id.recyclerview);
        singleItem = new ArrayList<>();
        adapterBand = new AdapterBand(getActivity(), singleItem);
        handler = new Handler();
        initVerticalRecycler();
        ((MainActivity) getActivity()).updateToolbar("My Band", true);
        //loadStatus();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    void loadStatus() {
        singleItem = new ArrayList<>();
        adapterBand = new AdapterBand(getActivity(), singleItem);
        handler = new Handler();
        initVerticalRecycler();
        String params = "inMusisi=" + sharedPreferencesGoogle.getInt("id", 0);
        sendBaseRequestArrayGet(getActivity(), "/groupband/rest/id?" + params);
    }

    private void loadRefreshSwipe() {
        singleItem = new ArrayList<>();
        adapterBand = new AdapterBand(getActivity(), singleItem);
        handler = new Handler();

    }

    private void initVerticalRecycler() {
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapterBand);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.actionAdd:
                Intent i = new Intent(getActivity(), AddBandActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
                        final String nama = c.getString("nama");
                        final int createdBy = c.getInt("createdBy");
                        final String createdDate = c.getString("createdDate");
                        final String createdByName = c.getString("createdByName");
                        final int modifiedBy = c.getInt("modifiedBy");
                        final String modifiedDate = c.getString("modifiedDate");
                        final String modifiedByName = c.getString("modifiedByName");
                        final String modelName = c.getString("modelName");
                        singleItem.add(new GroupBand(id, musisi, nama, createdDate, createdByName, modifiedDate, modifiedByName, modelName));
                    }
                    adapterBand.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
        loadStatus();
    }
}
