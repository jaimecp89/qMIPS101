package qmips.presentation.swing.fancy.onyxbits;

import java.awt.*;
import java.awt.event.*;

/**
 * 
 * @author Onyxbits (http://www.onyxbits.de/content/swing-mdi-coming-window-placement-algorithm-jinternalframes-jdesktoppanes)
 *
 * A simple window placement algorithm for "smartly" positioning placing
 * components inside a MDI style container.
 */
public class Usher implements ContainerListener {

	/**
	 * Spacing between components when tiling
	 */
	public static final int PADDING = 1;

	/**
	 * Distance between anchor points when cascading
	 */
	public static final int OFFSET = 34;

	/**
	 * Placement strategy: Skip component
	 */
	public static final int STRATEGY_IGNORE = 0;

	/**
	 * Placement strategy: Only tile
	 */
	public static final int STRATEGY_TILE = 1;

	/**
	 * Placement strategy: Only cascade
	 */
	public static final int STRATEGY_CASCADE = 2;

	/**
	 * Placement strategy: Tile first, then cascade
	 */
	public static final int STRATEGY_BOTH = 3;

	/** {@inheritDoc} */
	public void componentAdded(ContainerEvent e) {
		Component all[] = e.getContainer().getComponents();
		Component target = e.getChild();
		Point start = new Point(0, 0);
		Dimension size = e.getContainer().getSize();
		int strategy = STRATEGY_BOTH;
		Point p = null;

		// Maybe the component has preferences concerning it's placement?
		if (target instanceof PlacementHints) {
			Rectangle r = ((PlacementHints) target).desireRegion();
			if (r != null) {
				if (r.x > 0)
					start.x = r.x;
				else
					start.x = 0;

				if (r.y > 0)
					start.y = r.y;
				else
					start.y = 0;

				if (r.width + r.x < size.width)
					size.width = r.width;
				if (r.height + r.y < size.height)
					size.height = r.height;
			}
			strategy = ((PlacementHints) target).desireStrategy();
		}

		switch (strategy) {
		case STRATEGY_TILE: {
			p = descend(new boolean[all.length][2], size, all, target, start);
			break;
		}
		case STRATEGY_CASCADE: {
			p = cascade(start, size, all, target);
			break;
		}
		case STRATEGY_BOTH: {
			p = descend(new boolean[all.length][2], size, all, target, start);
			if (p == null)
				p = cascade(start, size, all, target);
			break;
		}
		}

		if (p != null)
			target.setLocation(p.x, p.y);
		else {
			// We tried our best but it wasn't good enough
		}
	}

	/** {@inheritDoc} */
	public void componentRemoved(ContainerEvent e) {
	}

	/**
	 * Recursively hop from component to component and try to find free space
	 * next to it.
	 * 
	 * @param visited
	 *            a boolean array that serves as a safeguard against going into
	 *            infinite recursions when frames overlap.
	 * @param size
	 *            region bounds
	 * @param all
	 *            all components inside the container
	 * @param target
	 *            the component to find a free place for
	 * @param test
	 *            The point to test for being free.
	 * @return Either the point to put the target on or null if no free space
	 *         can be found.
	 */
	private Point descend(boolean[][] visited, Dimension size, Component[] all,
			Component target, Point test) {
		Rectangle bounds = new Rectangle(target.getBounds());
		bounds.x = test.x;
		bounds.y = test.y;
		Point ret = new Point(test);

		// We are not in Kansas anymore... report dead end
		if (bounds.x + bounds.width > size.width
				|| bounds.y + bounds.height > size.height) {
			return null;
		}

		for (int i = 0; i < all.length; i++) {
			if (all[i] == target)
				continue;

			Rectangle tmp = all[i].getBounds();
			if (bounds.intersects(tmp)) {
				// We are touching another component. Recursively test it's NE
				// and SW corner until
				// we either find free space next to it or are leaving Kansas.
				Point ne = null;
				Point sw = null;
				ret = null;

				if (!visited[i][0]) {
					visited[i][0] = true; // Mark this path as taken
					ne = descend(visited, size, all, target, new Point(tmp.x
							+ tmp.width + 1 + PADDING, tmp.y));
				}
				if (!visited[i][1]) {
					visited[i][1] = true; // Mark this path as taken
					sw = descend(visited, size, all, target, new Point(tmp.x,
							tmp.y + tmp.height + 1 + PADDING));
				}

				ret = decide(ne, sw);
				if (ret != null) {
					break; // We found some free space. No need to look any
							// further.
				}
			}
		}

		// Just in case the user moved a window partly outside the visible area.
		// This might result in overlapping but thats still better than loosing
		// small frames north or west of Kansas.
		if (ret != null && ret.x < 0)
			ret.x = 0;
		if (ret != null && ret.y < 0)
			ret.y = 0;

		return ret;
	}

	/**
	 * Take two points and return the one fitting "best".
	 * 
	 * @param a
	 *            a point or null
	 * @param b
	 *            a point or null
	 * @return the "better" point or null if a and b are null.
	 */
	private Point decide(Point a, Point b) {
		if (a == null && b == null)
			return null;
		if (a == null)
			return b;
		if (b == null)
			return a;
		if (a.y < b.y)
			return a;
		else
			return b;
	}

	/**
	 * Hop to equispaced anchor points along a diagonal line, trying to find a
	 * free one.
	 * 
	 * @param start
	 *            region bounds
	 * @param size
	 *            region bounds
	 * @param all
	 *            all components in the container
	 * @return a free point to place the component on or null if none is found
	 *         within the bounds.
	 */
	private Point cascade(Point start, Dimension size, Component[] all,
			Component target) {
		boolean free = false;
		Point ret = new Point(start);

		while (!free) {
			free = true;
			for (int i = 0; i < all.length; i++) {
				if (all[i] == target)
					continue;
				Rectangle r = all[i].getBounds();
				if (r.x == ret.x && r.y == ret.y) {
					// Position taken - try next one
					ret.x += OFFSET;
					ret.y += OFFSET;

					if (ret.x + OFFSET > size.width
							|| ret.y + OFFSET > size.height) {
						return null; // We are not in Kansas any more
					}

					free = false;
					break;
				}
			}
		}
		return ret;
	}
}
