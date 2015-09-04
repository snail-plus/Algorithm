package com.whtriples.airPurge.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UEHLogger implements Thread.UncaughtExceptionHandler {
	
	    private static Logger logger = LoggerFactory.getLogger(UEHLogger.class);
	
	    @Override
		public void uncaughtException(Thread t, Throwable e) {
			logger.error("线程 "+t.getName()+" caughtException：" + e.getMessage());
		}
    } 