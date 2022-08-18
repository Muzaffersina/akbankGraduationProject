package com.msa.bankingsystem.services.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.msa.bankingsystem.dataAccess.log.ILogRepository;

@Service
public class KafkaLoggerManager implements IKafkaLoggerService {

	@Value(value = "${kafka.topicName}")
	public String topicName;
	private ILogRepository iLogRepository;

	@Autowired
	public KafkaLoggerManager(ILogRepository iLogRepository) {
		this.iLogRepository = iLogRepository;

	}

	@Override
	@KafkaListener(topics = "${kafka.topicName}", groupId = "group_id")
	public void kafkaListener(String message) {
		this.iLogRepository.info(message);
	}

}
