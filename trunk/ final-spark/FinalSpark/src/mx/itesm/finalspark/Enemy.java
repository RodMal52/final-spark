package mx.itesm.finalspark;
import java.util.ArrayList;
import java.lang.reflect.Field;

import com.threed.jpct.Object3D;

public class Enemy extends Character implements Move{
	
		private int centerX;//son las coordenadas en x del centro del enemigo
		private int centerY;//son las coordenadas en y del centro del enemigo
		private int speedX;//velocidad con la que cambia centerX
		private int speedY;//velocidad con la que cambia centerY
		private static final int RIGHT_WALL = 200;//Limites que no se cuales son
		private static final int LEFT_WALL = 1;
		private static final int DOWN_WALL = 200;
		private static final int UP_WALL = 1;
		private int type;
		private int x,y;
		private Object3D objEnemy;
		
		public void move(){
			
		    x += speedX;
		    y += speedY;
		    if (x >= RIGHT_WALL)
		    {
		        x = RIGHT_WALL;
		        moveRandomDirection();
		    }
		    if (y > DOWN_WALL)
		    {
		        y = DOWN_WALL;
		        moveRandomDirection();
		    }
		    if (x <= LEFT_WALL)
		    {
		        x = LEFT_WALL;
		        moveRandomDirection();
		    }
		    if (y < UP_WALL)
		    {
		        y = UP_WALL;
		        moveRandomDirection();
		    }
		}

		public void moveRandomDirection()
		{
		    double direction = Math.random() * 2.0 * Math.PI;
		    double speed = 10.0;
		    speedX = (int) (speed * Math.cos(direction));
		    speedY = (int) (speed * Math.sin(direction));
		}
		
		public  Object Load_enemy(int type){
			if(type == 1){
			objEnemy = Modelo.cargarModeloMTL(getBaseContext(),
					"enemy1.obj","objeto.mtl",  1);
			
			}else if(type == 2){
				objEnemy = Modelo.cargarModeloMTL(getBaseContext(),
						"enemy2.obj","objeto.mtl", 1);
			}else{
				objEnemy = Modelo.cargarModeloMTL(getBaseContext(),
						"enemy3.obj", "objeto.mtl", 1);//a todos les falta el objeto y la textura
			}
			return objEnemy;		
		}
		
		private void Start () {
			 
		}
		
		

		public void moveRight() {
			speedX = 6;
		}

		public void moveLeft() {
			speedX = -6;
		}

		public void stop() {
			speedX = 0;
		}

		private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
		
		public void shoot() {
			int a = 0,b =0;
			Projectile p = new Projectile(centerX + a, centerY + b);//a y b son las coordenadas de donde este el canon de la nave
			projectiles.add(p);
		}
		
		public ArrayList getProjectiles() {
			return projectiles;
		}
	
}
