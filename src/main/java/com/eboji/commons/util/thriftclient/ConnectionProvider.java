package com.eboji.commons.util.thriftclient;

/**
 * 类名名称: ConnectionProvider/连接提供接口 <br/>
 * @author zhoucl
 * @since JDK 1.7
 */
public interface ConnectionProvider {
	/**
	 * 取链接池中的一个链接
	 * 
	 * @return TSocket
	 */
	public TSocketBean getConnection();

	/**
	 * 返回链接
	 * 
	 * @param socket
	 */
	public void close(TSocketBean socketBean);
	
	/**
	 * 清理所有
	 */
	public void finish() throws Exception;
}
