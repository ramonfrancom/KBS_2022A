package test; 

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import net.sf.clipsrules.jni.*;

public class ClipsAgent extends Agent {

  Environment clips;
  protected void setup() {
    System.out.println("Hola soy "+getLocalName());
    try{
        clips = new Environment(); //En nuevas versiones el try-catch deberia ir aquí
    }catch(Exception e){}
    addBehaviour(new TellBehaviour());
    addBehaviour(new AskBehaviour());
  } 

  private class TellBehaviour extends Behaviour {
      
    boolean isDone = false; 

    public void action() {
        //En nuevas versiones el try-catch deberia ir aquí
        try{
          clips.eval("(clear)");
          // clips.load("/Users/ramon/Desktop/KBS/src/persons/load-persons.clp");
          // clips.load("/Users/ramon/Desktop/KBS/src/persons/load-persons-rules.clp");
          
          // clips.load("/Users/ramon/Desktop/KBS/src/prodcust/load-prod-cust.clp");
          // clips.load("/Users/ramon/Desktop/KBS/src/prodcust/load-prodcust-rules.clp");
          
          // clips.load("/Users/ramon/Desktop/KBS/src/market/templates.clp");
          // clips.load("/Users/ramon/Desktop/KBS/src/market/facts.clp");
          // clips.load("/Users/ramon/Desktop/KBS/src/market/rules.clp");
          clips.build("(deftemplate person (slot name) (slot gender) (slot age) (slot partner))");
          clips.build("(deffacts partnership (person (name Susan) (gender female)  (age 24) (partner Fred)))");
          clips.build("(defrule my-rule5 (person (gender female) (name ?n)) => (printout t ?n  is female crlf))");
          clips.reset();
          
          /*clips.load("/Users/gdlmo/Desktop/CLIPSJNI/work/resources/rules.clp");
          System.out.println("Archivo listo");*/
        }catch(Exception e){}
        isDone = true;
    } 
    
    public boolean done() {
      if (isDone)
        return true;
      else
	      return false;
    } 
  }
  
  private class AskBehaviour extends Behaviour {
    boolean isDone = false; 

    public void action() {
        
        //En nuevas versiones el try-catch deberia ir aquí
        try{
            System.out.println("Facts");
            clips.eval("(facts)"); 
            System.out.println("Rules");
            clips.eval("(rules)"); 
            
            clips.eval("(run)");
        }catch(Exception e){}
        isDone = true;
    } 
    
    public boolean done() {
      if (isDone)
        return true;
      else
	return false;
    }
   
    public int onEnd() {
      myAgent.doDelete();
      return super.onEnd();
    } 
  }
}
