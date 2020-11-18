package com.project.android.musisearch.ui.login;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.android.musisearch.BaseActivity;
import com.project.android.musisearch.R;
import com.project.android.musisearch.data.StaticConfig;
import com.project.android.musisearch.model.chat.User;
import com.project.android.musisearch.utils.Sessions;
import com.project.android.musisearch.ui.register.Step1Activity;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_GOOGLE;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnLoginGoogle;
    private Boolean isExistEmail = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pDialog = new ProgressDialog(this);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    signIn();
                }
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("13462922582-qjfivmd1jdrom48ts3gn9h702ikracu3.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            // signOut();
            Sessions.setupSessionLoginGoogle(this, 0, currentUser.getDisplayName(), currentUser.getEmail(), currentUser.getPhoneNumber(),null, null);
        } else {
            // No user is signed in
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String _user = user.getDisplayName();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new com.google.android.gms.tasks.OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateUI(null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signOutWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Sign Out Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }

                });
    }

    private void displayProgressDialog() {
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideProgressDialog() {
        pDialog.dismiss();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        displayProgressDialog();
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            StaticConfig.UID = user.getUid();
                            if (isExist(StaticConfig.UID)){
                                saveUserInfo(StaticConfig.UID);
                            }else {
                                initNewUserInfo(user);
                            }
                            //saveUserInfo();
                            SharedPreferences getPrefsSplash = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor e = getPrefsSplash.edit();
                            e.putBoolean(PREFS_LOGIN_GOOGLE, true);
                            e.apply();
                            Sessions.setupSessionLoginGoogle(LoginActivity.this, 0, user.getDisplayName(), user.getEmail(), user.getPhoneNumber(), null, null   );
                            Intent i = new Intent(LoginActivity.this, Step1Activity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login Failed: ", Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }

                });
    }
    boolean isExist(String uid){
        isExistEmail = false;
        FirebaseDatabase.getInstance().getReference().child("user/" + uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    isExistEmail = true;
                } else {
                    isExistEmail = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        return isExistEmail;
    }

    void saveUserInfo(String uid) {
        FirebaseDatabase.getInstance().getReference().child("user/" + uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap hashUser = (HashMap) dataSnapshot.getValue();
                User userInfo = new User();
                userInfo.name = (String) hashUser.get("name");
                userInfo.email = (String) hashUser.get("email");
                userInfo.avata = (String) hashUser.get("avata");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initNewUserInfo(FirebaseUser user) {
        User newUser = new User();
        newUser.email = user.getEmail();
        newUser.name = user.getEmail().substring(0, user.getEmail().indexOf("@"));
        newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
        FirebaseDatabase.getInstance().getReference().child("user/" + user.getUid()).setValue(newUser);
    }

    //region Permission
    private static final int PERMISSION_REQUEST_CODE = 200;
    private final Map<Integer, OnCompleteListener> permissionsListeners = new HashMap<>();

    private interface OnCompleteListener {
        void onComplete();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{INTERNET, READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE, ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE,
                CAMERA, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @TargetApi(23)
    @SuppressWarnings("unused")
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final @NonNull String permissions[],
                                           final @NonNull int[] grantResults) {
        final OnCompleteListener permissionsListener = permissionsListeners.remove(requestCode);
        boolean allgranted = false;
        if (allgranted) {
            permissionsListener.onComplete();
        } else {
            switch (requestCode) {
                case PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0) {

                        boolean result = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean result1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean result2 = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean result3 = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                        boolean result4 = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                        boolean result5 = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                        boolean result6 = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                        boolean result7 = grantResults[7] == PackageManager.PERMISSION_GRANTED;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(INTERNET) ||
                                    shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) ||
                                    shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) ||
                                    shouldShowRequestPermissionRationale(ACCESS_WIFI_STATE) ||
                                    shouldShowRequestPermissionRationale(ACCESS_NETWORK_STATE) ||
                                    shouldShowRequestPermissionRationale(CAMERA) ||
                                    shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) ||
                                    shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)
                            ) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{INTERNET, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE,
                                                                    ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE,
                                                                    CAMERA, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });

                                //return;
                            }
                        }

                        if (checkPermission()) {
                            //authUtils.signIn(editEmail.getText().toString(), editPassword.getText().toString());
                            signIn();
                        } else {
                        }

                    }


                    break;
            }
        }

    }

    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_WIFI_STATE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_NETWORK_STATE);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result6 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result7 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED &&
                result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED
                && result6 == PackageManager.PERMISSION_GRANTED && result7 == PackageManager.PERMISSION_GRANTED;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    void snackBar(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }
    //endregion

    @Override
    public void onResponseSuccessString(String response) {

    }

    @Override
    public void onResponseErrorString() {

    }
}
