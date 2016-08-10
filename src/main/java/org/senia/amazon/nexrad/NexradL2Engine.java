package org.senia.amazon.nexrad;


import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NexradL2Engine {
    public static Queue<String> queueMap = new LinkedList<String>();
	private static final Logger log = LoggerFactory.getLogger(NexradL2Engine.class);

	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		QueueMonitor qm = new QueueMonitor();
		log.info("QueueMontior Class: "+qm.getClass().getName());
		
		NexradMessageWorker nexradMsgThread = new NexradMessageWorker("NexradMessageThread", "file:///Users/gsenia/nexrad.queues");
		NexradS3Worker nexradS3Worker = new NexradS3Worker();
		nexradS3Worker.setName("NexradS3Thread");
		nexradS3Worker.start();
		nexradMsgThread.setName("NexradMessageThread");
		nexradMsgThread.start();
		log.info("Finished starting threads");

		long end = System.currentTimeMillis();
		log.info("StartupTime: "+(end - start) + "ms");

	}
}
