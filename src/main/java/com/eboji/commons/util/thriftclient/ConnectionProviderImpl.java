package com.eboji.commons.util.thriftclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.apache.thrift.transport.TSocket;

/**
 * 类名名称: ConnectionProviderImpl/连接提供接口实现 <br/>
 * @author zhoucl
 * @since JDK 1.7
 */
public class ConnectionProviderImpl implements ConnectionProvider {
	private String serviceURL;
	
	private int connTimeout;
	
	/** 可以从缓存池中分配对象的最大数量 */
	private int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;
	/** 缓存池中最大空闲对象数量 */
	private int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;
	/** 缓存池中最小空闲对象数量 */
	private int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;
	/** 阻塞的最大数量 */
	private long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;
	
	/** 从缓存池中分配对象，是否执行PoolableObjectFactory.validateObject方法 */
	private boolean testOnBorrow = GenericObjectPool.DEFAULT_TEST_ON_BORROW;
	private boolean testOnReturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;
	private boolean testWhileIdle = GenericObjectPool.DEFAULT_TEST_WHILE_IDLE;
	
	private List<ObjectPool<?>> objectPools = null;
	
	@Override
	@SuppressWarnings("unchecked")
	public TSocketBean getConnection() {
		ObjectPool<TSocket> objectPool = null;
		int maxNumIdle = 0;
		int index = 0;
		
		for(int i = 0; i < objectPools.size(); i++) {
			if(objectPools.get(i).getNumIdle() > maxNumIdle) {
				maxNumIdle = objectPools.get(i).getNumIdle();
				index = i;
			}
		}
		
		objectPool = (ObjectPool<TSocket>)objectPools.get(index);
		
		TSocketBean retSocketBean = new TSocketBean();
		TSocket socket = null;
		try {
			socket = (TSocket)objectPool.borrowObject();
			retSocketBean.settSocket(socket);
			retSocketBean.setPoolIndex(index);
		} catch (Exception e) {
			throw new RuntimeException("error getConnection", e);
		}
		
		return retSocketBean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void close(TSocketBean socketBean) {
		try {
			ObjectPool<TSocket> objectPool = (ObjectPool<TSocket>) objectPools.get(socketBean.getPoolIndex());
			objectPool.returnObject(socketBean.gettSocket());
		} catch (Exception e) {
			throw new RuntimeException("error returnCon()", e);
		}
	}
	
	@Override
	public void finish() throws Exception {
		try {
			for(ObjectPool<?> objectPool : objectPools) {
				objectPool.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("erorr destroy()", e);
		}
		
	}

	@SuppressWarnings("unchecked")
	public void manuleInit() throws Exception {
		Config config = new Config();
		config.maxActive = maxActive;
		config.maxIdle = maxIdle;
		config.maxWait = maxWait;
		config.testOnBorrow = testOnBorrow;
		config.testOnReturn = testOnReturn;
		config.testWhileIdle = testWhileIdle;
		config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		
		objectPools = new ArrayList<>();
		
		String[] urlArray = serviceURL.split(";");
		for(String  url: urlArray) {
			String[] urlArr = url.split(":");
			String serviceIP = urlArr[0];
			Integer servicePort = Integer.parseInt(urlArr[1]);
			
			ThriftPoolableObjectFactory thriftPoolableObjectFactory = new ThriftPoolableObjectFactory(
					serviceIP, servicePort, connTimeout);
			objectPools.add(new GenericObjectPool<>(thriftPoolableObjectFactory, config));
			System.out.println(url + " added to objectPools");
		}
	}
	

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public int getConnTimeout() {
		return connTimeout;
	}

	public void setConnTimeout(int connTimeout) {
		this.connTimeout = connTimeout;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
}
