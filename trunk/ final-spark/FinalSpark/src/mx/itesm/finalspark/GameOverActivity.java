package mx.itesm.finalspark;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class GameOverActivity extends Activity {
	private int puntos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		BDAdaptador base = new BDAdaptador(this);
		EditText tfNombre = (EditText) findViewById(R.id.tfNombre);
		
		puntos = getIntent().getExtras().getInt("Puntaje");
		base.abrir();
		Log.d("HOLAAA", tfNombre.getText().toString());
		base.guardarPuntos(""+tfNombre.getText().toString(), puntos);
		base.cerrar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game_over, menu);
		return true;
	}
	public void mostrarMenu (View view){
		Intent intent = new Intent(this,MenuActivity.class);
		
		this.startActivity(intent);
	}
}
