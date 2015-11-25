package com.eboji.commons.util.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zookeeper工具入口类
 * @author zhoucl
 *
 */
public class ZookeeperFactory {
	private static final Logger logger = LoggerFactory.getLogger(ZookeeperFactory.class);
	
	private String zookeeperURL = "localhost:2181";
	private Integer zookeeperTimeOut = 5000;
    private ZooKeeper zk = null;
    private ZookeeperMonitor zm = null;

    public ZookeeperFactory(String url, Integer timeout, ZookeeperMonitor zm) {
		this.zookeeperURL = url;
		this.zookeeperTimeOut = timeout;
		this.zm = zm;
		
		init();
	}
    
    private void init() {
    	try {
	    	zk = new ZooKeeper(zookeeperURL, zookeeperTimeOut, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if(zm != null) {
						zm.monitor(event);
					} else {
						logger.warn("ZookeeperMonitor is null!!!");
					}
				}
			});
    	} catch (Exception e) {
    		logger.error("initializing zookeeper failed! " + e.getMessage());
    	}
    }
    

	public String getZookeeperURL() {
		return zookeeperURL;
	}

	public void setZookeeperURL(String zookeeperURL) {
		this.zookeeperURL = zookeeperURL;
	}

	public Integer getZookeeperTimeOut() {
		return zookeeperTimeOut;
	}

	public void setZookeeperTimeOut(Integer zookeeperTimeOut) {
		this.zookeeperTimeOut = zookeeperTimeOut;
	}

	public ZooKeeper getZk() {
		return zk;
	}

	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}
}
