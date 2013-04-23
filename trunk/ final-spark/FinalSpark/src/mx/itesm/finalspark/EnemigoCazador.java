package mx.itesm.finalspark;

import java.util.ArrayList;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;

public class EnemigoCazador extends Enemigo {
	public EnemigoCazador(int dano, int vida) {
		this.dano= dano;
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

	public Object3D getMisil() {
		return misil;
	}

}