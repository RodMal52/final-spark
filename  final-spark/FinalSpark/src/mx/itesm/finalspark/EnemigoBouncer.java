package mx.itesm.finalspark;

import java.util.ArrayList;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;

/**
 * Clase que inicializa el modelo 3D que se asociara al enemigo que rebota, 
 * inicializa sus misiles y revisa si el objeto colisiona con alguno de los 
 * bordes de la pantalla para cambiar de direccion en caso de ser necesario.
 */
public class EnemigoBouncer extends Enemigo {
	private int velocidadX;
	private int velocidadY;

	/**
	 * Inicializa el modelo del enemigo a un cono, asigna los valores de dano y
	 * de vida de acuerdo a parametros recibidos, inicializa los valores de
	 * velocidad a los que se movera el enemigo e inicializa las banderas que
	 * permitiran su segura remocian del mundo.
	 * 
	 * @param dano Dano que hace el enemigo con cada disparo
	 * 
	 * @param vida Dano que puede soportar el enemigo antes de morir
	 */
	public EnemigoBouncer(int dano, int vida) {
		this.dano = dano;
		this.vida = vida;
		velocidadX = 3;
		velocidadY = 3;
		arregloDeProyectiles = new ArrayList<Object3D>();
		enemigo = Primitives.getCone(7);
		enemigo.strip();
		enemigo.build();
		float xa = (float) (Math.random() * (100));
		float ya = (float) (Math.random() * (-130));
		enemigo.translate(xa, ya, 0);
		enemigoExiste = true;
		enemigoRemovido = false;
		velocidadX = velocidadX * obtenerSigno();
		velocidadY = velocidadY * obtenerSigno();
	}

	/**
	 * Regresa los valores enteros 1 o -1, que serán usados para determinar la
	 * dirección del movimiento del enemigo en el mundo.
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

	/**
	 * Cambia la direccion hacia la que se mueve el enemigo sobre el eje X.
	 */
	public void cambiarVelocidadX() {
		velocidadX = -velocidadX;
	}

	/**
	 * Cambia la direccion hacia la que se mueve el enemigo sobre el eje Y.
	 */
	public void cambiarVelocidadY() {
		velocidadY = -velocidadY;
	}

	/**
	 * Basado en los valores de velocidad en los ejes X y Y del enemigo, lo
	 * mueve dentro del mundo y revisa si colisiona con algun borde de la
	 * pantalla, de ser asi, cambia la direccion del movimiento, creando la
	 * ilusion de que rebota en las paredes.
	 */
	public void mover(Object3D object) {
		if (enemigoExiste) {
			enemigo.rotateX(0.01f);
			enemigo.rotateY(0.01f);
			enemigo.rotateZ(0.01f);
			if (enemigo.getTransformedCenter().x < 100
					&& enemigo.getTransformedCenter().x > -100
					&& enemigo.getTransformedCenter().y < 155
					&& enemigo.getTransformedCenter().y > -148) {
				enemigo.translate(velocidadX, velocidadY, 0);
			} else {
				// Pared derecha
				if (enemigo.getTransformedCenter().x > 100) {
					cambiarVelocidadX();
					enemigo.translate(velocidadX, velocidadY, 0);
					if (enemigo.getTransformedCenter().y < -148) {
						cambiarVelocidadY();
						enemigo.translate(velocidadX, velocidadY, 0);
					} else if (enemigo.getTransformedCenter().y > 155) {
						cambiarVelocidadY();
						enemigo.translate(velocidadX, velocidadY, 0);
					}
				}
				// Pared izquierda
				if (enemigo.getTransformedCenter().x < -100) {
					cambiarVelocidadX();
					enemigo.translate(velocidadX, velocidadY, 0);
					if (enemigo.getTransformedCenter().y < -148) {
						cambiarVelocidadY();
						enemigo.translate(velocidadX, velocidadY, 0);
					} else if (enemigo.getTransformedCenter().y > 155) {
						cambiarVelocidadY();
						enemigo.translate(velocidadX, velocidadY, 0);
					}
				}
				// Pared superior
				if (enemigo.getTransformedCenter().y < -148) {
					cambiarVelocidadY();
					enemigo.translate(velocidadX, velocidadY, 0);
					if (enemigo.getTransformedCenter().x > 100) {
						cambiarVelocidadX();
						enemigo.translate(velocidadX, velocidadY, 0);
					} else if (enemigo.getTransformedCenter().x < -100) {
						cambiarVelocidadX();
						enemigo.translate(velocidadX, velocidadY, 0);
					}
				}
				// Pared inferior
				if (enemigo.getTransformedCenter().y > 155) {
					cambiarVelocidadY();
					enemigo.translate(velocidadX, velocidadY, 0);
					if (enemigo.getTransformedCenter().x > 100) {
						cambiarVelocidadX();
						enemigo.translate(velocidadX, velocidadY, 0);
					} else if (enemigo.getTransformedCenter().x < -100) {
						cambiarVelocidadX();
						enemigo.translate(velocidadX, velocidadY, 0);
					}
				}
			}
		}
	}

	/**
	 * Genera un proyectil que sera agregado al mundo para ser disparado por el
	 * enemigo, solamente si su bandera de existencia es verdadera, en caso
	 * contrario, hace nada. Los agrega ademas a su arreglo de proyectiles.
	 */
	public void disparar() {
		if (enemigoExiste) {
			misil = Primitives.getCube((float) .8);
			misil.strip();
			misil.build();
			misil.translate(0, 2, 0);
			misil.setOrigin(enemigo.getTransformedCenter());
			arregloDeProyectiles.add(misil);
		}
	}

	/**
	 * Regresa el objeto 3D que representa el misil en el mundo.
	 * 
	 * @return misil
	 */
	public Object3D getMisil() {
		return misil;
	}

}