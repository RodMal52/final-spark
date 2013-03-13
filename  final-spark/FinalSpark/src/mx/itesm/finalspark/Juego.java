package mx.itesm.finalspark;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
import com.threed.jpct.Primitives;
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
	private Object3D enemigo1;
	private Object3D enemigo2;
	private Object3D enemigo3;
	private Object3D misil;
	private ArrayList<Object3D> arregloDeProyectiles;
	private ArrayList<Object3D> arregloDeEnemigos;
	private Enemy enemigo;
	private boolean agregarObjeto;

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
			agregarObjeto = true;
			return true;
		}

		if (evento.getAction() == MotionEvent.ACTION_UP) { // Termina touch

			

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

			if (agregarObjeto) {
				//Object3D obj = Modelo.cargarModeloMTL(getBaseContext(),
					//	"aquila.obj", "aquila.mtl", 5);
				Object3D misil  = Primitives.getCube((float) .5);
				misil.strip();
				misil.build();
				misil.setOrigin(objNave.getTransformedCenter());
				misil.translate(0,-2, 0);

				
				Object3D misilIzq  = Primitives.getCube((float) .5);
				misilIzq.strip();
				misilIzq.build();
				misilIzq.setOrigin(objNave.getTransformedCenter());
				misilIzq.translate(-9,1, 0);
				
				Object3D misilDer  = Primitives.getCube((float) .5);
				misilDer.strip();
				misilDer.build();
				misilDer.setOrigin(objNave.getTransformedCenter());
				misilDer.translate(9,1, 0);

				// Mover
				arregloDeProyectiles.add(misil);// Agrega a la el misileto
				arregloDeProyectiles.add(misilIzq);
				arregloDeProyectiles.add(misilDer);
				
				mundo.addObject(misil);
				mundo.addObject(misilIzq);
				mundo.addObject(misilDer);
				agregarObjeto = false;
			}

			// ACTUALIZAR cámara
			for (int contarObjetos = 0; contarObjetos < arregloDeProyectiles
					.size(); contarObjetos++) {
				Object3D proyectil = arregloDeProyectiles.get(contarObjetos);
				proyectil.rotateZ(0.1f);
				proyectil.translate(0, -5.0f, 0);
				if (proyectil.getTransformedCenter().y < -205) {
					mundo.removeObject(proyectil);
					arregloDeProyectiles.remove(contarObjetos);
				}


			}

			// ACTUALIZAR objetos

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
				if (objNave.getTransformedCenter().x > 100) {
					//objNave.translate((float) -0.5, 0, 0);
					if (offsetVertical < 0) {
						objNave.translate(offsetVertical, 0, 0);
						offsetVertical = 0;
					}
					
					if (offsetHorizontal != 0) {
						objNave.translate(0, offsetHorizontal, 0);
						offsetHorizontal = 0;
					}
				}
				if (objNave.getTransformedCenter().x < -100) {
					//objNave.translate((float) 0.5, 0, 0);
					if (offsetVertical > 0) {
						objNave.translate(offsetVertical, 0, 0);
						offsetVertical = 0;
					}
					if (offsetHorizontal != 0) {
						objNave.translate(0, offsetHorizontal, 0);
						offsetHorizontal = 0;
					}
				}
				if (objNave.getTransformedCenter().y < -148) {
					//objNave.translate(0, (float) .5, 0);
					if (offsetHorizontal > 0) {
						objNave.translate(0, offsetHorizontal, 0);
						offsetHorizontal = 0;
					}
					if (offsetVertical != 0) {
						objNave.translate(offsetVertical, 0, 0);
						offsetVertical = 0;
					}
				}
				if (objNave.getTransformedCenter().y > 155) {
					//objNave.translate(0, (float) -.5, 0);
					if (offsetHorizontal < 0) {
						objNave.translate(0, offsetHorizontal, 0);
						offsetHorizontal = 0;
					}
					if (offsetVertical != 0) {
						objNave.translate(offsetVertical, 0, 0);
						offsetVertical = 0;
					}
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

				//enemigo = new Enemy(1);
				
				enemigo1 = Primitives.getCone(6);
				enemigo2 = Primitives.getCube(9);
				enemigo3 = Primitives.getSphere(15);
				
				enemigo1.strip();
				enemigo1.build();
				float xa = (float)(Math.random()*100-50);
				float ya = (float)(Math.random()*100-80);
				enemigo1.translate(xa, ya, 0);
				mundo.addObject(enemigo1);
				
				enemigo2.strip();
				enemigo2.build();
				float xb = (float)(Math.random()*100-50);
				float yb = (float)(Math.random()*100-90);
				enemigo2.translate(xb, yb, 0);
				mundo.addObject(enemigo2);
				
				enemigo3.strip();
				enemigo3.build();
				float xc = (float)(Math.random()*100-50);
				float yc = (float)(Math.random()*100-60);
				enemigo3.translate(xc, yc, 0);
				mundo.addObject(enemigo3);
				
				// Carga el avi—n
				objNave = Modelo.cargarModeloMTL(getBaseContext(),
						"freedom3000.obj", "freedom3000.mtl", 1);
				objNave.rotateY(3.141592f);
				objNave.rotateX((float) (1.5));
				// carga el misil
				/*misil = Modelo
						.cargarModelo(getBaseContext(), "misil.obj", null);

				objNave.addChild(misil);
				misil.scale((float) .25);
				misil.setOrigin(objNave.getTransformedCenter());
				misil.translate(0, -15, 0);
				mundo.addObject(misil);*/

				mundo.addObject(objNave);
				
				

				arregloDeProyectiles = new ArrayList<Object3D>();// projectile//

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