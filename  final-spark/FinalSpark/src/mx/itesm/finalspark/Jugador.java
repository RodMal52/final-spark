package mx.itesm.finalspark;

import android.content.Context;

import com.threed.jpct.Object3D;

public class Jugador {
	private int daño=10;
	private int vida = 100;
	private Object3D objNave;
	
	public Jugador(Context contexto){
		objNave = Modelo.cargarModeloMTL(contexto,
				"freedom3000.obj", "freedom3000.mtl", 1);
		objNave.rotateY(3.141592f);
		objNave.rotateX((float) (1.5));
	}
	
	public Object3D getObjNave() {
		return objNave;
	}
	
	public void mover(float offsetHorizontal, float offsetVertical){
		
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
