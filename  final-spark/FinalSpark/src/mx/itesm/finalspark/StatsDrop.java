package mx.itesm.finalspark;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

/**
 * Clase que inicializa el modelo 3D que se asociara a los drops que dejen los jefes, 
 * asi como las estadisticas que mejoraran del jugador.
 */
public class StatsDrop {
	private int danoAdicional;
	private int vidaAdicional;
	public Object3D statBox;
	public boolean statBoxExiste;
	
	
	/**
	 * Inicializa el modelo del drop a una piramide, asigna los valores de danoAdicional y
	 * de vidaAdicional para el jugador  de acuerdo a parametros recibidos y asigna la posicion 
	 * del drop a la misma de donde murio el jefe.
	 * 
	 * @param danoAdicional Dano que se aumentara a las estadisticas de los disparos del jugador. 
	 * @param vidaAdicional Vida que se aumentara a las estadisticas del jugador.
	 * @param origin Punto donde aparecera el objeto 3D que representara al drop en el mundo.
	 */
	public StatsDrop(int danoAdicional, int vidaAdicional, SimpleVector origin) {
		this.danoAdicional=danoAdicional;
		this.vidaAdicional=vidaAdicional;
		statBox = Primitives.getPyramide(5);
		statBox.strip();
		statBox.build();
		statBox.setOrigin(origin);
		statBoxExiste =true;
	
				
	}
	
	/**
	 * Regresa la vida que le aumentara al jugador al ser tomado.
	 * 
	 * @return vidaAdicional
	 */
	public int getVidaAdicional() {
		return vidaAdicional;
	}
	
	/**
	 * Regresa el dano que le aumentara al jugador al ser tomado.
	 * 
	 * @return danoAdicional
	 */
	public int getDanoAdicional() {
		return danoAdicional;
	}
	
	/**
	 * Revisa si el jugador toma (colisiona con) el drop de estadisticas para que, 
	 * en caso de ser asi, aumente los valores de vida y dano del jugador.
	 * 
	 * @param jugador Objeto 3D que represental al jugador en el mundo.
	 */
	public void incrementarStats(Jugador jugador){
		if(statBox.getTransformedCenter().x + 10 == jugador.getObjNave().getTransformedCenter().x &&
				statBox.getTransformedCenter().x - 10 == jugador.getObjNave().getTransformedCenter().x &&
				statBox.getTransformedCenter().y + 10 == jugador.getObjNave().getTransformedCenter().y &&
				statBox.getTransformedCenter().y - 10 == jugador.getObjNave().getTransformedCenter().y){
			jugador.setVida(jugador.getVida()+vidaAdicional);
			jugador.setDano(jugador.getDano()+danoAdicional);
			statBoxExiste=false;

		}
	}
	
}