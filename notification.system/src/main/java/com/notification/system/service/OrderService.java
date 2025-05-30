package com.notification.system.service;

import com.notification.system.exception.OrderException;
import com.notification.system.exception.OrderNotFoundException;
import com.notification.system.model.Order;
import com.notification.system.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger ( OrderService.class );
	private final AtomicLong idGenerator = new AtomicLong ( 1 );
	private final Map<Long, Order > orders = new ConcurrentHashMap <> (  );

	@Autowired
	private EmailService emailService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	public Order placeOrder(Order order){
		if ( order.getItems ()==null || order.getItems ().trim ().isEmpty () ){
			throw new OrderException ( "Order items cannot be empty!" );
		}

		if ( order.getCustomerEmail () == null || !order.getCustomerEmail ().matches ( "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$" ) ){
			throw new OrderException ( "Invalid email address!" );
		}

		order.setId ( idGenerator.getAndIncrement () );
		order.setStatus ( OrderStatus.PLACED );
		orders.put ( order.getId (), order );
		logger.info ( "Order placed: ID {}", order.getId () );

		emailService.sendOrderConfirmation ( order );

		messagingTemplate.convertAndSend ( "/topic/order", "Order #"+order.getId () + " status: "+order.getStatus () );
		logger.info ( "WebSocket notification sent for order {}", order.getId () );
		return order;
	}

	public Order getOrder (Long id){
		Order order = orders.get ( id );
		if ( order==null ){

			throw new OrderNotFoundException ( "Order not found with order id: "+id );
		}
		return order;
	}

	@Scheduled(fixedRate = 10000)
	public void updataOrderStatus(){
		for ( Order order:orders.values () ){
			if ( order.getStatus ()==OrderStatus.PLACED ){
				order.setStatus ( OrderStatus.PREPARING );
				messagingTemplate.convertAndSend ( "/topic/orders", "Order #"+order.getId () + "status " +order.getStatus () );
				logger.info ( "Order {} status: PREPARING", order.getId () );
			}
			else if ( order.getStatus () == OrderStatus.PREPARING ){
				order.setStatus ( OrderStatus.READY );
				messagingTemplate.convertAndSend ( "/topic/orders", "Order #" + order.getId () + "status" + order.getStatus () );
				logger.info ( "Order {} status: READY.", order.getId () );
			}
		}
	}
}
































