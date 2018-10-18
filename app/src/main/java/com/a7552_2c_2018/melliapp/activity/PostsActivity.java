package com.a7552_2c_2018.melliapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.MultiSelectionSpinner;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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

import static com.facebook.FacebookSdk.getApplicationContext;

public class PostsActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    private static final String TAG = "PostsActivity";
    private static final int REQUEST_IMAGE_CHOOSER = 1;
    private EditText title, desc, stock, price;
    private RadioButton isNew, makesShipping;
    private MultiSelectionSpinner paymentOptions;
    private Spinner categories;
    private Button loadImg, validatePost;
    private List<String> base64array = null;
    private double latitude, longitude;
    private LinearLayout secondLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getServerCategories();
        getServerPayments();

        title = findViewById(R.id.apTvTitle);
        desc = findViewById(R.id.apMlDesc);
        stock = findViewById(R.id.apNstock);
        price = findViewById(R.id.apNprice);
        isNew = findViewById(R.id.apRbNew);
        makesShipping = findViewById(R.id.apRbMakesShips);
        paymentOptions = findViewById(R.id.mySpinner);
        categories = findViewById(R.id.apScategories);
        secondLayout = findViewById(R.id.secondLayout);
        secondLayout.setVisibility(View.GONE);

        loadImg = findViewById(R.id.apBaddpictures);
        loadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ImageChooserMaker.newChooser(PostsActivity.this)
                        .add(new CameraChooser())
                        .add(new ImageChooser(true))
                        .create("Seleccione la imagen");
                startActivityForResult(intent, REQUEST_IMAGE_CHOOSER);
            }
        });

        validatePost = findViewById(R.id.apBsave);
        validatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    callBackend();
                    onBackPressed();
                } else {
                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.pa_msg));
                }
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
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        getServerCategoriesResponse(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d(TAG, "volley error check" + error.getMessage());
                        //OR
                        Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                        //OR
                        Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                        //Or if nothing works than splitting is the only option
                        //Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                    }
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
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        getServerPaymentsResponse(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d(TAG, "volley error check" + error.getMessage());
                        //OR
                        Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                        //OR
                        Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                        //Or if nothing works than splitting is the only option
                        Log.d(TAG, "volley msg4 " +new String(error.networkResponse.data).split(":")[1]);

                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                    }
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
                //int id = item.getInt("_id");
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
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            Location location = Objects.requireNonNull(lm).getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
        }
        Location location = Objects.requireNonNull(lm).getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Success");
                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.post_ok));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "volley error create " + error.getMessage());
                //OR
                Log.d(TAG, "volley msg " +error.getLocalizedMessage());
                //OR
                Log.d(TAG, "volley msg3 " +error.getLocalizedMessage());
                //Or if nothing works than splitting is the only option
                Log.d(TAG, "volley msg4 " + new String(error.networkResponse.data));

                PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title.getText().toString());
                params.put("desc", desc.getText().toString());
                params.put("stock", stock.getText().toString());
                params.put("price", price.getText().toString());
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

        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjRequest,REQUEST_TAG);
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

    public static String encode2ToBase64(Bitmap image2) {
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

}
