package edu.tcu.cs.hogwartsartifactsonline.wizard;


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




}


