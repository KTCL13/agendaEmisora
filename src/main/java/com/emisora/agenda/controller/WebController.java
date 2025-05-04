package com.emisora.agenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping(value = {"/", "/login", "/register"})
    public String index() {
        return "forward:/index.html";
    }
}