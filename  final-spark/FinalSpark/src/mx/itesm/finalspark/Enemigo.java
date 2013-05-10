package mx.itesm.finalspark;

import java.util.ArrayList;

import com.threed.jpct.Object3D;

/**
 * Clase que contiene las declaraciones del modelo 3D que se asociara a 
 * los enemigos, así como su vida, daño y proyectiles.
 *  
 */
public class Enemigo {
	public int vida;
	public int dano;
	public Object3D enemigo;
	public boolean enemigoExiste;
	public Object3D misil;
	public Object3D misil1;
	public Object3D misil2;
	public Object3D misil3;
	public ArrayList<Object3D> arregloDeProyectiles;
	public boolean enemigoRemovido;
	public boolean enemigoAgregadoSecundario;

	/**
	 * Constructor por default del enemigo
	 */
	public Enemigo() {

	}

	/**
	 * Cambia la vida del enemigo por valor el parámetro recibido.
	 * 
	 * @param vida Vida con la que se inicializará al enemigo
	 */
	public void setVida(int vida) {
		this.vida = vida;
	}

	/**
	 * Cambia el dano del enemigo por valor el parámetro recibido.
	 * 
	 * @param dano Dano con el que se inicializará al enemigo
	 */
	public void setDano(int dano){
		this.dano = dano;
	}
	
	/**
	 * Regresa la cantidad de dano que hace el enemigo con cada disparo.
	 * 
	 * @return dano
	 */
	public int getDano() {
		return dano;
	}

	/**
	 * Regresa el objeto 3D que representa al enemigo en el mundo del juego. 
	 * 
	 * @return enemigo
	 */
	public Object3D getEnemigo() {
		return enemigo;
	}

	/**
	 * Regresa la vida actual del enemigo.
	 * 
	 * @return vida
	 */
	public int getVida() {
		return vida;
	}

	/**
	 * Metodo que mueve al enemigo en el mundo.
	 * 
	 * @param object Toma como referencia el objeto 3D que representa al objetivo del disparo en el mundo.
	 */
	public void mover(Object3D object) {
	}

	/**
	 * Metodo encargado de generar los proyectiles que seran disparados por el enemigo. 
	 */
	public void disparar(String textura) {
	}

	/**
	 * Se encarga de infringir dano al enemigo basado en el dano actual del
	 * jugador.
	 * 
	 * @param danoRecibido Daño que se deducirá de la vida del enemigo.
	 */
	public void danar(int danoRecibido) {
		vida = vida - danoRecibido;
		if (vida <= 0) {
			enemigoExiste = false;
		}
	}

}