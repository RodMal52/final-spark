package mx.itesm.finalspark;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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
	
	private TextView textSensitivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		SharedPreferences preferencias = getSharedPreferences("sensibilidad", Context.MODE_PRIVATE);
		boolean hayRegistro = preferencias.contains("sensibilidadGuardada");
		Log.d("Booleano", "" + hayRegistro);
		int sensibilidadObtenida = preferencias.getInt("sensibilidadGuardada", 50);
		//Log.d("Preferencia", "Sensitivity: " + sensibilidadObtenida + "%");
		
		seekBar = (SeekBar)findViewById(R.id.accelBar);
		seekBar.setOnSeekBarChangeListener(this);
		
		textSensitivity = (TextView)findViewById(R.id.textViewSensitivity);
		textSensitivity.setText("Sensitivity: " + sensibilidadObtenida + "%");
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
		
		textSensitivity.setText("Sensitivity: " + progress + "%");
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	public void guardarSensibilidad () {
	
		int sensibilidadFinal = seekBar.getProgress();
		SharedPreferences preferencias = getSharedPreferences("marcadores", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferencias.edit();
		editor.putInt("sensibilidadGuardada", sensibilidadFinal);
		editor.commit();
		
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		//SharedPreferences preferencias = getSharedPreferences("sensibilidad", Context.MODE_PRIVATE);
		
		Log.d("Sensibilidad", "Sensitivity: " + seekBar.getProgress());
	}

}
