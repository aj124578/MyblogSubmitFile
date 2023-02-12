package shop.mtcoding.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.myblog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.myblog.handler.ex.CustomApiException;
import shop.mtcoding.myblog.model.Board;
import shop.mtcoding.myblog.model.BoardRepository;

@Transactional(readOnly = true)
@Service
public class BoardService {
    
    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public void 글쓰기(BoardSaveReqDto boardSaveReqDto, int userId){

        int result = boardRepository.insert(
                boardSaveReqDto.getTitle(),
                boardSaveReqDto.getContent(),
                "/images/dora.png",
                userId);
                
        if(result != 1){
            throw new CustomApiException("글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void 게시글삭제(int id, int userId) {
    Board boardPS = boardRepository.findById(id);
    if (boardPS == null) {
        throw new CustomApiException("없는 게시글을 삭제할 수 없습니다.");
    }
    if (boardPS.getUserId() != userId) {
        throw new CustomApiException("해당 게시글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    try {
        boardRepository.deleteById(id);
    } catch (Exception e) {
        throw new CustomApiException("서버에 일시적인 문제가 생겼습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    }
}
