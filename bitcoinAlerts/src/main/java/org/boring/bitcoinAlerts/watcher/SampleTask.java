package org.boring.bitcoinAlerts.watcher;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("sampleTask")
@Scope (value="prototype")
@Lazy(value = true)
public class SampleTask implements Runnable {

	@Override
	public void run() {
		System.out.println("MyTask is running");
	}

}
