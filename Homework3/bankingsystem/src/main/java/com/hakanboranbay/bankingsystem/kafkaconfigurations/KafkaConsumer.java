package com.hakanboranbay.bankingsystem.kafkaconfigurations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
	
	@KafkaListener(topics = "logs", groupId = "log_consumer_group")
	public void listenLogs(@Payload String message) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(new File("C:\\Users\\hakan\\Desktop\\Akbank Java Spring Bootcamp\\CodingPractises\\bankingsystem\\logs.txt"), true));
			bw.append(message + "\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
