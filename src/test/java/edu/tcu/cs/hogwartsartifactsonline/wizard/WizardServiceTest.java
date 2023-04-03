package edu.tcu.cs.hogwartsartifactsonline.wizard;


import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
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
class WizardServiceTest {

    @Mock //simulate below obj
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;


    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;


    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);
    }

    @AfterEach
    void tearDown() {
    }

    //mocking, return fake data
    @Test
    void testFindByIdSuccess() {


        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Albus Dumbledore");


        given(wizardRepository.findById(1)).willReturn(Optional.of(w)); //define mock obj behavior


        //when, act on the target behavior, method to be testing
        Wizard returnedWizard = wizardService.findById(1);






        //then, compare when with expected result, assert expected outcomes
        assertThat(returnedWizard.getId()).isEqualTo(w.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w.getName());
        verify(wizardRepository,times(1)).findById(1);

    }

    @Test
    void testFindByIdNotFound(){
        //given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());



        //when
        Throwable thrown = catchThrowable(()->{
            Wizard returnedWizard = wizardService.findById(1);



        });


        //then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with ID: 1 :(");
        verify(wizardRepository,times(1)).findById(1);


    }

    @Test
    void testFindAllSuccess(){
        //given
        given(wizardRepository.findAll()).willReturn(this.wizards);


        //when
        List<Wizard> actualWizards = wizardService.findAll();

        //then
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
        verify(wizardRepository, times(1)).findAll();


    }


    void testSaveSuccess(){
        //given
        Wizard newWizard = new Wizard();
        newWizard.setName("Heromine Granger");

        given(wizardRepository.save(newWizard)).willReturn(newWizard);

        //when
        Wizard savedWizard = wizardService.save(newWizard);

        //then
        assertThat(savedWizard.getId()).isEqualTo(newWizard.getName());
        verify(wizardRepository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateSuccess(){
        //given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");

        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - Update");

        given(wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);

        //when
        Wizard updatedWizard = wizardService.update(1, update);

        //then
        assertThat(updatedWizard.getId()).isEqualTo(1);
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound(){
        //given
        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - Update");

        given(wizardRepository.findById(1)).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () ->{
            wizardService.update(1, update);
        });

        //then
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess(){
        //given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");

        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(1);

        //when
        wizardService.delete(1);

        //then
        verify(wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound(){
        //given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () ->{
            wizardService.delete(1);
        });

        //then
        verify(wizardRepository, times(1)).findById(1);
    }


    @Test
    void testAssignArtifactSuccess(){
        //Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImgUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.of(w3));


        //when


        this.wizardService.assignArtifact(3,"1250808601744904192");




        //then

        assertThat(a.getOwner().getId()).isEqualTo(3);
        assertThat(w3.getArtifacts()).contains(a);

    }


    @Test
    void testAssignArtifactErrorWithNonExistentWizardId(){
        //Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImgUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);


        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.empty());


        //when


        Throwable thrown = assertThrows(ObjectNotFoundException.class,()->{
            this.wizardService.assignArtifact(3,"1250808601744904192");
        });




        //then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find wizard with ID: 3 :(");
        assertThat(a.getOwner().getId()).isEqualTo(2);

    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId(){
        //Given

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());


        //when


        Throwable thrown = assertThrows(ObjectNotFoundException.class,()->{
            this.wizardService.assignArtifact(3,"1250808601744904192");
        });




        //then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find artifact with ID: 1250808601744904192 :(");


    }




}


