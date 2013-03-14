package qmips.presentation.swing.fancy.onyxbits;

import java.awt.*;

/**
 * A component implementing this interface may pass placement hints to the
 * Usher.
 */
public interface PlacementHints {

	/**
	 * Request the component's origin to be placed within certain bounds.
	 * 
	 * @return null to allow free placement or a bounding rectangle. The
	 *         rectangle may be larger than the visual area of the container in
	 *         which case it'll be clipped.
	 */
	public Rectangle desireRegion();

	/**
	 * Request a placement strategy to be used
	 * 
	 * @return the preferred placement strategy to use
	 */
	public int desireStrategy();
}
