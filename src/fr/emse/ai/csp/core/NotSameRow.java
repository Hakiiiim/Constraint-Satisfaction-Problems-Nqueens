package fr.emse.ai.csp.core;

import java.util.ArrayList;
import java.util.List;


public class NotSameRow implements Constraint{
	private Variable var1;
    private Variable var2;
    private List<Variable> scope;

    public NotSameRow(Variable var1, Variable var2) {
        this.var1 = var1;
        this.var2 = var2;
        scope = new ArrayList<Variable>(2);
        scope.add(var1);
        scope.add(var2);
    }

    @Override
    public List<Variable> getScope() {
        return scope;
    }

    @Override
    public boolean isSatisfiedWith(Assignment assignment) {
        Object value1 = assignment.getAssignment(var1);
        Object value2 = assignment.getAssignment(var2);
        
        if ((value1 != null) & (value2 != null)) {
	        Integer y1 = (Integer) value1;
	        Integer y2 = (Integer) value2;
	        
	        int x1 = var1.getIndex();
	        int x2 = var2.getIndex();
	        
	        if ((y1 == y2) | (x1 == x2)) {
	        	return false;
	        } else if (((y1-y2)==(x1-x2))|((y1-y2)==(-x1+x2))) {
	        	return false;
	        } else {
	        	return true;
	        }
        } else {
        	return true;
        }
    }
}
