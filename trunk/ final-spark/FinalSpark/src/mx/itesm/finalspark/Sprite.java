package mx.itesm.finalspark;

import android.app.Activity;

import com.threed.jpct.Object3D;

public class Sprite extends Activity{
	int positionX,positionY,speedX, centerX, centerY;
	private Object3D nave;
	
	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

}
