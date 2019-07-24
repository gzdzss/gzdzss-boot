package com.gzdzss.redis.example;

import com.gzdzss.redis.example.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */

@Slf4j
@RestController
@SpringBootApplication
public class RedisAopLockExampleApplication {


    @Autowired
    private DemoService demoService;

    @GetMapping("/lock")
    public String lock() {
        return demoService.lock();
    }


    /**
     * 获取锁失败 抛出异常
     * @param id
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/lockById")
    public String lockById(String id) throws InterruptedException {
        return demoService.lockById(id);
    }

    /**
     * 获取锁失败 返回null
     * @param name
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/lockByName")
    public String lockByName(String name) throws InterruptedException {
        return demoService.lockByName(name);
    }


    /**
     * 获取锁失败， 发起重试
     * @param id
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/continueLock")
    public String continueLock(String id) throws InterruptedException {
        return demoService.continueLock(id);
    }



    @GetMapping("/hello")
    public String hello() {
        return demoService.hello();
    }




    public static void main(String[] args) {
        SpringApplication.run(RedisAopLockExampleApplication.class, args);
    }
}
