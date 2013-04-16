package mx.itesm.finalspark;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class GameOverActivity extends Activity {
	private int puntos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		BDAdaptador base = new BDAdaptador(this);
		
		base.abrir();
		long id = base.guardarPuntos("Alejandro", puntos);
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
		puntos = getIntent().getIntExtra("Puntaje", 0);
		this.startActivity(intent);
	}
}
