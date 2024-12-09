package com.boardgames.ScotLandYardGames.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class WebController {

    @GetMapping("/index")
    public ModelAndView index() {
        System.out.println("somebody joined");
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }
}
