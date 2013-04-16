package mx.itesm.finalspark;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.widget.Toast;

public class HighscoresActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscores);
BDAdaptador bases = new BDAdaptador(this);
		
		bases.abrir();
		Cursor c = bases.obtenerJugadores();
		if(c.moveToFirst())
		{
			do {
				Desplegar(c);
			} while (c.moveToNext());
		}
		bases.cerrar();
	}
	
	public void Desplegar(Cursor c)
	{
        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                "Jugador: " + c.getString(1) + "\n" +
                "Puntaje:  " + c.getString(2),
                Toast.LENGTH_LONG).show();
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_highscores, menu);
		return true;
	}

}
