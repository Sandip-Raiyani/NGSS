package io.krumbs.sdk.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		mPasswordView = (EditText) findViewById(R.id.password);

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				final String email = mEmailView.getText().toString();
				final String password = mPasswordView.getText().toString();

				if (email.length() == 0 || password.length() == 0) {
					Toast.makeText(getApplicationContext(), "Please enter UserName / Password", Toast.LENGTH_LONG).show();
					return;
				}

				String url = "http://52.36.188.37/fabflix/LoginAndroid";
				StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.i("Response", response);

						if (response.equals("Correct Password\n")) {
							Toast.makeText(getApplication(), "Welcome to UCI Connect...", Toast.LENGTH_LONG).show();
							Intent gotoSearch = new Intent(LoginActivity.this, MainActivity.class);
							startActivity(gotoSearch);

						} else {
							Toast.makeText(getApplication(), "Wrong UserName / Password", Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("Response", "ERROR");
					}
				}) {
					@Override
					protected Map<String, String> getParams() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("userEMail", email);
						params.put("userPassword", password);

						return params;
					}
				};

				Volley.newRequestQueue(getApplicationContext()).add(sr);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			alertbox.setTitle("Quit");
			alertbox.setMessage("Are you sure you want to exit?");

			alertbox.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							finish();
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_HOME);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					});

			alertbox.setNeutralButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});

			alertbox.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}