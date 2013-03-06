package mx.itesm.finalspark;
import com.threed.jpct.Object3D;

public class Character {

	private int maxHealth, currentHealth, power, speedX, centerX, centerY;
	private Object3D nave;
		
		
	public int getMaxHealth() {
		return maxHealth;
	}

		public void setMaxHealth(int maxHealth) {
			this.maxHealth = maxHealth;
		}

		public int getCurrentHealth() {
			return currentHealth;
		}

		public void setCurrentHealth(int currentHealth) {
			this.currentHealth = currentHealth;
		}

		public int getPower() {
			return power;
		}

		public void setPower(int power) {
			this.power = power;
		}

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

		public void die() {
		}
		public void attack() {
		}
	}

