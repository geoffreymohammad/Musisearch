package com.project.android.musisearch.ui.main.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
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
import com.project.android.musisearch.BaseFragment;
import com.project.android.musisearch.R;
import com.project.android.musisearch.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.project.android.musisearch.utils.AppConstant.END_POINT;
import static com.project.android.musisearch.utils.AppConstant.WS_ADDRESS;

public class FragmentSearch extends BaseFragment {


    public FragmentSearch() {
        // Required empty public constructor
    }

    int region = 0;
    ArrayList<String> genreName, instrumentName;
    ArrayList<Integer> genreId, instrumentId;
    int idGenre, idInstrument1;
    private Spinner spinnerGenre, spinnerInstrument1;
    private Button btnSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_search, container, false);
        // Inflate the layout for this fragment
        spinnerGenre = view.findViewById(R.id.spinnerGenre);
        spinnerInstrument1 = view.findViewById(R.id.spinnerInstrument1);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
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
                    case R.id.radio_all:
                        region = 0;
                        break;
                }
            }
        });
        ((MainActivity) getActivity()).updateToolbar("Search", false);

        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SearchActivity.class);
                i.putExtra("idGenre", idGenre);
                i.putExtra("idInstrument1", idInstrument1);
                i.putExtra("region", region);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    void loadGenre() {
        sendBaseRequestArrayGetGenre(getActivity(), "/genre/rest/all");
        sendBaseRequestArrayGetInstrument(getActivity(), "/instrument/rest/all");
    }


    @Override
    public void onResponseSuccessString(String response) {

    }

    @Override
    public void onResponseErrorString() {

    }

    public void onResponseSuccessGenre(String response) {
        if ((response != null) && (!response.equals("null")) && (!response.equals("[]"))) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                genreName = new ArrayList<>();
                genreId = new ArrayList<>();
                genreName.add("All");
                genreId.add(0);
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

    private void initSpinnerGenre() {
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, genreName);
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
    }

    private void initSpinnerInstrument() {
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, instrumentName);
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

    }

    void snackBar(String message) {
        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGenre();
    }
}
