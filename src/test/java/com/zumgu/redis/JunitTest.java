package com.zumgu.redis;

import static org.junit.Assert.*;

import com.zumgu.redis.service.RedisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 강홍구 on 2016-12-31.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisDemoApplication.class)
public class JunitTest {

    @Autowired
    RedisService redisService;


    @Before
    public void init () {

    }

    @Test
    public void 테스트_setValue () {
        redisService.setValue("test:line", "라인");
        assertEquals(redisService.getValue("test:line"), "라인");
    }

    @Test
    public void 테스트_pushList () {
        List<String> testList = new ArrayList<>();
        testList.add("하이");
        testList.add("안녕");
        testList.add("니하오");

        redisService.pushList("test:hello", testList);

        assertEquals(testList, redisService.getAllList("test:hello"));
    }



}
