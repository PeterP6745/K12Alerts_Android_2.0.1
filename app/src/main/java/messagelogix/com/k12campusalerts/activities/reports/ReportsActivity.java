package messagelogix.com.k12campusalerts.activities.reports;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.activities.CampaignStatusActivity;

/**
 * @author dipenp
 *
 */
public class ReportsActivity extends BaseActivity implements View.OnClickListener {

	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * Adding our layout to parent class frame layout.
		 */
		getLayoutInflater().inflate(R.layout.activity_reports, frameLayout);
		
		/**
		 * Setting title and itemChecked  
		 */
		mDrawerList.setItemChecked(position, true);
		setTitle(navDrawerItems.get(position).getTitle());

		Button emailButton = (Button) findViewById(R.id.emailButton);
		emailButton.setOnClickListener(this);

		Button smsButton = (Button) findViewById(R.id.smsButton);
		smsButton.setOnClickListener(this);

		Button phoneButton = (Button) findViewById(R.id.phoneButton);
		phoneButton.setOnClickListener(this);

		Button campButton = (Button) findViewById(R.id.camp_button);
		campButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CampaignStatusActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, ReportListActivity.class);
		switch(v.getId()) {
			case R.id.emailButton:
				intent.putExtra("type", "1");
				break;
			case R.id.smsButton:
				intent.putExtra("type", "2");
				break;
			case R.id.phoneButton:
				intent.putExtra("type", "3");
				break;
			default:
				break;
		}
		startActivity(intent);
	}
}
