package mx.itesm.finalspark;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

public class StatsDrop {
	private int danoAdicional;
	private int vidaAdicional;
	public Object3D statBox;
	public boolean statBoxExiste;
	
	public StatsDrop(int danoAdicional, int vidaAdicional, SimpleVector origin) {
		this.danoAdicional=danoAdicional;
		this.vidaAdicional=vidaAdicional;
		statBox = Primitives.getPyramide(5);
		statBox.strip();
		statBox.build();
		statBox.setOrigin(origin);
		statBoxExiste =true;
	
				
	}
	
	public int getVidaAdicional() {
		return vidaAdicional;
	}
	public int getDanoAdicional() {
		return danoAdicional;
	}
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
