package com.luj8n.repofinder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    private final SearchService searchService;

    public MainController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("searchInputs", new SearchInputs());
        return "index";
    }

    @RequestMapping("/search")
    public String search(@ModelAttribute SearchInputs searchInputs, Model model) {
        model.addAttribute("result", searchService.search(searchInputs));
        return "search";
    }
}
