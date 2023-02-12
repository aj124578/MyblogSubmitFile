package shop.mtcoding.myblog.controller;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.myblog.dto.ResponseDto;
import shop.mtcoding.myblog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.myblog.dto.board.BoardReq.BoradUpdateReqDto;
import shop.mtcoding.myblog.handler.ex.CustomApiException;
import shop.mtcoding.myblog.handler.ex.CustomException;
import shop.mtcoding.myblog.model.Board;
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

    /* 게시글 수정 */
    @PutMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> update(@PathVariable int id, @RequestBody BoradUpdateReqDto boradUpdateReqDto, HttpServletResponse response){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        if (boradUpdateReqDto.getTitle() == null || boradUpdateReqDto.getTitle().isEmpty()) {
            throw new CustomApiException("title을 작성해주세요.");
        }

        if (boradUpdateReqDto.getContent() == null || boradUpdateReqDto.getContent().isEmpty()) {
            throw new CustomApiException("content를 작성해주세요.");
        }

        boardService.게시글수정(id, boradUpdateReqDto, principal.getId());
        
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글수정성공", null), HttpStatus.OK);
    }

    /* 게시글 삭제 */ 
    @DeleteMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id){
        User principal = (User) session.getAttribute("principal");
        if(principal == null){
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        boardService.게시글삭제(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제성공", null), HttpStatus.OK);
    }

    /* 게시글 쓰기 */
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
    public String updateForm(@PathVariable int id, Model model){
        User principal = (User) session.getAttribute("principal");
        Board boardPS = boardRepository.findById(id);

        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED);
        }

        if (boardPS == null) {
            throw new CustomException("없는 게시글을 수정할 수 없습니다");
        }
        if (boardPS.getUserId() != principal.getId()) {
            throw new CustomException("게시글을 수정할 권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        model.addAttribute("board", boardPS);
        return "board/updateForm";
    }
}
