package org.senia.amazon.nexrad;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
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
				String site = nexradPath.split("/")[3];
				try {
					File file = nexradS3.downloadNexrad(nexradPath, nexradOutputPath);
					String nexr = file.toString();
					String nexrout = nexr.split(".ar2v")[0];
					WctExporter wct = new WctExporter();
					wct.convert2NC(nexr, nexrout, l2refConfig);
					 String[] command = {"/home/gsenia/radar_scripts/radar_ref_msg1.sh", site, nexrout};
				        ProcessBuilder probuilder = new ProcessBuilder( command );
				        //You can set up your work directory				        
				        Process process = probuilder.start();
				        process.isAlive();
				      //Read out dir output
				        InputStream is = process.getInputStream();
				        InputStreamReader isr = new InputStreamReader(is);
				        BufferedReader br = new BufferedReader(isr);
				        String line;
				        System.out.printf("Output of running %s is:\n",
				                Arrays.toString(command));
				        while ((line = br.readLine()) != null) {
				            System.out.println(line);
				        }
				        
				        //Wait to get exit value
				        try {
				            int exitValue = process.waitFor();
				            System.out.println("\n\nExit Value is " + exitValue);
				        } catch (InterruptedException e) {
				            // TODO Auto-generated catch block
				            e.printStackTrace();
				        }
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
