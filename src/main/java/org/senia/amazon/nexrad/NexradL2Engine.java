package org.senia.amazon.nexrad;


import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NexradL2Engine {
    public static Queue<String> queueMap = new LinkedList<String>();
	private static final Logger log = LoggerFactory.getLogger(NexradL2Engine.class);
	public static String nexradOutputPath;
	public static String l2refConfig;
	public static String nexradQueueConfig;
	public static String gradsScript;

	public static void main(String[] args) {
		nexradOutputPath = System.getProperty("nexradOutputPath");
		l2refConfig = System.getProperty("l2refNexradConfig");
		nexradQueueConfig = System.getProperty("nexradQueueConfig");
		
		long start = System.currentTimeMillis();
		QueueMonitor qm = new QueueMonitor();
		log.info("QueueMontior Class: "+qm.getClass().getName());
		
		NexradMessageWorker nexradMsgThread = new NexradMessageWorker("NexradMessageThread", nexradQueueConfig);
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
