package com.project.android.musisearch.ui.main.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.project.android.musisearch.BaseFragment;
import com.project.android.musisearch.R;
import com.project.android.musisearch.data.StaticConfig;
import com.project.android.musisearch.model.chat.Friend;
import com.project.android.musisearch.ui.main.MainActivity;
import com.project.android.musisearch.ui.main.search.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.project.android.musisearch.utils.AppConstant.END_POINT;
import static com.project.android.musisearch.utils.AppConstant.WS_ADDRESS;

public class FragmentChat extends BaseFragment {


    public FragmentChat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        ((MainActivity) getActivity()).updateToolbar("Chat", false);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void checkBeforAddFriend(final String idFriend, Friend userInfo) {
        addFriend(idFriend, true);
    }

    private void addFriend(final String idFriend, boolean isIdFriend) {
        if (idFriend != null) {
            if (isIdFriend) {
                FirebaseDatabase.getInstance().getReference().child("friend/" + StaticConfig.UID).push().setValue(idFriend)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    addFriend(idFriend, false);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            } else {
                FirebaseDatabase.getInstance().getReference().child("friend/" + idFriend).push().setValue(StaticConfig.UID).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addFriend(null, false);
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        } else {

        }
    }

    @Override
    public void onResponseSuccessString(String response) {

    }

    @Override
    public void onResponseErrorString() {

    }



}
