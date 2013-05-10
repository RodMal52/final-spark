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
 * Clase que inicializa el modelo 3D que se asociará al jugador 
 * y sera controlado por el mismo, inicializa los misiles que 
 * seran agregados al mundo y disparados en el mismo y maneja la vida del jugador. 
 */
public class Jugador {

	private int dano = 5;
	private int vida = 100;
	private Object3D objNave;
	public Object3D misil;
	public Object3D misilIzq;
	public Object3D misilDer;
	public ArrayList<Object3D> arregloDeProyectiles;

	/**
	 * Constructor de la clase, recibe el contexto del mundo como parametro y
	 * utiliza a la clase Modelo para cargar un modelo 3D del tipo "obj".
	 * Ademas, modifica la posicion de la nave para que inicie en la parte
	 * inferior de la pantalla y mirando hacia arriba.
	 * 
	 * @param contexto Contexto del mundo al cual se agregara la nave.
	 */
	public Jugador(Context contexto, Resources resource) {
		if (!TextureManager.getInstance().containsTexture("textnave.png")) {
			Texture textura = new Texture(BitmapHelper.rescale(BitmapHelper
					.convert(resource.getDrawable(R.drawable.textnave)),
					512, 512));
			TextureManager.getInstance()
					.addTexture("textnave.png", textura);
		}
		objNave = Modelo.cargarModeloMTL(contexto, "nave.obj",
				"nave.mtl", 1);
		//objNave = Primitives.getCone(7);
		objNave.rotateY(3.141592f);
		objNave.rotateX((float) (1.5));
		objNave.translate(0, 100, 0);
		arregloDeProyectiles = new ArrayList<Object3D>();
	}

	/**
	 * Regresa el objeto 3D que representa al jugador en el mundo del juego. 
	 * 
	 * @return objNave
	 */
	public Object3D getObjNave() {
		return objNave;
	}

	/**
	 * Cambia el dano del jugador por valor el parámetro recibido.
	 * 
	 * @param vida Dano con la que se inicializará al jugador.
	 */
	public void setDano(int dano) {
		this.dano = dano;
	}

	/**
	 * Cambia la vida del jugador por valor el parámetro recibido.
	 * 
	 * @param vida Vida con la que se inicializará al jugador.
	 */
	public void setVida(int vida) {
		this.vida = vida;
	}

	/**
	 * Regresa el valor del dano actual del jugador.
	 * 
	 * @return dano
	 */
	public int getDano() {
		return dano;
	}

	/**
	 * Regresa el valor de la vida actual del jugador.
	 * 
	 * @return vida
	 */
	public int getVida() {
		return vida;
	}

	/**
	 * Genera tres misiles del tipo object3D en forma de cubos y los ubica bajo
	 * los cañones de la nave en una posición relativa a sus coordenadas
	 * actuales en el mundo.
	 */
	public void disparar(String textura) {
		misil = Primitives.getPlane(4, (float) .5);
		misil.strip();
		misil.build();
		misil.setOrigin(objNave.getTransformedCenter());
		misil.translate(0, -2, 0);
		misil.calcTextureWrapSpherical();
		misil.setTexture(textura);

		misilIzq = Primitives.getPlane(4, (float) .5);
		misilIzq.strip();
		misilIzq.build();
		misilIzq.setOrigin(objNave.getTransformedCenter());
		misilIzq.translate(-9, 1, 0);
		misilIzq.calcTextureWrapSpherical();
		misilIzq.setTexture(textura);

		misilDer = Primitives.getPlane(4, (float) .5);
		misilDer.strip();
		misilDer.build();
		misilDer.setOrigin(objNave.getTransformedCenter());
		misilDer.translate(9, 1, 0);
		misilDer.calcTextureWrapSpherical();
		misilDer.setTexture(textura);
		
		arregloDeProyectiles.add(misil);// Agrega a la el misileto
		arregloDeProyectiles.add(misilIzq);
		arregloDeProyectiles.add(misilDer);
		
	}

	/**
	 * Permite controlar la nave a través del movimiento del dispositivo usando
	 * el acelerómetro integrado en el mismo.
	 * 
	 * @param offsetHorizontal Es la desviación horizontal del dispositivo.
	 * 
	 * @param offsetVerticl Es la desviación vertical del dispositivo.
	 */
	public void mover(float offsetHorizontal, float offsetVertical) {

		if (objNave.getTransformedCenter().x < 100
				&& objNave.getTransformedCenter().x > -100
				&& objNave.getTransformedCenter().y < 155
				&& objNave.getTransformedCenter().y > -118) {
			if (offsetVertical != 0) {
				objNave.translate(0, offsetVertical, 0);
				offsetVertical = 0;
			}

			if (offsetHorizontal != 0) {
				objNave.translate(offsetHorizontal, 0, 0);
				offsetHorizontal = 0;
			}

		} else {

			// Pared derecha
			if (objNave.getTransformedCenter().x > 100) {

				if (offsetHorizontal < 0) {
					objNave.translate(offsetHorizontal, 0, 0);
					offsetHorizontal = 0;
				}

				if (objNave.getTransformedCenter().y < -118) {

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

			// Pared izquierda
			if (objNave.getTransformedCenter().x < -100) {

				if (offsetHorizontal > 0) {
					objNave.translate(offsetHorizontal, 0, 0);
					offsetHorizontal = 0;
				}

				if (objNave.getTransformedCenter().y < -118) {

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

			// Pared superior
			if (objNave.getTransformedCenter().y < -118) {

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

			// Pared inferior
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

	}

}