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
	final ArrayList<String> lista = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscores);
		BDAdaptador bases = new BDAdaptador(this);

		bases.abrir();
		Cursor c = bases.obtenerJugadores();
		if (c.moveToFirst()) {

			for (int i = 1; i <= 10; i++) {
				lista.add(i + ".- \t" + c.getString(1) + "\t\t\t"
						+ c.getString(2));
				Desplegar(c);
				c.moveToNext();
				
			}
		}
		bases.cerrar();
	}

	public void Desplegar(Cursor c) {
		ListView lvlista = (ListView) findViewById(R.id.lista);
		lvlista.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lista));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_highscores, menu);
		return true;
	}

}
