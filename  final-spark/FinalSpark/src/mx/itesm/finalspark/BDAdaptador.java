package mx.itesm.finalspark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Clase que se encarga de crear y manejar la base de datos en SQLite en la que
 * se almacenaran las puntuaciones de los jugadores.
 */
public class BDAdaptador {
	static final String KEY_ROWID = "_id";
	static final String KEY_NOMBRE = "nombre";
	static final String KEY_SCORE = "score";
	static final String TAG = "DBAdapter";
	static final String DATABASE_NAME = "Score";
	static final String DATABASE_TABLE = "jugadores";
	static final int DATABASE_VERSION = 3;
	// Contiene la declaraciÃ³n para crear la base de datos.
	static final String DATABASE_CREATE = "create table jugadores (_id integer primary key autoincrement, "
			+ "nombre text not null, score int not null);";
	final Context context;
	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	/**
	 * Genera un adaptador que se encarga de pasar los parametros recibidos a la
	 * base de datos.
	 * 
	 * @param ctx Contexto en el cual se creara el adaptador.
	 */
	public BDAdaptador(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	/**
	 * Este metodo extiende SQLiteOpenHelper para manejar la creacion y
	 * actualizacion de version de la base de datos.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
		 * Crea base de datos en caso de no existir.
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Metodo llamado para actualizar la base de datos.
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Actualizando base de la versiÃ³n " + oldVersion
					+ " a la versiÃ³n " + newVersion
					+ ", que destruirÃ¡ la base anterior.");
			db.execSQL("DROP TABLE IF EXISTS jugadores");
			onCreate(db);
		}
	}

	/**
	 * Abre la base de datos
	 * 
	 * @throws SQLException
	 */
	public BDAdaptador abrir() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Cierra la base de datos
	 */
	public void cerrar() {
		DBHelper.close();
	}

	/**
	 * Insertar un contacto a la base de datos
	 * 
	 * @param nombre Nombre del jugador a guardar.
	 * @param score Puntuacion que obtuvo el jugador en esta partida.
	 * @return
	 */
	public long guardarPuntos(String nombre, int score) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOMBRE, nombre);
		initialValues.put(KEY_SCORE, score);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Eliminar un jugador particular de la base de datos
	 * 
	 * @param rowId ID de la linea que sera borrada.
	 * @return
	 */
	public boolean deletePlayer(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Obtiene los 10 primeros
	 * 
	 * @return Regresa una query con el nombre y puntuacion de un jugador.
	 * @throws SQLException
	 */
	public Cursor obtenerJugadores() throws SQLException {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NOMBRE,
				KEY_SCORE }, null, null, null, null, KEY_SCORE + " DESC", "10");
	}

}