package com.a7552_2c_2018.melliapp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class ItemActivity extends AppCompatActivity {

    CarouselView carouselView;

    String[] sampleImages = null;

    TextView tvTitle, tvSeller, tvPrice, tvDesc, tvPayments;
    Button btnBuy;

    ImageListener imageListener = new ImageListener() {
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carouselView = findViewById(R.id.carouselView);
        tvTitle = findViewById(R.id.aiTvTitle);
        tvSeller = findViewById(R.id.aiTvSeller);
        tvPrice = findViewById(R.id.aiTvPrice);
        tvDesc = findViewById(R.id.aiTvDesc);
        tvPayments = findViewById(R.id.aiTvPayments);
        btnBuy = findViewById(R.id.aiBtnBuy);

        mocking();
    }

    //remove this method
    public void mocking(){
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
        String fullString = pays[0];
        for (int i=1; i<pays.length; i++){
            fullString = fullString + ", " + pays[i];
        }
        tvPayments.setText(fullString);
    }
}
