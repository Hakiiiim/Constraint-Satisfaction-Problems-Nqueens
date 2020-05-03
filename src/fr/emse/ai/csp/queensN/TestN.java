package fr.emse.ai.csp.queensN;
import java.util.List;
import java.util.ArrayList;

import fr.emse.ai.csp.core.Assignment;
import fr.emse.ai.csp.core.AC3Strategy;
import fr.emse.ai.csp.core.BacktrackingStrategy;
import fr.emse.ai.csp.core.MinConflictsStrategy;
import fr.emse.ai.csp.core.CSP;
import fr.emse.ai.csp.core.CSPStateListener;
import fr.emse.ai.csp.core.Variable;

public class TestN {
	public static void main(String[] args) {
		
		int n = 20;
		queensN Queens = new queensN(n);
		AC3Strategy arc = new AC3Strategy();
		arc.reduceDomains(Queens);
		BacktrackingStrategy bts = new BacktrackingStrategy();
		MinConflictsStrategy min = new MinConflictsStrategy(100000);
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
		min.addCSPStateListener(new CSPStateListener() {
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
		//Assignment sol = min.solve(Queens);
		double end = System.currentTimeMillis();
		System.out.println(sol);
		System.out.println("Time to solve = " + (end - start));
		System.out.println(n+" Queens Problem");
		
		List<Integer> queenArray;
		queenArray = new ArrayList<Integer>();
		
		for (int i=0; i<n; i++) {
			Object value = sol.getAssignment(Queens.Variableslist.get(i));
			queenArray.add((Integer)value);
		}
        
        
        
        
		for(int col=0; col<n; col++) {
			for(int row=1; row<=n; row++) {
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
