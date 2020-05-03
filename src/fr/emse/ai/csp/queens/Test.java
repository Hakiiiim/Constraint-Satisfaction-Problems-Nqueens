package fr.emse.ai.csp.queens;
import java.util.List;
import java.util.ArrayList;

import fr.emse.ai.csp.core.Assignment;
import fr.emse.ai.csp.core.BacktrackingStrategy;
import fr.emse.ai.csp.core.CSP;
import fr.emse.ai.csp.core.CSPStateListener;
import fr.emse.ai.csp.core.Variable;

public class Test {
	public static void main(String[] args) {
		
		queens Queens = new queens();
		BacktrackingStrategy bts = new BacktrackingStrategy();
		bts.addCSPStateListener(new CSPStateListener() {
		    @Override
		    public void stateChanged(Assignment assignment, CSP csp) {
		        System.out.println("Assignment evolved : " + assignment.toString());
		    }

		    @Override
		    public void stateChanged(CSP csp) {
		        System.out.println("CSP evolved : " + csp);
		    }
		});
		double start = System.currentTimeMillis();
		Assignment sol = bts.solve(Queens);
		double end = System.currentTimeMillis();
		System.out.println(sol);
		System.out.println("Time to solve = " + (end - start));
		System.out.println("4 Queens Problem");
		
		Object value1 = sol.getAssignment(queens.Q1);
        Object value2 = sol.getAssignment(queens.Q2);
        Object value3 = sol.getAssignment(queens.Q3);
        Object value4 = sol.getAssignment(queens.Q4);
        
        List<Integer> queenArray;
        queenArray = new ArrayList<Integer>();
        queenArray.add((Integer) value1);
        queenArray.add((Integer) value2);
        queenArray.add((Integer) value3);
        queenArray.add((Integer) value4);

		for(int col=0; col<4; col++) {
			for(int row=1; row<=4; row++) {
				if(queenArray.get(col) == row) {
					System.out.print("Q");
				}
				else {
					System.out.print("-");
				}
			}
			System.out.println();
		}
	
		

	}
}
