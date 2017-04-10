package io.krumbs.sdk.starter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import io.krumbs.sdk.starter.R;

public class ShowImageActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_image);

		Bundle bundle = getIntent().getExtras();
		String url = bundle.getString("imageURL");
		Log.i("Image URL", url);

		ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap response) {
				ImageView imageView = (ImageView) findViewById(R.id.itemImageView);
				imageView.setImageBitmap(response);
			}
		}, 0, 0, null, null);

		Volley.newRequestQueue(getApplicationContext()).add(ir);

	}

}
