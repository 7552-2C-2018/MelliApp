package com.a7552_2c_2018.melliapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.model.UserInfo;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.MultiSelectionSpinner;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tuanchauict.intentchooser.ImageChooserMaker;
import com.tuanchauict.intentchooser.selectphoto.CameraChooser;
import com.tuanchauict.intentchooser.selectphoto.ImageChooser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    private static final String TAG = "PostsActivity";
    private static final int REQUEST_IMAGE_CHOOSER = 1;
    private EditText title, desc, stock, price;
    private RadioButton isNew;
    private MultiSelectionSpinner paymentOptions;
    private Spinner categories;
    private Button loadImg, validatePost;
    private List<String> base64array = null;
    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] payments_array = getResources().getStringArray(R.array.payments_array);
        String[] categories_array = getResources().getStringArray(R.array.categories_array);

        title = findViewById(R.id.apTvTitle);
        desc = findViewById(R.id.apMlDesc);
        stock = findViewById(R.id.apNstock);
        price = findViewById(R.id.apNprice);
        isNew = findViewById(R.id.apRbNew);

        paymentOptions = findViewById(R.id.mySpinner);
        paymentOptions.setItems(payments_array);
        paymentOptions.setListener(this);

        categories = findViewById(R.id.apScategories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

        loadImg = findViewById(R.id.apBaddpictures);
        loadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ImageChooserMaker.newChooser(PostsActivity.this)
                        .add(new CameraChooser())
                        .add(new ImageChooser(true))
                        .create("Select Image");
                startActivityForResult(intent, REQUEST_IMAGE_CHOOSER);
            }
        });

        validatePost = findViewById(R.id.apBsave);
        validatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs() == true) {
                    callBackend();
                } else {
                    PopUpManager.showToastError(getApplicationContext(), getString(R.string.pa_msg));
                }
            }
        });
    }

    public boolean validateInputs() {
        if (title.getText().toString().isEmpty() || desc.getText().toString().isEmpty() ||
        stock.getText().toString().isEmpty() || price.getText().toString().isEmpty() ||
                paymentOptions.getSelectedItemsAsString().isEmpty()) {
            return false;
        }
        if (Integer.parseInt(stock.getText().toString()) <= 0 ||
                Integer.parseInt(price.getText().toString()) <= 0 ) {
            return false;
        }
        return true;
    }

    public void callBackend(){
        String REQUEST_TAG = "createPost";
        //String url = R.string.local_server + "/login";
        //String url = "http://127.0.0.1:5000/login";
        //TODO: add the real uri
        String url = getString(R.string.remote_login);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        JSONObject data = new JSONObject();
        UserInfo user = SingletonUser.getInstance().getUser();
        try {
            data.put("facebookId", user.getFacebookID());
            data.put("title", title.getText().toString());
            data.put("desc", desc.getText().toString());
            data.put("stock", Integer.parseInt(stock.getText().toString()));
            data.put("price", Integer.parseInt(price.getText().toString()));
            data.put("new", isNew.isChecked());
            data.put("payments", paymentOptions.getSelectedStrings().toArray());
            data.put("category", categories.getSelectedItem().toString());
            data.put("pictures", base64array.toArray());
            data.put("latitude", latitude);
            data.put("longitude", longitude);
            Log.d(TAG, getString(R.string.send_server) + data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, getString(R.string.json_error) + e.toString());
            PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getServerLoginResponse(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d(TAG, "error: " + error.toString());
                        PopUpManager.showToastError(getApplicationContext(), getString(R.string.general_error));
                    }
                }
        );
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest,REQUEST_TAG);

    }

    private void getServerLoginResponse(JSONObject response) {
        PopUpManager.showToastError(getApplicationContext(), getString(R.string.post_ok));
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
            ArrayList<Bitmap> mBitmapsSelected = new ArrayList<Bitmap>();
            base64array = new ArrayList<String>();
            for (int i = 0; i < imageUris.size(); i++) {
                Uri uri = imageUris.get(i);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mBitmapsSelected.add(bitmap);
                base64array.add(encode2Tobase64(bitmap));
            }
        }
    }

    public static String encode2Tobase64(Bitmap image2) {
        Bitmap immage = image2;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
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
