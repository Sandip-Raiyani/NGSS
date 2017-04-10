package io.krumbs.sdk.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ResultsActivity extends AppCompatActivity {

	ArrayList<String> imageURL = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ScrollView sv = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);

		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);

		Bundle extras = getIntent().getExtras();

		String jsonResults = extras.getString("JSON Results");

		try {
			JSONArray resultsArray = new JSONArray(jsonResults);
			if (resultsArray.length() == 0) {
				TextView resultsTextView = new TextView(ResultsActivity.this);
				resultsTextView.setText(Html.fromHtml("<br> <h3> No Results Found</h3>"));
				resultsTextView.setGravity(Gravity.CENTER);
				ll.addView(resultsTextView);
			} else {
				TextView resultsTextView = new TextView(ResultsActivity.this);
				resultsTextView.setText(Html.fromHtml("<br> <h3> Search Results </h3>"));
				resultsTextView.setGravity(Gravity.CENTER);
				ll.addView(resultsTextView);

			}

			for (int i = 0; i < resultsArray.length(); i++) {
				TextView resultsTextView = new TextView(ResultsActivity.this);
				resultsTextView.setId(i);
				StringBuilder finalDisplayString = new StringBuilder();
				JSONObject jsonObject = (JSONObject) resultsArray.get(i);
				finalDisplayString.append("<br><h3>Result " + (i + 1) + "</h3>");
				finalDisplayString.append("<b>ID: </b>" + jsonObject.getString("id") + "<br>");
				finalDisplayString.append("<b>DISTANCE: </b>" + jsonObject.getString("distanceFromUser") + "<br>");
				finalDisplayString.append("<b>CAPTION: </b>" + jsonObject.getString("caption") + "<br>");

				if (jsonObject.getString("cost").equals("0"))
					finalDisplayString.append("<b>PRICE: </b>" + "Unspecified" + " $<br>");
				else
					finalDisplayString.append("<b>PRICE: </b>" + jsonObject.getString("cost") + " $<br>");

				if (jsonObject.getString("condition").length() != 0)
					finalDisplayString.append("<b>CONDITION: </b>" + jsonObject.getString("condition") + "<br>");
				else
					finalDisplayString.append("<b>CONDITION: </b> Unspecified <br>");

				finalDisplayString.append("<b>AUDIO LINK: </b><a href='" + jsonObject.getString("media_source_audio") + "'>Download -> </a><br>");

				resultsTextView.setText(Html.fromHtml(finalDisplayString.toString()));
				resultsTextView.setMovementMethod(LinkMovementMethod.getInstance());
				ll.addView(resultsTextView);

				imageURL.add(jsonObject.getString("media_source_image"));
				Button imageButton = new Button(this);
				imageButton.setId(1000 + i);
				imageButton.setText("View Product Image");
				imageButton.setGravity(Gravity.CENTER_HORIZONTAL);
				imageButton.setWidth(100);
				imageButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent goToShowImageActivity = new Intent(getApplicationContext(), ShowImageActivity.class);
						String url = imageURL.get(v.getId() - 1000);
						Log.i("Results Activity IMGURL", url);
						goToShowImageActivity.putExtra("imageURL", url);
						startActivity(goToShowImageActivity);
					}
				});

				ll.addView(imageButton);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		this.setContentView(sv);

	}
}
