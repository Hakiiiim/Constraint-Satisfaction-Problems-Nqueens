package fr.emse.ai.csp.australia;
import java.util.List;
import java.util.ArrayList;

import fr.emse.ai.csp.core.AC3Strategy;
import fr.emse.ai.csp.core.Assignment;
import fr.emse.ai.csp.core.BacktrackingStrategy;
import fr.emse.ai.csp.core.MinConflictsStrategy;
import fr.emse.ai.csp.core.CSP;
import fr.emse.ai.csp.core.CSPStateListener;
import fr.emse.ai.csp.core.Variable;

public class TestAustralia {
	public static void main(String[] args) {
		
		MapCSP map = new MapCSP();
		AC3Strategy arc = new AC3Strategy();
		arc.reduceDomains(map);
		BacktrackingStrategy bts = new BacktrackingStrategy();
		MinConflictsStrategy min = new MinConflictsStrategy(1000);
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
		Assignment sol = bts.solve(map);
		//Assignment sol = min.solve(map);
		double end = System.currentTimeMillis();
		System.out.println(sol);
		System.out.println("Time to solve = " + (end - start));
		System.out.println("Australia Problem");
	
	}
}
