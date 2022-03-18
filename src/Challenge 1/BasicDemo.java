package test;
import net.sf.clipsrules.jni.*;

public class BasicDemo {
    public static void main(String args[]) throws Exception {
        

        Environment clips;
        
        clips = new Environment();
        
        System.out.print("Hola");
        
        clips.eval("(clear)");
        
        clips.load("/Users/ramon/Desktop/KBS/src/persons/load-persons.clp");
        clips.load("/Users/ramon/Desktop/KBS/src/persons/load-persons-rules.clp");

        clips.eval("(reset)");
        clips.eval("(facts)");
        clips.eval("(rules)");

        clips.run();

        System.out.print("Adios");
    }

}