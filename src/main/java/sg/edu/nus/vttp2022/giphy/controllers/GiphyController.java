package sg.edu.nus.vttp2022.giphy.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.vttp2022.giphy.service.GiphyService;

@Controller
public class GiphyController {
    
    @Autowired
    private GiphyService giphyService;

    @GetMapping(path="/search")
    public String getPage(@RequestParam String q, @RequestParam Integer limit, @RequestParam String rating, Model model) {
        
        List<String> results = giphyService.getGiphs(q, rating, limit);
        model.addAttribute("q", q);
        model.addAttribute("results", results);

        return "searchResults";
    }

    
}
