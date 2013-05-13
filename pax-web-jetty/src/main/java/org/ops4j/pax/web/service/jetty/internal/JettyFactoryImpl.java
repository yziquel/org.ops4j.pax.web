/* Copyright 2007 Alin Dreghiciu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.web.service.jetty.internal;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.ops4j.lang.NullArgumentException;
import org.ops4j.pax.web.service.spi.model.ServerModel;

class JettyFactoryImpl implements JettyFactory {

	/**
	 * Associated server model.
	 */
	private final ServerModel serverModel;

	/**
	 * Constrcutor.
	 * 
	 * @param serverModel
	 *            asscociated server model
	 */
	JettyFactoryImpl(final ServerModel serverModel) {
		NullArgumentException.validateNotNull(serverModel, "Service model");
		this.serverModel = serverModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JettyServer createServer() {
		return new JettyServerImpl(serverModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServerConnector createConnector(final Server server, final String name,
			final int port, final String host) {

		// HTTP Configuration
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme(name);
		http_config.setSecurePort(8443);
		http_config.setOutputBufferSize(32768);

		// HTTP connector
		ServerConnector http = new ServerConnector(server,
				new HttpConnectionFactory(http_config));
		http.setPort(port);
		http.setHost(host);
		http.setIdleTimeout(30000);

		return http;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServerConnector createSecureConnector(Server server, final String name,
			final int port, final String sslKeystore, final String sslPassword,
			final String sslKeyPassword, final String host,
			final String sslKeystoreType, final boolean isClientAuthNeeded,
			final boolean isClientAuthWanted) {
		/*
		 * old code! // TODO: PAXWEB-339 configurable ContextFactory
		 * SslContextFactory sslContextFactory = new
		 * SslContextFactory(sslKeystore);
		 * sslContextFactory.setKeyStorePassword(sslKeyPassword);
		 * sslContextFactory.setKeyManagerPassword(sslPassword);
		 * sslContextFactory.setNeedClientAuth(isClientAuthNeeded);
		 * sslContextFactory.setWantClientAuth(isClientAuthWanted); if
		 * (sslKeystoreType != null) {
		 * sslContextFactory.setKeyStoreType(sslKeystoreType); }
		 * 
		 * // create a https connector final SslSocketConnector connector = new
		 * SslSocketConnector( sslContextFactory);
		 * 
		 * connector.setName(name); connector.setPort(port);
		 * connector.setHost(host); connector.setConfidentialPort(port); // Fix
		 * for PAXWEB-430
		 * 
		 * return connector;
		 */
		// SSL Context Factory for HTTPS and SPDY
		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath(sslKeystore);
		sslContextFactory.setKeyStorePassword(sslKeyPassword);
		sslContextFactory.setKeyManagerPassword(sslPassword);
		sslContextFactory.setNeedClientAuth(isClientAuthNeeded);
		sslContextFactory.setWantClientAuth(isClientAuthWanted);
		if (sslKeystoreType != null) {
			sslContextFactory.setKeyStoreType(sslKeystoreType);
		}

		// HTTP Configuration
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme(name);
		http_config.setSecurePort(8443);
		http_config.setOutputBufferSize(32768);

		// HTTPS Configuration
		HttpConfiguration https_config = new HttpConfiguration(http_config);
		https_config.addCustomizer(new SecureRequestCustomizer());

		// HTTPS connector
		ServerConnector https = new ServerConnector(server,
				new SslConnectionFactory(sslContextFactory, name),
				new HttpConnectionFactory(https_config));
		https.setPort(port);
		https.setIdleTimeout(500000);
		
		return https;
	}

}
