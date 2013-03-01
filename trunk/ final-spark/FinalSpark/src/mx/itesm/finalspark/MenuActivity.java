package mx.itesm.finalspark;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}
	
	public void mostrarSettings (View view){
		Intent intent = new Intent(this,SettingsActivity.class);
		this.startActivity(intent);
	}
	
	public void mostrarAbout (View view){
		Intent intent = new Intent(this,AboutActivity.class);
		this.startActivity(intent);
	}

	public void mostrarHighscores (View view){
		Intent intent = new Intent(this,HighscoresActivity.class);
		this.startActivity(intent);
	}
	public void mostrarControls (View view){
		Intent intent = new Intent(this,ControlsActivity.class);
		this.startActivity(intent);
	}
}
