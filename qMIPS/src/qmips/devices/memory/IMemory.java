package qmips.devices.memory;

import qmips.others.LogicVector;

/**
 * 
 * Interfaz utilizada para decirle al compilador que
 * puede insertar el codigo compilado con la funcion load(...).
 * 
 * @author Jaime Coello de Portugal
 *
 */
public interface IMemory {

	public void load(LogicVector v, int dir);
	
	public int size();
	
}
