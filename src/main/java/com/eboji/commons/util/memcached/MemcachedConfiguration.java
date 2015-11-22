package com.eboji.commons.util.memcached;

import com.whalin.MemCached.SockIOPool;

public class MemcachedConfiguration {
	private int minConn = 5;       
	private int maxConn = 100;
	private int maxIdle = 1000 * 60 * 5;
	private long maxBusyTime = 1000 * 30;
	
	private int socketTO = 1000 * 3; 
	private int socketConnectTO = 1000 * 3; 
	
	private boolean failover = true; 
	private boolean failback = true; 
	
	private boolean nagle = false;
	
	private int HashingAlg = SockIOPool.CONSISTENT_HASH;
	
	public int getMinConn() {
		return minConn;
	}
	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}
	public int getMaxConn() {
		return maxConn;
	}
	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}
	public int getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	public long getMaxBusyTime() {
		return maxBusyTime;
	}
	public void setMaxBusyTime(long maxBusyTime) {
		this.maxBusyTime = maxBusyTime;
	}
	public int getSocketTO() {
		return socketTO;
	}
	public void setSocketTO(int socketTO) {
		this.socketTO = socketTO;
	}
	public int getSocketConnectTO() {
		return socketConnectTO;
	}
	public void setSocketConnectTO(int socketConnectTO) {
		this.socketConnectTO = socketConnectTO;
	}
	public boolean isFailover() {
		return failover;
	}
	public void setFailover(boolean failover) {
		this.failover = failover;
	}
	public boolean isFailback() {
		return failback;
	}
	public void setFailback(boolean failback) {
		this.failback = failback;
	}
	public boolean isNagle() {
		return nagle;
	}
	public void setNagle(boolean nagle) {
		this.nagle = nagle;
	}
	public int getHashingAlg() {
		return HashingAlg;
	}
	public void setHashingAlg(int hashingAlg) {
		HashingAlg = hashingAlg;
	}
}
