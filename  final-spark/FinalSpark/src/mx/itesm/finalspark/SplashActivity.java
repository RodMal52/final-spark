package mx.itesm.finalspark;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;


/**
 * Muestra la pantalla de splash al iniciar la aplicacion.
 */
public class SplashActivity extends Activity {
	protected int _splashTime = 1000;
	private Thread splashTread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		 final SplashActivity sPlashScreen = this; 

         // thread for displaying the SplashScreen
         splashTread = new Thread() {
             @Override
             public void run() {
                 try {
                     synchronized(this){

                             //wait 5 sec
                             wait(_splashTime);
                     }

                 } catch(InterruptedException e) {}
                 finally {
                     finish();

                     //start a new activity
                     Intent i = new Intent();
                     i.setClass(sPlashScreen, MenuActivity.class);
                             startActivity(i);

                   //  stop();
                 }
             }
         };

         splashTread.start();

	}

	

	

}
