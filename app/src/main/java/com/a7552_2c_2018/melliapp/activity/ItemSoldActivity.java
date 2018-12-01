package com.a7552_2c_2018.melliapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
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

public class ItemSoldActivity extends AppCompatActivity {

    private static final String TAG = "ItemSoldActivity";
    private String[] sampleImages = null;
    private String Id;

    @BindView(R.id.aisCarouselView)
    CarouselView carouselView;

    @BindView(R.id.aisTvTitle)
    TextView tvTitle;

    @BindView(R.id.aisTvSeller)
    TextView tvSeller;

    @BindView(R.id.aisTvPrice)
    TextView tvPrice;

    @BindView(R.id.aisTvDesc)
    TextView tvDesc;

    @BindView(R.id.aisTvPayments)
    TextView tvPayments;

    @BindView(R.id.aisTvShipping)
    TextView tvShipping;

    @BindView(R.id.aisRlQuestions)
    RelativeLayout rlQuestions;

    @BindView(R.id.aisBtnAction)
    Button btnAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_sold);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.st_buys));

        Id = getIntent().getStringExtra("ID");

        rlQuestions.setOnClickListener(v -> {
            Intent qstIntent = new Intent(ItemSoldActivity.this, QuestionsActivity.class);
            qstIntent.putExtra("ID", Id);
            startActivity(qstIntent);
        });

        btnAction.setOnClickListener(v -> {

        });
        getPostData();
    }

    private final ImageListener imageListener = (position, imageView) -> {
        String base64Image = sampleImages[position];
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    };

    private void getPostData() {
        String REQUEST_TAG = "getPost";
        //String url = getString(R.string.remote_login);
        String url = getString(R.string.remote_posts);
        url = url + Id;
        JsonObjectRequest jsonObtRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::getPostResponse,
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
            int price = response.getInt("price");
            tvPrice.setText(String.format(getString(R.string.price_holder), price));
            JSONObject seller = response.getJSONObject("name");
            tvSeller.setText(String.format(getString(R.string.ia_holder), seller.getString("nombre"),
                    seller.getString("apellido")));
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

            JSONObject id = response.getJSONObject("_id");
            String sellerId = id.getString("facebookId");
            if (sellerId.equals(SingletonUser.getInstance().getUser().getFacebookID())){
                String user = "seller";
            }

            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(sampleImages.length);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
