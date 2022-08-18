package com.msa.bankingsystem.dataAccess.log;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.stereotype.Repository;

@Repository
public class MyLog4JRepository  implements ILogRepository{

	static Logger logger = Logger.getLogger(MyLog4JRepository.class);

	public MyLog4JRepository() {

		DOMConfigurator.configure("log4j.xml");

	}

	@Override
	public void info(String text) {
		logger.info(text);
	}

}
