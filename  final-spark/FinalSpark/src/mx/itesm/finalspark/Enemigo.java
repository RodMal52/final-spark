package mx.itesm.finalspark;

import java.util.ArrayList;

import com.threed.jpct.Object3D;

public class Enemigo {
	public int vida;
	public int dano;
	public Object3D enemigo;
	public boolean enemigoExiste;
	public Object3D misil;
	public ArrayList<Object3D> arregloDeProyectiles;

	public Enemigo() {

	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getDano() {
		return dano;
	}

	public Object3D getEnemigo() {
		return enemigo;
	}

	public int getVida() {
		return vida;
	}

	public void mover(Object3D object) {
	}

	public void disparar() {
	}

	public void danar(int danoRecibido) {
		vida = vida - danoRecibido;
		if (vida <= 0) {
			enemigoExiste = false;
		}
	}

}