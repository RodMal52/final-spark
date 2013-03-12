package mx.itesm.finalspark;

import com.threed.jpct.Object3D;

public class Projectile extends Character{
	private int x, y, speedY,speedX;
	private boolean visible;
	private int damage;
	private Object3D objProjectile;
    int facing;
	
	public Projectile(int startX, int startY){
		x = startX;
		y = startY;
		speedY = 7;
		objProjectile = Modelo.cargarModeloMTL(getBaseContext(),
				"projectile.obj","projectile.mtl",  1);
		visible = true;
		
	}
	
	/*public void move(float interpolation) {

		switch(facing) {
	      case 0: y += speedY*interpolation; break;
	      case 1: y += speedY*Math.sin(Math.PI/4)*interpolation;
	            x -= speedX*Math.cos(Math.PI/4)*interpolation;
	            break;
	      case 2: x -= speedX*interpolation; break;
	      case 3: y -= speedY*Math.sin(Math.PI/4)*interpolation;
	            x -= speedX*Math.cos(Math.PI/4)*interpolation;
	            break;
	      case 4: y -= speedY*interpolation; break;
	      case 5: y -= speedY*Math.sin(Math.PI/4)*interpolation;
	            x += speedX*Math.cos(Math.PI/4)*interpolation;
	            break;
	      case 6: x += speedX*interpolation; break;
	      case 7: y += speedY*Math.sin(Math.PI/4)*interpolation;
	            x += speedX*Math.cos(Math.PI/4)*interpolation;
	            break;
	      }
	   }*/

	public void update(){
		x += speedY;
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

	public int getspeedY() {
		return speedY;
	}

	public void setspeedY(int speedY) {
		this.speedY = speedY;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
