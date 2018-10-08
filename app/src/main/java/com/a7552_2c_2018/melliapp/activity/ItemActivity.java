package com.a7552_2c_2018.melliapp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.Objects;

public class ItemActivity extends AppCompatActivity {

    private CarouselView carouselView;

    private String[] sampleImages = null;

    private TextView tvTitle;
    private TextView tvSeller;
    private TextView tvPrice;
    private TextView tvDesc;
    private TextView tvPayments;
    // --Commented out by Inspection (01/10/2018 23:20):Button btnBuy;

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

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        carouselView = findViewById(R.id.carouselView);
        tvTitle = findViewById(R.id.aiTvTitle);
        tvSeller = findViewById(R.id.aiTvSeller);
        tvPrice = findViewById(R.id.aiTvPrice);
        tvDesc = findViewById(R.id.aiTvDesc);
        tvPayments = findViewById(R.id.aiTvPayments);

        mocking();
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
