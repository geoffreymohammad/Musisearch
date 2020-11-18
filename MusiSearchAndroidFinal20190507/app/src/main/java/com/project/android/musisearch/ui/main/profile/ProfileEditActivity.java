package com.project.android.musisearch.ui.main.profile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.android.musisearch.BaseActivity;
import com.project.android.musisearch.R;
import com.project.android.musisearch.ui.main.MainActivity;
import com.project.android.musisearch.utils.Sessions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.project.android.musisearch.utils.AppConstant.END_POINT;
import static com.project.android.musisearch.utils.AppConstant.WS_ADDRESS;
import static com.project.android.musisearch.utils.AppConstant.WS_ADDRESS_IMAGE;
import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_GOOGLE;
import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_STEP2;

public class ProfileEditActivity extends BaseActivity {
    TextView textName;
    EditText editBio;
    CircularImageView imageProfile;
    private String mImageFileLocation = "";
    private Uri ImageUri;
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    boolean isGetPhoto = false, isImage = false;
    private String _images = null;
    private boolean isCover = false;
    private String[] listItems;
    private Spinner spinnerGenre, spinnerInstrument1, spinnerInstrument2, spinnerInstrument3;
    ArrayList<String> genreName, instrumentName;
    ArrayList<Integer> genreId, instrumentId;
    int idGenre, idInstrument1, idInstrument2, idInstrument3;
    private Button btnSave;
    private SharedPreferences sharedPreferencesGoogle;
    int region;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        setBaseLoadingProgress(this);
        sharedPreferencesGoogle = getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isGetPhoto = false;
                sendRequestArrayBasePost(ProfileEditActivity.this, "/musisi/rest/update", getParamsBody());
            }
        });
        spinnerGenre = findViewById(R.id.spinnerGenre);
        spinnerInstrument1 = findViewById(R.id.spinnerInstrument1);
        spinnerInstrument2 = findViewById(R.id.spinnerInstrument2);
        spinnerInstrument3 = findViewById(R.id.spinnerInstrument3);
        listItems = getResources().getStringArray(R.array.pickimage);
        imageProfile = findViewById(R.id.imageProfile);
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileEditActivity.this);
                mBuilder.setTitle("Choose");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                isCover = true;
                                break;
                            case 1:
                                isCover = true;
                                takePhoto();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        textName = findViewById(R.id.textName);
        textName.setText(getIntent().getStringExtra("name"));
        editBio = findViewById(R.id.editBio);
        editBio.setText(getIntent().getStringExtra("bio"));
        RequestOptions requestOptions = new RequestOptions();
        Glide
                .with(this)
                .load(WS_ADDRESS_IMAGE + sharedPreferencesGoogle.getString("imagePath", null))
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .apply(requestOptions.error(R.drawable.ic_profile))
                .apply(requestOptions.placeholder(R.drawable.ic_profile))
                .into(imageProfile);
        loadGenreInstrument();
        region = getIntent().getIntExtra("region",0);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radio_jakbar:
                        region = 2;
                        break;
                    case R.id.radio_jaktim:
                        region = 3;
                        break;
                    case R.id.radio_jakpus:
                        region = 5;
                        break;
                    case R.id.radio_jakut:
                        region = 4;
                        break;
                    case R.id.radio_jaksel:
                        region = 1;
                        break;
                }
            }
        });
        switch (getIntent().getIntExtra("region",0)){
            case 1:
                radioGroup.check(R.id.radio_jaksel);
                break;
            case 2:
                radioGroup.check(R.id.radio_jakbar);
                break;
            case 3:
                radioGroup.check(R.id.radio_jaktim);
                break;
            case 4:
                radioGroup.check(R.id.radio_jakut);
                break;
            case 5:
                radioGroup.check(R.id.radio_jakpus);
                break;
        }

    }

    void loadStatus() {
        sendBaseRequestArrayGet(this, "/musisi/rest/all?inId=" + sharedPreferencesGoogle.getInt("id", 0));
    }

    void loadGenreInstrument() {
        sendBaseRequestArrayGetGenre(getApplicationContext(), "/genre/rest/all");
        sendBaseRequestArrayGetInstrument(getApplicationContext(), "/instrument/rest/all");
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
        spinnerGenre.setSelection(adapter.getPosition(getIntent().getStringExtra("genreName")), true);
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
        spinnerInstrument1.setSelection(adapter.getPosition(getIntent().getStringExtra("instrument1Name")), true);

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
        spinnerInstrument2.setSelection(adapter.getPosition(getIntent().getStringExtra("instrument2Name")), true);

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
        spinnerInstrument3.setSelection(adapter.getPosition(getIntent().getStringExtra("instrument3Name")), true);
    }

    //region Take Photo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == Activity.RESULT_OK) {
            //region Is Camera
            rotateImage(setReducedImageSize());
            //endregion
        }
    }

    private Bitmap setReducedImageSize() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bmp = BitmapFactory.decodeFile(mImageFileLocation, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return bmp;
    }

    public void takePhoto() {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String authorities = getPackageName() + ".fileprovider";
        ImageUri = FileProvider.getUriForFile(this, authorities, photoFile);
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "musisearch_" + timeStamp + "_";
        File storageDirectory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "musisearch");
        // Create the storage directory if it does not exist
        if (!storageDirectory.exists()) {
            if (!storageDirectory.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();
        return image;
    }

    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImageFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        //int orientation = 6;
        int rotationInDegrees = exifToDegrees(orientation);
        Matrix matrix = new Matrix();
        //matrix.setRotate(-90);
        matrix.setRotate(rotationInDegrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        _images = getStringImage(rotatedBitmap);
        //imageView.setImageBitmap(rotatedBitmap);
        isGetPhoto = true;
        if (!isCover) {
            scanGallery(this, mImageFileLocation);
        } else {
            scanGalleryCover(this, mImageFileLocation);
        }
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private void scanGalleryCover(final Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    //unimplemeted method
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        isImage = false;
        isGetPhoto = true;
        //String params = "inId=" + sharedPreferencesGoogle.getInt("id", 0) + "&inImagePath=" + path.substring(path.lastIndexOf("/") + 1) + "&inImageString=" + _images;
        sendRequestArrayBasePost(this, "/musisi/rest/update", getParamsBody(path));
    }

    private JSONObject getParamsBody(String path) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", sharedPreferencesGoogle.getInt("id", 0));
            jsonBody.put("imagePath", path.substring(path.lastIndexOf("/") + 1));
            jsonBody.put("imageString", _images);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonBody;
    }

    private JSONObject getParamsBody() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", sharedPreferencesGoogle.getInt("id", 0));
            jsonBody.put("genre", idGenre);
            jsonBody.put("region", region);
            jsonBody.put("biography", editBio.getText().toString());
            jsonBody.put("instrument1", idInstrument1);
            jsonBody.put("instrument2", idInstrument2);
            jsonBody.put("instrument3", idInstrument3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonBody;
    }

    public String getStringImage(Bitmap bmp) {
        final int maxSize = 480;
        int outWidth;
        int outHeight;
        int inWidth = bmp.getWidth();
        int inHeight = bmp.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, outWidth, outHeight, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void scanGallery(final Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    //unimplemeted method
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

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

    @Override
    public void onResponseSuccessString(String response) {
        if ((response != null) && (!response.equals("null")) && (!response.equals("[]"))) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                if (isGetPhoto) {
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
                    }
                    if (progressDialog != null) {
                        statusBaseLoading(false, true);
                    }
                } else {
                    if (jsonObj.getString("errorResult").equals("false")) {
                        JSONObject object = jsonObj.getJSONObject("object");
                        onBackPressed();
                    }
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
        if (progressDialog != null) {
            statusBaseLoading(false, true);
        }
    }

    @Override
    public void onResponseErrorString() {

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
    public void onResume() {
        super.onResume();
    }

}
