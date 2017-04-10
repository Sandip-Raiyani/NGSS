package io.krumbs.sdk.starter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.net.URLEncoder;

public class BuyerCategories extends AppCompatActivity {

	private View fanButton;
	private View tubelightButton;
	private View cleanerButton;
	private View laptopButton;
	private View adapterButton;
	private View mobilesButton;
	private View tvButton;
	private View carsButton;
	private View bikesButton;
	private View auxiliariesButton;
	private View tablesButton;
	private View chairsButton;
	private View mattressButton;
	private View booksButton;

	private double latitude;
	private double longitude;
	public static final String PREFS_NAME = "BUYERS_PREFS";

	Intent results;

	public void displayResults(String item) {

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = settings.edit();

		editor.clear();
		editor.putString("Category", item);
		editor.commit();

		String url = "http://52.36.188.37:8080/NGSSUCIConnect/UCIConnectServer?item=" + URLEncoder.encode(item) + "&lat=" + latitude + "&long=" + longitude;
		Log.i("Search URL", url);

		Toast.makeText(getApplicationContext(), "Please wait, We are fetching the best deals", Toast.LENGTH_LONG).show();

		JsonArrayRequest jsonRequest = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {

						Log.i("JSON DATA", response.toString());
						results = new Intent(BuyerCategories.this, ResultsActivity.class);
						results.putExtra("JSON Results", response.toString());
						startActivity(results);
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.i("RESPONSE", "ERROR");
				Toast.makeText(getApplicationContext(), "Unable to connect to Internet", Toast.LENGTH_LONG).show();
			}
		});

		jsonRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buyer_categories);
		fanButton = findViewById(R.id.fanButton);
		tubelightButton = findViewById(R.id.tubelightButton);
		cleanerButton = findViewById(R.id.cleanerButton);
		laptopButton = findViewById(R.id.laptopButton);
		adapterButton = findViewById(R.id.adapterButton);
		mobilesButton = findViewById(R.id.mobilesButton);
		tvButton = findViewById(R.id.tvButton);
		carsButton = findViewById(R.id.carsButton);
		bikesButton = findViewById(R.id.bikesButton);
		auxiliariesButton = findViewById(R.id.auxilliariesButton);
		tablesButton = findViewById(R.id.tablesButton);
		chairsButton = findViewById(R.id.chairsButton);
		mattressButton = findViewById(R.id.mattressButton);
		booksButton = findViewById(R.id.booksButton);

		GPSTracker gps = new GPSTracker(getApplicationContext());
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		}

		Intent getItemsService = new Intent(getApplicationContext(), GetItemsService.class);
		startService(getItemsService);

		fanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Fan");
			}
		});

		tubelightButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Tubelights");
			}
		});

		cleanerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Vaccum Cleaner");
			}
		});

		laptopButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Laptop");
			}
		});

		adapterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Adapters");
			}
		});

		mobilesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Mobiles");
			}
		});

		tvButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("TV");
			}
		});

		carsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Cars");
			}
		});

		bikesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Bikes");
			}
		});

		auxiliariesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Auxiliaries");
			}
		});

		tablesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Table");
			}
		});

		chairsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Chairs");
			}
		});

		mattressButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Mattress");
			}
		});

		booksButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayResults("Books");
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
}
