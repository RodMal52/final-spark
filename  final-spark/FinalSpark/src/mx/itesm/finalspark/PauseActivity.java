package mx.itesm.finalspark;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class PauseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pause);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pause, menu);
		return true;
	}
	
	public void mostrarControls (View view){
		Intent intent = new Intent(this,ControlsActivity.class);
		this.startActivity(intent);
	}
	public void mostrarMenu (View view){
		Intent intent = new Intent(this,MenuActivity.class);
		this.startActivity(intent);
	}

}