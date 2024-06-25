package com.project.FlightReservation.services;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailService
{

	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String to, String subject, String text) throws MessagingException
	{
		log.info("Sending Booking confirmation email to : {}, subject: {}", to, subject);
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);

		mailSender.send(message);
		log.info("Email send successfully");

	}
}
