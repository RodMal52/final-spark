package mx.itesm.finalspark;

import com.threed.jpct.Object3D;

public class Projectile extends Character{
	private int x, y, speedX;
	private boolean visible;
	private int damage;
	private Object3D objProjectile;
	
	
	public Projectile(int startX, int startY){
		x = startX;
		y = startY;
		speedX = 7;
		objProjectile = Modelo.cargarModeloMTL(getBaseContext(),
				"projectile.obj","projectile.mtl",  1);
		visible = true;
		
	}

	public void update(){
		x += speedX;
		if (x > 1000) { // puse 1000 porque no se como poner el height del dispositivo
		   visible = false;
		}

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
