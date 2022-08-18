package com.msa.bankingsystem.services.log;

public interface IKafkaLoggerService {
	void kafkaListener(String message);
}
