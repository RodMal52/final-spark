package mx.itesm.finalspark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	    static final String KEY_ROWID = "_id";
	    static final String KEY_NOMBRE = "nombre";
	    static final String KEY_SCORE = "score";
	    static final String TAG = "DBAdapter";
	    static final String DATABASE_NAME = "Score";
	    static final String DATABASE_TABLE = "jugadores";
	    static final int DATABASE_VERSION = 3;
	    //Contiene la declaración para crear la base de datos.
	    static final String DATABASE_CREATE =
            "create table jugadores (_id integer primary key autoincrement, "
            + "nombre text not null, score int not null);";
        final Context context;
        DatabaseHelper DBHelper;
        SQLiteDatabase db;
        
        
        public DBAdapter(Context ctx)
        {
            this.context = ctx;
            DBHelper = new DatabaseHelper(context);
        }
        
        //Este método extiende SQLiteOpenHelper para manejar la creación y actualización
        //de versión de la base de datos.
        private static class DatabaseHelper extends SQLiteOpenHelper
        {
            DatabaseHelper(Context context)
            {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }
            
            //Crea base de datos en caso de no existir.
            @Override
            public void onCreate(SQLiteDatabase db)
            {
                try {
                    db.execSQL(DATABASE_CREATE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            //Método llamado para actualizar la base de datos.
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
            {
                Log.w(TAG, "Actualizando base de la versión " + oldVersion + " a la versión "
                        + newVersion + ", que destruirá la base anterior.");
                db.execSQL("DROP TABLE IF EXISTS jugadores");
                onCreate(db);
            }
        }
        //Abre la base de datos
        public DBAdapter open() throws SQLException 
        {
            db = DBHelper.getWritableDatabase();
            return this;
        }
        //Cierra la base de datos
        public void close() 
        {
            DBHelper.close();
        }
        
        //Insertar un contacto a la base de datos
        public long insertPlayer(String nombre, int score) 
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_NOMBRE, nombre);
            initialValues.put(KEY_SCORE, score);
            return db.insert(DATABASE_TABLE, null, initialValues);
        }        
        
        //Eliminar un jugador particular de la base de datos
        public boolean deletePlayer(long rowId) 
        {
            return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
        }
        
        //Obtiene los 10 primeros
        public Cursor getTen() throws SQLException 
        {        
            return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,
                   KEY_NOMBRE, KEY_SCORE}, null, null,null,null,KEY_SCORE+" DESC");
        }
        
}