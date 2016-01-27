En el proyecto se creó la clase DBAdapter.java. En ella se encuentran los métodos necesarios para poder crear un  base de dato y trabajar en ella. Lo único que hay que hacer para usarla es crear un objeto de BDAdapter en la clase que la necesita. Dejo un ejemplo abajo:

package mx.itesm.finalspark;


import android.app.Activity;

import android.database.Cursor;

import android.os.Bundle;

import android.widget.Toast;


public class DatabasesActivity extends Activity {

> / Llamado cuando la actividad es creada por primera vez /

> @Override

> public void onCreate(Bundle savedInstanceState) {

> super.onCreate(savedInstanceState);

> setContentView(R.layout.activity\_main);

> DBAdapter db = new DBAdapter(this);


> /

> //añadir un jugador

> db.open();

> long id = db.insertPlayer("Alejandro", 2600);

> id = db.insertPlayer(Rodrigo, 3000);

> db.close();

  * /


> //Obtener todos los jugadores

> db.open();

> Cursor c = db.getAllPlayers();

> if (c.moveToFirst())

> {

> do {

> DisplayPlayer(c);

> } while (c.moveToNext());

> }

> db.close();


> }


> public void DisplayPlayer(Cursor c)

> {

> Toast.makeText(this,

> "id: " + c.getString(0) + "\n" +

> "Name: " + c.getString(1) + "\n" +

> "Puntuación  " + c.getString(2),

> Toast.LENGTH\_LONG).show();

> }

}