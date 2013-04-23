package mx.itesm.finalspark;

//Imports de paqueteria de Java
import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
//Imports de paqueteria de Android
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
//Imports de paqueteria Jpct-AE 
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

public class Juego extends Activity implements SensorEventListener {
	private static Juego main;
	private GLSurfaceView mGLView; // Contenedor para dibujar
	private Renderer renderer; // El objeto que hace los trazos
	private FrameBuffer buffer; // Buffer para trazar
	private World mundo; // Un escenario de nuestro juego
	private RGBColor colorFondo; // Color de fondo
	private Camera camara; // Camara
	private Object3D background;
	private boolean agregarObjeto; // Valor booleano para comprobar si se
									// agregan misiles
	public ArrayList<Enemigo> arregloDeEnemigos; // Arreglo de enemigos
	private MediaPlayer player;
	private Jugador jugador;
	private Enemigo enemigo;
	private int fps; // contador frames
	private int contadorEnemigos = 0;
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
	private int disparosEnemigo = 0;
	private int jefesDestruidos = 0;

	public void mostrarGameOver(View view) {
		Intent intent = new Intent(this, GameOverActivity.class);
		intent.putExtra("Puntaje", puntajeFinal);
		this.startActivity(intent);
	}

	// -------------------------- Metodo onCreate()
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (main != null) { // Si ya existe el juego, copiar sus campos
			copiar(main);
		} else {
			dialogoEspera = ProgressDialog.show(this, "Final Spark",
					"Loading...");
		}
		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplicationContext());
		bitmap = Bitmap.createBitmap(512, 64, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		p = new Paint();
		pixeles = new int[512 * 64 * 4];
		renderer = new Renderer();
		mGLView.setRenderer(renderer);
		setContentView(mGLView);
	}

	// -------------------------- Metodo onPause()
	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	// -------------------------- Metodo onResume()
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

	// -------------------------- Metodo onStop()
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

	// -------------------------- Metodo onDestroy()
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// -------------------------- Metodo copiar()
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

		// -------------------------- Metodo onDrawFrame()
		@Override
		public void onDrawFrame(GL10 gl) { // ACTUALIZACIONES

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
				if (jefesDestruidos <= 3) {
					for (int i = 0; i < 3 + (jefesDestruidos + 1); i++) {
						enemigo = new EnemigoCazador(5 + (jefesDestruidos * 2),
								15 + (jefesDestruidos * 3));
						mundo.addObject(enemigo.getEnemigo());
						arregloDeEnemigos.add(enemigo);
					}
					contadorEnemigos = 3 + (jefesDestruidos + 1);
				} else if (jefesDestruidos > 3) {
					for (int i = 0; i < jefesDestruidos - 1; i++) {
						enemigo = new EnemigoCazador(5 + (jefesDestruidos * 2),
								15 + (jefesDestruidos * 3));
						mundo.addObject(enemigo.getEnemigo());
						arregloDeEnemigos.add(enemigo);
					}
					for (int j = 0; j < jefesDestruidos - 2; j++) {
						enemigo = new EnemigoBouncer(3 + (jefesDestruidos * 2),
								10 + (jefesDestruidos * 3));
						mundo.addObject(enemigo.getEnemigo());
						arregloDeEnemigos.add(enemigo);
					}
					contadorEnemigos = (jefesDestruidos * 2) - 3;
				}
				noMasEnemigos = false;
			}

			if (puntaje == 300) {
				enemigo = new Jefe(10, 200, getBaseContext());
				mundo.addObject(enemigo.getEnemigo());
				arregloDeEnemigos.add(enemigo);
				hayJefe = true;
				puntaje = 0;
			}

			// Revisa si los enemigos han muerto
			if (contadorEnemigos == 0) {
				noMasEnemigos = true;
			}

			disparosEnemigo++;

			if (disparosEnemigo > 6) {
				disparosEnemigo = 0;
			}
			if (disparosEnemigo == 0) {
				for (int i = 0; i < arregloDeEnemigos.size(); i++) {
					arregloDeEnemigos.get(i).disparar();
					if (arregloDeEnemigos.get(i).enemigoExiste) {
						if (arregloDeEnemigos.get(i) instanceof Jefe) {
							mundo.addObject(arregloDeEnemigos.get(i).misil);
							mundo.addObject(arregloDeEnemigos.get(i).misil1);
							mundo.addObject(arregloDeEnemigos.get(i).misil2);
							mundo.addObject(arregloDeEnemigos.get(i).misil3);
						} else {
							mundo.addObject(arregloDeEnemigos.get(i).misil);
						}
					}
				}
			}
			for (int i = 0; i < arregloDeEnemigos.size(); i++) {
				arregloDeEnemigos.get(i).mover(jugador.getObjNave());
				if (arregloDeEnemigos.get(i) instanceof Jefe) {
					Object3D objJefe = arregloDeEnemigos.get(i).getEnemigo();
					if ((jugador.getObjNave().getTransformedCenter().x) < (objJefe
							.getTransformedCenter().x + 50)
							&& (jugador.getObjNave().getTransformedCenter().x) > (objJefe
									.getTransformedCenter().x - 50)
							&& (jugador.getObjNave().getTransformedCenter().y) > (objJefe
									.getTransformedCenter().y - 42)
							&& (jugador.getObjNave().getTransformedCenter().y) < (objJefe
									.getTransformedCenter().y + 60)) {
						View view = null;
						mostrarGameOver(view);
						main = null;
					}
				} else {
					Object3D cubo = arregloDeEnemigos.get(i).getEnemigo();
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
					}
				}
			}

			// ******************************************************************************
			for (Enemigo enemigoActual : arregloDeEnemigos) {
				for (int contarMisiles = enemigoActual.arregloDeProyectiles
						.size() - 1; contarMisiles >= 0; contarMisiles--) {
					Object3D proyectil = enemigoActual.arregloDeProyectiles
							.get(contarMisiles);
					proyectil.rotateZ(0.1f);
					// if (enemigoActual instanceof EnemigoCazador) {
					if (enemigoActual instanceof Jefe) {
						proyectil.translate(0, 2.0f, 0);
					} else {
						proyectil.translate(0, 5.0f, 0);
					}
					/*
					 * }else if (enemigoActual instanceof EnemigoBouncer) {
					 * proyectil.translate(
					 * (jugador.getObjNave().getTransformedCenter().x) / 25 -
					 * (proyectil.getTransformedCenter().x) / 25,
					 * (jugador.getObjNave() .getTransformedCenter().y) / 25 -
					 * (proyectil.getTransformedCenter().y) / 25, 0); }
					 */
					if (proyectil.getTransformedCenter().x < jugador
							.getObjNave().getTransformedCenter().x + 5
							&& proyectil.getTransformedCenter().x > (jugador
									.getObjNave().getTransformedCenter().x - 5)
							&& proyectil.getTransformedCenter().y > (jugador
									.getObjNave().getTransformedCenter().y - 5)
							&& proyectil.getTransformedCenter().y < (jugador
									.getObjNave().getTransformedCenter().y + 5)) {
						enemigoActual.arregloDeProyectiles
								.remove(contarMisiles);
						mundo.removeObject(proyectil);
						jugador.setVida(jugador.getVida()
								- enemigoActual.getDano());
						if (jugador.getVida() <= 0) {
							View view = null;
							mostrarGameOver(view);
							main = null;
						}

						proyectil = null;
					}

					if (proyectil == null) {
						continue;
					}
					if (proyectil.getTransformedCenter().y > 205) {
						mundo.removeObject(proyectil);
						enemigoActual.arregloDeProyectiles
								.remove(contarMisiles);
					}
				}
			}

			// *******************************************************************************
			// Revisa si el proyectil ha salido del mundo o colisionado con
			// algun enemigo
			for (int contarObjetos = jugador.arregloDeProyectiles.size() - 1; contarObjetos >= 0; contarObjetos--) {
				Object3D proyectil = jugador.arregloDeProyectiles
						.get(contarObjetos);
				proyectil.rotateZ(0.1f);
				proyectil.translate(0, -5.0f, 0);
				for (int contarEnemigos = arregloDeEnemigos.size() - 1; contarEnemigos >= 0; contarEnemigos--) {
					if (arregloDeEnemigos.get(contarEnemigos) instanceof Jefe) {
						if (proyectil.getTransformedCenter().x < (arregloDeEnemigos
								.get(contarEnemigos).getEnemigo()
								.getTransformedCenter().x + 50)
								&& proyectil.getTransformedCenter().x > (arregloDeEnemigos
										.get(contarEnemigos).getEnemigo()
										.getTransformedCenter().x - 50)
								&& proyectil.getTransformedCenter().y > (arregloDeEnemigos
										.get(contarEnemigos).getEnemigo()
										.getTransformedCenter().y - 42)
								&& proyectil.getTransformedCenter().y < (arregloDeEnemigos
										.get(contarEnemigos).getEnemigo()
										.getTransformedCenter().y + 60)) {
							jugador.arregloDeProyectiles.remove(contarObjetos);
							mundo.removeObject(proyectil);
							arregloDeEnemigos.get(contarEnemigos).danar(
									jugador.getDano());
							proyectil = null;
							break;
						}
					} else {
						if (proyectil.getTransformedCenter().x < (arregloDeEnemigos
								.get(contarEnemigos).getEnemigo()
								.getTransformedCenter().x + 10)
								&& proyectil.getTransformedCenter().x > (arregloDeEnemigos
										.get(contarEnemigos).getEnemigo()
										.getTransformedCenter().x - 10)
								&& proyectil.getTransformedCenter().y > (arregloDeEnemigos
										.get(contarEnemigos).getEnemigo()
										.getTransformedCenter().y - 10)
								&& proyectil.getTransformedCenter().y < (arregloDeEnemigos
										.get(contarEnemigos).getEnemigo()
										.getTransformedCenter().y + 10)) {
							jugador.arregloDeProyectiles.remove(contarObjetos);
							mundo.removeObject(proyectil);
							arregloDeEnemigos.get(contarEnemigos).danar(
									jugador.getDano());
							proyectil = null;
							break;
						}

					}
					if (!arregloDeEnemigos.get(contarEnemigos).enemigoExiste) {

						if (!arregloDeEnemigos.get(contarEnemigos).enemigoRemovido) {
							mundo.removeObject(arregloDeEnemigos.get(
									contarEnemigos).getEnemigo());
							arregloDeEnemigos.get(contarEnemigos).enemigoRemovido = true;
							if (!hayJefe) {
								puntaje = puntaje + 10;
							}
							puntajeFinal = puntajeFinal + 10;
							if (arregloDeEnemigos.get(contarEnemigos) instanceof Jefe) {
								hayJefe = false;
								puntaje = 0;
								puntajeFinal = puntajeFinal + 100;
								jefesDestruidos++;
							} else {
								contadorEnemigos--;
							}
						}
						if (arregloDeEnemigos.get(contarEnemigos).arregloDeProyectiles
								.size() == 0) {

							arregloDeEnemigos.remove(contarEnemigos);
						}

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

			// *********************** MOVIMIENTO NAVE Y COLISION CON BORDES
			jugador.mover(offsetHorizontal, offsetVertical);
			generarImagenScore();
			// *********************** BUFFER
			buffer.clear(colorFondo); // Borrar el buffer
			mundo.renderScene(buffer);// Calculos sobre los objetos a dibujar
			mundo.draw(buffer); // Redibuja todos los objetos
			buffer.blit(pixeles, 512, 64, 0, 0, 250, 20, 512, 64, true);
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
				// *********************** CRECION DE MUNDO Y LUZ
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
				arregloDeEnemigos = new ArrayList<Enemigo>();
				// enemigo = new Enemigo();
				// *********************** MANEJO DE CAMARA
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

		// -------------------------- Metodo onSurfaceChanged()
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		}
	}

	// ********************** ACELEROMETRO
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
		canvas.drawText("Health:" + jugador.getVida()
				+ "                         Score:" + puntajeFinal, 0, 30, p);
		bitmap.getPixels(pixeles, 0, 512, 0, 0, 512, 64);
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