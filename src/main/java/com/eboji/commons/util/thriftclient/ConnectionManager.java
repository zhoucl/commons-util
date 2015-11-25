package com.eboji.commons.util.thriftclient;

import org.apache.thrift.transport.TSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名名称: ConnectionManager/Thrift客户端连接管理器 <br/>
 * @author zhoucl
 * @since JDK 1.7
 */
public class ConnectionManager {

	/** 日志记录器 */
	public Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	
	/** 保存local对象 */
	ThreadLocal<TSocket> socketThreadSafe = new ThreadLocal<TSocket>();

	/** 连接提供池 */
	public ConnectionProvider connectionProvider;

	/**
	 * 获取socket
	 * @return TSocket
	 */
	public TSocket getSocket() {
		TSocketBean socketBean = null;
		TSocket socket = null;
		try {
			socketBean = connectionProvider.getConnection();
			socket = socketBean.gettSocket();
			socketThreadSafe.set(socket);

			return socketThreadSafe.get();
		} catch (Exception e) {
			logger.error("error ConnectionManager.invoke()", e);
		} finally {
			connectionProvider.close(socketBean);
			socketThreadSafe.remove();
		}

		return socket;
	}

	public ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

	public void setConnectionProvider(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

}
