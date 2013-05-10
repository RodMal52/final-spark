package mx.itesm.finalspark;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.util.BitmapHelper;

/**
 * Clase que inicializa el modelo 3D que se asociara a un jefe, inicializa sus
 * misiles y revisa si el objeto colisiona con alguno de los limites
 * establecidos para el mismo para cambiar de direccion en caso de ser necesario
 */
public class Jefe extends Enemigo {

	private float velocidadX;
	private float velocidadY;

	/**
	 * Carga el modelo del jefe, asigna los valores de dano y de vida de acuerdo
	 * a parametros recibidos, inicializa los valores de velocidad a los que se
	 * movera el enemigo e inicializa las banderas que permitiran su segura
	 * remocian del mundo.
	 * 
	 * @param dano
	 *            Dano que hace el jefe con cada disparo
	 * 
	 * @param vida
	 *            Dano que puede soportar el jefe antes de morir
	 */
	public Jefe(int dano, int vida, Context contexto, Resources resource) {
		this.dano = dano;
		this.vida = vida;
		velocidadX = (float) 1.5;
		velocidadY = (float) 1.5;
		arregloDeProyectiles = new ArrayList<Object3D>();
		if (!TextureManager.getInstance().containsTexture("texturamantis.png")) {
			Texture texturajefe = new Texture(BitmapHelper.rescale(BitmapHelper
					.convert(resource.getDrawable(R.drawable.texturamantis)),
					512, 512));
			TextureManager.getInstance()
					.addTexture("texturamantis.png", texturajefe);
		}
		enemigo = Modelo.cargarModeloMTL(contexto, "mantis.obj", "mantis.mtl",
				3);

		enemigo.rotateX(3.141592f / 2);

		enemigo.translate(0, -90, 0);
		enemigoExiste = true;
		enemigoRemovido = false;
		enemigoAgregadoSecundario = false;
		velocidadX = velocidadX * obtenerSigno();
		velocidadY = velocidadY * obtenerSigno();
	}

	/**
	 * Regresa los valores enteros 1 ò -1, que serán usados para determinar la
	 * dirección del movimiento del enemigo en el mundo
	 * 
	 * @return 1
	 * 
	 * @return -1
	 */
	public int obtenerSigno() {
		float aleatorio = (float) (Math.random());
		if (aleatorio < 0.5) {
			return -1;
		} else {
			return 1;
		}
	}

	public void cambiarVelocidadX() {
		velocidadX = -velocidadX;
	}

	public void cambiarVelocidadY() {
		velocidadY = -velocidadY;
	}

	/**
	 * Basado en los valores de velocidad en los ejes X y Y del jefe, lo mueve
	 * dentro del mundo y revisa si colisiona con alguno de los limites
	 * establecidos para el mismo, de ser asi, cambia la direccion del
	 * movimiento, creando la ilusion de que rebota en esos limites
	 */
	public void mover(Object3D object) {
		if (enemigoExiste) {
			if (enemigo.getTransformedCenter().x < 100
					&& enemigo.getTransformedCenter().x > -100
					&& enemigo.getTransformedCenter().y < 0
					&& enemigo.getTransformedCenter().y > -118) {
				enemigo.translate(velocidadX, velocidadY, 0);
			} else {
				// Pared derecha
				if (enemigo.getTransformedCenter().x >= 100) {
					cambiarVelocidadX();
					enemigo.translate(velocidadX, velocidadY, 0);
					if (enemigo.getTransformedCenter().y <= -118) {
						cambiarVelocidadY();
						enemigo.translate(velocidadX, velocidadY, 0);
					} else if (enemigo.getTransformedCenter().y >= 0) {
						cambiarVelocidadY();
						enemigo.translate(velocidadX, velocidadY, 0);
					}
				}
				// Pared izquierda
				if (enemigo.getTransformedCenter().x <= -100) {
					cambiarVelocidadX();
					enemigo.translate(velocidadX, velocidadY, 0);
					if (enemigo.getTransformedCenter().y <= -118) {
						cambiarVelocidadY();
						enemigo.translate(velocidadX, velocidadY, 0);
					} else if (enemigo.getTransformedCenter().y >= 0) {
						cambiarVelocidadY();
						enemigo.translate(velocidadX, velocidadY, 0);
					}
				}
				// Pared superior
				if (enemigo.getTransformedCenter().y <= -118) {
					cambiarVelocidadY();
					enemigo.translate(velocidadX, velocidadY, 0);
					if (enemigo.getTransformedCenter().x >= 100) {
						cambiarVelocidadX();
						enemigo.translate(velocidadX, velocidadY, 0);
					} else if (enemigo.getTransformedCenter().x <= -100) {
						cambiarVelocidadX();
						enemigo.translate(velocidadX, velocidadY, 0);
					}
				}
				// Pared inferior
				if (enemigo.getTransformedCenter().y >= 0) {
					cambiarVelocidadY();
					enemigo.translate(velocidadX, velocidadY, 0);
					if (enemigo.getTransformedCenter().x >= 100) {
						cambiarVelocidadX();
						enemigo.translate(velocidadX, velocidadY, 0);
					} else if (enemigo.getTransformedCenter().x <= -100) {
						cambiarVelocidadX();
						enemigo.translate(velocidadX, velocidadY, 0);
					}
				}
			}
		}
	}

	/**
	 * Genera cinco proyectiles que seran agregados al mundo para ser disparados
	 * por el jefe, solamente si su bandera de existencia es verdadera, en caso
	 * contrario, hace nada. Los agrega ademas a su arreglo de proyectiles
	 */
	public void disparar(String textura) {
		
		if (enemigoExiste) {
			misil = Primitives.getPlane(4, (float) 1.0);
			misil.strip();
			misil.build();
			misil.setOrigin(enemigo.getTransformedCenter());
			misil.translate(-9, -8, 0);
			misil.calcTextureWrapSpherical();
			misil.setTexture(textura);

			misil1 = Primitives.getPlane(4, (float) 1.0);
			misil1.strip();
			misil1.build();
			misil1.setOrigin(enemigo.getTransformedCenter());
			misil1.translate(9, -8, 0);
			misil1.calcTextureWrapSpherical();
			misil1.setTexture(textura);

			misil2 = Primitives.getPlane(4, (float) 1.0);
			misil2.strip();
			misil2.build();
			misil2.setOrigin(enemigo.getTransformedCenter());    
			misil2.translate(-15, 6, 0);
			misil2.calcTextureWrapSpherical();
			misil2.setTexture(textura);

			misil3 = Primitives.getPlane(4, (float) 1.0);
			misil3.strip();
			misil3.build();
			misil3.setOrigin(enemigo.getTransformedCenter());
			misil3.translate(15, 6, 0);
			misil3.calcTextureWrapSpherical();
			misil3.setTexture(textura);
			
			arregloDeProyectiles.add(misil);// Agrega a la el misileto
			arregloDeProyectiles.add(misil1);
			arregloDeProyectiles.add(misil2);
			arregloDeProyectiles.add(misil3);
		}
	}

	public Object3D getMisil() {
		return misil;
	}

}
