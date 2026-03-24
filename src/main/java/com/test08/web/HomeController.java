package com.test08.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    @GetMapping("/product")
    public String product() {
        return "forward:/product.html";
    }

    @GetMapping("/orders")
    public String orders() {
        return "forward:/orders.html";
    }
}