package com.gzdzss.redis.example;

import com.gzdzss.redis.plugin.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:zhouyanjie666666@gmail.com">zyj</a>
 * @date 2019/7/24
 */

@Slf4j
@RestController
@SpringBootApplication
public class RedisLockExampleApplication {


    @Autowired
    private DistributedLock distributedLock;

    /**
     * 采用默认的 过期时间， 重试次数， 重试间隔
     *
     * @return
     * @See DistributedLock{TIMEOUT_MILLIS, RETRY_TIMES, SLEEP_MILLIS }
     */
    @GetMapping("/lock")
    public Boolean lock() {
        String key = "gzdzss:lock.demo";
        return distributedLock.lock(key);
    }

    /**
     *
     * 设置过期时间为 2秒
     * @return
     */
    @GetMapping("/lock0")
    public Boolean lock0() {
        String key = "gzdzss:lock.demo0";
        return distributedLock.lock(key, 2000L);
    }

    /**
     * 设置重试次为 0
     *
     * @return
     */
    @GetMapping("/lock1")
    public Boolean lock1() {
        String key = "gzdzss:lock.demo1";
        return distributedLock.lock(key, 0);
    }


    /**
     * 设置过期时间为5秒， 重试3次
     *
     * @return
     */
    @GetMapping("/lock2")
    public Boolean lock2() {
        String key = "gzdzss:lock.demo3";
        return distributedLock.lock(key, 5000L, 3);
    }



    /**
     * 设置重试次数为 2， 间隔为 3秒
     *
     * @return
     */
    @GetMapping("/lock3")
    public Boolean lock3() {
        String key = "gzdzss:lock.demo3";
        return distributedLock.lock(key, 2, 3000L);
    }


    /**
     * 设置 5秒过期 ， 重试2次， 每次3秒
     *
     * @return
     */
    @GetMapping("/lock4")
    public Boolean lock4() {
        String key = "gzdzss:lock.demo4";
        return distributedLock.lock(key, 5000L, 2, 3000L);
    }

    /**
     * 手动释放锁
     *
     * @return
     */
    @GetMapping("/lock5")
    public String lock5() {
        String key = "gzdzss:lock.demo5";
        distributedLock.lock(key);
        log.debug("execute...");
        distributedLock.releaseLock(key);
        return HttpStatus.OK.toString();
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisLockExampleApplication.class, args);
    }
}
