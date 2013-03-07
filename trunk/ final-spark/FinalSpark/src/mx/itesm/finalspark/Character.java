package mx.itesm.finalspark;

import android.app.Activity;

import com.threed.jpct.Object3D;

public class Character extends Sprite {


	private int maxHealth, currentHealth, power;

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

	public void die() {
	}

	public void attack() {
	}
	
}
