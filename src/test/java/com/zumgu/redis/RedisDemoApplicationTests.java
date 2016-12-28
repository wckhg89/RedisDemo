package com.zumgu.redis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisDemoApplicationTests {

	@Resource(name="redisTemplate")
	private ValueOperations<String, String> valueOperations;

	@Resource(name = "redisTemplate")
	private ListOperations<String, String> listOperations;

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> hashOperations;

	@Resource(name = "redisTemplate")
	private SetOperations<String, String> setOperations;

	@Resource(name="redisTemplate")
	private ZSetOperations<String, String> zSetOperations;


	@Before
	public void init () {
        valueOperations.set("test:line", "\n-------\n");
        //list put
        listOperations.rightPush("test:task", "자기소개"); // rpush test:task 자기소개 rpush [key] [value]
        listOperations.rightPush("test:task", "취미소개");
        listOperations.rightPush("test:task", "소망소개");
        listOperations.rightPush("test:task", "선임이직");
        //hash put
        hashOperations.put("test:user:kingbbode", "name", "권뽀대"); // hset [key] [field] [name]
        hashOperations.put("test:user:kingbbode", "age", "28");
        //set put
        setOperations.add("test:user:kingbbode:hobby", "개발"); // sadd [key] [value]
        setOperations.add("test:user:kingbbode:hobby", "잠");
        setOperations.add("test:user:kingbbode:hobby", "옷 구경");
        //zset (sorted set)
        zSetOperations.add("test:user:kingbbode:wish", "배포한 것에 장애없길", 1); // zadd [key] [value] [score]
        zSetOperations.add("test:user:kingbbode:wish", "배포한거 아니여도 장애없길", 2);
        zSetOperations.add("test:user:kingbbode:wish", "경력직 채용", 3);
        zSetOperations.add("test:user:kingbbode:wish", "잘자기", 4);
    }

	@Test
	public void redisTest1() {
		String task = listOperations.leftPop("test:task");
		StringBuilder stringBuilder = new StringBuilder();
		while (task != null) {
			switch (task) {
				case "자기소개":
					Map<String, String> intro = hashOperations.entries("test:user:kingbbode"); // hash value를 자바 오브젝트로 converting 해주는듯
					stringBuilder.append("\n******자기소개********");
					stringBuilder.append("\n이름은 ");
					stringBuilder.append(intro.get("name")); // hget [key] [field]
					stringBuilder.append("\n나이는 ");
					stringBuilder.append(intro.get("age")); // hget [key] [field]
					break;
				case "취미소개":
					Set<String> hobbys = setOperations.members("test:user:kingbbode:hobby"); // smember [key] : get all the members in set
					stringBuilder.append("\n******취미소개******");
					stringBuilder.append("취미는");
					for (String hobby : hobbys) {
						stringBuilder.append("\n");
						stringBuilder.append(hobby);
					}
					break;
				case "소망소개":
					Set<String> wishes = zSetOperations.range("test:user:kingbbode:wish", 0, 2); // zrange [key] [start] [stop]
					stringBuilder.append("\n******소망소개******");
					int rank = 1;
					for (String wish : wishes){
						stringBuilder.append("\n");
						stringBuilder.append(rank);
						stringBuilder.append("등 ");
						stringBuilder.append(wish);
						rank++;
					}
					break;
				case "선임이직":
					stringBuilder.append("\n!!! 믿었던 선임 이직");
					zSetOperations.incrementScore("test:user:kingbbode:wish", "경력직 채용", -3);
					listOperations.rightPush("test:task", "소망소개");
					break;
				default:
					stringBuilder.append("nonone");

			}
			task = listOperations.leftPop("test:task");
		}
		System.out.println(stringBuilder.toString());
	}
}
