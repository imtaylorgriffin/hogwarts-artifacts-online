package edu.tcu.cs.hogwartsartifactsonline.artifact;

public class ArtifactNotFoundExeception extends RuntimeException{
    public ArtifactNotFoundExeception(String id){
        super("Could not find artifact with ID: "+id+" :(");
    }



}
