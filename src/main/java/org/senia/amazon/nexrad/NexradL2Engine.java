package org.senia.amazon.nexrad;


import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;


public class NexradL2Engine {
    public static Queue<String> queueMap = new LinkedList<String>();
	private static final Logger log = LoggerFactory.getLogger(NexradL2Engine.class);
	public static String nexradOutputPath;
	public static String l2refConfig;
	public static String l2bvConfig;
	public static String l2ccConfig;
	public static String l2zdrConfig;
	public static String l2kdpConfig;
	public static String l2swConfig;
	public static String radar_home;




	public static String nexradQueueConfig;
	public static String gradsScript;

	public static void main(String[] args) {
		nexradOutputPath = System.getProperty("nexradOutputPath");
		l2refConfig = System.getProperty("l2refNexradConfig");
		l2bvConfig = System.getProperty("l2bvNexradConfig");
		l2ccConfig = System.getProperty("l2ccNexradConfig");
		l2zdrConfig = System.getProperty("l2zdrNexradConfig");
		l2kdpConfig = System.getProperty("l2kdpNexradConfig");
		l2swConfig = System.getProperty("l2swNexradConfig");
		radar_home = System.getProperty("radar_home");



		nexradQueueConfig = System.getProperty("nexradQueueConfig");
		
		long start = System.currentTimeMillis();
		// Optionally remove existing handlers attached to j.u.l root logger
		 SLF4JBridgeHandler.removeHandlersForRootLogger();  // (since SLF4J 1.6.5)

		 // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
		 // the initialization phase of your application
		 SLF4JBridgeHandler.install();
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
