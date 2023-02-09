package shop.mtcoding.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {
    
    @GetMapping({"/", "/board"})
    public String main(){
        return "board/main";
    }

    @GetMapping("/detail/{id}")
    public String detail(){
        return "board/detail";
    }

    @GetMapping("/detail/saveForm")
    public String saveForm(){
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id){
        return "board/updateForm";
    }
}
