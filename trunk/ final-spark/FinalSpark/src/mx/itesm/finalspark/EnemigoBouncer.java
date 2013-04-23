package mx.itesm.finalspark;

import java.util.ArrayList;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;

public class EnemigoBouncer extends Enemigo {
	private int velocidadX;
	private int velocidadY;

	public EnemigoBouncer(int dano, int vida) {
		this.dano= dano;
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

	public Object3D getMisil() {
		return misil;
	}

}