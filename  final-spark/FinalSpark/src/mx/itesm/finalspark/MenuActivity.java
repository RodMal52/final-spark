package mx.itesm.finalspark;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

/**
 * Muestra el menu principal del juego.
 */
public class MenuActivity extends Activity {

	@Override
	public void onBackPressed() {
	}
	
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
	
	/**
	 * Lleva al usuario a la pantalla de configuracion.
	 */
	public void mostrarSettings (View view){
		Intent intent = new Intent(this,SettingsActivity.class);
		this.startActivity(intent);
	}

	/**
	 * Lleva al usuario a la pantalla de acerca de.
	 */
	public void mostrarAbout (View view){
		Intent intent = new Intent(this,AboutActivity.class);
		this.startActivity(intent);
	}

	/**
	 * Lleva al usuario a la pantalla de puntuaciones mas altas.
	 */
	public void mostrarHighscores (View view){
		Intent intent = new Intent(this,HighscoresActivity.class);
		this.startActivity(intent);
	}
	
	/**
	 * Lleva al usuario a la pantalla de controles, que posteriormente lo llevara al juego.
	 */
	public void mostrarControls (View view){
		Intent intent = new Intent(this,ControlsActivity.class);
		this.startActivity(intent);
		
	}
}
