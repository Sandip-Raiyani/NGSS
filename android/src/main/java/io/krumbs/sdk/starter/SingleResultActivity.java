package io.krumbs.sdk.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SingleResultActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ScrollView sv = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);

		Bundle bundle = getIntent().getExtras();

		ll.setOrientation(LinearLayout.VERTICAL);

		TextView textView = new TextView(getApplicationContext());
		textView.append(bundle.getString("singleResult"));
		ll.addView(textView);

		sv.addView(ll);
		setContentView(sv);

	}
}
