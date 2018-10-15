//pom 依赖
	<dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>


 //连接redis
 Jedis jedis = new Jedis("localhost");
 //或用RedisConfig配置文件加载
 List<GovUnfinishedItem> govUnfinishedItems = hbHandleDao.findNoDetailHb();
//存到redis
 redisTemplate.opsForValue().set("labelStrList", govUnfinishedItems, 86400L, TimeUnit.SECONDS);
 //取出redis数据
 JSONObject o = (JSONObject) redisTemplate.opsForValue().get("labelStrList");
 System.out.println(o.toString());
        