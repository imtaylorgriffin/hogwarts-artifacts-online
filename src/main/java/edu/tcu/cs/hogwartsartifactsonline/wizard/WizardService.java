package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;
    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public List<Wizard> findAll(){
        return this.wizardRepository.findAll();
    }

    public Wizard findById(Integer wizardId){
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(()-> new ObjectNotFoundException(wizardId));
    }

    public Wizard save(Wizard newWizard){
        return this.wizardRepository.save(newWizard);
    }

    public Wizard update(Integer wizardId, Wizard update){
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard -> {
                    oldWizard.setName(update.getName());
                    return this.wizardRepository.save(oldWizard);
                })
                .orElseThrow(()->new ObjectNotFoundException(wizardId));
    }

    public void delete(Integer wizardId){
        Wizard wizardToBeDeleted = this.wizardRepository.findById(wizardId)
                .orElseThrow(()->new ObjectNotFoundException(wizardId));

        wizardToBeDeleted.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }


    public void assignArtifact(Integer wizardId, String artifactId){

        // find artifact by id
        Artifact artifactToBeAssigned = this.artifactRepository.findById(artifactId)
                .orElseThrow(()->new ObjectNotFoundException("artifact",artifactId));
        // find wiz by id
        Wizard wizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(()->new ObjectNotFoundException("wizard",wizardId));

        //artifact assignment
        // see if artifact already owned
        if (artifactToBeAssigned.getOwner()!=null){
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }
        wizard.addArtifact(artifactToBeAssigned);


    }

}
