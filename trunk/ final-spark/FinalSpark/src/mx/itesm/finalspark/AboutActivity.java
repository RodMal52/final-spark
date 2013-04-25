package mx.itesm.finalspark;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * Muestra la pantalla con los datos acerca de los creadores del juego 
 * la materia para la que fue hecho y la institución a la que pertenecen.
 */
public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_about, menu);
		return true;
	}

}
