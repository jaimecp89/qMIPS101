package qmips.presentation.swing.fancy;

import java.io.File;
import java.util.Map;

import qmips.devices.Device;
import qmips.devices.clock.Clock;
import qmips.devices.control.ControlUnit;
import qmips.others.Bus;
import qmips.presentation.builders.Builder;

public class MainWindowController implements MainWindow.Controller {

	private Builder builder;
	private boolean systemBuilt = false;
	private ControlUnit control;
	private Bus rst;
	private Clock clk;
	private MainWindow view;

	public MainWindowController(Builder builder) {
		this.builder = builder;
		view = new MainWindow(this);
	}

	@Override
	public void runOneCycle() {
		if (testBuild()) {

		}
	}

	@Override
	public void runCycles(int cycles) {
		if (testBuild()) {

		}
	}

	@Override
	public void resetSignal() {
		if (testBuild()) {

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
		view.displayDevicesViews(builder.getDisplayableDevices());
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
	public void loadSource(File file, int start) {
		if (testBuild()) {
			
		}
	}

	@Override
	public void runUntilTrap() {
		if (testBuild()) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					while (control.checkTrap() == -1) {
						clk.runCycles(1);
					}
					Log.inf.println("Program terminated with code: " + control.checkTrap());
				}
			}).start();
		}
	}

	@Override
	public void buildAndLoadSource(File file, int start) {
		buildSystem();
		loadSource(file, start);
	}

	private boolean testBuild() {
		if (!systemBuilt)
			Log.err.println("System hasn't been built. Build it first.");
		return systemBuilt;
	}
	

}
