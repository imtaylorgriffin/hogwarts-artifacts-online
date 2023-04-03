package edu.tcu.cs.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

    List<Artifact>artifacts;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a1.setImgUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImgUrl("ImageUrl");
        this.artifacts.add(a2);



    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {

        //given
        given(this.artifactService.findById("1250808601744904192")).willReturn(this.artifacts.get(1));


        //when
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl+"/artifacts/1250808601744904192").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value("Invisibility Cloak"));


    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {

        //given
        given(this.artifactService.findById("1250808601744904192")).willThrow(new ObjectNotFoundException("1250808601744904192"));


        //when
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl+"/artifacts/1250808601744904192").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 1250808601744904192 :("))
                .andExpect(jsonPath("$.data").isEmpty());


    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        //given
        given(this.artifactService.findAll()).willReturn(this.artifacts);


        //when and then
        this.mockMvc.perform(get(this.baseUrl+"/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data[0].name").value("Deluminator"))
                .andExpect(jsonPath("$.data[1].id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data[1].name").value("Invisibility Cloak"));




    }

    @Test
    void testAddArtifact() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto(null, "Remembrall", "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.", "ImageUrl", null);
            //above simulates frontend data

        String json = this.objectMapper.writeValueAsString(artifactDto);

        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("1250808601744904197");
        savedArtifact.setName("Remembrall");
        savedArtifact.setDescription("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
        savedArtifact.setImgUrl("ImageUrl");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn((savedArtifact));
        //when and then

        this.mockMvc.perform(post(this.baseUrl+"/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imgUrl").value(savedArtifact.getImgUrl()));





    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto("1250808601744904197", "Invisibility Cloak", "example desc2", "ImageUrl", null);
        //above simulates frontend data

        String json = this.objectMapper.writeValueAsString(artifactDto);

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("1250808601744904192");
        updatedArtifact.setName("Invisibility Cloak");
        updatedArtifact.setDescription("example desc2");
        updatedArtifact.setImgUrl("ImageUrl");

        given(this.artifactService.update(eq("1250808601744904192"), Mockito.any(Artifact.class))).willReturn((updatedArtifact));
        //when and then

        this.mockMvc.perform(put(this.baseUrl+"/artifacts/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imgUrl").value(updatedArtifact.getImgUrl()));


    }

    @Test
    void testUpdateArtifactErrorWithNonExistentId() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto("1250808601744904197", "Invisibility Cloak", "example desc2", "ImageUrl", null);
        //above simulates frontend data

        String json = this.objectMapper.writeValueAsString(artifactDto);

        given(this.artifactService.update(eq("1250808601744904192"), Mockito.any(Artifact.class))).willThrow(new ObjectNotFoundException("1250808601744904192"));
        //when and then

        this.mockMvc.perform(put(this.baseUrl+"/artifacts/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 1250808601744904192 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        //given
        doNothing().when(this.artifactService).delete("1250808601744904191");

        //then and when
        this.mockMvc.perform(delete(this.baseUrl+"/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());



    }

    @Test
    void testDeleteArtifactErrorWithNonExistentId() throws Exception {
        //given
        doThrow(new ObjectNotFoundException("1250808601744904191")).when(this.artifactService).delete("1250808601744904191");
        //then and when
        this.mockMvc.perform(delete(this.baseUrl+"/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 1250808601744904191 :("))
                .andExpect(jsonPath("$.data").isEmpty());



    }




}