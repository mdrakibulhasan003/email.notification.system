package com.notification.system.controller;

import com.notification.system.exception.OrderException;
import com.notification.system.exception.OrderNotFoundException;
import com.notification.system.model.Order;
import com.notification.system.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private OrderService orderService;

	public OrderController ( OrderService orderService ) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<?> placeOrder(@RequestBody Order order ){
		try{
			Order savedOrder = orderService.placeOrder ( order );
			return ResponseEntity.ok ( savedOrder );
		}
		catch ( OrderException exception ){
			Map<String, String> errorResponse = new HashMap <> (  );
			errorResponse.put ( "error", exception.getMessage () );
			return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( errorResponse );
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrder(@PathVariable Long id){
		try{
			Order order = orderService.getOrder ( id );
			return ResponseEntity.ok ( order );
		}
		catch ( OrderNotFoundException exception ){
			Map<String, String> errorResponse  = new HashMap <> (  );
			errorResponse.put ( "error", exception.getMessage () );
			return ResponseEntity.status ( HttpStatus.NOT_FOUND ).body ( errorResponse );
		}
	}
}
