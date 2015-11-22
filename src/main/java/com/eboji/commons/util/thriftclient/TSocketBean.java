package com.eboji.commons.util.thriftclient;

import org.apache.thrift.transport.TSocket;

/**
 * 类名名称: TSocketBean/包含TSocket和index序号的Bean <br/>
 * @author zhoucl
 * @since JDK 1.7
 */
public class TSocketBean {
	int poolIndex;
	TSocket tSocket;
	public int getPoolIndex() {
		return poolIndex;
	}
	public void setPoolIndex(int poolIndex) {
		this.poolIndex = poolIndex;
	}
	public TSocket gettSocket() {
		return tSocket;
	}
	public void settSocket(TSocket tSocket) {
		this.tSocket = tSocket;
	}
	
}
