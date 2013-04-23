package mx.itesm.finalspark;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends Activity {
	private int puntos;
	private EditText tfNombre;
	private TextView score;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
			
		puntos = getIntent().getExtras().getInt("Puntaje");
		score = (TextView) findViewById(R.id.score);
		score.setText("Score: "+puntos);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game_over, menu);
		return true;
	}
	public void mostrarMenu (View view){
		tfNombre = (EditText) findViewById(R.id.tfNombre);
		if (tfNombre.getText().length()!=0){
			Intent intent = new Intent(this,MenuActivity.class);
			BDAdaptador base = new BDAdaptador(this);
			base.abrir();
			base.guardarPuntos(tfNombre.getText().toString(), puntos);
			base.cerrar();
			this.startActivity(intent);
			this.finish();
			
		}else {
			Toast.makeText(this, "Please enter a valid username", Toast.LENGTH_LONG).show();
		}
		
	}
}
