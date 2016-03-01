package com.zlp.cache;

/**
 * 描述：
 * @author zhoulongpeng
 * @date   2016-02-28
 */
public interface TokenStoreIfc {
	public void save(String key,String value,int expire);
	public void extendTTL(String key,int expire);
	public String getValue(String key);
	public boolean exists(String key);
	public void remove(String key);
	public void refresh(String key, String value);
}
