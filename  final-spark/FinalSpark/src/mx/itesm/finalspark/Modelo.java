package mx.itesm.finalspark;

import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.util.Log;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;

public class Modelo
{
	public static Object3D cargarModelo(Context contexto, String archivo, String textura) {
	Object3D modelo=null;
			try {
				InputStream is = contexto.getAssets().open(archivo);
				Object3D[] objetos = Loader.loadOBJ(is, null, 10);
				modelo = objetos[0];
				modelo.calcTextureWrapSpherical();
				if ( textura!=null ) {
					modelo.setTexture(textura);
				}
				modelo.strip();
				modelo.build();
				is.close();
			} catch (IOException e) {
				Log.d("Modelo.cargarModelo()", "No existe el archivo: "+archivo);
			}
		return modelo;
	}
	
	public static Object3D cargarModeloMTL(Context contexto, String archivo, String material,int escala) {
		Object3D modelo=null;
		try {
			InputStream is = contexto.getAssets().open(archivo);
			InputStream isMat = null;
			if ( material!=null) {
				isMat = contexto.getAssets().open(material);
			}
			Object3D[] objetos = Loader.loadOBJ(is, isMat, escala);
			modelo = new Object3D(0);
modelo = new Object3D(0);
// Carga TODOS los objetos
for (Object3D obj : objetos) {
  modelo = Object3D.mergeObjects(modelo, obj);
  modelo.build();
}
modelo.strip();
modelo.build();
is.close();
} catch (IOException e) {
Log.d("Modelo.cargarModelo()", "No existe el archivo: "+archivo);
}
return modelo;
}

public static Object3D cargarModelo3DS(Context contexto, String archivo, int escala) {
Object3D modelo=null;
try {
InputStream is = contexto.getAssets().open(archivo);
Object3D[] objetos = Loader.load3DS(is, escala);
modelo = objetos[0];
modelo.strip();
modelo.build();
is.close();
} catch (IOException e) {
Log.d("Modelo.cargarModelo()", "No existe el archivo: "+archivo);
}
return modelo;
}
}
