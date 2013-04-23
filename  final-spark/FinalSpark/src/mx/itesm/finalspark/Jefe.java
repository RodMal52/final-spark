package mx.itesm.finalspark;



import java.util.ArrayList;


import android.content.Context;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;

public class Jefe extends Enemigo{

    private float velocidadX;
    private float velocidadY;

public Jefe(int dano, int vida, Context contexto) {
	this.dano= dano;
	this.vida = vida;
	velocidadX = (float) 1.5;
	velocidadY = (float) 1.5;
	arregloDeProyectiles = new ArrayList<Object3D>();
	enemigo = Modelo.cargarModelo(contexto,
			"mantis.obj", null);
	enemigo.rotateX(3.141592f / 2);
	enemigo.scale(0.25f);
	enemigo.translate(0, -90, 0);
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
                    if (enemigo.getTransformedCenter().x < 100
                                    && enemigo.getTransformedCenter().x > -100
                                    && enemigo.getTransformedCenter().y < 0
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
                                    } else if (enemigo.getTransformedCenter().y > 0) {
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
                                    } else if (enemigo.getTransformedCenter().y > 0) {
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
                            if (enemigo.getTransformedCenter().y > 0) {
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
                misil = Primitives.getCube((float) 1.0);
                misil.strip();
                misil.build();
                misil.setOrigin(enemigo.getTransformedCenter());
                misil.translate(-9, -8, 0);

                misil1 = Primitives.getCube((float) 1.0);
                misil1.strip();
                misil1.build();
                misil1.setOrigin(enemigo.getTransformedCenter());
                misil1.translate(9, -8, 0);

                misil2 = Primitives.getCube((float) 1.0);
                misil2.strip();
                misil2.build();
                misil2.setOrigin(enemigo.getTransformedCenter());
                misil2.translate(-25, 6, 0);
                
                misil3 = Primitives.getCube((float) 1.0);
                misil3.strip();
                misil3.build();
                misil3.setOrigin(enemigo.getTransformedCenter());
                misil3.translate(25, 6, 0);
                arregloDeProyectiles.add(misil);// Agrega a la el misileto
                arregloDeProyectiles.add(misil1);
                arregloDeProyectiles.add(misil2);
                arregloDeProyectiles.add(misil3);
            }
    }

    public Object3D getMisil() {
            return misil;
    }

	
}