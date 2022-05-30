package Agents;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.core.behaviours.Behaviour;

import java.util.*;

import net.sf.clipsrules.jni.*;

/**
   This example shows how to implement the responder role in 
   a FIPA-contract-net interaction protocol. In this case in particular 
   we use a <code>ContractNetResponder</code>  
   to participate into a negotiation where an initiator needs to assign
   a task to an agent among a set of candidates.
   @author Giovanni Caire - TILAB
 */
public class SellerAgent extends Agent {
    Environment clips;

	protected void setup() {
		try {
			clips = new Environment();
		}catch (Exception e) {
			System.out.println(e);
		}

		addBehaviour(new TellBehaviour());
		// addBehaviour(new AskBehaviour());

		System.out.println("Agent " + getLocalName() + " waiting for CFP...");

		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
				MessageTemplate.MatchPerformative(ACLMessage.CFP) );

		addBehaviour(new ContractNetResponder(this, template) {
			@Override
			protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
                            
				List<FactAddressValue> agentOffers = new LinkedList<FactAddressValue>();
				System.out.println("Agent" + getLocalName() + ": has a new order from: " + cfp.getSender().getLocalName());
                                
                                // Extract data from the CFP 
				String [] content = cfp.getContent().split(":");
				String card = content[0];
				String products = content[1];
				String[] productsList = products.replace("[", "").replace("]", "").split(",");
				String message = "";
				
				try {
                                    //Change string names
                                    String assertProducts = null;
                                    for(int i = 0; i<productsList.length; i++){
                                        assertProducts = "(order (order-id " + cfp.getSender().getLocalName() + ") (seller-id " + getLocalName() + ") (card " + card + ") (product " + productsList[i] + "))";
                                        clips.assertString(assertProducts);
                                    }

                                    // Rules
                                    clips.run();

                                    List<FactAddressValue> offers = clips.findAllFacts("offer");

                                    for(FactAddressValue offer: offers) {
                                            if(cfp.getSender().getLocalName().equals(offer.getSlotValue("order-id").toString())){
                                                    agentOffers.add(offer);
                                            }
                                    }

                                    for(FactAddressValue offer: agentOffers) {
                                            message += offer.getSlotValue("product") + ":" + offer.getSlotValue("price") + ",";
                                    }

				} catch (Exception e) {
                                    System.out.println(e);
				}

				if(agentOffers.size() <= 0){ // seller does not have the products, so it refuses
					System.out.println("Agent "+getLocalName()+": Refuse");
					throw new RefuseException("No products found");
				}


				// Provide a proposal
				System.out.println("Agent "+getLocalName()+": Proposing "+ message);
				ACLMessage propose = cfp.createReply();
				propose.setPerformative(ACLMessage.PROPOSE);
				propose.setContent(message);
				return propose;
			}

			@Override
			protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
				String [] products = accept.getContent().split(",");
				String message = "Products purchased";
				String assertProduct = "";
				try {
					for(String product: products){
                                            assertProduct = "(purchase (order-id " + cfp.getSender().getLocalName() + ") (product " + product + "))";
                                            clips.assertString(assertProduct);
					}
					clips.run();


					List<FactAddressValue> offersMsg = clips.findAllFacts("purchase-msg");
					
					for(FactAddressValue offer: offersMsg) {
						if(cfp.getSender().getLocalName().equals(offer.getSlotValue("order-id").toString())){
							message += "\n" + offer.getSlotValue("message").toString().replace("(", "").replace(")", "");
						}
					}

				} catch (Exception e) {
					System.out.println(e);
				}

				// System.out.println("Agent "+getLocalName()+": Proposal accepted");
				System.out.println("Agent "+ getLocalName() + ": is looking for " + accept.getContent());

				if (productInStock()) {
					System.out.println("Agent " + getLocalName() + ": Products getting ready");
					ACLMessage inform = accept.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					inform.setContent(message);
					return inform;
				}
				else {
					System.out.println("Agent "+getLocalName()+": Action execution failed");
					throw new FailureException("unexpected-error");
				}	
			}

			protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
				System.out.println("Agent " + getLocalName() + ": Proposal rejected");
			}
		} );
		
	}

	private boolean productInStock() {
		return (Math.random() > 0.2);
//                return true;
	}
        

	private class TellBehaviour extends Behaviour {

        boolean tellDone = false;
		
		
        public void action() {

            try {
                clips.eval("(clear)");
                
                clips.load("/Users/ramon/Documents/NetBeansProjects/HandsOn3/src/clps/load-templates.clp");
                clips.load("/Users/ramon/Documents/NetBeansProjects/HandsOn3/src/clps/load-facts.clp");
                clips.load("/Users/ramon/Documents/NetBeansProjects/HandsOn3/src/clps/load-rules.clp");

                clips.eval("(reset)");
            } catch (Exception e) {
                System.out.println(e);
            }

            tellDone =  true;
        } 
    
        public boolean done() {
            if (tellDone)
                return true;
            else
        return false;
        }
   
    }

//	private class AskBehaviour extends Behaviour {
//
//        boolean askDone = false;
//
//        public void action() {
//            try {
//                // System.out.println(clips.eval("(facts)"));
//                // clips.eval("(facts)");
//                // clips.eval("(rules)");
//                clips.run();
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//            
//            askDone = true;
//        } 
//    
//        public boolean done() {
//            if (askDone)
//                return true;
//            else
//                return false;
//        }
//
//        // public int onEnd(){
//        //     // myAgent.doDelete();
//        //     // return super.onEnd();
//        // }
//    }// END of inner class AskBehaviour
}

