package qmips.presentation.swing.fancy;

import java.awt.Dialog;
import java.io.File;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import qmips.compiler.MIPSCompiler;
import qmips.devices.Device;
import qmips.devices.clock.Clock;
import qmips.devices.control.ControlUnit;
import qmips.devices.memory.IMemory;
import qmips.others.Bus;
import qmips.presentation.builders.Builder;

public class MainWindowController implements MainWindow.Controller {

	private Builder builder;
	private boolean systemBuilt = false;
	private ControlUnit control;
	private Bus rst;
	private Clock clk;
	private IMemory instr;
	private MainWindow view;
	private Thread simulationThread;
	private File compilerFile;

	public MainWindowController(Builder builder) {
		this.builder = builder;
		view = new MainWindow(this);
	}

	@Override
	public void runOneCycle() {
		if (testBuild()) {
			simulationThread = new Thread(new Runnable(){
				@Override
				public void run() {
					view.displayModalInfo("Simulating...", true);
					System.out.println("hola");
					System.out.flush();
					if (control.checkTrap() == -1) {
						clk.runCycles(1);
					}
					Log.inf.println("Program terminated with code: " + control.checkTrap());
					view.hideModalInfo();
				}
			});
			simulationThread.start();
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
		this.instr = builder.getInstrMemory();
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
			compilerFile = file;
			new Thread(new Runnable(){

				@Override
				public void run() {
					boolean err = false;
					Log.inf.println("Compiling file: \"" + compilerFile.getName() + "\"...");
					long t = System.currentTimeMillis();
					try{
						view.displayModalInfo("Compiling...", false);
						MIPSCompiler.compile(compilerFile, instr);
						view.hideModalInfo();
					}catch(Exception e){
						err = true;
						Log.err.println("Compilation error: " + e.getMessage());
					}
					if(!err)
						Log.inf.println("Successful compilation in: " + (System.currentTimeMillis() - t) + " miliseconds.");
				}
				
			}).start();
			
		}
	}

	@Override
	public void runUntilTrap() {
		if (testBuild()) {
			simulationThread = new Thread(new Runnable(){
				@Override
				public void run() {
					view.displayModalInfo("Simulating...", true);
					while (control.checkTrap() == -1) {
						clk.runCycles(1);
					}
					Log.inf.println("Program terminated with code: " + control.checkTrap());
					view.hideModalInfo();
				}
			});
			simulationThread.start();
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

	@Override
	public void killSimulationThread() {
		simulationThread.interrupt();
		view.hideModalInfo();
	}
	

}
