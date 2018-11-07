package com.a7552_2c_2018.melliapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemActivity extends AppCompatActivity {

    private static final String TAG = "ItemActivity";

    private String[] sampleImages = null;
    private String Id;
    private int price;

    @BindView(R.id.carouselView)
    CarouselView carouselView;

    @BindView(R.id.aiTvTitle)
    TextView tvTitle;

    @BindView(R.id.aiTvSeller)
    TextView tvSeller;

    @BindView(R.id.aiTvPrice)
    TextView tvPrice;

    @BindView(R.id.aiTvDesc)
    TextView tvDesc;

    @BindView(R.id.aiTvPayments)
    TextView tvPayments;

    @BindView(R.id.aiTvShipping)
    TextView tvShipping;

    @BindView(R.id.aiBtnBuy)
    Button btnBuy;

    private final ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            String base64Image = sampleImages[position];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Id = getIntent().getStringExtra("ID");

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buyIntent = new Intent(ItemActivity.this, BuyingActivity.class);
                buyIntent.putExtra("ID", Id);
                buyIntent.putExtra("title", tvTitle.getText().toString());
                buyIntent.putExtra("price", price);
                startActivity(buyIntent);
            }
        });

        //mocking();
        getPostData();

    }

    private void getPostData() {
        String REQUEST_TAG = "getPost";
        //String url = getString(R.string.remote_login);
        String url = getString(R.string.remote_posts);
        url = url + Id;
        JsonObjectRequest jsonObtRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getPostResponse(response);
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
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("facebookId", SingletonUser.getInstance().getUser().getFacebookID());
                params.put("token", SingletonUser.getInstance().getToken());

                return params;
            }
        };
        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(jsonObtRequest,REQUEST_TAG);
    }

    private void getPostResponse(JSONObject response) {
        Log.d(TAG, response.toString());
        try {
            tvTitle.setText(response.getString("title"));
            tvDesc.setText(response.getString("description"));
            price = response.getInt("price");
            tvPrice.setText("$ " + String.valueOf(price));
            JSONObject seller = response.getJSONObject("name");
            tvSeller.setText("vendido por " + seller.getString("nombre") + " " +
                    seller.getString("apellido"));
            JSONArray payments = response.getJSONArray("payments");
            StringBuilder fullString = new StringBuilder(payments.getString(0));
            for (int i=1; i<payments.length(); i++){
                fullString.append(", ").append(payments.getString(i));
            }
            tvPayments.setText(fullString.toString());
            JSONArray pictures = response.getJSONArray("pictures");
            sampleImages = new String[pictures.length()];
            for (int i=0; i<pictures.length(); i++){
                sampleImages[i]= pictures.getString(i);
            }
            if (response.getBoolean("shipping")){
                tvShipping.setText(getString(R.string.ia_ship_yes));
            } else {
                tvShipping.setText(getString(R.string.ia_ship_no));
            }

            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(sampleImages.length);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //remove this method
    private void mocking(){
        sampleImages = new String[4];
        for (int i=0; i<4; i++){
            sampleImages[i]= getString(R.string.base64mock);
        }

        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        tvTitle.setText(getString(R.string.mock_title));
        tvDesc.setText(getString(R.string.mock_desc));
        tvPrice.setText(getString(R.string.mock_price));
        tvSeller.setText(getString(R.string.mock_seller));
        String[] pays = getResources().getStringArray(R.array.mock_payments_array);
        StringBuilder fullString = new StringBuilder(pays[0]);
        for (int i=1; i<pays.length; i++){
            fullString.append(", ").append(pays[i]);
        }
        tvPayments.setText(fullString.toString());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
