package qmips.devices.clock;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import qmips.others.Bus;
import qmips.others.LogicVector;
import qmips.sync.SyncShortcut;

/**
 * Representa el reloj principal del sistema. No es hijo de la clase Device
 * porque es un dispositivo especial, que debe ser despertado en el momento
 * oportuno y sin ninguna excitacion externa. De esto se encargan las clases
 * Synchronization.
 * 
 * @author Jaime Coello de Portugal
 * 
 */
public class Clock implements Runnable {

	private Bus clk;
	private LogicVector lv;
	private int cycleCount = 0, semicycle = 0, remainingCycles = 0;
	private ClockFrame disp;

	public Clock(Bus clk) {
		this.clk = clk;
		this.disp = new ClockFrame();
		lv = new LogicVector(1);
	}

	/**
	 * 
	 * @return el numero de semiciclos transcurridos desde el ultimo reset.
	 */
	public int getCycleCount() {
		return cycleCount;
	}

	/**
	 * 
	 * @param cycleCount
	 *            el nuevo numero de semiciclos al que se quiera llevar el
	 *            reloj.
	 */
	public void setCycleCount(int cycleCount) {
		this.cycleCount = cycleCount;
		disp.cycleNumberLabel.setText(cycleCount + "");
	}

	/**
	 * Este metodo se ejecuta cada vez que el reloj es despertado y se encarga
	 * de dar el flanco correspondiente en el bus clk.
	 * 
	 */
	public void refreshOutput() {
		if (semicycle == 0) {
			lv.set(0, false);
			clk.write(lv);
			semicycle++;
		} else {
			lv.set(0, true);
			clk.write(lv);
			semicycle--;
			cycleCount++;
			disp.getCycleNumberLabel().setText(cycleCount + "");
		}
	}

	public boolean endCondition() {
		return true;
	}

	/**
	 * 
	 * Tarea principal del hilo reloj. Mientras se le permita avanzar llama a ta
	 * tarea que da el flanco de reloj y se duerme a la espera de que la
	 * sincronizacion le despierte.
	 * 
	 */
	@Override
	public synchronized void run() {
		while (endCondition()) {
			try {
				while (remainingCycles == 0) {
					wait();
				}
				refreshOutput();
				SyncShortcut.sync.taskEnded();
				SyncShortcut.sync.clockLockWait();
				remainingCycles--;
			} catch (InterruptedException e) {
				remainingCycles = 0;
				return;
			}
		}
		SyncShortcut.sync.terminate();
	}

	/**
	 * Llamar a este metodo para iniciar el funcionamiento del reloj y por tanto
	 * del todo el simulador.
	 * 
	 * @return La referencia al hilo reloj.
	 */
	public Thread startRunning() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}

	/**
	 * Permite al reloj correr el numero de ciclos indicado.
	 * 
	 * @param cycleNum
	 *            Numero de ciclos que se quiere que el reloj ejecute
	 *            automaticamente.
	 * 
	 */
	public synchronized void runCycles(int cycleNum) {
		remainingCycles = cycleNum * 2;
		notifyAll();
	}

	/**
	 * 
	 * Interfaz basica del reloj. Muestra simplemente un numero indicando el
	 * ciclo de reloj actual.
	 * 
	 * @author Jaime Coello de Portugal
	 * 
	 */
	class ClockFrame extends JPanel {
		public ClockFrame() {

			setLayout(new BorderLayout());
			JLabel lblNewLabel = new JLabel("Cycle number");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			add(lblNewLabel, BorderLayout.NORTH);

			cycleNumberLabel = new JLabel("0");
			cycleNumberLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
			cycleNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
			add(cycleNumberLabel, BorderLayout.CENTER);
			setVisible(true);
			setPreferredSize(new Dimension(150, 100));
		}

		private static final long serialVersionUID = 9048677220479688042L;
		private JLabel cycleNumberLabel;

		public JLabel getCycleNumberLabel() {
			return cycleNumberLabel;
		}
	}

	public JPanel getDisplay() {
		return disp;
	}

}