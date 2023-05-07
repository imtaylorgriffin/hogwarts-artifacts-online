package edu.tcu.cs.hogwartsartifactsonline.system;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.UserRepository;
import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.UserService;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DBDataInitalizer implements CommandLineRunner{

    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;

    //private final UserRepository userRepository;
    private final UserService userService;


    public DBDataInitalizer(ArtifactRepository artifactRepository, WizardRepository wizardRepository, UserService userService) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
        this.userService = userService;
        //this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        /*
             "id": "1250808601744904191",
      "name": "Deluminator",
      "description": "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
      "imageUrl": "ImageUrl",
      "owner": {
        "id": 1,
        "name": "Albus Dumbledore",
        "numberOfArtifacts": 2
      }
    },
    {
      "id": "1250808601744904192",
      "name": "Invisibility Cloak",
      "description": "An invisibility cloak is used to make the wearer invisible.",
      "imageUrl": "ImageUrl",
      "owner": {
        "id": 2,
        "name": "Harry Potter",
        "numberOfArtifacts": 2
      }
    },
    {
      "id": "1250808601744904193",
      "name": "Elder Wand",
      "description": "The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.",
      "imageUrl": "ImageUrl",
      "owner": {
        "id": 1,
        "name": "Albus Dumbledore",
        "numberOfArtifacts": 2
      }
    },
    {
      "id": "1250808601744904194",
      "name": "The Marauder's Map",
      "description": "A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.",
      "imageUrl": "ImageUrl",
      "owner": {
        "id": 2,
        "name": "Harry Potter",
        "numberOfArtifacts": 2
      }
    },
    {
      "id": "1250808601744904195",
      "name": "The Sword Of Gryffindor",
      "description": "A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.",
      "imageUrl": "ImageUrl",
      "owner": {
        "id": 3,
        "name": "Neville Longbottom",
        "numberOfArtifacts": 1
      }
    },
    {
      "id": "1250808601744904196",
      "name": "Resurrection Stone",
      "description": "The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.",
      "imageUrl": "ImageUrl",
      "owner": null
         */

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

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("example desc3");
        a3.setImgUrl("ImgUrl");

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("example desc4");
        a4.setImgUrl("ImgUrl");

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("example desc5");
        a5.setImgUrl("ImgUrl");

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("example desc6");
        a6.setImgUrl("ImgUrl");

        /*
        "id": 1,
      "name": "Albus Dumbledore",
      "numberOfArtifacts": 2
    },
    {
      "id": 2,
      "name": "Harry Potter",
      "numberOfArtifacts": 2
    },
    {
      "id": 3,
      "name": "Neville Longbottom",
      "numberOfArtifacts": 1
         */

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        w3.addArtifact(a5);

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);
        artifactRepository.save(a6);


        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("taylor");
        u2.setPassword("678912345");
        u2.setEnabled(true);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        this.userService.save(u1);
        this.userService.save(u2);
        this.userService.save(u3);






    }


}

