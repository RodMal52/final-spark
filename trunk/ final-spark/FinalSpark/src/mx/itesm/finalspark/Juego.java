package mx.itesm.finalspark;

// Imports de paqueter�a de Java
import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
//Imports de paqueter�a de Android
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
//Imports de paqueter�a Jpct-AE 
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;

import com.threed.jpct.util.MemoryHelper;

public class Juego extends Activity implements SensorEventListener {
	private static Juego main;
	private int contador;
	private GLSurfaceView mGLView; // Contenedor para dibujar
	private Renderer renderer; // El objeto que hace los trazos
	private FrameBuffer buffer; // Buffer para trazar
	private World mundo; // Un escenario de nuestro juego
	private RGBColor colorFondo; // Color de fondo :)
	private Camera camara; // C�mara
	private Object3D objEnemigo; // Modelo enemigo
	private Object3D background;
	private boolean agregarObjeto; // Valor booleano para comprobar si se agregan misiles
	private MediaPlayer player;
	private Jugador jugador;
	private Enemigo enemigo;
	private int fps; // contador frames
	private int contadorDanoEnemigo = 0; // HP enemigo
	private int contadorEnemigos = 0;
	private boolean enemigoMuerto = false;
	private boolean noMasEnemigos = true;
	private boolean hayJefe = false;
	@SuppressWarnings("unused")
	private float xPos = -1;
	@SuppressWarnings("unused")
	private float yPos = -1;
	private float offsetVertical = 0; // Izquierda-derecha
	private float offsetHorizontal = 0; // Arriba-abajo
	private int disparos = 0;
	private int puntaje = 0;
	private int puntajeFinal = 0;
	private Bitmap bitmap;
	private Canvas canvas;
	private Paint p;
	private int[] pixeles; // sustituye la textura
	private ProgressDialog dialogoEspera; // dialogo de espera

	public void mostrarGameOver(View view) {
		Intent intent = new Intent(this, GameOverActivity.class);
		intent.putExtra("Puntaje", puntajeFinal);

		this.startActivity(intent);
	}

	// -------------------------- M�todo onCreate()
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (main != null ) { // Si ya existe el juego, copiar sus campos
			copiar(main);
		} else {
			dialogoEspera = ProgressDialog.show(this, "Final Spark",
					"Cargando...");
		}
		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplicationContext());
		bitmap = Bitmap.createBitmap(256, 64, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		p = new Paint();
		pixeles = new int[256 * 64 * 4];
		renderer = new Renderer();
		mGLView.setRenderer(renderer);
		setContentView(mGLView);
	}

	// -------------------------- M�todo onPause()
	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	// -------------------------- M�todo onResume()
	@SuppressWarnings("static-access")
	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sm.registerListener(this,
				sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sm.SENSOR_DELAY_GAME);
		Log.d("ACELEROMETRO", "REGISTRA");

	}

	// -------------------------- M�todo onStop()
	@Override
	protected void onStop() {
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

		sm.unregisterListener(this);
		if (player != null && player.isPlaying()) {
			player.stop();
			player.release();
		}
		super.onStop();
		Log.d("ACELEROMETRO", "DESREGISTRA");

	}

	// -------------------------- M�todo onDestroy()
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// -------------------------- M�todo copiar()
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

	// *********************** TOUCH
	// EVENT*************************************************************************************
	@Override
	public boolean onTouchEvent(MotionEvent evento) {
		if (evento.getAction() == MotionEvent.ACTION_DOWN) { // Inicia touch
			xPos = evento.getX();
			yPos = evento.getY();
			agregarObjeto = true;
			disparos = 0;
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

	// ++++++++++++++++++++++++++++++++++++++ CLASE RENDERER
	class Renderer implements GLSurfaceView.Renderer {

		private long tiempo = System.currentTimeMillis();

		// -------------------------- M�todo onDrawFrame()
		@Override
		public void onDrawFrame(GL10 gl) { // ACTUALIZACIONES

			if (puntaje == 300) {
				objEnemigo = Modelo.cargarModelo(getBaseContext(), "mantis.obj", null);
				objEnemigo.rotateX(3.141592f/2);
				objEnemigo.scale(0.25f);
				objEnemigo.translate(0, -90, 0);
				mundo.addObject(objEnemigo);
				hayJefe = true;
				puntaje = 0;
				enemigoMuerto = false;
				contadorDanoEnemigo = 0;
			}

			// *********************** MISILES

			disparos++;

			if (disparos > 3) {
				disparos = 0;
			}

			if (agregarObjeto && (disparos == 0)) {
				jugador.disparar();
				mundo.addObject(jugador.misil);
				mundo.addObject(jugador.misilIzq);
				mundo.addObject(jugador.misilDer);
			}

			// *********************** GENERACION ALEATORIA DE ENEMIGOS

			if (noMasEnemigos) {
				enemigo.generarEnemigos();
				mundo.addObject(enemigo.getEnemigo1());
				mundo.addObject(enemigo.getEnemigo2());
				mundo.addObject(enemigo.getEnemigo3());
				mundo.addObject(enemigo.getEnemigo4());
				mundo.addObject(enemigo.getEnemigo5());

				contadorEnemigos = 5;

				noMasEnemigos = false;
			}
			// Revisa si los enemigos han muerto
			if (contadorEnemigos == 0) {
				noMasEnemigos = true;
			}

			for (Object3D cubo : enemigo.arregloDeEnemigos) {
				cubo.rotateX(0.01f);
				cubo.rotateX(0.01f);
				cubo.rotateX(0.01f);
				cubo.translate((jugador.getObjNave().getTransformedCenter().x)
						/ 50 - (cubo.getTransformedCenter().x) / 50,
						(jugador.getObjNave().getTransformedCenter().y) / 50
								- (cubo.getTransformedCenter().y) / 50, 0);

				if ((jugador.getObjNave().getTransformedCenter().x) < (cubo
						.getTransformedCenter().x + 10)
						&& (jugador.getObjNave().getTransformedCenter().x) > (cubo
								.getTransformedCenter().x - 10)
						&& (jugador.getObjNave().getTransformedCenter().y) < (cubo
								.getTransformedCenter().y + 10)
						&& (jugador.getObjNave().getTransformedCenter().y) > (cubo
								.getTransformedCenter().y - 10)) {
					View view = null;
					mostrarGameOver(view);
					
					main = null;
					// dialogoEspera.dismiss();

				}
			}
			// Revisa si el proyectil ha salido del mundo o colisionado con
			// alg�n enemigo
			for (int contarObjetos = jugador.arregloDeProyectiles.size() - 1; contarObjetos >= 0; contarObjetos--) {

				Object3D proyectil = jugador.arregloDeProyectiles
						.get(contarObjetos);
				proyectil.rotateZ(0.1f);
				proyectil.translate(0, -5.0f, 0);

				if (hayJefe) {
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
						jugador.arregloDeProyectiles.remove(contarObjetos);
						contadorDanoEnemigo++;
						continue;
					}
				}
				for (int contarEnemigos = enemigo.arregloDeEnemigos.size() - 1; contarEnemigos >= 0; contarEnemigos--) {

					if (proyectil.getTransformedCenter().x < (enemigo.arregloDeEnemigos
							.get(contarEnemigos).getTransformedCenter().x + 10)
							&& proyectil.getTransformedCenter().x > (enemigo.arregloDeEnemigos
									.get(contarEnemigos).getTransformedCenter().x - 10)
							&& proyectil.getTransformedCenter().y > (enemigo.arregloDeEnemigos
									.get(contarEnemigos).getTransformedCenter().y - 10)
							&& proyectil.getTransformedCenter().y < (enemigo.arregloDeEnemigos
									.get(contarEnemigos).getTransformedCenter().y + 10)) {
						jugador.arregloDeProyectiles.remove(contarObjetos);
						mundo.removeObject(proyectil);
						mundo.removeObject(enemigo.arregloDeEnemigos
								.get(contarEnemigos));
						enemigo.arregloDeEnemigos.remove(contarEnemigos);

						if (!hayJefe) {
							puntaje = puntaje + 10;
						}
						puntajeFinal = puntajeFinal + 10;
						contadorEnemigos--;
						proyectil = null;
						break;
					}

				}
				if (proyectil == null) {
					continue;
				}
				if (proyectil.getTransformedCenter().y < -205) {
					mundo.removeObject(proyectil);
					jugador.arregloDeProyectiles.remove(contarObjetos);
					
				}
			}

			// Revisa si el enemigo debe morir y si la nave ha chocado con �l
			if (hayJefe) {
				if (!enemigoMuerto && contadorDanoEnemigo > 45) {
					mundo.removeObject(objEnemigo);
					enemigoMuerto = true;
					hayJefe = false;
					puntaje = 0;
					puntajeFinal = puntajeFinal + 100;
				}
			}
			// *********************** COLISION CON Enemigo
			if (hayJefe) {
				if (!enemigoMuerto
						&& (jugador.getObjNave().getTransformedCenter().x) < (objEnemigo
								.getTransformedCenter().x + 24)
						&& (jugador.getObjNave().getTransformedCenter().x) > (objEnemigo
								.getTransformedCenter().x - 24)
						&& (jugador.getObjNave().getTransformedCenter().y) > (objEnemigo
								.getTransformedCenter().y - 52)
						&& (jugador.getObjNave().getTransformedCenter().y) < (objEnemigo
								.getTransformedCenter().y + 52)) {
					View view = null;
					mostrarGameOver(view);
					main = null;

				}
			}
			// *********************** MOVIMIENTO NAVE Y COLISION CON BORDES
			jugador.mover(offsetHorizontal, offsetVertical);
			generarImagenScore();
			// *********************** BUFFER

			buffer.clear(colorFondo); // Borrar el buffer
			mundo.renderScene(buffer);// C�lculos sobre los objetos a dibujar
			mundo.draw(buffer); // Redibuja todos los objetos
			buffer.blit(pixeles, 256, 64, 0, 0, 250, 20, 256, 64, true);
			buffer.display(); // Actualiza en pantalla

			// *********************** CONTADOR FPS
			if (System.currentTimeMillis() - tiempo > 1000) {
				Log.d("FPS", fps + "fps");
				tiempo = System.currentTimeMillis();
				fps = 0;
			}
			fps++;
		}

		// -------------------------- Metodo onSurfaceChanged()

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			if (buffer != null) {
				buffer.dispose();
			}
			buffer = new FrameBuffer(gl, width, height);
			if (main == null) {

				// *********************** CRECI�N DE MUNDO Y LUZ
				mundo = new World();
				mundo.setAmbientLight(255, 255, 255);
				colorFondo = new RGBColor(0, 0, 0, 0);

				Texture textura = new Texture(BitmapHelper.rescale(
						BitmapHelper.convert(getResources().getDrawable(
								R.drawable.space8)), 1024, 1024));
				TextureManager.getInstance().addTexture("space8.jpg", textura);
				background = Modelo.cargarModeloMTL(getBaseContext(),
						"space.obj", "space.mtl", 2);
				background.rotateX((float) (3.1415 / 2));
				background.translate(0, 0, 150);
				mundo.addObject(background);
				// *********************** CARGA DEL MODELO DE LA NAVE
				jugador = new Jugador(getBaseContext());
				mundo.addObject(jugador.getObjNave());
				enemigo = new Enemigo();
				// *********************** MANEJO DE C�MARA
				camara = mundo.getCamera();
				camara.moveCamera(Camera.CAMERA_MOVEOUT, 200);
				camara.moveCamera(Camera.CAMERA_MOVEUP, 3);
				// *********************** MEMORIA
				MemoryHelper.compact();
				if (main == null) {
					main = Juego.this;
				}
				dialogoEspera.dismiss();
				reproducirSonido();
			}

		}

		// -------------------------- M�todo onSurfaceChanged()
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		}
	}

	// ********************** ACELER�METRO
	private void reproducirSonido() {
		try {
			if (player != null && player.isPlaying()) {
				player.stop();
				player.release();
			}
			player = new MediaPlayer();
			AssetFileDescriptor descriptor = getAssets()
					.openFd("gamemusic.mp3");
			player.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			player.prepare();
			player.setVolume(0.5f, 0.5f);
			player.setLooping(false);
			player.start();
		} catch (Exception e) {
			Log.d("Error", "MP3");
		}
	}

	public void generarImagenScore() {
		canvas.drawARGB(255, 0, 0, 0);
		p.setColor(0xFF00FF00);
		p.setTextSize(24);
		canvas.drawText("Score:" + puntajeFinal, 40, 30, p);
		bitmap.getPixels(pixeles, 0, 256, 0, 0, 256, 64);
	}

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
