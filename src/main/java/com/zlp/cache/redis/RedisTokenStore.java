package com.zlp.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.zlp.cache.TokenStoreIfc;

/**
 * 描述：
 * @author zhoulongpeng
 * @date   2016-02-28
 */
public class RedisTokenStore implements TokenStoreIfc {
	
	private JedisPool pool;
	
	private String prefix="";
	
	public RedisTokenStore() {
	}
	
	public JedisPool getPool() {
		return pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	private String getSaveKey(String key){
		return prefix+key;
	}

	@Override
	public void save(String key,String value,int expire) {
		Jedis conn = pool.getResource();
		try{
			conn.setex(getSaveKey(key),expire, value);
		}finally{
			conn.close();
		}
	}

	@Override
	public String getValue(String key) {
		Jedis conn = pool.getResource();
		try{
			return conn.get(getSaveKey(key));
		}finally{
			conn.close();
		}		
	}

	@Override
	public void remove(String key) {
		Jedis conn = pool.getResource();
		try{
			conn.del(getSaveKey(key));
		}finally{
			conn.close();
		}			
	}

	@Override
	public boolean exists(String key) {
		Jedis conn = pool.getResource();
		try{
			return conn.exists(getSaveKey(key));
		}finally{
			conn.close();
		}	
	}

	@Override
	public void extendTTL(String key,int expire) {
		Jedis conn = pool.getResource();
		try{
			conn.expire(getSaveKey(key), expire);
		}finally{
			conn.close();
		}	
		
	}

	@Override
	public void refresh(String key, String value) {
		Jedis conn = pool.getResource();
		try{
			final String saveKey = getSaveKey(key);
			Long expire =conn.ttl(saveKey);
			if (expire != null)
				conn.setex(saveKey,expire.intValue(), value);
		}finally{
			conn.close();
		}
	}


}
