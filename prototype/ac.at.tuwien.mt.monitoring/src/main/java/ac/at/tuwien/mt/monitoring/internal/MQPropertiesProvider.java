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

package ac.at.tuwien.mt.monitoring.internal;

import java.util.ResourceBundle;

/**
 * Provider class which reads the properties from the properties file:
 * message_queue.properties
 * 
 * @author Florin Bogdan Balint
 *
 */
public class MQPropertiesProvider {

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("activemq");

	/**
	 * Returns the value as String for the given key.
	 *
	 * @param query
	 *            the properties key
	 * @return String value of the property
	 */
	public static String getString(MQProperty property) {
		return BUNDLE.getString(property.getProperty());
	}

	/**
	 * Returns the value as Integer for the given key.
	 * 
	 * @param property
	 * @return Integer value of the property
	 */
	public static int getInteger(MQProperty property) {
		String valueAsString = BUNDLE.getString(property.getProperty());
		int valueAsLong = Integer.parseInt(valueAsString);
		return valueAsLong;
	}
}
