package mx.itesm.finalspark;

import java.util.ArrayList;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;

/**
 * Clase que inicializa el modelo 3D que se asociara al enemigo que persigue, 
 * inicializa sus misiles y revisa la posicion del jugador en el mundo para 
 * indicar al enemigo a donde moverse.  
 */
public class EnemigoCazador extends Enemigo {

	public EnemigoCazador(int dano, int vida) {
		this.dano = dano;
		this.vida = vida;
		arregloDeProyectiles = new ArrayList<Object3D>();
		enemigo = Primitives.getCube(7);
		enemigo.strip();
		enemigo.build();
		float xa = (float) (Math.random() * (100));
		float ya = (float) (Math.random() * (-130));
		enemigo.translate(xa, ya, 0);
		enemigoExiste = true;
		enemigoRemovido = false;
	}

	/**
	 * Si el enemigo aun sigue en el mundo, sigue al jugador de acuerdo a su
	 * posicion en el mundo.
	 * 
	 * @param object Recibe al object3D que esta relacionado al jugador en el
	 * mundo.
	 */
	public void mover(Object3D object) {
		if (enemigoExiste) {
			Object3D cubo = enemigo;
			cubo.rotateX(0.01f);
			cubo.translate(
					(object.getTransformedCenter().x) / 50
							- (cubo.getTransformedCenter().x) / 50,
					(object.getTransformedCenter().y) / 50
							- (cubo.getTransformedCenter().y) / 50, 0);
		}
	}

	/**
	 * Genera un proyectil que sera agregado al mundo para ser disparado por el
	 * enemigo, solamente si su bandera de existencia es verdadera, en caso
	 * contrario, hace nada. Los agrega ademas a su arreglo de proyectiles.
	 */
	public void disparar() {
		if (enemigoExiste) {
			misil = Primitives.getCube((float) .5);
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