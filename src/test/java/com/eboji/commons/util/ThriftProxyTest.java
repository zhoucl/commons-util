package com.eboji.commons.util;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eboji.commons.util.thriftclient.ConnectionManager;
import com.eboji.commons.util.thriftclient.ConnectionProviderImpl;
import com.eboji.thrift.service.HelloWorldService;
import com.eboji.thrift.service.HelloWorldService.Client;

public class ThriftProxyTest {
	private Logger logger = LoggerFactory.getLogger(ThriftProxyTest.class);

	private ConnectionManager connectionManager = new ConnectionManager();
	
	public void init() throws Exception {
		ConnectionProviderImpl cp = new ConnectionProviderImpl();
		cp.setServiceURL("127.0.0.1:9090;127.0.0.1:9090");
		cp.setConnTimeout(30000);
		cp.manuleInit();
		
		connectionManager.setConnectionProvider(cp);
	}
	
	public HelloWorldService.Client getHelloWorldServiceClient() throws Exception {
		HelloWorldService.Client object = null;
		try {
			TSocket tsockt = connectionManager.getSocket();
			TTransport transport = new TFramedTransport(tsockt);
			TProtocol protocol = new TCompactProtocol(transport);
			TMultiplexedProtocol tMultiplexedProtocol = new TMultiplexedProtocol(
					protocol, "HelloWorldService");
			object = new HelloWorldService.Client(tMultiplexedProtocol);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return object;
	}
	
	public static void main(String[] args) throws Exception {
		ThriftProxyTest tp = new ThriftProxyTest();
		tp.init();
		for(int i = 0; i < 100; i++) {
			Client client = tp.getHelloWorldServiceClient();
			client.sub(100, 20);
		}
		
		System.out.println("=======================");
		final ThriftProxyTest tp1 = new ThriftProxyTest();
		tp1.init();
		ExecutorService es = Executors.newFixedThreadPool(2);
		CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(es);
		for(int i = 0; i < 100000; i++) {
			cs.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					Client client = tp1.getHelloWorldServiceClient();
					client.sub(100, 20);
					return 10;
				}
			});
		}
		
		es.shutdown();
	}
}
