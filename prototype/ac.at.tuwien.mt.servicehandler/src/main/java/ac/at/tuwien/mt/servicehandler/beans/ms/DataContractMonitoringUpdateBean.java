/*
 * Copyright (c) 2016. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS".
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 * 
 * Author: Florin Bogdan Balint
 * 
 */
package ac.at.tuwien.mt.servicehandler.beans.ms;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;

import ac.at.tuwien.mt.dao.monitor.MonitoredDataContractDAO;
import ac.at.tuwien.mt.dao.monitor.impl.MonitoredDataContractDAOImpl;
import ac.at.tuwien.mt.model.thing.monitor.MonitoredDataContract;
import ac.at.tuwien.mt.model.util.StringUtil;

@Component
public class DataContractMonitoringUpdateBean {

	private static final Logger LOGGER = Logger.getLogger(DataContractMonitoringUpdateBean.class);

	private MongoClient mongoClient;

	@Value("${mongo_db_name}")
	private String database;
	@Value("${mongo_db_collection_dc_monitor}")
	private String collectionName;

	@Autowired
	public DataContractMonitoringUpdateBean(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	@Handler
	public void process(Exchange exchange, @Body MonitoredDataContract dataContract) throws Exception {
		LOGGER.info("Updating monitoring information about the contractId: " + dataContract.getContractId());

		if (StringUtil.isNullOrBlank(dataContract.getContractId())) {
			exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, Response.Status.BAD_REQUEST.getStatusCode());
			exchange.getOut().setBody(Response.Status.BAD_REQUEST.getReasonPhrase());
		}

		MonitoredDataContractDAO dao = new MonitoredDataContractDAOImpl(mongoClient, database, collectionName);
		List<MonitoredDataContract> found = dao.find(dataContract.getContractId());

		if (found == null || found.isEmpty()) {
			exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, Response.Status.NOT_FOUND.getStatusCode());
			exchange.getOut().setBody(Response.Status.NOT_FOUND.getReasonPhrase());
		} else {
			dao.update(dataContract);

			// return what was found
			exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, Response.Status.OK.getStatusCode());
			exchange.getOut().setBody(dataContract);
		}
	}

}
