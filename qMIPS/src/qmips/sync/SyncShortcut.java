package qmips.sync;

/**
 * 
 * Acceso directo a la sincronizacion.
 * Aqui se define que sincronizacion se quiere usar.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class SyncShortcut {

	public static Synchronization sync = new PoolSync();
	
}
