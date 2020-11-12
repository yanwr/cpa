package br.com.softplan.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/")
@ApiIgnore
public class HomeController {

    @RequestMapping("/")
    public @ResponseBody
    String hello() {
        return "Hello, World";
    }
}