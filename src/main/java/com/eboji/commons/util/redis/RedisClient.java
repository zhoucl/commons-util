package com.eboji.commons.util.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.jute.compiler.JRecord;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {
	private RedisClient redisClient = null;
	
	private Jedis jedis = null;
	
	private JedisPoolConfig config = null;
	
	private String host;

	private Integer port;
	
	public RedisClient(JedisPoolConfig config, String host, Integer port) {
		if(config == null) {
			this.config = new JedisPoolConfig();
		}
		this.host = host;
		this.port = port;
		
		
	}
	
	protected void init() {
		JedisPool pool = new JedisPool(this.config, this.host, this.port); 
	}
 }
