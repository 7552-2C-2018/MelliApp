package com.a7552_2c_2018.melliapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.model.UserInfo;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.MultiSelectionSpinner;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.tuanchauict.intentchooser.ImageChooserMaker;
import com.tuanchauict.intentchooser.selectphoto.CameraChooser;
import com.tuanchauict.intentchooser.selectphoto.ImageChooser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PostsActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener, LocationListener {

    private static final String TAG = "PostsActivity";
    private static final int REQUEST_IMAGE_CHOOSER = 1;
    private double latitude = 0, longitude = 0;

    @BindView(R.id.apTvTitle)
    EditText title;

    @BindView(R.id.apMlDesc)
    EditText desc;

    @BindView(R.id.apNstock)
    EditText stock;

    @BindView(R.id.apNprice)
    EditText price;

    @BindView(R.id.apAddress)
    EditText address;

    @BindView(R.id.apRbNew)
    RadioButton isNew;

    @BindView(R.id.apRbMakesShips)
    RadioButton makesShipping;

    @BindView(R.id.mySpinner)
    MultiSelectionSpinner paymentOptions;

    @BindView(R.id.apScategories)
    Spinner categories;

    @BindView(R.id.apBaddpictures)
    Button loadImg;

    @BindView(R.id.apBsave)
    Button validatePost;

    @BindView(R.id.secondLayout)
    LinearLayout secondLayout;

    private List<String> base64array = null;
    private LocationManager locationManager;
    private Criteria criteria;
    private String bestProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nueva Publicación");

        getServerCategories();
        getServerPayments();

        secondLayout.setVisibility(View.GONE);

        loadImg.setOnClickListener(v -> {
            Intent intent = ImageChooserMaker.newChooser(PostsActivity.this)
                    .add(new CameraChooser())
                    .add(new ImageChooser(true))
                    .create("Seleccione la imagen");
            startActivityForResult(intent, REQUEST_IMAGE_CHOOSER);
        });

        validatePost.setOnClickListener(v -> {
            if (validateInputs()) {
                callBackend();
                onBackPressed();
            } else {
                PopUpManager.showToastError(getApplicationContext(), getString(R.string.pa_msg));
            }
        });

    }

    private void getServerCategories() {
        String REQUEST_TAG = "getCategories";
        String url = getString(R.string.remote_getCat);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::getServerCategoriesResponse,
                error -> {
                    Log.d(TAG, "volley error check" + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                    //Or if nothing works than splitting is the only option
                    //Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());

                return params;
            }
        };
        Log.d(TAG, "categories request " + jsonArrayRequest.toString());
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest,REQUEST_TAG);
    }

    private void getServerCategoriesResponse(JSONArray response) {
        Log.d(TAG, response.toString());
        String[] categories_array = null;
        try {
            categories_array = new String[response.length()];
            for (int i = 0; i < response.length(); ++i) {
                JSONObject item = response.getJSONObject(i);
                String cat = item.getString("name");
                categories_array[i] = cat;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
    }

    private void getServerPayments() {
        String REQUEST_TAG = "getPayments";
        String url = getString(R.string.remote_getPays);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::getServerPaymentsResponse,
                error -> {
                    Log.d(TAG, "volley error check" + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                    //Or if nothing works than splitting is the only option
                    Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());

                return params;
            }
        };
        Log.d(TAG, "payments request " + jsonArrayRequest.toString());
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest,REQUEST_TAG);
    }

    private void getServerPaymentsResponse(JSONArray response) {
        Log.d(TAG, response.toString());
        String[] payments_array = null;
        try {
            payments_array = new String[response.length()];
            for (int i = 0; i < response.length(); ++i) {
                JSONObject item = response.getJSONObject(i);
                String pay = item.getString("name");
                payments_array[i] = pay;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        paymentOptions.setItems(payments_array);
        paymentOptions.setListener(this);
    }

    private boolean validateInputs() {
        if (title.getText().toString().isEmpty() || desc.getText().toString().isEmpty() ||
        stock.getText().toString().isEmpty() || price.getText().toString().isEmpty() ||
                paymentOptions.getSelectedItemsAsString().isEmpty()) {
            return false;
        }
        return Integer.parseInt(stock.getText().toString()) > 0 &&
                Integer.parseInt(price.getText().toString()) > 0;
    }

    private void callBackend(){
        String REQUEST_TAG = "createPost";
        String url = getString(R.string.remote_posts);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            //Location location = Objects.requireNonNull(lm).getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //longitude = location.getLongitude();
            //latitude = location.getLatitude();

            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

            //You can still do this if you like, you might get lucky:
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            else{
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
            Location location = Objects.requireNonNull(locationManager).getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                response -> {
                    Log.d(TAG, "Success");
                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.post_ok));
                }, error -> {
                    Log.d(TAG, "volley error create " + error.getMessage());
                    //OR
                    Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                    //OR
                    Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                    //Or if nothing works than splitting is the only option
                    Log.d(TAG, "volley msg4 " + new String(error.networkResponse.data));

                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", title.getText().toString());
                params.put("desc", desc.getText().toString());
                params.put("stock", stock.getText().toString());
                params.put("price", price.getText().toString());
                params.put("street", address.getText().toString());
                params.put("new", String.valueOf(isNew.isChecked()));
                params.put("shipping", String.valueOf(makesShipping.isChecked()));
                List<String> aux = paymentOptions.getSelectedStrings();
                for (int i=0; i<aux.size(); i++){
                    params.put("payments", aux.get(i));
                }
                params.put("category", categories.getSelectedItem().toString());
                if (base64array != null) {
                    for (int j=0; j<base64array.size(); j++){
                        params.put("pictures", base64array.get(j));
                    }
                }
                UserInfo user = SingletonUser.getInstance().getUser();
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(stringRequest,REQUEST_TAG);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        Toast.makeText(this, strings.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CHOOSER && resultCode == RESULT_OK) {
            List<Uri> imageUris = ImageChooserMaker.getPickMultipleImageResultUris(this, data);
            //ArrayList<Bitmap> mBitmapsSelected = new ArrayList<Bitmap>();
            if(secondLayout.getChildCount() > 0)
                secondLayout.removeAllViews();
            base64array = new ArrayList<>();
            for (int i = 0; i < imageUris.size(); i++) {
                Uri uri = imageUris.get(i);
                Bitmap bitmap;
                String output;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    output = encode2ToBase64(Objects.requireNonNull(bitmap));
                    base64array.add(output);
                    ImageView miniPhoto = new ImageView(this);
                    miniPhoto.setImageBitmap(bitmap);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    miniPhoto.setLayoutParams(params);
                    secondLayout.addView(miniPhoto);
                    secondLayout.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            secondLayout.setVisibility(View.GONE);
        }
    }

    private static String encode2ToBase64(Bitmap image2) {
        ByteArrayOutputStream base = new ByteArrayOutputStream();
        image2.compress(Bitmap.CompressFormat.JPEG, 100, base);
        byte[] b = base.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);

        //open the map:
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
