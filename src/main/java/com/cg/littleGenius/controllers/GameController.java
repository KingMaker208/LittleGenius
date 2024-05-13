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

@Controller
@RequestMapping("/")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public String game(Model model) {
    	 model.addAttribute("tokensCompleted", gameService.getTokensCompleted().toString().toString().replace('[', ' ').replace(']', ' ' ));
         model.addAttribute("tokensSkipped", gameService.getTokensSkipped().toString().toString().replace('[', ' ').replace(']', ' ' ));
        if (!gameService.hasTokensLeft()) {
            return "finish";
        }
        model.addAttribute("board", gameService.getBoard());
        model.addAttribute("currentToken", gameService.getCurrentToken());
        model.addAttribute("points", gameService.getPoints());

        return "game";
    }

    @PostMapping("/play")
    public String play(@RequestParam("numbers") List<Integer> numbers, Model model) {
        boolean correct = gameService.validateNumbers(numbers);
        
        model.addAttribute("tokensCompleted", gameService.getTokensCompleted().toString().toString().replace('[', ' ').replace(']', ' ' ));
        model.addAttribute("tokensSkipped", gameService.getTokensSkipped().toString().toString().replace('[', ' ').replace(']', ' ' ));
        model.addAttribute("points", gameService.getPoints());
        if (correct) {
            gameService.nextToken();
        }
        if (!gameService.hasTokensLeft()) {
            return "finish";
        }
        model.addAttribute("board", gameService.getBoard());
        model.addAttribute("currentToken", gameService.getCurrentToken());
        
        
        model.addAttribute("message", correct ? "Correct selection and order! Well done." : "Incorrect selection or order. Try again.");
        return "game";
    }

    @PostMapping("/skip")
    public String skip(Model model) {
    	gameService.addTokenSkipped();
        gameService.nextToken();
        model.addAttribute("tokensCompleted", gameService.getTokensCompleted().toString().toString().replace('[', ' ').replace(']', ' ' ));
        model.addAttribute("tokensSkipped", gameService.getTokensSkipped().toString().replace('[', ' ').replace(']', ' ' ));
        model.addAttribute("points", gameService.getPoints());
        if (!gameService.hasTokensLeft()) {
            return "finish";
        }
        model.addAttribute("board", gameService.getBoard());
        model.addAttribute("currentToken", gameService.getCurrentToken());
        
       
        model.addAttribute("message", "Skipped!");
        return "game";
    }

    @PostMapping("/reset")
    public String resetGame(Model model) {
        gameService.resetGame();
        return "redirect:/";
    }
}
