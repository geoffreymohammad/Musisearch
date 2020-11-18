package com.project.android.musisearch.ui.main.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.android.musisearch.BaseActivity;
import com.project.android.musisearch.R;
import com.project.android.musisearch.adapter.AdapterMusisi;
import com.project.android.musisearch.adapter.AdapterStatus;
import com.project.android.musisearch.data.FriendDB;
import com.project.android.musisearch.data.StaticConfig;
import com.project.android.musisearch.model.Musisi;
import com.project.android.musisearch.model.Status;
import com.project.android.musisearch.model.chat.Friend;
import com.project.android.musisearch.model.chat.ListFriend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_GOOGLE;

public class SearchActivity extends BaseActivity {
    private SharedPreferences sharedPreferencesGoogle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<Musisi> singleItem;
    private AdapterMusisi adapterMusisi;
    protected Handler handler;
    TextView textEmpty;
    boolean isLikes = true;
    private ListFriend dataListFriend = null;
    private ArrayList<String> listFriendID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sharedPreferencesGoogle = getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setEnabled(false);
        textEmpty = findViewById(R.id.textEmpty);
        recyclerView = findViewById(R.id.recyclerview);
        singleItem = new ArrayList<>();
        adapterMusisi = new AdapterMusisi(this, singleItem);
        handler = new Handler();
        initVerticalRecycler();

        if (dataListFriend == null) {
            dataListFriend = FriendDB.getInstance(getApplicationContext()).getListFriend();
            if (dataListFriend.getListFriend().size() > 0) {
                listFriendID = new ArrayList<>();
                for (Friend friend : dataListFriend.getListFriend()) {
                    listFriendID.add(friend.id);
                }
            }
        }
        if (listFriendID == null) {
            listFriendID = new ArrayList<>();

            getListFriendUId();
        }
    }



    private void initVerticalRecycler() {
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapterMusisi);
    }

    void loadMusisi() {
        isLikes = false;
        singleItem = new ArrayList<>();
        adapterMusisi = new AdapterMusisi(this, singleItem);
        handler = new Handler();
        initVerticalRecycler();
        String _genre = "";
        String _region = "";
        if (getIntent().getIntExtra("idGenre", 0) > 0) {
            _genre = "inGenre=" + getIntent().getIntExtra("idGenre", 0);
        }
        if (getIntent().getIntExtra("region", 0) > 0) {
            _region = "&inRegion=" + getIntent().getIntExtra("region", 0);
        }
        //String params = "inGenre="+getIntent().getIntExtra("idGenre",0)+"&inInstrument1="+getIntent().getIntExtra("idInstrument1",0)+"&inRegion="+getIntent().getIntExtra("region",0);
        String params = _genre + "&inInstrument1=" + getIntent().getIntExtra("idInstrument1", 0) + _region + "&likeBy=" + sharedPreferencesGoogle.getInt("id", 0);
        sendBaseRequestArrayGet(this, "/musisi/rest/all?" + params);
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

    public void findIDEmail(String email) {
        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    //email not found

                } else {
                    String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                    if (id.equals(StaticConfig.UID)) {

                    } else {
                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                        Friend user = new Friend();
                        user.name = (String) userMap.get("name");
                        user.email = (String) userMap.get("email");
                        user.avata = (String) userMap.get("avata");
                        user.id = id;
                        user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
                        checkBeforAddFriend(id, user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkBeforAddFriend(final String idFriend, Friend userInfo) {
        if (listFriendID.contains(idFriend)) {

        }else {
            addFriend(idFriend, true);
            listFriendID.add(idFriend);
            dataListFriend.getListFriend().add(userInfo);
            FriendDB.getInstance(getApplicationContext()).addFriend(userInfo);
        }
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
                                    onBackPressed();
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
                            onBackPressed();
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

    private void getListFriendUId() {
        FirebaseDatabase.getInstance().getReference().child("friend/" + StaticConfig.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    HashMap mapRecord = (HashMap) dataSnapshot.getValue();
                    Iterator listKey = mapRecord.keySet().iterator();
                    while (listKey.hasNext()) {
                        String key = listKey.next().toString();
                        listFriendID.add(mapRecord.get(key).toString());
                    }
                    getAllFriendInfo(0);
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getAllFriendInfo(final int index) {
        if (index == listFriendID.size()) {
            //save list friend

        } else {
            final String id = listFriendID.get(index);
            FirebaseDatabase.getInstance().getReference().child("user/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Friend user = new Friend();
                        HashMap mapUserInfo = (HashMap) dataSnapshot.getValue();
                        user.name = (String) mapUserInfo.get("name");
                        user.email = (String) mapUserInfo.get("email");
                        user.avata = (String) mapUserInfo.get("avata");
                        user.id = id;
                        user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
                        dataListFriend.getListFriend().add(user);
                        FriendDB.getInstance(getApplicationContext()).addFriend(user);
                    }
                    getAllFriendInfo(index + 1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void insertLikes(final int idMusisi) {
        isLikes = true;
        sendRequestArrayBasePost(getApplicationContext(), "/musisi/rest/likes?idLike="+sharedPreferencesGoogle.getInt("id", 0)+"&idMusisi="+idMusisi, getParamsBody());
    }

    private JSONObject getParamsBody() {
        JSONObject jsonBody = new JSONObject();
        return jsonBody;
    }

    @Override
    public void onResponseSuccessString(String response) {
        if ((response != null) && (!response.equals("null")) && (!response.equals("[]"))) {
            if (!isLikes) {
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
                            final String nama = c.getString("nama");
                            final String email = c.getString("email");
                            final String telp = c.getString("telp");
                            final String biography = c.getString("biography");
                            final String tglLahir = c.getString("tglLahir");
                            final int gender = c.getInt("gender");
                            final String genderName = c.getString("genderName");
                            final int genre = c.getInt("genre");
                            final String genreName = c.getString("genreName");
                            final int region = c.getInt("region");
                            final String regionName = c.getString("regionName");
                            final int instrument1 = c.getInt("instrument1");
                            final String instrument1Name = c.getString("instrument1Name");
                            final int instrument2 = c.getInt("instrument2");
                            final String instrument2Name = c.getString("instrument2Name");
                            final int instrument3 = c.getInt("instrument3");
                            final String instrument3Name = c.getString("instrument3Name");
                            final int createdBy = c.getInt("createdBy");
                            final String createdDate = c.getString("createdDate");
                            final String createdByName = c.getString("createdByName");
                            final int modifiedBy = c.getInt("modifiedBy");
                            final String modifiedDate = c.getString("modifiedDate");
                            final String modifiedByName = c.getString("modifiedByName");
                            final int total = c.getInt("total");
                            final int isLikes = c.getInt("isLikes");
                            singleItem.add(new Musisi(id, gender, genre, region, instrument1, instrument2, instrument3, nama, email, telp, biography, tglLahir,
                                    genderName, genreName, regionName, instrument1Name, instrument2Name, instrument3Name, createdDate, createdByName,
                                    modifiedDate, modifiedByName, total, isLikes));
                        }
                        adapterMusisi.notifyDataSetChanged();
                        if (singleItem.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            textEmpty.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            textEmpty.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getString("errorResult").equals("false")) {
                        loadMusisi();
                    } else {
                        Toast.makeText(this, "Error while save data", Toast.LENGTH_SHORT).show();
                    }
                    if (progressDialog != null) {
                        statusBaseLoading(false, true);
                    }
                } catch (JSONException e) {
                    if (progressDialog != null) {
                        statusBaseLoading(false, true);
                    }
                    Toast.makeText(this, "Error while loading data", Toast.LENGTH_SHORT).show();
                }
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
        loadMusisi();

    }
}
