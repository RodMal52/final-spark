package mx.itesm.finalspark;

// Imports de paquetería de Java
import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
//Imports de paquetería de Android
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
//Imports de paquetería Jpct-AE 
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;

import com.threed.jpct.util.MemoryHelper;

public class Juego extends Activity {
	private static Juego main;
	private GLSurfaceView mGLView; // Contenedor para dibujar
	private Renderer renderer; // El objeto que hace los trazos
	private FrameBuffer buffer; // Buffer para trazar
	private World mundo; // Un escenario de nuestro juego
	private RGBColor colorFondo; // Color de fondo :)
	private Camera camara; // Cámara
	private Object3D objEnemigo; // Modelo enemigo
	private Object3D background;
	private ArrayList<Object3D> arregloDeEnemigos; // Arreglo de enemigos
	private boolean agregarObjeto; // Valor booleano para comprobar si se agregan misiles
	private MediaPlayer player;
	private Jugador jugador; 
	private int fps; // contador frames
	private int contadorDañoEnemigo = 0; // HP enemigo
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
	private int[] pixeles; //sustituye la textura
	private boolean juegoEstaPausado;	//Manejo de pausa
	private ProgressDialog dialogoEspera; 	//dialogo de espera
		
	public void mostrarGameOver(View view) {
		Intent intent = new Intent(this, GameOverActivity.class);
		this.startActivity(intent);
	}

	// -------------------------- Método onCreate()
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (main != null) { // Si ya existe el juego, copiar sus campos
			copiar(main);
		}else{
			dialogoEspera = ProgressDialog.show(this, "Aviso", "LOADING...");
		}
		juegoEstaPausado = false;
		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplicationContext());
		bitmap = Bitmap.createBitmap(256,64,Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		p = new Paint();
		pixeles = new int [256*64*4];
		
		
		//TextureManager.getInstance().addTexture("textureBG", new Texture(BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(R.drawable.space2)), 1024, 1024)));
		renderer = new Renderer();
		mGLView.setRenderer(renderer);
		setContentView(mGLView);
	}

	// -------------------------- Método onPause()
	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	// -------------------------- Método onResume()
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

	// -------------------------- Método onStop()
	@Override
	protected void onStop() {
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sm.unregisterListener(miListener);
		if (player != null && player.isPlaying()){
			player.stop();
			player.release();
		}
		super.onStop();
		
	}
	// -------------------------- Método onDestroy()
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	// -------------------------- Método copiar()
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

	// *********************** TOUCH EVENT*************************************************************************************
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

		// -------------------------- Método onDrawFrame()
		@Override
		public void onDrawFrame(GL10 gl) { // ACTUALIZACIONES

			if (puntaje == 300) {
				objEnemigo = Primitives.getCone(20);
				objEnemigo.rotateY(3.141592f);
				objEnemigo.rotateZ(3.141592f);
				objEnemigo.translate(0, -120, 0);
				mundo.addObject(objEnemigo);
				hayJefe = true;
				puntaje = 0;
				enemigoMuerto = false;
				contadorDañoEnemigo = 0;
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

			// *********************** GENERACIÓN ALEATORIA DE ENEMIGOS

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
			// Revisa si el proyectil ha salido del mundo o colisionado con algún enemigo
			for (int contarObjetos = 0; contarObjetos < jugador.arregloDeProyectiles
					.size(); contarObjetos++) {

				Object3D proyectil = jugador.arregloDeProyectiles.get(contarObjetos);
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
						contadorDañoEnemigo++;
					}
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
						jugador.arregloDeProyectiles.remove(contarObjetos);
						mundo.removeObject(arregloDeEnemigos
								.get(contarEnemigos));
						arregloDeEnemigos.remove(contarEnemigos);
						if (!hayJefe) {
							puntaje = puntaje + 10;
						}
						puntajeFinal = puntajeFinal + 10;

						contadorEnemigos--;
					}
					
				}

				if (proyectil.getTransformedCenter().y < -205) {
					mundo.removeObject(proyectil);
					jugador.arregloDeProyectiles.remove(contarObjetos);
				}
			}

			// Revisa si el enemigo debe morir y si la nave ha chocado con él
			if (hayJefe) {
				if (!enemigoMuerto && contadorDañoEnemigo > 45) {
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
			//buffer.blit(TextureManager.getInstance().getTexture("textureBG"), 1024, 1024, 0, 0, 1024, 1024, false);
			mundo.renderScene(buffer);// Cálculos sobre los objetos a dibujar
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

		// -------------------------- Método onSurfaceChanged()

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			if (buffer != null) {
				buffer.dispose();
			}
			buffer = new FrameBuffer(gl, width, height);
			if (main == null) {
				
		 		// *********************** CRECIÓN DE MUNDO Y LUZ
				mundo = new World();
				mundo.setAmbientLight(255, 255, 255);
				colorFondo = new RGBColor(0, 0, 0, 0);
				
				
				Texture textura = new Texture(
						BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(
								R.drawable.space8)), 1024, 1024));
				// pisoverde.jpg es el nombre que se encuentra en el archivo .mtl
				TextureManager.getInstance().addTexture("space8.jpg",textura);
				
				background = Modelo.cargarModeloMTL(getBaseContext(),
						"space.obj", "space.mtl",2);
				background.rotateX((float) (3.1415/2));
				
				background.translate(0,0, 150);

				mundo.addObject(background)	;			
				// *********************** CARGA DEL MODELO DE LA NAVE
				
				jugador = new Jugador(getBaseContext()); 
				mundo.addObject(jugador.getObjNave());
				// *********************** CARGA DEL MODELO DE ENEMIGOS
				arregloDeEnemigos = new ArrayList<Object3D>();// Inicializa arreglo de enemigos
				// *********************** MANEJO DE CÁMARA
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
		// -------------------------- Método onSurfaceChanged()
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		}
	}
	// ********************** ACELERÓMETRO
	private void reproducirSonido(){
		try{
			if (player != null && player.isPlaying()){
				player.stop();
				player.release();
			}
			player = new MediaPlayer();
			AssetFileDescriptor descriptor = getAssets().openFd("gamemusic.mp3");
			player.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			player.prepare();
			player.setVolume(0.5f, 0.5f);
			player.setLooping(false);
			player.start();
		}catch (Exception e){
			Log.d("Error","MP3");
		}
	}
	public void generarImagenScore(){
		canvas.drawARGB(255, 0, 0, 0);
		p.setColor(0xFF00FF00);
		p.setTextSize(24);
		canvas.drawText("Score:"+puntajeFinal, 40, 30, p);
		bitmap.getPixels(pixeles, 0, 256, 0, 0, 256, 64);
	}
	
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