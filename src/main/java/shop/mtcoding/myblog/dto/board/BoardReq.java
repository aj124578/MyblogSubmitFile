package shop.mtcoding.myblog.dto.board;

import lombok.Getter;
import lombok.Setter;

public class BoardReq {
    
    @Setter
    @Getter
    public static class BoardSaveReqDto{
        private String title;
        private String content;
    }

    @Setter
    @Getter
    public static class BoradUpdateReqDto {
        private String title;
        private String content;
    }

}
