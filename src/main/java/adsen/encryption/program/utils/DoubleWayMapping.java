package adsen.encryption.program.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Provides a double mapping from one set to another. This means that neither set can have duplicate entries, which may
 * be an issue in some scenarios, but is perfect in my case as I'm only using it to map 64 chars to 64 integer values in
 * {@link Utils#CHARACTERS}
 */
public class DoubleWayMapping<A, B> {
    private Map<A, B> AToBMapping;
    private Map<B, A> BToAMapping;

    public DoubleWayMapping(){
        this.AToBMapping = new HashMap<>();
        this.BToAMapping = new HashMap<>();
    }

    public DoubleWayMapping(Set<A> setA, Set<B> setB){
        this();
        //could technically iterate over the loops below and then check if hasNext() returns true for any of the sets,
        //but that's too slow
        if(setA.size()!=setB.size())
            throw new IllegalArgumentException("Parameters 'setA' and 'setB' must be of the same length");

        //using iterators cos Sets have no inherent natural ordering
        Iterator<A> iteratorA = setA.iterator();
        Iterator<B> iteratorB = setB.iterator();

        //in theory a for loop with the set sizes should be enough, this is jic something is messed up.
        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            A nextA = iteratorA.next();
            B nextB = iteratorB.next();

            this.AToBMapping.put(nextA, nextB);
            this.BToAMapping.put(nextB, nextA);
        }

        if(iteratorA.hasNext() || iteratorB.hasNext())
            throw new IllegalStateException(String.format("How did we get here?\n" +
                    "(Parameter '%s' has extra values)", iteratorA.hasNext()? "setA" : "setB"));
    }

    /**
     * Gets the type A (i.e the first of the two class type parameters used when initiating this {@link DoubleWayMapping})
     * mapped to the given type B. To get a type B from a type A, see {@link DoubleWayMapping#getB(A key)}
     */
    public A getA(B key){
        return BToAMapping.get(key);
    }

    /**
     * Gets the type B (i.e the second of the two class type parameters used when initiating this {@link DoubleWayMapping})
     * mapped to the given type A. To get a type A from a type B, see {@link DoubleWayMapping#getA(B key)}
     */
    public B getB(A key){
        return AToBMapping.get(key);
    }

    /**
     * Maps a type {@code A} to a type {@code B}, if neither are already present in the mapping
     */
    public void put(A valueA, B valueB){
        if(AToBMapping.containsKey(valueA) || BToAMapping.containsKey(valueB))
            return;

        AToBMapping.put(valueA, valueB);
        BToAMapping.put(valueB, valueA);
    }

    /**
     * Returning the size of {@link DoubleWayMapping#AToBMapping}, as it's irrelevant, both {@link DoubleWayMapping#AToBMapping}
     * and {@link DoubleWayMapping#BToAMapping} have the same size.
     * @return the size of this {@link DoubleWayMapping}
     */
    public int size(){
        return AToBMapping.size();
    }

    @Override
    public DoubleWayMapping<A, B> clone() {
        return new DoubleWayMapping<>(this.AToBMapping.keySet(), this.BToAMapping.keySet());
    }

    @Override
    public String toString(){
        return AToBMapping.toString();
    }

    @Override
    public int hashCode(){
        return AToBMapping.hashCode() ^ BToAMapping.hashCode();
    }
}
