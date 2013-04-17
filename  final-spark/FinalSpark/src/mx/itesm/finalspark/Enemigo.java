package mx.itesm.finalspark;

import java.util.ArrayList;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;

public class Enemigo {
	private int dano = 1;
	private int vida = 20;
	public ArrayList<Object3D> arregloDeEnemigos; // Arreglo de enemigos
	private Object3D enemigo1;
	private Object3D enemigo2;
	private Object3D enemigo3;
	private Object3D enemigo4;
	private Object3D enemigo5;

	public Enemigo() {
		arregloDeEnemigos = new ArrayList<Object3D>();// Inicializa arreglo de enemigos
	}

	public int getDano() {
		return dano;
	}

	public Object3D getEnemigo1() {
		return enemigo1;
	}

	public Object3D getEnemigo2() {
		return enemigo2;
	}

	public Object3D getEnemigo3() {
		return enemigo3;
	}

	public Object3D getEnemigo4() {
		return enemigo4;
	}

	public Object3D getEnemigo5() {
		return enemigo5;
	}

	public int getVida() {
		return vida;
	}

	public void generarEnemigos() {
		enemigo1 = Primitives.getCube(7);
		enemigo1.strip();
		enemigo1.build();
		float xa = (float) (Math.random() * (100));
		float ya = (float) (Math.random() * (-130));
		enemigo1.translate(xa, ya, 0);

		enemigo2 = Primitives.getCube(7);
		enemigo2.strip();
		enemigo2.build();
		xa = (float) (Math.random() * -(100));
		ya = (float) (Math.random() * (-90));
		enemigo2.translate(xa, ya, 0);

		enemigo3 = Primitives.getCube(7);
		enemigo3.strip();
		enemigo3.build();
		xa = (float) (Math.random() * (50));
		ya = (float) (Math.random() * (-150));
		enemigo3.translate(xa, ya, 0);

		enemigo4 = Primitives.getCube(7);
		enemigo4.strip();
		enemigo4.build();
		xa = (float) (Math.random() * (40));
		ya = (float) (Math.random() * (-110));
		enemigo4.translate(xa, ya, 0);

		enemigo5 = Primitives.getCube(7);
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
	}

}
