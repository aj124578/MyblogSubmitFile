package shop.mtcoding.myblog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.myblog.dto.ResponseDto;
import shop.mtcoding.myblog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.myblog.handler.ex.CustomApiException;
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

    @DeleteMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id){
        User principal = (User) session.getAttribute("principal");
        if(principal == null){
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        boardService.게시글삭제(id);
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제성공", null), HttpStatus.OK);
    }

    // 글쓰기
    @PostMapping("/board")
    public String save(BoardSaveReqDto boardSaveReqDto){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED);
        }

        if(boardSaveReqDto.getTitle() == null || boardSaveReqDto.getTitle().isEmpty()){
            throw new CustomApiException("title을 작성해주세요.");
        }

        if (boardSaveReqDto.getContent() == null || boardSaveReqDto.getContent().isEmpty()) {
            throw new CustomApiException("content를 작성해주세요.");
        }

        if (boardSaveReqDto.getTitle().length() > 100) {
            throw new CustomApiException("title의 길이가 100자 이하여야 합니다.");
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
