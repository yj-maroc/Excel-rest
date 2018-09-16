package com.ositel.ws.excel.demo.configuration;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class ExcelRestApplication extends ResourceConfig{
	
	public ExcelRestApplication() {
        packages("com.ositel.ws.excel.demo");
        register(MultiPartFeature.class);
    }
}
