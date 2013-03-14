package mx.itesm.finalspark;

// Imports de paquetería de Java
import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//Imports de paquetería de Android
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
//Imports de paquetería Jpct-AE 
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

public class Juego extends Activity {
	private static Juego main;
	private GLSurfaceView mGLView; // Contenedor para dibujar
	private Renderer renderer; // El objeto que hace los trazos
	private FrameBuffer buffer; // Buffer para trazar
	private World mundo; // Un escenario de nuestro juego
	private RGBColor colorFondo; // Color de fondo :)
	private Camera camara; // Cámara
	private Object3D objNave; // Modelo de la nave
	@SuppressWarnings("unused")
	private Object3D misil; // Modelo del misil
	private Object3D objEnemigo; // Modelo enemigo
	private ArrayList<Object3D> arregloDeProyectiles; // Arreglo de misiles
	private ArrayList<Object3D> arregloDeEnemigos; // Arreglo de enemigos
	private boolean agregarObjeto; // Valor booleano para comprobar si se
									// agregan misiles
	private int fps; // contador frames
	private int contadorDañoEnemigo = 0; // HP enemigo
	private int contadorEnemigos = 0;
	private boolean enemigoMuerto = false;
	private boolean noMasEnemigos = true;
	@SuppressWarnings("unused")
	private float xPos = -1;
	@SuppressWarnings("unused")
	private float yPos = -1;
	private float offsetVertical = 0; // Izquierda-derecha
	private float offsetHorizontal = 0; // Arriba-abajo
	private int disparos = 0;
	
	public void mostrarGameOver (View view){
		Intent intent = new Intent(this,GameOverActivity.class);
		this.startActivity(intent);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------- Método onCreate()
	// -------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
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

	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------- Método onPause()
	// -------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------- Método onResume()
	// -------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
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

	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------- Método onStop()
	// -------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	@Override
	protected void onStop() {
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sm.unregisterListener(miListener);
		super.onStop();
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
		// -------------------------- Método onDestroy()
		// -------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------
	@Override 
	protected void onDestroy(){
		super.onDestroy();
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------- Método copiar()
	// -------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
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

	// **********************************************************************************************************************************
	// *********************** TOUCH
	// EVENT*********************************************************************************************
	// **********************************************************************************************************************************
	@Override
	public boolean onTouchEvent(MotionEvent evento) {
		if (evento.getAction() == MotionEvent.ACTION_DOWN) { // Inicia touch
			xPos = evento.getX();
			yPos = evento.getY();
			agregarObjeto = true;
			return true;
		}
		if (evento.getAction() == MotionEvent.ACTION_UP) { // Termina touch
			agregarObjeto = false;
			return true;
		}
		if (evento.getAction() == MotionEvent.ACTION_MOVE) { // Drag
			return true;
		}
		return super.onTouchEvent(evento);
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++++++++++++++++ CLASE RENDERER
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	class Renderer implements GLSurfaceView.Renderer {

		private long tiempo = System.currentTimeMillis();

		// --------------------------------------------------------------------------------------------------------------------------------------
		// -------------------------- Método onDrawFrame()
		// -------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------
		@Override
		public void onDrawFrame(GL10 gl) { // ACTUALIZACIONES

			// **********************************************************************************************************************************
			// *********************** MISILES
			// ***********************************************************************************************
			// **********************************************************************************************************************************

			disparos++;
			
			if (disparos > 3){
				disparos = 0;
			}
			
			if (agregarObjeto && (disparos == 0)) {

				Object3D misil = Primitives.getCube((float) .5);
				misil.strip();
				misil.build();
				misil.setOrigin(objNave.getTransformedCenter());
				misil.translate(0, -2, 0);

				Object3D misilIzq = Primitives.getCube((float) .5);
				misilIzq.strip();
				misilIzq.build();
				misilIzq.setOrigin(objNave.getTransformedCenter());
				misilIzq.translate(-9, 1, 0);

				Object3D misilDer = Primitives.getCube((float) .5);
				misilDer.strip();
				misilDer.build();
				misilDer.setOrigin(objNave.getTransformedCenter());
				misilDer.translate(9, 1, 0);
				arregloDeProyectiles.add(misil);// Agrega a la el misileto
				arregloDeProyectiles.add(misilIzq);
				arregloDeProyectiles.add(misilDer);

				mundo.addObject(misil);
				mundo.addObject(misilIzq);
				mundo.addObject(misilDer);
			}

			// **********************************************************************************************************************************
			// *********************** GENERACIÓN ALEATORIA DE ENEMIGOS
			// ***********************************************************************************************
			// **********************************************************************************************************************************

			if (noMasEnemigos) {

				Object3D enemigo1 = Primitives.getCube(7);
				enemigo1.strip();
				enemigo1.build();
				float xa = (float) (Math.random() * (100));
				float ya = (float) (Math.random() * (-130));
				enemigo1.translate(xa, ya, 0);

				Object3D enemigo2 = Primitives.getCube(7);
				enemigo2.strip();
				enemigo2.build();
				xa = (float) (Math.random() * -(100));
				ya = (float) (Math.random() * (-90));
				enemigo2.translate(xa, ya, 0);

				Object3D enemigo3 = Primitives.getCube(7);
				enemigo3.strip();
				enemigo3.build();
				xa = (float) (Math.random() * (50));
				ya = (float) (Math.random() * (-150));
				enemigo3.translate(xa, ya, 0);

				Object3D enemigo4 = Primitives.getCube(7);
				enemigo4.strip();
				enemigo4.build();
				xa = (float) (Math.random() * (40));
				ya = (float) (Math.random() * (-110));
				enemigo4.translate(xa, ya, 0);

				Object3D enemigo5 = Primitives.getCube(7);
				enemigo5.strip();
				enemigo5.build();
				xa = (float) (Math.random() * (50));
				ya = (float) (Math.random() * (-165));
				enemigo5.translate(xa, ya, 0);

				arregloDeEnemigos.add(enemigo1);// Agrega a la lista el enemigo
				arregloDeEnemigos.add(enemigo2);
				arregloDeEnemigos.add(enemigo3);
				arregloDeEnemigos.add(enemigo4);
				arregloDeEnemigos.add(enemigo5);
				mundo.addObject(enemigo1);
				mundo.addObject(enemigo2);
				mundo.addObject(enemigo3);
				mundo.addObject(enemigo4);
				mundo.addObject(enemigo5);

				contadorEnemigos = 5;

				noMasEnemigos = false;
			}

			// Revisa si los enemigos han muerto

			if (contadorEnemigos == 0) {
				noMasEnemigos = true;
			}

			for (Object3D cubo : arregloDeEnemigos) {
				cubo.rotateX(0.01f);
				cubo.rotateX(0.01f);
				cubo.rotateX(0.01f);
			}
			// Revisa si el proyectil ha salido del mundo o colisionado con
			// algún enemigo

			for (int contarObjetos = 0; contarObjetos < arregloDeProyectiles
					.size(); contarObjetos++) {

				Object3D proyectil = arregloDeProyectiles.get(contarObjetos);
				proyectil.rotateZ(0.1f);
				proyectil.translate(0, -5.0f, 0);

				if (!enemigoMuerto
						&& proyectil.getTransformedCenter().x < (objEnemigo
								.getTransformedCenter().x + 24)
						&& proyectil.getTransformedCenter().x > (objEnemigo
								.getTransformedCenter().x - 24)
						&& proyectil.getTransformedCenter().y > (objEnemigo
								.getTransformedCenter().y - 42)
						&& proyectil.getTransformedCenter().y < (objEnemigo
								.getTransformedCenter().y + 42)) {
					mundo.removeObject(proyectil);
					arregloDeProyectiles.remove(contarObjetos);
					contadorDañoEnemigo++;
				}

				for (int contarEnemigos = 0; contarEnemigos < arregloDeEnemigos
						.size(); contarEnemigos++) {
					if (proyectil.getTransformedCenter().x < (arregloDeEnemigos
							.get(contarEnemigos).getTransformedCenter().x + 10)
							&& proyectil.getTransformedCenter().x > (arregloDeEnemigos
									.get(contarEnemigos).getTransformedCenter().x - 10)
							&& proyectil.getTransformedCenter().y > (arregloDeEnemigos
									.get(contarEnemigos).getTransformedCenter().y - 10)
							&& proyectil.getTransformedCenter().y < (arregloDeEnemigos
									.get(contarEnemigos).getTransformedCenter().y + 10)) {
						mundo.removeObject(proyectil);
						arregloDeProyectiles.remove(contarObjetos);
						mundo.removeObject(arregloDeEnemigos
								.get(contarEnemigos));
						arregloDeEnemigos.remove(contarEnemigos);
						contadorEnemigos--;
					}
				}

				if (proyectil.getTransformedCenter().y < -205) {
					mundo.removeObject(proyectil);
					arregloDeProyectiles.remove(contarObjetos);
				}
			}

			// Revisa si el enemigo debe morir y si la nave ha chocado con él

			if (!enemigoMuerto && contadorDañoEnemigo > 45) {
				mundo.removeObject(objEnemigo);
				enemigoMuerto = true;
			}

			// **********************************************************************************************************************************
			// *********************** COLISION CON Enemigo
			// *********************************************************************************
			// **********************************************************************************************************************************

			if (!enemigoMuerto
					&& (objNave.getTransformedCenter().x) < (objEnemigo.getTransformedCenter().x + 24)
					&& (objNave.getTransformedCenter().x) > (objEnemigo.getTransformedCenter().x - 24)
					&& (objNave.getTransformedCenter().y) > (objEnemigo.getTransformedCenter().y - 52)
					&& (objNave.getTransformedCenter().y) < (objEnemigo.getTransformedCenter().y + 52)) {
				View view = null;
				mostrarGameOver(view);
				main = null;				
				
				//finish();
			}

			// **********************************************************************************************************************************
			// *********************** MOVIMIENTO NAVE Y COLISION CON BORDES
			// *****************************************************************
			// **********************************************************************************************************************************

			if (objNave.getTransformedCenter().x < 100
					&& objNave.getTransformedCenter().x > -100
					&& objNave.getTransformedCenter().y < 155
					&& objNave.getTransformedCenter().y > -148) {
				if (offsetVertical != 0) {
					objNave.translate(0, offsetVertical, 0);
					offsetVertical = 0;
				}

				if (offsetHorizontal != 0) {
					objNave.translate(offsetHorizontal, 0, 0);
					offsetHorizontal = 0;
				}

			} else {

				if (objNave.getTransformedCenter().x > 100) {

					if (offsetHorizontal < 0) {
						objNave.translate(offsetHorizontal, 0, 0);
						offsetHorizontal = 0;
					}

					if (objNave.getTransformedCenter().y < -148) {

						if (offsetVertical > 0) {
							objNave.translate(0, offsetVertical, 0);
							offsetVertical = 0;
						}

					} else if (objNave.getTransformedCenter().y > 155) {

						if (offsetVertical < 0) {
							objNave.translate(0, offsetVertical, 0);
							offsetVertical = 0;
						}

					} else {

						if (offsetVertical != 0) {
							objNave.translate(0, offsetVertical, 0);
							offsetVertical = 0;
						}

					}

				}

				// ---------------------------------------------------------------------------

				if (objNave.getTransformedCenter().x < -100) {

					if (offsetHorizontal > 0) {
						objNave.translate(offsetHorizontal, 0, 0);
						offsetHorizontal = 0;
					}

					if (objNave.getTransformedCenter().y < -148) {

						if (offsetVertical > 0) {
							objNave.translate(0, offsetVertical, 0);
							offsetVertical = 0;
						}

					} else if (objNave.getTransformedCenter().y > 155) {

						if (offsetVertical < 0) {
							objNave.translate(0, offsetVertical, 0);
							offsetVertical = 0;
						}

					} else {

						if (offsetVertical != 0) {
							objNave.translate(0, offsetVertical, 0);
							offsetVertical = 0;
						}

					}

				}

				// ---------------------------------------------------------------------------

				if (objNave.getTransformedCenter().y < -148) {

					if (offsetVertical > 0) {
						objNave.translate(0, offsetVertical, 0);
						offsetVertical = 0;
					}

					if (objNave.getTransformedCenter().x > 100) {

						if (offsetHorizontal < 0) {
							objNave.translate(offsetHorizontal, 0, 0);
							offsetHorizontal = 0;
						}

					} else if (objNave.getTransformedCenter().x < -100) {

						if (offsetHorizontal > 0) {
							objNave.translate(offsetHorizontal, 0, 0);
							offsetHorizontal = 0;
						}

					} else {

						if (offsetHorizontal != 0) {
							objNave.translate(offsetHorizontal, 0, 0);
							offsetHorizontal = 0;
						}

					}

				}

				// ---------------------------------------------------------------------------

				if (objNave.getTransformedCenter().y > 155) {
					if (offsetVertical < 0) {
						objNave.translate(0, offsetVertical, 0);
						offsetVertical = 0;
					}

					if (objNave.getTransformedCenter().x > 100) {

						if (offsetHorizontal < 0) {
							objNave.translate(offsetHorizontal, 0, 0);
							offsetHorizontal = 0;
						}

					} else if (objNave.getTransformedCenter().x < -100) {

						if (offsetHorizontal > 0) {
							objNave.translate(offsetHorizontal, 0, 0);
							offsetHorizontal = 0;
						}

					} else {
						if (offsetHorizontal != 0) {
							objNave.translate(offsetHorizontal, 0, 0);
							offsetHorizontal = 0;
						}
					}
				}
			}

			// **********************************************************************************************************************************
			// *********************** BUFFER
			// ***********************************************************************************************
			// **********************************************************************************************************************************
			buffer.clear(colorFondo); // Borrar el buffer
			mundo.renderScene(buffer);// Cálculos sobre los objetos a dibujar
			mundo.draw(buffer); // Redibuja todos los objetos
			buffer.display(); // Actualiza en pantalla

			// **********************************************************************************************************************************
			// *********************** CONTADOR FPS
			// ***********************************************************************************************
			// **********************************************************************************************************************************
			if (System.currentTimeMillis() - tiempo > 1000) {
				Log.d("FPS", fps + "fps");
				tiempo = System.currentTimeMillis();
				fps = 0;
			}
			fps++;
		}

		// --------------------------------------------------------------------------------------------------------------------------------------
		// -------------------------- Método onSurfaceChanged()
		// -------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			if (buffer != null) {
				buffer.dispose();
			}
			buffer = new FrameBuffer(gl, width, height);
			if (main == null) {
				// **********************************************************************************************************************************
				// *********************** CRECIÓN DE MUNDO Y LUZ
				// ******************************************************************************
				// **********************************************************************************************************************************
				mundo = new World();
				mundo.setAmbientLight(255, 255, 255);
				colorFondo = new RGBColor(0x0, 0x0, 0x0);

				// **********************************************************************************************************************************
				// *********************** CARGA DEL MODELO DE LA NAVE
				// ****************************************************************************
				// **********************************************************************************************************************************
				objNave = Modelo.cargarModeloMTL(getBaseContext(),
						"freedom3000.obj", "freedom3000.mtl", 1);
				objNave.rotateY(3.141592f);
				objNave.rotateX((float) (1.5));
				mundo.addObject(objNave);
				arregloDeProyectiles = new ArrayList<Object3D>();// Inicializa
																	// arreglo
																	// de
																	// proyectiles

				// **********************************************************************************************************************************
				// *********************** CARGA DEL MODELO DE ENEMIGOS
				// ****************************************************************************
				// **********************************************************************************************************************************
				objEnemigo = Modelo.cargarModeloMTL(getBaseContext(),
						"robot.obj", "robot.mtl", (float) 0.03);
				objEnemigo.rotateY(3.141592f);
				objEnemigo.rotateZ(3.141592f);
				objEnemigo.rotateX((float) (-1.5));
				objEnemigo.translate(0, -120, 0);
				mundo.addObject(objEnemigo);

				arregloDeEnemigos = new ArrayList<Object3D>();// Inicializa
																// arreglo de
																// enemigos
				// **********************************************************************************************************************************
				// *********************** MANEJO DE CÁMARA
				// ***********************************************************************************************
				// **********************************************************************************************************************************
				camara = mundo.getCamera();
				camara.moveCamera(Camera.CAMERA_MOVEOUT, 200);
				camara.moveCamera(Camera.CAMERA_MOVEUP, 3);
				// **********************************************************************************************************************************
				// *********************** MEMORIA
				// ***********************************************************************************************
				// **********************************************************************************************************************************
				MemoryHelper.compact();
				if (main == null) {
					main = Juego.this;
				}
			}

		}

		// --------------------------------------------------------------------------------------------------------------------------------------
		// -------------------------- Método onSurfaceChanged()
		// -------------------------------------------------------------------------------------
		// --------------------------------------------------------------------------------------------------------------------------------------

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		}
	}

	// **********************************************************************************************************************************
	// ********************** ACELERÓMETRO
	// ******************************************************************************************
	// **********************************************************************************************************************************
	final SensorEventListener miListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				offsetVertical = (float) (event.values[0] / 0.5);
				offsetHorizontal = (float) (event.values[1] / .5);
				break;
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

}