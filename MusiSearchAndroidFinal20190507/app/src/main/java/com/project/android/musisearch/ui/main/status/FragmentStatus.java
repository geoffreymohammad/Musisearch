package com.project.android.musisearch.ui.main.status;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
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
import com.project.android.musisearch.adapter.AdapterStatus;
import com.project.android.musisearch.model.Status;
import com.project.android.musisearch.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentStatus extends BaseFragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<Status> singleItem;
    private AdapterStatus adapterStatus;
    protected Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_status, container, false);
        setBaseLoadingProgress(getActivity());

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setEnabled(false);
        recyclerView = view.findViewById(R.id.recyclerview);
        singleItem = new ArrayList<>();
        adapterStatus = new AdapterStatus(getActivity(), singleItem);
        handler = new Handler();
        initVerticalRecycler();
        ((MainActivity) getActivity()).updateToolbar("Musisearch", true);
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
        adapterStatus = new AdapterStatus(getActivity(), singleItem);
        handler = new Handler();
        initVerticalRecycler();
        sendBaseRequestArrayGet(getActivity(), "/status/rest/all");
    }

    private void loadRefreshSwipe() {
        singleItem = new ArrayList<>();
        adapterStatus = new AdapterStatus(getActivity(), singleItem);
        handler = new Handler();

    }

    private void initVerticalRecycler() {
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapterStatus);
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
                Intent i = new Intent(getActivity(), AddStatusActivity.class);
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
                }else {
                    JSONArray lstObject = jsonObj.getJSONArray("lstObject");
                    for (int i = 0; i < lstObject.length(); i++) {
                        JSONObject c = lstObject.getJSONObject(i);
                        final int id = c.getInt("id");
                        final int musisi = c.getInt("musisi");
                        final String musisiName = c.getString("musisiName");
                        final String email = c.getString("email");
                        final String telp = c.getString("telp");
                        final String tglLahir = c.getString("tglLahir");
                        final String initialStatusName = c.getString("initialStatusName");
                        final int initialStatus = c.getInt("initialStatus");
                        final String nama = c.getString("nama");
                        final int createdBy = c.getInt("createdBy");
                        final String createdDate = c.getString("createdDate");
                        final String createdByName = c.getString("createdByName");
                        final int modifiedBy = c.getInt("modifiedBy");
                        final String modifiedDate = c.getString("modifiedDate");
                        final String modifiedByName = c.getString("modifiedByName");
                        final String modelName = c.getString("modelName");
                        singleItem.add(new Status(id, musisi, initialStatus, musisiName, email, telp, tglLahir, initialStatusName, nama, createdDate, createdByName, modifiedDate, modifiedByName, modelName));
                    }
                    adapterStatus.notifyDataSetChanged();
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
