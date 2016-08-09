package org.senia.amazon.nexrad;

import java.io.IOException;
import java.util.Iterator;

public class NexradS3Worker extends Thread implements Runnable {
	public AwsS3 nexradS3;

	public NexradS3Worker() {
		nexradS3 = new AwsS3();
	}

	public void run() {
		while (true) {
			Iterator<String> it = NexradL2Engine.queueMap.iterator();
			while (it.hasNext()) {
				String nexradPath = NexradL2Engine.queueMap.poll();
				try {
					nexradS3.downloadNexrad(nexradPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			synchronized (QueueMonitor.lockObj) {
				try {
					QueueMonitor.lockObj.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}

}
