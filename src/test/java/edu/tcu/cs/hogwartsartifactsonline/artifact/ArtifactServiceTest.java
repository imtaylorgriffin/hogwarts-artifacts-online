package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock //simulate below obj
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;


    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("example desc1");
        a1.setImgUrl("ImgUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("example desc2");
        a2.setImgUrl("ImgUrl");

        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    //mocking, return fake data
    @Test
    void testFindByIdSuccess() {
        // given, arrange inputs and targets, define behavior of mock objs
            //"id": "1250808601744904192",
            //    "name": "Invisibility Cloak",
            //    "description": "An invisibility cloak is used to make the wearer invisible.",
            //    "imageUrl": "ImageUrl",

        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImgUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setOwner(w);


        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a)); //define mock obj behavior


        //when, act on the target behavior, method to be testing
        Artifact returnedArtifact = artifactService.findById("1250808601744904192");






        //then, compare when with expected result, assert expected outcomes
        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImgUrl()).isEqualTo(a.getImgUrl());
        verify(artifactRepository,times(1)).findById("1250808601744904192");

    }

    @Test
    void testFindByIdNotFound(){
        //given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());



        //when
        Throwable thrown = catchThrowable(()->{
            Artifact returnedArtifact = artifactService.findById("1250808601744904192");



        });


        //then
        assertThat(thrown)
                .isInstanceOf(ArtifactNotFoundExeception.class)
                .hasMessage("Could not find artifact with ID: 1250808601744904192 :(");
        verify(artifactRepository,times(1)).findById("1250808601744904192");


    }

    @Test
    void testFindAllSuccess(){
        //given
        given(artifactRepository.findAll()).willReturn(this.artifacts);


        //when
        List<Artifact> actualArtifacts = artifactService.findAll();

        //then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();


    }

    @Test
    void testSaveSuccess(){
        //given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Description");
        newArtifact.setImgUrl("ImageUrl...");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);




        //when
        Artifact savedArtifact = artifactService.save(newArtifact);

        //then

        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImgUrl()).isEqualTo(newArtifact.getImgUrl());
        verify(artifactRepository, times(1)).save(newArtifact);


    }

    @Test
    void testUpdateSuccess(){
        //given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904191");
        oldArtifact.setName("Deluminator");
        oldArtifact.setDescription("example desc1");
        oldArtifact.setImgUrl("ImgUrl");

        Artifact update = new Artifact();
        update.setId("1250808601744904191");
        update.setName("Deluminator");
        update.setDescription("A new description");
        update.setImgUrl("ImgUrl");

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);




        //when
        Artifact updatedArtifact = artifactService.update("1250808601744904191", update);


        //then
        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepository, times(1)).findById("1250808601744904191");
        verify(artifactRepository, times(1)).save(oldArtifact);






    }

    @Test
    void testUpdateNotFound(){
        //given
        Artifact update = new Artifact();
        update.setId("1250808601744904191");
        update.setName("Deluminator");
        update.setDescription("A new description");
        update.setImgUrl("ImgUrl");

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());


        // when
        assertThrows(ArtifactNotFoundExeception.class, () ->{
            artifactService.update("1250808601744904191", update);
        });


        //then
        verify(artifactRepository, times(1)).findById("1250808601744904191");


    }

    @Test
    void testDeleteSuccess(){
        //given
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904191");
        artifact.setName("Deluminator");
        artifact.setDescription("example desc1");
        artifact.setImgUrl("ImgUrl");

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("1250808601744904191");

        //when
        artifactService.delete("1250808601744904191");


        //then
        verify(artifactRepository, times(1)).deleteById("1250808601744904191");


    }


    @Test
    void testDeleteNotFound(){
        //given

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());

        //when
        assertThrows(ArtifactNotFoundExeception.class, () ->{
            artifactService.delete("1250808601744904191");

        });

        //then
        verify(artifactRepository, times(1)).findById("1250808601744904191");


    }


}