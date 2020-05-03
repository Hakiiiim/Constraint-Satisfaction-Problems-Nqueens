package fr.emse.ai.csp.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import fr.emse.ai.util.Pair;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.5, Page 215.<br>
 * <br>
 * <p/>
 * <pre>
 * <code>
 * function BACKTRACKING-SEARCH(csp) returns a solution, or failure
 *    return BACKTRACK({ }, csp)
 *
 * function BACKTRACK(assignment, csp) returns a solution, or failure
 *    if assignment is complete then return assignment
 *    var = SELECT-UNASSIGNED-VARIABLE(csp)
 *    for each value in ORDER-DOMAIN-VALUES(var, assignment, csp) do
 *       if value is consistent with assignment then
 *          add {var = value} to assignment
 *          inferences = INFERENCE(csp, var, value)
 *          if inferences != failure then
 *             add inferences to assignment
 *             result = BACKTRACK(assignment, csp)
 *             if result != failure then
 *                return result
 *          remove {var = value} and inferences from assignment
 *    return failure
 * </code>
 * </pre>
 * <p/>
 * Figure 6.5 A simple backtracking algorithm for constraint satisfaction
 * problems. The algorithm is modeled on the recursive depth-first search of
 * Chapter 3. By varying the functions SELECT-UNASSIGNED-VARIABLE and
 * ORDER-DOMAIN-VALUES, we can implement the general-purpose heuristic discussed
 * in the text. The function INFERENCE can optionally be used to impose arc-,
 * path-, or k-consistency, as desired. If a value choice leads to failure
 * (noticed wither by INFERENCE or by BACKTRACK), then value assignments
 * (including those made by INFERENCE) are removed from the current assignment
 * and a new value is tried.
 *
 * @author Ruediger Lunde
 */
public class BacktrackingStrategy extends SolutionStrategy {

    public Assignment solve(CSP csp) {
        return recursiveBackTrackingSearch(csp, new Assignment());
    }

    /**
     * Template method, which can be configured by overriding the three
     * primitive operations below.
     */
    private Assignment recursiveBackTrackingSearch(CSP csp,
                                                   Assignment assignment) {
    	AC3Strategy AC3= new AC3Strategy();
        Assignment result = null;
        if (assignment.isComplete(csp.getVariables())) {
            result = assignment;
        } else {
            Variable var = selectUnassignedVariable(assignment, csp,"Degree");
            /**
             * If random String is written we choose the naive version of selecting variables
             * If "MVR" is chosen we use the MVR to choose the variable
             * If "Degree" is chosen we use the Degree heuristic to choose the variable
             */
            for (Object value : orderDomainValuesLCV(var, assignment, csp)) {
            	/**
            	 * We can use two functions here:
            	 * - orderDomainValues: Normal function
            	 * - orderDomainValuesLCV: function that uses LCV heuristic
            	 */
                assignment.setAssignment(var, value);
                AC3.reduceDomains(csp);
                fireStateChanged(assignment, csp);
                if (assignment.isConsistent(csp.getConstraints(var))) {
                    DomainRestoreInfo info = inference(var, assignment, csp);
                    if (!info.isEmpty())
                        fireStateChanged(csp);
                    if (!info.isEmptyDomainFound()) {
                        result = recursiveBackTrackingSearch(csp, assignment);
                        if (result != null)
                            break;
                    }
                    info.restoreDomains(csp);
                }
                assignment.removeAssignment(var);
            }
        }
        return result;
    }

    /**
     * Primitive operation, selecting a not yet assigned variable. This default
     * implementation just selects the first in the ordered list of variables
     * provided by the CSP.
     */
    protected Variable selectUnassignedVariable(Assignment assignment, CSP csp, String Choice ) {
    	if (Choice == "MVR" ) {
			return applyMRVHeuristic(assignment, csp).get(0);
    	}
    	if (Choice == "Degree" ) {
			ArrayList<Variable> vars = applyMRVHeuristic(assignment, csp);
			return applyDegreeHeuristic(vars, assignment, csp).get(0);
    	}
    	
    	
    	else {
			for (Variable var : csp.getVariables()) {
				if (!(assignment.hasAssignmentFor(var)))
					return var;
			}
		}
		return null;
	}
    
    
    
    protected ArrayList<Variable> applyMRVHeuristic(Assignment assignment, CSP csp) {
    	ArrayList<Variable> result = new ArrayList<Variable>();
    	int k=Integer.MAX_VALUE;
    	for (Variable var : csp.getVariables()) {
        	int nb =0;
            if (!(assignment.hasAssignmentFor(var))) {
            	
            	for (Constraint constraint : csp.getConstraints(var)) {
            		Variable neighbor= csp.getNeighbor(var,constraint);
            		if (neighbor != null) {
            			nb++;
            		}	
            	}
            	if (nb<=k) {
            		k=nb;
            		result.add(var);
            	} 
            }
        }
    	return result;
    }

    protected ArrayList<Variable> applyDegreeHeuristic(ArrayList<Variable> vars,Assignment assignment , CSP csp) {
    	ArrayList<Variable> result = new ArrayList<Variable>();
		int maxDegree = Integer.MIN_VALUE;
		for (Variable var : vars) {
			int degree = 0;
			for (Constraint constraint : csp.getConstraints(var)) {
				Variable neighbor = csp.getNeighbor(var, constraint);
				if (!assignment.hasAssignmentFor(neighbor)
						&& csp.getDomain(neighbor).size() > 1)
					++degree;
			}
			if (degree >= maxDegree) {
				if (degree > maxDegree) {
					result.clear();
					maxDegree = degree;
				}
				result.add(var);
			}
		}
		return result;
    }
    
    
    private ArrayList<Object> orderDomainValuesLCV(Variable var, Assignment assignment,
			CSP csp) {
		ArrayList<Pair<Object, Integer>> pairs = new ArrayList<Pair<Object, Integer>>();
		for (Object value : csp.getDomain(var)) {
			int num = countLostValues(var, value, csp);
			pairs.add(new Pair<Object, Integer>(value, num));
		}
		Collections.sort(pairs, new Comparator<Pair<Object, Integer>>() {
			@Override
			public int compare(Pair<Object, Integer> o1,
					Pair<Object, Integer> o2) {
				return o1.getSecond() < o2.getSecond() ? -1
						: o1.getSecond() > o2.getSecond() ? 1 : 0;
			}
		});
		ArrayList<Object> result = new ArrayList<Object>();
		for (Pair<Object, Integer> pair : pairs)
			result.add(pair.getFirst());
		return result;
	}    
    
    private int countLostValues(Variable var, Object value, CSP csp) {
		int result = 0;
		Assignment assignment = new Assignment();
		assignment.setAssignment(var, value);
		for (Constraint constraint : csp.getConstraints(var)) {
			Variable neighbor = csp.getNeighbor(var, constraint);
			for (Object nValue : csp.getDomain(neighbor)) {
				assignment.setAssignment(neighbor, nValue);
				if (!constraint.isSatisfiedWith(assignment)) {
					++result;
				}
			}
		}
		return result;
	}
    
    /**
     * Primitive operation, ordering the domain values of the specified
     * variable. This default implementation just takes the default order
     * provided by the CSP.
     */
    protected Iterable<?> orderDomainValues(Variable var,
                                            Assignment assignment, CSP csp) {
        return csp.getDomain(var);
    }

    /**
     * Primitive operation, which tries to prune out values from the CSP which
     * are not possible anymore when extending the given assignment to a
     * solution. This default implementation just leaves the original CSP as it
     * is.
     *
     * @return An object which provides informations about (1) whether changes
     * have been performed, (2) possibly inferred empty domains , and
     * (3) how to restore the domains.
     */
    protected DomainRestoreInfo inference(Variable var, Assignment assignment,
                                          CSP csp) {
        return new DomainRestoreInfo().compactify();
    }
}
