package com.eboji.commons.util.redis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisClient {
	private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
	
	private String host = "localhost";
	private int port = 6379;
	
	private static JedisPool jedisPool = null;
	
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public RedisClient(String host, int port, JedisPoolConfig config) {
		this.host = host;
		this.port = port;
		
		
		if(jedisPool == null) {
			JedisPoolConfig jConfig = (JedisPoolConfig)config;
			if(jConfig == null) {
				jConfig = new JedisPoolConfig();
	            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
	            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。  
				jConfig.setMaxTotal(500);
	            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。  
				jConfig.setMaxIdle(5);  
	            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；  
				jConfig.setMaxWaitMillis(5000);
	            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；  
				jConfig.setTestOnBorrow(true);  
			}
            jedisPool = new JedisPool(jConfig, this.host, this.port);
		}
	}
	
	protected void returnResource(Jedis jedis) {
		if(jedis != null)
			jedis.close();
	}
	
	public String get(String key) {
		String value = null;
		Jedis jedis = null;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				value = jedis.get(key);
			}
		} catch (Exception e) {
			logger.error("Redis get key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
		
		return value;
	}
	
	public void set(String key, String value) {
		Jedis jedis = null;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				jedis.getSet(key, value);
			}
		} catch (Exception e) {
			logger.error("Redis getSet key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
	}
	
	public void set(String key, List<String> value) {
		Jedis jedis = null;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				jedis.lpush(key, (String[])value.toArray());
			}
		} catch (Exception e) {
			logger.error("Redis lpush key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
	}
	
	public List<String> get(String key, int start, int end) {
		List<String> values = null;
		Jedis jedis = null;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				values = jedis.lrange(key, start, end);
			}
		} catch (Exception e) {
			logger.error("Redis lrange key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
		
		return values;
	}
	
	public String getPop(String key) {
		String value = null;
		Jedis jedis = null;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				value = jedis.lpop(key);
			}
		} catch (Exception e) {
			logger.error("Redis lpop key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
		
		return value;
	}
	
	public static void main(String[] args) {
		RedisClient client = new RedisClient("localhost", 6379, null);
		System.out.println(client.get("hello"));
		
		client.set("hello", "你好");
		System.out.println(client.get("hello"));
	}
 }
