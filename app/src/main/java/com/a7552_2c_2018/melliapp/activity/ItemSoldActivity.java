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
    private static final int RESULT_RANKING = 1;
    private String[] sampleImages = null;
    private String buyId;
    private String postId;
    private String user = "";
    private String categ = "";
    private String status = "";

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

    @BindView(R.id.aisStatus)
    TextView tvStatus;

    @BindView(R.id.aisBtnAction)
    Button btnAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_sold);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        buyId = getIntent().getStringExtra("buyId");
        postId = getIntent().getStringExtra("postId");
        categ = getIntent().getStringExtra("categ");
        status = getIntent().getStringExtra("status");

        tvStatus.setText(String.format(getString(R.string.isa_status), status));

        if (status.equals("Finalizado")){
            btnAction.setVisibility(View.VISIBLE);
        }

        if (categ.equals("sold")) {
            getSupportActionBar().setTitle(getString(R.string.st_sold_item));
        } else {
            getSupportActionBar().setTitle(getString(R.string.st_buy_item));
        }

        rlQuestions.setOnClickListener(v -> {
            Intent qstIntent = new Intent(ItemSoldActivity.this, QuestionsActivity.class);
            qstIntent.putExtra("ID", postId);
            qstIntent.putExtra("user", user);
            startActivity(qstIntent);
        });

        String btnText;
        if (categ.equals("buy")) {
            btnText = getString(R.string.isa_btn_rank_v);
        } else {
            btnText = getString(R.string.isa_btn_rank_c);
        }
        btnAction.setText(btnText);

        btnAction.setOnClickListener(v -> {
            Intent rankingIntent = new Intent(ItemSoldActivity.this, RankingActivity.class);
            rankingIntent.putExtra("postId", postId);
            rankingIntent.putExtra("buyId", buyId);
            rankingIntent.putExtra("user", user);
            startActivityForResult(rankingIntent, RESULT_RANKING);
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
        String url = getString(R.string.remote_posts);
        url = url + postId;
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
            tvDesc.setText(response.getString("description").replaceAll("\\\\n", System.getProperty("line.separator")));
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
                user = "seller";
            }

            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(sampleImages.length);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_RANKING:
                if (resultCode == RESULT_OK) {
                    getPostData();
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode, data);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
