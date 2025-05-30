package com.notification.system.service;

import com.notification.system.exception.OrderException;
import com.notification.system.model.Order;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger ( EmailService.class );

	@Autowired
	private JavaMailSender mailSender;


	public void sendOrderConfirmation( Order order ){
		try{
			MimeMessage message = mailSender.createMimeMessage ();
			MimeMessageHelper helper = new MimeMessageHelper ( message, true );
			helper.setTo ( order.getCustomerEmail () );
			helper.setSubject ( "Order Confirmation - Order #" +order.getId () );
			helper.setText ( "Dear Customer, \n\n Your order has been placed successfully!\n\n" +
			                 "Order ID: " +order.getId () + "\n" +
			                 "Item: " +order.getItems ()+ "\n" +
			                 "Estimated Time: 30 minutes \n\n"+
			                 "Thank you for choosing us!", true );
			mailSender.send ( message );
			logger.info ( "Email sent to {}", order.getCustomerEmail () );
		}
		catch ( Exception exception ){
			logger.error ( "Email sending failed: {}", exception.getMessage () );
			throw new OrderException ( "Email failed to sent : "+exception.getMessage () );
		}
	}

}
