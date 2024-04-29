package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventL {

  @Autowired
  private Call call;

  @EventListener
  public void handleRefresh(ContextRefreshedEvent event) {//method start when spring started
    call.start();
  }
}
