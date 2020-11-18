package com.project.android.musisearch.ui.main.profile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.android.musisearch.BaseFragment;
import com.project.android.musisearch.R;
import com.project.android.musisearch.adapter.AdapterBand;
import com.project.android.musisearch.model.GroupBand;
import com.project.android.musisearch.ui.login.LoginActivity;
import com.project.android.musisearch.ui.main.MainActivity;
import com.project.android.musisearch.ui.main.groupBand.AddBandActivity;
import com.project.android.musisearch.ui.main.status.AddStatusActivity;
import com.project.android.musisearch.utils.Sessions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.project.android.musisearch.utils.AppConstant.WS_ADDRESS_IMAGE;
import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_GOOGLE;

public class FragmentProfile extends BaseFragment {
    TextView textName, textGenre, textInstrument, textBio;
    CircularImageView imageProfile;
    private String mImageFileLocation = "";
    private Uri ImageUri;
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    boolean isGetPhoto = false, isImage = false;
    private String _images = null;
    private boolean isCover = false;
    private String[] listItems;
    private SharedPreferences sharedPreferencesGoogle;
    private int region, genre, instrument1, instrument2, instrument3;
    private String genreName, regionName, instrument1Name, instrument2Name, instrument3Name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_profile, container, false);
        setBaseLoadingProgress(getActivity());
        sharedPreferencesGoogle = getActivity().getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);
        listItems = getResources().getStringArray(R.array.pickimage);
        imageProfile = view.findViewById(R.id.imageProfile);
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
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
        textName = view.findViewById(R.id.textName);
        textGenre = view.findViewById(R.id.textGenre);
        textInstrument = view.findViewById(R.id.textInstrument);
        textBio = view.findViewById(R.id.textBio);
        ((MainActivity) getActivity()).updateToolbar("Profile", true);
        RequestOptions requestOptions = new RequestOptions();
        Glide
                .with(getActivity())
                .load(WS_ADDRESS_IMAGE + sharedPreferencesGoogle.getString("imagePath", null))
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .apply(requestOptions.error(R.drawable.ic_profile))
                .apply(requestOptions.placeholder(R.drawable.ic_profile))
                .into(imageProfile);
        //loadStatus();
        return view;
    }

    void loadStatus() {
        sendBaseRequestArrayGet(getActivity(), "/musisi/rest/all?inId=" + sharedPreferencesGoogle.getInt("id", 0));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == Activity.RESULT_OK) {
            //region Is Camera
            rotateImage(setReducedImageSize());
            //endregion
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        String authorities = getActivity().getPackageName() + ".fileprovider";
        ImageUri = FileProvider.getUriForFile(getActivity(), authorities, photoFile);
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

    private Bitmap setReducedImageSize() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bmp = BitmapFactory.decodeFile(mImageFileLocation, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return bmp;
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
            scanGallery(getActivity(), mImageFileLocation);
        } else {
            scanGalleryCover(getActivity(), mImageFileLocation);
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
        sendRequestArrayBasePost(getActivity(), "/musisi/rest/update", getParamsBody(path));
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

    @Override
    public void onResponseSuccessString(String response) {
        if ((response != null) && (!response.equals("null")) && (!response.equals("[]"))) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                if (isGetPhoto) {
                    if (jsonObj.getString("errorResult").equals("false")) {

                    }
                } else {
                    if (jsonObj.getString("errorResult").equals("false")) {
                        JSONObject object = jsonObj.getJSONObject("object");
                        int id = object.getInt("id");
                        String nama = object.getString("nama");
                        String email = object.getString("email");
                        genreName = object.getString("genreName");
                        genre = object.getInt("genre");
                        region = object.getInt("region");
                        regionName = object.getString("regionName");
                        instrument1 = object.getInt("instrument1");
                        instrument2 = object.getInt("instrument2");
                        instrument3 = object.getInt("instrument3");
                        instrument1Name = object.getString("instrument1Name");
                        instrument2Name = object.getString("instrument2Name");
                        instrument3Name = object.getString("instrument3Name");
                        String tglLahir = object.getString("tglLahir");
                        String biography = object.getString("biography").replace("//", "/");
                        textName.setText(nama);
                        textGenre.setText(genreName);
                        textInstrument.setText(instrument1Name);
                        textBio.setText(biography);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.actionEdit:
                Intent i = new Intent(getActivity(), ProfileEditActivity.class);
                i.putExtra("name", textName.getText().toString());
                i.putExtra("bio", textBio.getText().toString());
                i.putExtra("genreName", genreName);
                i.putExtra("regionName", regionName);
                i.putExtra("region", region);
                i.putExtra("instrument1Name", instrument1Name);
                i.putExtra("instrument2Name", instrument2Name);
                i.putExtra("instrument3Name", instrument3Name);

                startActivity(i);
                return true;
            case R.id.actionLogout:
                SharedPreferences getPrefsSplash = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor e = getPrefsSplash.edit();
                e.putBoolean(Sessions.PREFS_LOGIN_GOOGLE, false);
                e.apply();

                Intent in = new Intent(getActivity(), LoginActivity.class);
                startActivity(in);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatus();
        RequestOptions requestOptions = new RequestOptions();
        Glide
                .with(getActivity())
                .load(WS_ADDRESS_IMAGE + sharedPreferencesGoogle.getString("imagePath", null))
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .apply(requestOptions.error(R.drawable.ic_profile))
                .apply(requestOptions.placeholder(R.drawable.ic_profile))
                .into(imageProfile);
    }
}
