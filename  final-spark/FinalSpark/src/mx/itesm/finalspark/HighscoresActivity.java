package mx.itesm.finalspark;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
		ListView lvlista = (ListView) findViewById(R.id.lista);
		final ArrayList<String> lista = new ArrayList<String>();
		for (int i = 0; i <= 10; ++i) {
		      lista.add(c.getString(1)+c.getString(2));
		    }
		lvlista.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista));
		
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
