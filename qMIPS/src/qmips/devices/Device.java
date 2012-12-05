package qmips.devices;

import javax.swing.JPanel;

import qmips.others.Behavior;
import qmips.others.Bus;

/**
 * 
 * Esta clase debe ser padre de todos los dispositivos comunes.
 * 
 * 
 * @author Jaime Coello de Portugal
 *
 */
public abstract class Device {
	
	/**
	 * 
	 * Define la reaccion que debe tener el dispositivo a los cambios
	 * en ciertos buses.
	 * Se puede llamar mas de una vez si se quieren definir reacciones distintas
	 * a distintos buses.
	 * 
	 * @param sensivity Los buses a los que reaccionara el dispositivo. Lo que en 
	 * lenguajes de descripcion de hardware se llama "lista de sensibilidad".
	 * @param process El proceso que se ejecutara si cambia alguno de los buses.
	 */
	protected void behavior(Bus[] sensivity, Behavior process){
		for(Bus b : sensivity){
			b.addProcess(process);
		}
	}
	
	/**
	 * 
	 * Metodo para definir el comportamiento del dispositivo. Debe contener llamadas
	 * a behavior(...). Se debe invocar al final del contructor.
	 * 
	 */
	protected abstract void defineBehavior();
	
	/**
	 * Este metodo debe ser sobreescrito por los dispositivos con interfaz.
	 * 
	 * @return La interfaz del dispositivo.
	 */
	public JPanel display(){
		return null;
	}

}
