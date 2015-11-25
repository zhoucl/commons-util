package com.eboji.commons.util.zookeeper;

import org.apache.zookeeper.WatchedEvent;

/**
 * zookeeperMonitor接口类
 * @author zhoucl
 *
 */
public interface ZookeeperMonitor {
	public void monitor(WatchedEvent event);
}
