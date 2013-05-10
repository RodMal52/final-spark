package mx.itesm.finalspark;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Muestra la pantalla para ajustar la sensibilidad del acelerometro.
 */
public class SettingsActivity extends Activity implements OnSeekBarChangeListener{

	private SeekBar seekBar;
	private int sensibilidad;
	private TextView textSensitivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		/*SharedPreferences preferencias = getSharedPreferences("sensibilidadObtenida", Context.MODE_PRIVATE);
		boolean hayRegistro = preferencias.contains("sensibilidadGuardada");
		Log.d("Booleano", "" + hayRegistro);
		int sensibilidadObtenida = preferencias.getInt("sensibilidadGuardada", 50);*/
		//Log.d("Preferencia", "Sensitivity: " + sensibilidadObtenida + "%");
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		sensibilidad = preferences.getInt("sensibilidad", 50);
		seekBar = (SeekBar)findViewById(R.id.accelBar);
		seekBar.setProgress(sensibilidad);
		seekBar.setOnSeekBarChangeListener(this);
		
			
		textSensitivity = (TextView)findViewById(R.id.textViewSensitivity);
		textSensitivity.setText("Accelerometer Sensitivity: " + sensibilidad + "%");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		
		textSensitivity.setText("Accelerometer Sensitivity: " + progress + "%");
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	/*public void guardarSensibilidad () {
	
		int sensibilidadFinal = seekBar.getProgress();
		SharedPreferences preferencias = getSharedPreferences("marcadores", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferencias.edit();
		editor.putInt("sensibilidadGuardada", sensibilidadFinal);
		editor.commit();
		
	}*/
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		//SharedPreferences preferencias = getSharedPreferences("sensibilidad", Context.MODE_PRIVATE);
		sensibilidad = seekBar.getProgress();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		  SharedPreferences.Editor editor = preferences.edit();
		  editor.putInt("sensibilidad", sensibilidad);
		  editor.commit();
		Log.d("Sensibilidad", "Accelerometer Sensitivity: " + seekBar.getProgress());
	}

}