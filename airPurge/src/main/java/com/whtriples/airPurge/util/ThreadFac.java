package com.whtriples.airPurge.util;

import java.util.concurrent.ThreadFactory;


public class ThreadFac {
	 
	public  static ThreadFactory readFactory;
	public static ThreadFactory getFac(final String threadname){
		readFactory = new ThreadFactory() {
			int threadNo = 0;
			public Thread newThread(Runnable task) {
				threadNo++;
				Thread thread = new Thread(task, threadname + threadNo);
				thread.setUncaughtExceptionHandler(new UEHLogger());
				return thread;
			}
		 
		};
		
		return readFactory;
	}
	 
}