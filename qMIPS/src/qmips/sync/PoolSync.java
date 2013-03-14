package qmips.sync;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import qmips.presentation.swing.fancy.Log;

/**
 * 
 * Clase de sincronizacion de tareas.
 * Utiliza una piscina de hilos de tamaño indefinido, lo que
 * podria hacer dispararse el numero de hilos.
 * Las tareas se ejecutan por fases. Primero las activadas por el reloj,
 * cuando todas estas ha acabado se lanzan las que las anteriores hayan
 * activado y asi sucesivamente hasta que no despierte ninguna, momento
 * en que se libera al reloj.
 * 
 * @author Jaime Coello de Portugal
 *
 */
public class PoolSync implements Synchronization {

	private ExecutorService pool;
	private Set<Runnable> waitingTasks;
	private int runningTasksNum = 1;

	public PoolSync() {
		waitingTasks = new HashSet<Runnable>();
		pool = Executors.newCachedThreadPool();
	}

	@Override
	public synchronized void activateProcesses(Set<Runnable> processes) {
		waitingTasks.addAll(processes);
	}

	@Override
	public synchronized void taskEnded() {
		runningTasksNum--;
		if (runningTasksNum <= 0) {
			runningTasksNum = waitingTasks.size();
			if (runningTasksNum == 0) {
				notifyAll();
			} else {
				Set<Runnable> aux = waitingTasks;
				waitingTasks = new HashSet<Runnable>();
				for (Runnable r : aux) {
					try{
						pool.submit(r);
					}catch(RejectedExecutionException e){
						Log.inf.println("Execution canceled.");
					}
				}
			}
		}
	}

	@Override
	public synchronized void clockLockWait() throws InterruptedException{
		while (runningTasksNum != 0 || !waitingTasks.isEmpty()) {
			wait();
		}
		runningTasksNum++;
	}

	@Override
	public void terminate() {
		try {
			clockLockWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pool.shutdown();
	}

	@Override
	public void exception(Exception e) {
		System.err.println(e.getMessage());
	}
	
	

}
