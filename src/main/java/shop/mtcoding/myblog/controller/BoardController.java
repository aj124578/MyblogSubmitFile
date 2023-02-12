package shop.mtcoding.myblog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.myblog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.myblog.handler.ex.CustomException;
import shop.mtcoding.myblog.model.BoardRepository;
import shop.mtcoding.myblog.model.User;
import shop.mtcoding.myblog.service.BoardService;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    // 글쓰기
    @PostMapping("/board")
    public String save(BoardSaveReqDto boardSaveReqDto){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED);
        }

        if(boardSaveReqDto.getTitle() == null || boardSaveReqDto.getTitle().isEmpty()){
            throw new CustomException("title을 작성해주세요.");
        }

        if (boardSaveReqDto.getContent() == null || boardSaveReqDto.getContent().isEmpty()) {
            throw new CustomException("content를 작성해주세요.");
        }

        if (boardSaveReqDto.getTitle().length() > 100) {
            throw new CustomException("title의 길이가 100자 이하여야 합니다.");
        }

        boardService.글쓰기(boardSaveReqDto, principal.getId());
     
        return "redirect:/";
    }


    @GetMapping({"/", "/board"})
    public String main(Model model){
        model.addAttribute("dtos", boardRepository.findAllWithUser());
        return "board/main";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, Model model){
        model.addAttribute("dto", boardRepository.findByIdWithUser(id));
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String saveForm(){
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id){
        return "board/updateForm";
    }
}
