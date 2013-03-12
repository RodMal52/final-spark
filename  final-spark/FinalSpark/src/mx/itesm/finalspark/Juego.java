package mx.itesm.finalspark;

import java.lang.reflect.Field;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class Juego extends Activity {
	private static Juego main;
	private GLSurfaceView mGLView; // Contenedor para dibujar
	private Renderer renderer; // El objeto que hace los trazos
	private FrameBuffer buffer; // Buffer para trazar
	private World mundo; // Un escenario de nuestro juego
	private RGBColor colorFondo; // Color de fondo :)
	private Camera camara;

	// Agregamos objetos en el mundo
	private Object3D objNave;
	private Object3D misil;

	// Para guardar el desplazamiento del touch (pixeles)
	@SuppressWarnings("unused")
	private float xPos = -1;
	@SuppressWarnings("unused")
	private float yPos = -1;
	// Longitud de desplazamiento
	private float offsetHorizontal = 0; // Izquierda-derecha
	private float offsetVertical = 0; // Arriba-abajo

	// contador frames
	private int fps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (main != null) { // Si ya existe el juego, copiar sus campos
			copiar(main);
		}
		super.onCreate(savedInstanceState);
		// Pantalla completa
		requestWindowFeature(Window.FEATURE_NO_TITLE); // Título de la Actividad
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // Barra de estado
		// *********************************
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
		sm.registerListener(miListener,
				sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
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
			// SimpleVector position = objNave.getTransformedCenter();
			// Log.d("position", position + "");
			return true;
		}

		if (evento.getAction() == MotionEvent.ACTION_UP) { // Termina touch
			xPos = -1;
			yPos = -1;
			return true;
		}

		if (evento.getAction() == MotionEvent.ACTION_MOVE) { // Drag

			return true;
		}

		return super.onTouchEvent(evento);

	}

	class Renderer implements GLSurfaceView.Renderer {

		private long tiempo = System.currentTimeMillis();

		@Override
		public void onDrawFrame(GL10 gl) { // ACTUALIZACIONES

			// ACTUALIZAR objetos
			boolean flagOutOfBounds = false;

			if (objNave.getTransformedCenter().x < 100
					&& objNave.getTransformedCenter().x > -100
					&& objNave.getTransformedCenter().y < 155
					&& objNave.getTransformedCenter().y > -148) {
				if (offsetHorizontal != 0) {
					objNave.translate(0, offsetHorizontal, 0);
					offsetHorizontal = 0;
				}

				if (offsetVertical != 0) {
					objNave.translate(offsetVertical, 0, 0);
					offsetVertical = 0;
				}

			} else {
				flagOutOfBounds = true;
			}

			if (flagOutOfBounds) {
				if (objNave.getTransformedCenter().x > 100) {
					objNave.translate((float) -0.5, 0, 0);
					flagOutOfBounds = false;
				}
				if (objNave.getTransformedCenter().x < -100) {
					objNave.translate((float) 0.5, 0, 0);
					flagOutOfBounds = false;
				}
				if (objNave.getTransformedCenter().y < -148) {
					objNave.translate(0, (float) .5, 0);
					flagOutOfBounds = false;
				}
				if (objNave.getTransformedCenter().y > 155) {
					objNave.translate(0, (float) -.5, 0);
					flagOutOfBounds = false;
				}

			}

			// final
			buffer.clear(colorFondo); // Borrar el buffer
			mundo.renderScene(buffer);// C‡lculos sobre los objetos a dibujar
			mundo.draw(buffer); // Redibuja todos los objetos
			
			buffer.display(); // Actualiza en pantalla

			// cuenta fps
			if (System.currentTimeMillis() - tiempo > 1000) {
				Log.d("FPS", fps + "fps");
				tiempo = System.currentTimeMillis();
				fps = 0;
			}
			fps++;

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

				// Carga el avi—n
				objNave = Modelo.cargarModeloMTL(getBaseContext(),
						"freedom3000.obj", "freedom3000.obj.mtl", 1);
				objNave.rotateY(3.141592f);
				objNave.rotateX((float) (1.5));
				// carga el misil
				misil = Modelo
						.cargarModelo(getBaseContext(), "misil.obj", null);

				objNave.addChild(misil);
				misil.scale((float) .25);
				misil.setOrigin(objNave.getTransformedCenter());
				misil.translate(0, -15, 0);
				mundo.addObject(misil);

				mundo.addObject(objNave);

				// Para manejar la c‡mara
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

	// Crear Listener para acelerometro
	final SensorEventListener miListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				offsetHorizontal = (float) (event.values[0] / 0.5);
				offsetVertical = (float) (event.values[1] / .5);
				break;
			}

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};
}