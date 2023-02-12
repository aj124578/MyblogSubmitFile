package shop.mtcoding.myblog.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.myblog.dto.board.BoardResp.BoardMainRespDto;

@Mapper
public interface BoardRepository {
    public List<BoardMainRespDto> findAllWithUser();

    public List<Board> findAll();

    public Board findById(int id);

    public int insert(@Param("title") String title, @Param("content") String content,
                @Param("thumbnail") String thumbnail, @Param("userId") int userId);

    public int updateById(@Param("id") int id, @Param("title") String title,
            @Param("content") String content, @Param("thumbnail") String thumbnail);

    public int deleteById(int id);
}
