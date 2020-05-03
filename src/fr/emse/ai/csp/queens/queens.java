package fr.emse.ai.csp.queens;

import java.util.List;

import fr.emse.ai.csp.core.CSP;
import fr.emse.ai.csp.core.Domain;
import fr.emse.ai.csp.core.NotSameRow;
import fr.emse.ai.csp.core.Variable;


public class queens extends CSP {
    
	public static final Variable Q1 = new Variable("Q1",1);
	public static final Variable Q2 = new Variable("Q2",2);
	public static final Variable Q3 = new Variable("Q3",3);
	public static final Variable Q4 = new Variable("Q4",4);
	public static final int A= 1;
	public static final int B= 2;
	public static final int C= 3;
	public static final int D= 4;
    public queens() {
        collectVariables();

        Domain positions = new Domain(new Object[] {A,B,C,D});

        for (Variable var : getVariables())
            setDomain(var, positions);
        
        addConstraint(new NotSameRow(Q1, Q2));
        addConstraint(new NotSameRow(Q1, Q3));
        addConstraint(new NotSameRow(Q1, Q4));
        addConstraint(new NotSameRow(Q2, Q3));
        addConstraint(new NotSameRow(Q2, Q4));
        addConstraint(new NotSameRow(Q3, Q4));
       
    }

    
    
    private void collectVariables() {
    	addVariable(Q1);
    	addVariable(Q2);
    	addVariable(Q3);
    	addVariable(Q4);
    }
}