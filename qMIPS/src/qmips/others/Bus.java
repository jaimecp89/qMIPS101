package qmips.others;

import java.util.HashSet;
import java.util.Set;

import qmips.sync.SyncShortcut;

/**
 * 
 * Clase para definir un bus de cualquier tamaño.
 * 
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class Bus {

	private Bus parentBus = null;
	private Set<Runnable> processes;
	private LogicVector content;
	private boolean trace = false;
	private String name;

	public Bus(int size) {
		processes = new HashSet<Runnable>();
		content = new LogicVector(size);
	}

	public Bus(LogicVector content) {
		processes = new HashSet<Runnable>();
		this.content = content;
	}

	public Bus(int content, int size) {
		this(new LogicVector(content, size));
	}

	public Bus addProcess(Runnable process) {
		processes.add(process);
		if (parentBus != null)
			parentBus.addProcess(process);
		return this;
	}

	public Bus getRange(int from, int to) {
		Bus res = new Bus(content.get(from, to));
		res.parentBus = this;
		for (Runnable r : processes)
			res.addProcess(r);
		return res;
	}

	public LogicVector read() {
		LogicVector res = new LogicVector(this.size());
		for (int i = 0; i < this.size(); i++)
			res.set(i, content.get(i));
		return res;
	}
	
	public synchronized void write(int value, int size){
		write(new LogicVector(value, size));
	}

	public synchronized void write(LogicVector in) {
		if (trace)
			System.out.println("[" + name + "] " + " written: " + in);
		if (!in.equals(content)) {
			for (int i = 0; i < content.size() && i < in.size(); i++)
				content.set(i, in.get(i));
			SyncShortcut.sync.activateProcesses(processes);
		}
	}

	public int size() {
		return content.size();
	}

	public static Bus[] createBusArray(int busSize, int arraySize) {
		Bus[] res = new Bus[arraySize];
		for (int i = 0; i < arraySize; i++)
			res[i] = new Bus(busSize);
		return res;
	}
	
	public Bus trace(String name){
		trace = true;
		this.name = name;
		return this;
	}
	
	public void untrace(){
		trace = false;
	}

}
