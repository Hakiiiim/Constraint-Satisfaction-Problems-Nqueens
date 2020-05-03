package fr.emse.ai.csp.core;

/**
 * A variable is a distinguishable object with a name.
 *
 * @author Ruediger Lunde
 */
public class Variable {
    private String name;
    private Integer index;

    public Variable(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }
    
    public Integer getIndex() {
        return index;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() == getClass())
            return this.name.equals(((Variable) obj).name);
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
