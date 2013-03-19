package qmips.presentation.swing.fancy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import qmips.compiler.MIPSCompiler;
import qmips.devices.Device;
import qmips.devices.clock.Clock;
import qmips.devices.control.ControlUnit;
import qmips.devices.memory.IMemory;
import qmips.others.Bus;
import qmips.presentation.builders.Builder;
import qmips.sync.PoolSync;
import qmips.sync.SyncShortcut;

public class MainWindowController implements MainWindow.Controller {

	private Builder builder;
	private boolean systemBuilt = false;
	private ControlUnit control;
	private Bus rst;
	private Clock clk;
	private IMemory instr;
	private MainWindow view;
	private Thread simulationThread;
	private Thread clockThread;
	private File compilerFile;

	public MainWindowController(Builder builder) {
		this.builder = builder;
		view = new MainWindow(this);
	}

	@Override
	public void runOneCycle() {
		if (testBuild()) {
			simulationThread = new Thread(new Runnable() {
				@Override
				public void run() {
					if (control.checkTrap() == -1 && !Thread.interrupted()) {
						clk.runCycles(1);
					} else
						Log.inf.println("Program terminated with code: "
								+ control.checkTrap());
					view.hideModalInfo();
				}
			});
			simulationThread.start();
			view.displayModalInfo("Simulating...", true);
		}
	}

	@Override
	public void runCycles(int cycles) {
		final int c = cycles;
		if (testBuild()) {
			simulationThread = new Thread(new Runnable() {
				@Override
				public void run() {
					int num = c;
					while (control.checkTrap() == -1 && num > 0 && !Thread.interrupted()) {
						clk.runCycles(1);
						num--;
					}
					if (control.checkTrap() != -1)
						Log.inf.println("Program terminated with code: "
								+ control.checkTrap());
					view.hideModalInfo();
				}
			});
			simulationThread.start();
			view.displayModalInfo("Simulating...", true);
		}

	}

	@Override
	public void resetSignal() {
		if (testBuild()) {
			rst.write(1,1);
			SyncShortcut.sync.taskEnded();
			synchronized(SyncShortcut.sync){
				try {
					SyncShortcut.sync.clockLockWait();
				} catch (InterruptedException e) {}
				rst.write(0,1);
			}
			clk.setCycleCount(0);
		}
	}

	@Override
	public void buildSystem() {
		Log.inf.println("Building system...");
		long t = System.currentTimeMillis();
		builder.build();
		this.clk = builder.getClock();
		this.rst = builder.getResetBus();
		this.control = builder.getControlUnit();
		this.instr = builder.getInstrMemory();
		Map<String, JPanel> views = new HashMap<String, JPanel>();
		for(Entry<String, Device> e: builder.getDisplayableDevices().entrySet()){
			views.put(e.getKey(), e.getValue().display());
		}
		view.displayDevicesViews(views);
		this.clockThread = builder.getClock().startRunning();
		systemBuilt = true;
		Log.inf.println("System successfully built in "
				+ (System.currentTimeMillis() - t) + " miliseconds.");
	}

	@Override
	public void dismountSystem() {
		if (testBuild()) {
			systemBuilt = false;
			Log.inf.println("System dismounted.");
		}
	}

	@Override
	public boolean loadSource(File file, int start) {
		if (testBuild()) {
			compilerFile = file;

			new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					boolean err = false;
					Log.inf.println("Compiling file: \""
							+ compilerFile.getName() + "\"...");
					long t = System.currentTimeMillis();
					try {
						MIPSCompiler.compile(compilerFile, instr);
					} catch (Exception e) {
						err = true;
						Log.err.println("Compilation error: " + e.getMessage());
					}
					if (!err)
						Log.inf.println("Successful compilation in: "
								+ (System.currentTimeMillis() - t)
								+ " miliseconds.");
					return null;
				}

				protected void done() {
					view.hideModalInfo();
				}

			}.execute();

			view.displayModalInfo("Compiling...", false);
			return true;
		}else return false;
	}

	@Override
	public void runUntilTrap() {
		if (testBuild()) {
			simulationThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (control.checkTrap() == -1 && !Thread.interrupted()) {
						clk.runCycles(1);
					}
					Log.inf.println("Program terminated with code: "
							+ control.checkTrap());
					view.hideModalInfo();
				}
			});
			simulationThread.start();
			view.displayModalInfo("Simulating...", true);
		}
	}

	@Override
	public boolean buildAndLoadSource(File file, int start) {
		buildSystem();
		return loadSource(file, start);
	}

	private boolean testBuild() {
		if (!systemBuilt)
			Log.err.println("System hasn't been built. Build it first.");
		return systemBuilt;
	}

	@Override
	public void killSimulationThread() {
		SyncShortcut.sync.terminate();
		clockThread.interrupt();
		simulationThread.interrupt();
		SyncShortcut.sync = new PoolSync();
		clockThread = clk.startRunning();
		view.hideModalInfo();
	}

}
