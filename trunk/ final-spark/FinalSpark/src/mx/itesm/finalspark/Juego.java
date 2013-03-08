package mx.itesm.finalspark;

import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class Juego extends Activity {
	private static Juego main; 
	private GLSurfaceView mGLView; // Contenedor para dibujar
	private Renderer renderer; // El objeto que hace los trazos
	private FrameBuffer buffer; // Buffer para trazar
	private World mundo; // Un escenario de nuestro juego
	private RGBColor colorFondo; // Color de fondo :)
	private Camera camara;

	// Agregamos objetos en el mundo
	private Object3D objAvion;
	private Object3D misil;

	// Para guardar el desplazamiento del touch (pixeles)
	private float xPos = -1;
	private float yPos = -1;
	// Longitud de desplazamiento
	private float offsetHorizontal = 0; // Izquierda-derecha
	private float offsetVertical = 0; // Arriba-abajo
	
	//contador frames
	private int fps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (main != null) { // Si ya existe el juego, copiar sus campos
			copiar(main);
		}
		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplicationContext());
		renderer = new Renderer();
		mGLView.setRenderer(renderer);
		setContentView(mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
		
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sm.registerListener(miListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sm.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onStop() {
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sm.unregisterListener(miListener);
		super.onStop();
	}

	private void copiar(Object src) {
		try {
			Field[] fs = src.getClass().getDeclaredFields();
			for (Field f : fs) {
				f.setAccessible(true);
				f.set(this, f.get(src));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// Hay un evento de touch en la pantalla
	@Override
	public boolean onTouchEvent(MotionEvent evento) {
		
		if (evento.getAction() == MotionEvent.ACTION_DOWN) { // Inicia touch
			xPos = evento.getX();
			yPos = evento.getY();
			return true;
		}

		if (evento.getAction() == MotionEvent.ACTION_UP) { // Termina touch
			xPos = -1;
			yPos = -1;
			offsetHorizontal = 0;
			offsetVertical = 0;
			return true;
		}

		if (evento.getAction() == MotionEvent.ACTION_MOVE) { // Drag
			float xd = evento.getX() - xPos;
			float yd = evento.getY() - yPos;

			xPos = evento.getX();
			yPos = evento.getY();

			offsetHorizontal = xd / -100f;
			offsetVertical = yd / -100f;
			return true;
		}

		return super.onTouchEvent(evento);
	
	}

	class Renderer implements GLSurfaceView.Renderer {
		
		private long tiempo = System.currentTimeMillis();

		@Override
		public void onDrawFrame(GL10 gl) { // ACTUALIZACIONES

			// ACTUALIZAR objetos

			if (offsetHorizontal != 0) {
				objAvion.translate(0,offsetHorizontal,0);

				offsetHorizontal = 0;
			} else {
				objAvion.translate(0, 0, 0.1f);
				
			}

			if (offsetVertical != 0) {
				objAvion.translate(offsetVertical,0,0);
				offsetVertical = 0;
			}

			// final
			buffer.clear(colorFondo); // Borrar el buffer
			mundo.renderScene(buffer);// C�lculos sobre los objetos a dibujar
			mundo.draw(buffer); // Redibuja todos los objetos
			buffer.display(); // Actualiza en pantalla
			
			
			//cuenta fps
			if (System.currentTimeMillis()-tiempo >1000){
				Log.d("FPS", fps+"fps");
				tiempo= System.currentTimeMillis();
				fps=0;
			}
			fps ++;
			
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {

			if (buffer != null) {
				buffer.dispose();
			}

			buffer = new FrameBuffer(gl, width, height);

			if (main == null) {

				mundo = new World();
				mundo.setAmbientLight(128, 128, 128);
				colorFondo = new RGBColor(0x0, 0x0, 0x0);


				// Carga el avi�n
				objAvion = Modelo.cargarModeloMTL(getBaseContext(),
						"freedom3000.obj", "freedom3000.obj.mtl",1);
				
				//carga el misil
				misil = Modelo.cargarModelo(getBaseContext(),
						"misil.obj", null);
				objAvion.addChild(misil);
				misil.scale((float) .25);
				misil.setOrigin(objAvion.getTransformedCenter());
				misil.translate(0, -15, 0);
				// Lo rota para que quede en la posici�n correcta
				objAvion.rotateY(3.141592f);
				objAvion.rotateX((float) (1.5));
				mundo.addObject(objAvion);
				mundo.addObject(misil);

				// Para manejar la c�mara
				camara = mundo.getCamera();
				camara.moveCamera(Camera.CAMERA_MOVEOUT, 200); 
				camara.moveCamera(Camera.CAMERA_MOVEUP, 3);

				MemoryHelper.compact();
				if (main == null) {
					main = Juego.this;
				}
			}
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		}

	}
	
	//Crear Listener para acelerometro
	final SensorEventListener miListener = new SensorEventListener() {
				
		@Override
		public void onSensorChanged(SensorEvent event) {
			switch (event.sensor.getType()){
			case Sensor.TYPE_ACCELEROMETER:
				offsetHorizontal = event.values [0]/2;
				offsetVertical = event.values [1]/2;
				break;
			}
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
	};
}