package com.github.seungki.controller;

import com.github.seungki.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rest-jms-sample")
public class DefaultController {

    @Autowired
    ProducerService producerService;

    @PostMapping(value = "/async")
    public Map<String, Object> async(@RequestBody Map<String, Object> paramMap) throws Exception{

        log.info("============================= ASYNC =============================");
        Map<String, Object> resultMap = producerService.send(paramMap);

        return resultMap;
    }

    @PostMapping(value = "/sync")
    public Map<String, Object> sync(@RequestBody Map<String, Object> paramMap) throws Exception{

        log.info("============================= SYNC =============================");
        Map<String, Object> resultMap = producerService.sendAndReceive(paramMap);

        return resultMap;
    }
}
