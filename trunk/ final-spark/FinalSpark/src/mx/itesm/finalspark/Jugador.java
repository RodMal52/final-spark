package mx.itesm.finalspark;

import java.util.ArrayList;

import android.content.Context;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.World;

public class Jugador {
	private int dano = 5;
	private int vida = 30;
	private Object3D objNave;
	public Object3D misil;
	public Object3D misilIzq;
	public Object3D misilDer;
	public ArrayList<Object3D> arregloDeProyectiles;

	public Jugador(Context contexto) {
		objNave = Modelo.cargarModeloMTL(contexto, "freedom3000.obj",
				"freedom3000.mtl", 1);
		objNave.rotateY(3.141592f);
		objNave.rotateX((float) (1.5));
		arregloDeProyectiles = new ArrayList<Object3D>();
	}

	public Object3D getObjNave() {
		return objNave;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getDano() {
		return dano;
	}

	public int getVida() {
		return vida;
	}

	public void disparar() {
		misil = Primitives.getCube((float) .5);
		misil.strip();
		misil.build();
		misil.setOrigin(objNave.getTransformedCenter());
		misil.translate(0, -2, 0);

		misilIzq = Primitives.getCube((float) .5);
		misilIzq.strip();
		misilIzq.build();
		misilIzq.setOrigin(objNave.getTransformedCenter());
		misilIzq.translate(-9, 1, 0);

		misilDer = Primitives.getCube((float) .5);
		misilDer.strip();
		misilDer.build();
		misilDer.setOrigin(objNave.getTransformedCenter());
		misilDer.translate(9, 1, 0);
		arregloDeProyectiles.add(misil);// Agrega a la el misileto
		arregloDeProyectiles.add(misilIzq);
		arregloDeProyectiles.add(misilDer);
	}

	public void mover(float offsetHorizontal, float offsetVertical) {

		if (objNave.getTransformedCenter().x < 100
				&& objNave.getTransformedCenter().x > -100
				&& objNave.getTransformedCenter().y < 155
				&& objNave.getTransformedCenter().y > -148) {
			if (offsetVertical != 0) {
				objNave.translate(0, offsetVertical, 0);
				offsetVertical = 0;
			}

			if (offsetHorizontal != 0) {
				objNave.translate(offsetHorizontal, 0, 0);
				offsetHorizontal = 0;
			}

		} else {

			if (objNave.getTransformedCenter().x > 100) {

				if (offsetHorizontal < 0) {
					objNave.translate(offsetHorizontal, 0, 0);
					offsetHorizontal = 0;
				}

				if (objNave.getTransformedCenter().y < -148) {

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

			// ---------------------------------------------------------------------------

			if (objNave.getTransformedCenter().x < -100) {

				if (offsetHorizontal > 0) {
					objNave.translate(offsetHorizontal, 0, 0);
					offsetHorizontal = 0;
				}

				if (objNave.getTransformedCenter().y < -148) {

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

			// ---------------------------------------------------------------------------

			if (objNave.getTransformedCenter().y < -148) {

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

			// ---------------------------------------------------------------------------

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
