package com.api.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import com.api.rest.model.Usuario;

@Configuration
public class RepositoryConfiguration implements RepositoryRestConfigurer  {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Usuario.class);
		RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config);
	}
}