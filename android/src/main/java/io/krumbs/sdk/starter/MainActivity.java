/*
 * Copyright (c) 2016 Krumbs Inc
 * All rights reserved.
 *
 */
package io.krumbs.sdk.starter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Map;

import io.krumbs.sdk.KrumbsSDK;
import io.krumbs.sdk.krumbscapture.KCaptureCompleteListener;


public class MainActivity extends AppCompatActivity {
	private View cameraView;
	private View startCaptureButton;
	private View buyButton;
	private View kLogoutButton;

	public static final String PREFS_NAME = "BUYERS_PREFS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startCaptureButton = findViewById(R.id.kcapturebutton);
		cameraView = findViewById(R.id.camera_container);
		buyButton = findViewById(R.id.kBuyStuffButton);
		kLogoutButton = findViewById(R.id.kLogoutButton);

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		String lastPref = settings.getString("Category", null);
		if (lastPref != null) {
			Toast.makeText(getApplicationContext(), "Last pref was " + lastPref, Toast.LENGTH_LONG).show();
		}

		startCaptureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startCaptureButton.setVisibility(View.INVISIBLE);
				buyButton.setVisibility(View.INVISIBLE);
				kLogoutButton.setVisibility(View.INVISIBLE);
				cameraView.setVisibility(View.VISIBLE);
				startCapture();
			}
		});

		buyButton.setOnClickListener(new View.OnClickListener() {
			                             @Override
			                             public void onClick(View v) {
				                             Intent intent = new Intent(MainActivity.this, BuyerCategories.class);
				                             startActivity(intent);
			                             }
		                             }
		);

		kLogoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder alertBox = new AlertDialog.Builder(MainActivity.this);
				alertBox.setTitle("LogOut");
				alertBox.setMessage("Do you want to logout?");

				alertBox.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								finish();
								Intent goToLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
								goToLoginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(goToLoginActivity);
							}
						});

				alertBox.setNeutralButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});

				alertBox.show();

			}
		});
	}


	private void startCapture() {
		int containerId = R.id.camera_container;
// SDK usage step 4 - Start the K-Capture component and add a listener to handle returned images and URLs
		KrumbsSDK.startCapture(containerId, this, new KCaptureCompleteListener() {
			@Override
			public void captureCompleted(CompletionState completionState, boolean audioCaptured, Map<String, Object> map) {
				// DEBUG LOG
				if (completionState != null) {
					Log.d("KRUMBS-CALLBACK", completionState.toString());
				}
				if (completionState == CompletionState.CAPTURE_SUCCESS) {
// The local image url for your capture
					String imagePath = (String) map.get(KCaptureCompleteListener.CAPTURE_MEDIA_IMAGE_PATH);
					if (audioCaptured) {
// The local audio url for your capture (if user decided to record audio)
						String audioPath = (String) map.get(KCaptureCompleteListener.CAPTURE_MEDIA_AUDIO_PATH);
					}
// The mediaJSON url for your capture
					String mediaJSONUrl = (String) map.get(KCaptureCompleteListener.CAPTURE_MEDIA_JSON_URL);
					Log.i("KRUMBS-CALLBACK", mediaJSONUrl + ", " + imagePath);
					Intent goToMainActivity = new Intent(MainActivity.this, MainActivity.class);
					startActivity(goToMainActivity);

				} else if (completionState == CompletionState.CAPTURE_CANCELLED ||
						completionState == CompletionState.SDK_NOT_INITIALIZED) {
					cameraView.setVisibility(View.INVISIBLE);
					startCaptureButton.setVisibility(View.VISIBLE);
					buyButton.setVisibility(View.VISIBLE);
					kLogoutButton.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (cameraView.getVisibility() == View.INVISIBLE)
				Toast.makeText(getApplicationContext(), "Please press LOGOUT to go back", Toast.LENGTH_LONG).show();
			else {
				Intent goToMainActivity = new Intent(MainActivity.this, MainActivity.class);
				startActivity(goToMainActivity);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
