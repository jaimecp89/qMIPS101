package qmips.devices.control;

/**
 * 
 * Interfaz comun para las interfaces visuales de las unidades
 * de control.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public interface IControlUnitDisplay {

	void setState(String state);
	
	void setDescription(String description);
	
	void setState(String state, String description);
	
}
