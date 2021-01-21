package ru.study.shop.adapters.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop/app")
public class ApplicationController {

    private static final int DEFAULT_SHUTDOWN_CODE = 0;
    private ApplicationContext applicationContext;

    public ApplicationController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @GetMapping("/shutdown")
    public void shutdownApplication() {
        SpringApplication.exit(applicationContext, () -> DEFAULT_SHUTDOWN_CODE);
    }
}
