package io.krumbs.sdk.starter;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetItemsService extends Service {

	public static final String PREFS_NAME = "BUYERS_PREFS";

	double latitude;
	double longitude;

	public GetItemsService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}

		class LocationRequest implements LocationListener {

			@Override
			public void onLocationChanged(Location location) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
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

		LocationRequest listener = new LocationRequest();

		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				recurse();
			}
		});
		thread.start();
	}


	void recurse() {

		final SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		final Editor editor = settings.edit();

		String lastPref = settings.getString("Category", null);
		if (lastPref != null) {

			String url = "http://52.36.188.37:8080/NGSSUCIConnect/UCIConnectServer?item=" + lastPref + "&lat=" + latitude + "&long=" + longitude;

			Log.i("SERVICE URL", url);
			JsonArrayRequest jsonRequest = new JsonArrayRequest(url,
					new Response.Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							Log.i("SERVICE JSON DATA", response.toString());
							try {
								JSONObject firstResult = (JSONObject) response.get(0);
								String lastId = settings.getString("lastId", null);
								String resultFirstId = firstResult.getString("id");
								Log.i("SERVICE resultFirst ID", resultFirstId);

								if (lastId == null) {
									editor.putString("lastId", firstResult.getString("id"));
									editor.commit();
								} else if (!lastId.equals(firstResult.getString("id"))) {
									Log.i("SERVICE lastId", lastId);
									editor.putString("lastId", firstResult.getString("id"));
									editor.commit();

									Intent goToSingleResultActivity = new Intent(getApplicationContext(), SingleResultActivity.class);
									goToSingleResultActivity.putExtra("singleResult", firstResult.toString());
									PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), goToSingleResultActivity, 0);

									Notification notification = new Notification.Builder(getApplicationContext())
											.setSmallIcon(R.drawable.emoji_1)
											.setContentTitle("New Item")
											.setContentText(firstResult.toString())
											.setContentIntent(pendingIntent)
											.setAutoCancel(true).build();
									NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
									notificationManager.notify(0, notification);

								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.i("RESPONSE", "ERROR");
				}
			});

			jsonRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);


		}
		try {
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		recurse();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
	}
}
