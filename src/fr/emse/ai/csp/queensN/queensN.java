package fr.emse.ai.csp.queensN;

import java.util.ArrayList;
import java.util.List;

import fr.emse.ai.csp.core.CSP;
import fr.emse.ai.csp.core.Domain;
import fr.emse.ai.csp.core.NotSameRow;
import fr.emse.ai.csp.core.Variable;


public class queensN extends CSP {
	
	ArrayList<Variable> Variableslist = new ArrayList<Variable>();
	ArrayList<Integer> Domainlist = new ArrayList<Integer>();
	
    public queensN(int n) {
        
    	for(int i=1; i<=n; i++) {
    		Variableslist.add(new Variable("Q"+i,i));
    		System.out.println(Variableslist.get(i-1));
    		System.out.println(Variableslist.get(i-1).getIndex());
    		Domainlist.add(i);
		}
    	
    	collectVariables(n);
    	
    	System.out.println(getVariables());

        Domain positions = new Domain(Domainlist);

        for (Variable var : getVariables())
            setDomain(var, positions);
        
        System.out.println(getDomain(Variableslist.get(0)));
        
        for(int col=0; col<n; col++) {
			for(int row=0; row<n; row++) {
				if (row < col) {
					addConstraint(new NotSameRow(Variableslist.get(col),Variableslist.get(row)));
					System.out.println("Contrainte entre :");
					System.out.println(Variableslist.get(col));
					System.out.println("et");
					System.out.println(Variableslist.get(row));
					System.out.println("\n");
				}
			}
		}
        
        
        
    }

    
    
    private void collectVariables(int n) {
    	for(int i=0; i<n; i++) {
    		addVariable(Variableslist.get(i));
		}

    }
}