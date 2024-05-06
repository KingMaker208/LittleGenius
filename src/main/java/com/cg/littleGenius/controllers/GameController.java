package com.cg.littleGenius.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cg.littleGenius.services.GameService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping()
    public String game(Model model, HttpServletRequest request) {
        System.out.println("Session ID: " + request.getSession().getId()); // Log session ID
        model.addAttribute("board", gameService.getBoard());
        model.addAttribute("currentToken", gameService.getCurrentToken());
        model.addAttribute("message", "Select the cells and click on submit");
        return "game";
    }

    @PostMapping("/play")
    public String play(@RequestParam("numbers") List<Integer> numbers, Model model) {
        System.out.println("Received numbers: " + numbers);  // This will log the numbers received
        boolean correct = gameService.validateNumbers(numbers);
        if (correct) {
            gameService.nextToken();
        }
        model.addAttribute("board", gameService.getBoard());
        model.addAttribute("currentToken", gameService.getCurrentToken());
        model.addAttribute("message", correct ? "Correct selection and order! Well done." : "Incorrect selection or order. Try again.");

        return "game";
    }

    @PostMapping("/skip")
    public String skip(Model model) {
    	  model.addAttribute("board", gameService.getBoard());
    	  gameService.nextToken();
          model.addAttribute("currentToken", gameService.getCurrentToken());
          model.addAttribute("message", "Skipped a token. Select the cells and click on submit");
    	return "game";
    }

}
