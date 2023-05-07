package edu.tcu.cs.hogwartsartifactsonline;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.UserService;
import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    List<HogwartsUser> users;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.users = new ArrayList<>();
        HogwartsUser user1 = new HogwartsUser();
        user1.setId(1);
        user1.setUsername("john");
        user1.setRoles("test");
        user1.setPassword("12345");
        this.users.add(user1);

        HogwartsUser user2 = new HogwartsUser();
        user2.setId(21);
        user2.setUsername("super john");
        user2.setRoles("big test");
        user2.setPassword("222212345");
        this.users.add(user2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {

        //given
        given(this.userService.findById(1)).willReturn(this.users.get(0));

        //when and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("john"));
    }

    @Test
    void testFindUserByIdNotFound() throws Exception {

        //given
        given(this.userService.findById(3)).willThrow(new ObjectNotFoundException("user",3));

        //when and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with ID: 3 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


}