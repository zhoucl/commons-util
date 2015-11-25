package com.eboji.commons.util.memcached;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eboji.commons.util.date.DateUtil;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * @author zhoucl
 */
public abstract class MemCachedFactory {
	private static final Logger logger = LoggerFactory.getLogger(MemCachedFactory.class);
	
	private String memcacheName = "default-memcache";
	
	private String[] serverList = { "localhost:11211" };
	
	private Integer[] weights = {1};
	
	private MemCachedClient mc = null;
	
	private MemcachedConfiguration configuration;
	
	public MemCachedFactory(MemcachedConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public void initialContext() {
		if(serverList.length == weights.length) {
			SockIOPool pool = SockIOPool.getInstance(getMemcacheName(), true);
			pool.setServers( serverList );
	
			pool.setInitConn(configuration.getMinConn());
			pool.setMinConn(configuration.getMinConn());
			pool.setMaxConn(configuration.getMaxConn());
			pool.setMaxBusyTime(configuration.getMaxBusyTime());
			pool.setMaxIdle(configuration.getMaxIdle());
			pool.setNagle(configuration.isNagle());
			pool.setHashingAlg(configuration.getHashingAlg());
			pool.setSocketConnectTO(configuration.getSocketConnectTO());
			pool.setSocketTO(configuration.getSocketTO());
			pool.setFailover(configuration.isFailover());
			pool.setFailback(configuration.isFailback());
			pool.setWeights(weights);
			
			pool.initialize();
			
			mc = new MemCachedClient(getMemcacheName());
		} else {
			logger.error("initial SockIOPool falied as serverlist count is not equal weights count"
					+ "\nplease modify the configuration, then restart the server!!!");
			try {
				TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean add(String key, Object value) {
		boolean ret = mc.add(key, value);
		if(ret) {
			logger.info("add the key = [" + key + "] to memcached successed.");
		} else {
			logger.warn("add the key = [" + key + "] to memcached failed.");
		}
		return ret;
	}
	
	public boolean add(String key, Object value, Date expiry) {
		boolean ret = mc.add(key, value, expiry);
		if(ret) {
			logger.info("add the key = [" + key + "] to memcached expiry with " +
				DateUtil.formatDate(expiry, DateUtil.FORMATTER_YYYYMMDDHHMMSS) + 
				" successed.");
		} else {
			logger.warn("add the key = [" + key + "] to memcached expiry with " +
				DateUtil.formatDate(expiry, DateUtil.FORMATTER_YYYYMMDDHHMMSS) +
				" failed.");
		}
		return ret;
	}
	
	public boolean set(String key, Object value) {
		boolean ret = mc.set(key, value);
		if(ret) {
			logger.info("set the key = [" + key + "] to memcached successed.");
		} else {
			logger.warn("set the key = [" + key + "] to memcached failed.");
		}
		return ret;
	}
	
	public boolean set(String key, Object value, Date expiry) {
		boolean ret = mc.set(key, value, expiry);
		if(ret) {
			logger.info("set the key = [" + key + "] to memcached expiry with " +
				DateUtil.formatDate(expiry, DateUtil.FORMATTER_YYYYMMDDHHMMSS) + 
				" successed.");
		} else {
			logger.warn("set the key = [" + key + "] to memcached expiry with " +
				DateUtil.formatDate(expiry, DateUtil.FORMATTER_YYYYMMDDHHMMSS) +
				" failed.");
		}
		return ret;
	}
	
	public boolean replace(String key, Object value) {
		boolean ret = mc.replace(key, value);
		if(ret) {
			logger.info("replace the key = [" + key + "] to memcached successed.");
		} else {
			logger.warn("replace the key = [" + key + "] to memcached failed.");
		}
		return ret;
	}
	
	public boolean replace(String key, Object value, Date expiry) {
		boolean ret = mc.replace(key, value, expiry);
		if(ret) {
			logger.info("replace the key = [" + key + "] to memcached expiry with " +
				DateUtil.formatDate(expiry, DateUtil.FORMATTER_YYYYMMDDHHMMSS) + 
				" successed.");
		} else {
			logger.warn("replace the key = [" + key + "] to memcached expiry with " +
				DateUtil.formatDate(expiry, DateUtil.FORMATTER_YYYYMMDDHHMMSS) +
				" failed.");
		}
		return ret;
	}
	
	public boolean delete(String key) {
		boolean ret = mc.delete(key);
		if(ret) {
			logger.info("delete the key = [" + key + "] to memcached successed.");
		} else {
			logger.warn("delete the key = [" + key + "] to memcached failed.");
		}
		return ret;
	}	
	
	public String getMemcacheName() {
		return memcacheName;
	}

	public void setMemcacheName(String memcacheName) {
		this.memcacheName = memcacheName;
	}

	public String[] getServerList() {
		return serverList;
	}

	public void setServerList(String[] serverList) {
		this.serverList = serverList;
	}
}
