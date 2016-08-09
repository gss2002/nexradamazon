package org.senia.amazon.nexrad;


import java.util.LinkedList;
import java.util.Queue;


public class NexradL2Engine {
    public static Queue<String> queueMap = new LinkedList<String>();

	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		QueueMonitor qm = new QueueMonitor();
		System.out.println(qm.getClass().getName());
		/*ExecutorService executor = Executors.newFixedThreadPool(5);
		for (int i = 1; i <= 5; i++) {
			Runnable worker = new Worker("" + i);
			executor.execute(worker);
		}
		*/
		
		
		NexradMessageWorker nexradMsgThread = new NexradMessageWorker("NexradMessageThread", "file:///Users/gsenia/nexrad.queues");
		NexradS3Worker nexradS3Worker = new NexradS3Worker();
		nexradS3Worker.setName("NexradS3Thread");
		nexradS3Worker.start();
		nexradMsgThread.setName("NexradMessageThread");
		nexradMsgThread.start();
		System.out.println("All Threads Started");

		long end = System.currentTimeMillis();
		System.out.println((end - start) + "ms");

	}
}
