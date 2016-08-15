package org.senia.amazon.nexrad;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class NexradS3Worker extends Thread implements Runnable {
	public AwsS3 nexradS3;
	private static final Logger log = LoggerFactory.getLogger(NexradS3Worker.class);
	private String nexradOutputPath = NexradL2Engine.nexradOutputPath;
	private String l2refConfig = NexradL2Engine.nexradOutputPath;
	public NexradS3Worker() {
		nexradS3 = new AwsS3();
	}

	public void run() {
		log.debug("Nexrad S3 Worker starting");
		while (true) {
			log.trace("Nexrad S3 Worker looking for files to download");
			Iterator<String> it = NexradL2Engine.queueMap.iterator();
			while (it.hasNext()) {
				String nexradPath = NexradL2Engine.queueMap.poll();
				try {
					File file = nexradS3.downloadNexrad(nexradPath, nexradOutputPath);
					String nexr = file.toString();
					String nexrout = nexr.split(".ar2v")[0];
					WctExporter wct = new WctExporter();
					wct.convert2NC(nexr, nexrout, l2refConfig);
				} catch (IOException | NumberFormatException | XPathExpressionException | InstantiationException | SAXException | ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			synchronized (QueueMonitor.lockObj) {
				try {
					log.debug("Putting Nexrad S3 Worker into wait");
					QueueMonitor.lockObj.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}

}
