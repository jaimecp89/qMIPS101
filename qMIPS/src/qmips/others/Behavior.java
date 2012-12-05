package qmips.others;

import qmips.sync.SyncShortcut;

/**
 * 
 * Clase abstracta para definir las tareas de cada
 * dispositivo.
 * Avisa a la sincronizacion al terminar la tarea.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public abstract class Behavior implements Runnable{

	public void run(){
		try{
			task();
		}catch(Exception e){
			
		}
		SyncShortcut.sync.taskEnded();
	}
	
	public abstract void task();
	
}
