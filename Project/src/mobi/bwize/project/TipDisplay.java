package mobi.bwize.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TipDisplay extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tip);
		
		Intent intent=getIntent();
		
		TextView title=(TextView) findViewById(R.id.tipTitle);
		TextView tip=(TextView) findViewById(R.id.tipBody);
		
		title.setText(intent.getStringExtra("title"));
		tip.setText(intent.getStringExtra("tip"));
	}
}
