package shop.mtcoding.myblog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.myblog.dto.board.BoardResp;
import shop.mtcoding.myblog.dto.board.BoardReq.BoradUpdateReqDto;
import shop.mtcoding.myblog.dto.board.BoardResp.BoardDetailRespDto;
import shop.mtcoding.myblog.model.User;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BoardControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    private MockHttpSession mockSession;

    @BeforeEach // test 메서드 실행 직전 마다 호출
    public void setUp() {
        // 세션 주입
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setEmail("ssar@nate.com");
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    /* update_test */
    @Test
    public void update_test() throws Exception {
        // given
        int id = 1;
        BoradUpdateReqDto boradUpdateReqDto = new BoradUpdateReqDto();
        boradUpdateReqDto.setTitle("제목1-수정");
        boradUpdateReqDto.setContent("내용1-수정");

        String requestBody = om.writeValueAsString(boradUpdateReqDto);
        System.out.println("update_test : " + requestBody);


        // when
        ResultActions resultActions = mvc.perform(put("/board/" + id)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .session(mockSession));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(1));

    }

    /* save_test */
    @Test
    public void save_test() throws Exception {
        // given
        String title = "";
        for (int i = 0; i < 10; i++) {
            title += "가";
        }

        String requestBody = "title=" + title + "&content=내용1";

        // when
        ResultActions resultActions = mvc.perform(post("/board").content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .session(mockSession)
                        );

        System.out.println("save_test : ");
        // then
        resultActions.andExpect(status().is3xxRedirection());
        
    }

    /* detail_test */
    @Test
    public void detail_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(get("/board/" + id));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardDetailRespDto dto = (BoardDetailRespDto) map.get("dto");
        String model = om.writeValueAsString(dto);
        System.out.println("detail_test : " + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(dto.getUsername()).isEqualTo("ssar");
        assertThat(dto.getUserId()).isEqualTo(1);
        assertThat(dto.getTitle()).isEqualTo("1번째 제목");
    }

    /* main_test */
    @Test
    public void main_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/"));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardResp.BoardMainRespDto> dtos = (List<BoardResp.BoardMainRespDto>) map.get("dtos");
        String model = om.writeValueAsString(dtos);
        System.out.println("main_test : " + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(dtos.size()).isEqualTo(6);
        assertThat(dtos.get(0).getUsername()).isEqualTo("ssar");
        assertThat(dtos.get(0).getTitle()).isEqualTo("1번째 제목");
    }

    /* delete_test */
    @Test
    public void delete_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(delete("/board/" + id)
                                      .session(mockSession));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();                                        
        System.out.println("delete_test : " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(1));
    }
}
