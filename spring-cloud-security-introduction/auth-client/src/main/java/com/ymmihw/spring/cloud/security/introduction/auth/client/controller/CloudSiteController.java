package com.ymmihw.spring.cloud.security.introduction.auth.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

@RestController
public class CloudSiteController {

  @Autowired
  private RestOperations restOperations;

  @GetMapping("/")
  @ResponseBody
  public String helloFromBaeldung() {
    return "Hello From Baeldung!";
  }

  @GetMapping("/personInfo")
  @ResponseBody
  public String person() {
    String personResourceUrl = "http://localhost:9090/person";
    return restOperations.getForObject(personResourceUrl, String.class);
  }
}
