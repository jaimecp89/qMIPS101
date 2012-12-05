package qmips.sync;

import java.util.Set;

/**
 * 
 * Esta interfaz define el comportamiento requerido a una clase
 * de sincronizacion, para poder definir varias.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public interface Synchronization {

	/**
	 * 
	 * Lo invocan los buses cuando se se escribe sobre ellos para
	 * pedir a la sincronizacion que se activen las tareas que le
	 * tienen en su lista de sensibilidad.
	 * 
	 * @param processes Las tareas que se deben activar.
	 */
	public void activateProcesses(Set<Runnable> processes);
	
	/**
	 * 
	 * Le indica a la sincronizacion que una tarea ha terminado.
	 * 
	 */
	public void taskEnded();
	
	/**
	 * 
	 * Invocado por el reloj para esperar en el monitor de la sincronizacion
	 * a que todas las tareas hayan concluido.
	 * 
	 */
	public void clockLockWait();
	
	/**
	 * 
	 * Indica a la sincronizacion que termine de operar y libere los recursos.
	 * 
	 */
	public void terminate();
	
	/**
	 * Llamado en caso de excepcion en algun hilo de la piscina.
	 * 
	 * @param e La excepcion producida.
	 */
	public void exception(Exception e);
	
}
