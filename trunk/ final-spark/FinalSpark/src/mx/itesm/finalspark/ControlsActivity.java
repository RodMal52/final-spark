package mx.itesm.finalspark;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.util.Log;
import android.view.Menu;
import android.view.View;

/**
 * Muestra la pantalla de la historia y controles del juego antes de ejecutar el mismo. 
 */
public class ControlsActivity extends Activity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controls);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_controls, menu);
		return true;
	}
	
	/**
	 * Inicia la actividad en la que se ubica el juego.
	 * 
	 * @param view
	 */
	public void mostrarGame (View view){
		Intent intent = new Intent(this,Juego.class);

		startActivity(intent);
		finish();
	}}
	
	
	
