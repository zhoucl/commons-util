package com.eboji.commons.util.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
			JedisPoolConfig jConfig = null;
			if(config == null) {
				jConfig = new JedisPoolConfig();
	            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
	            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。  
				jConfig.setMaxTotal(200);
	            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。  
				jConfig.setMaxIdle(5);  
	            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；  
				jConfig.setMaxWaitMillis(5000);
	            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；  
				jConfig.setTestOnBorrow(true);  
				jConfig.setTestOnCreate(true);
				jConfig.setTestOnReturn(true);
				jConfig.setTestWhileIdle(true);
			} else {
				jConfig = (JedisPoolConfig)config;
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
	
	public boolean exists(String key) {
		Jedis jedis = null;
		boolean value = false;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				value = jedis.exists(key);
			}
		} catch (Exception e) {
			logger.error("Redis exists key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
		
		return value;
	}
	
	public void remove(String key) {
		Jedis jedis = null;
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				jedis.del(key);
			}
		} catch (Exception e) {
			logger.error("Redis hexists key = [" + key + "] failed!",
					e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
	}
	
	public String hlpop(String key) {
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
	
	public void hlset(String key, List<String> value) {
		Jedis jedis = null;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				jedis.lpush(key, value.toArray(new String[0]));
			}
		} catch (Exception e) {
			logger.error("Redis lpush key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
	}
	
	public List<String> hlget(String key, int start, int end) {
		List<String> values = null;
		Jedis jedis = null;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				values = jedis.lrange(key, start, end);
			}
		} catch (Exception e) {
			logger.error("Redis lrange key = [" + key + "], start = [" + start + "], end = [" 
					+ end +"] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
		
		return values;
	}
	
	public void hmset(String key, Map<String, String> value, Integer expire) {
		Jedis jedis = null;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				jedis.hmset(key, value);
				if(expire != null && expire > 0)
					jedis.expire(key, expire);
			}
		} catch (Exception e) {
			logger.error("Redis hmset key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
	}
	
	public void hmset(String key, String field, String value) {
		Jedis jedis = null;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				jedis.hset(key, field, value);
			}
		} catch (Exception e) {
			logger.error("Redis hmset key = [" + key + "], field = [" + field + "] failed!",
					e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
	}
	
	public Map<String, String> hmget(String key) {
		Jedis jedis = null;
		Map<String, String> values = new HashMap<String, String>();
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				Iterator<String> iter = jedis.hkeys(key).iterator();
				while(iter.hasNext()) {
					String field = iter.next();
					values.put(field,  jedis.hmget(key, field).get(0));
				}
			}
		} catch (Exception e) {
			logger.error("Redis hmset key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
		
		return values;
	}
	
	public String hmget(String key, String field) {
		Jedis jedis = null;
		String value =  null;
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				List<String> vList = jedis.hmget(key, field);
				if(vList != null && vList.size() == 1)
					value = vList.get(0);
			}
		} catch (Exception e) {
			logger.error("Redis hmget key = [" + key + "], field = [" + field + "] failed!", 
					e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
		
		return value;
	}
	
	public boolean hexists(String key, String field) {
		Jedis jedis = null;
		boolean value =  false;
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				value = jedis.hexists(key, field);
			}
		} catch (Exception e) {
			logger.error("Redis hexists key = [" + key + "], field = [" + field + "] failed!",
					e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
		
		return value;
	}
	
	public void hsset(String key, Set<String> value) {
		Jedis jedis = null;
		
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				jedis.sadd(key, value.toArray(new String[0]));
			}
		} catch (Exception e) {
			logger.error("Redis hmset key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
	}
	
	public Set<String> hsget(String key) {
		Jedis jedis = null;
		Set<String> value =  null;
		try {
			if(jedisPool != null) {
				jedis = jedisPool.getResource();
				value = jedis.smembers(key);
			}
		} catch (Exception e) {
			logger.error("Redis hmset key = [" + key + "] failed!", e.getCause());
		} finally {
			if(jedis != null)
				jedis.close();
		}
		
		return value;
	}
	
	public static void main(String[] args) {
		RedisClient client = new RedisClient("192.168.6.164", 6379, null);
		System.out.println(client.get("hello"));
		
		client.set("hello", "你好");
		System.out.println(client.get("hello"));
		
		List<String> list = new ArrayList<String>();
		list.add("list1");
		list.add("list2");
		list.add("list3");
		
		client.hlset("list", list);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("a1", "LSKJALJDLASJFLAJLJLKSAJLKJLSAJDLSAJDLSAJDLSAJDLSAJDLSAKJDLSAJDLSAJDLSAKJDLAKJDLSAJDLSAKJ");
		map.put("a2", "LSKJALJDLASJFLAJLJLKSAJLKJLSAJDLSAJDLSAJDLSAJDLSAJDLSAKJDLSAJDLSAJDLSAKJDLAKJDLSAJDLSAKJ");
		map.put("a3", "LSKJALJDLASJFLAJLJLKSAJLKJLSAJDLSAJDLSAJDLSAJDLSAJDLSAKJDLSAJDLSAJDLSAKJDLAKJDLSAJDLSAKJ");
		map.put("a4", "LSKJALJDLASJFLAJLJLKSAJLKJLSAJDLSAJDLSAJDLSAJDLSAJDLSAKJDLSAJDLSAJDLSAKJDLAKJDLSAJDLSAKJ");
		map.put("a5", "LSKJALJDLASJFLAJLJLKSAJLKJLSAJDLSAJDLSAJDLSAJDLSAJDLSAKJDLSAJDLSAJDLSAKJDLAKJDLSAJDLSAKJ");
		client.hmset("product", map, 1800);
	}
 }
