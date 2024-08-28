package com.iftm.client.services.util;

import org.springframework.stereotype.Component;
import com.iftm.client.services.exceptions.ResourceNotFoundException;


@Component
public class Validador {
	
	public void eValido(Long id) {
		if (id<0 || id>100000) {
			throw new ResourceNotFoundException("Invalid Id : " + id);
		}
	}

}