package com.redis.distributed;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 * 1.redis连接池
 * 项目名称：redisDistributed 
 * 类名称：JedisPoolUtil
 * 开发者：Lenovo
 * 开发时间：2019年9月8日下午3:43:12
 */
public class JedisPoolUtil {
	//构造方法私有化
		private JedisPoolUtil() {};

		//创建一个静态对象
		private static volatile JedisPool jedisPool = null;

		/**
		 * 1. 对外提供一个创建连接的访问方法
		 * @return
		 */
		public static JedisPool getJedisPoolInstance() {
			if(null == jedisPool) {
				synchronized (JedisPoolUtil.class) {
					if(null == jedisPool) {
						JedisPoolConfig poolConfig = new JedisPoolConfig();
						//poolConfig.setMaxActive(1000);
						poolConfig.setMaxIdle(32);
						//poolConfig.setMaxWait(100*1000);
						poolConfig.setTestOnBorrow(true);
						jedisPool = new JedisPool(poolConfig,"192.168.24.1");
					}
				}
			}
			return jedisPool;
		}


		/**
		 * 2. 释放资源
		 * @param jedisPool
		 * @param jedis
		 */
		@SuppressWarnings("deprecation")
		public static void release(JedisPool jedisPool,Jedis jedis){
			if(null != jedis){	
				jedisPool.returnResourceObject(jedis);
			}
		}
}
