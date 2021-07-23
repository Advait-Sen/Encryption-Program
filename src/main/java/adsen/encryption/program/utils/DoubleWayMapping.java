package adsen.encryption.program.utils;


import java.util.*;//todo replace this with individual imports later

/**
 * This file is purely academic, because I felt that it was a cool idea to have it and that it was an interesting challenge.
 * It basically maps one set to another, with no duplicate keys on any side, allowing for quick 2-way lookups.
 */
public class DoubleWayMapping<A, B> implements Collection<Pair<A, B>> {

    Map<A, B> AtoBMap = new HashMap<>();
    Map<B, A> BtoAMap = new HashMap<>();

    public DoubleWayMapping() {

    }

    public DoubleWayMapping(List<Pair<A, B>> pairs) {
        for (Pair<A, B> pair : pairs) {
            A a = pair.getA();
            B b = pair.getB();

            if (AtoBMap.containsKey(a))
                throw new IllegalArgumentException("Invalid duplicate key '" + a.toString() + "'");
            if (BtoAMap.containsKey(b))
                throw new IllegalArgumentException("Invalid duplicate key '" + b.toString() + "'");

            AtoBMap.put(a, b);
            BtoAMap.put(b, a);
        }
    }

    public A getA(B o) {
        return BtoAMap.get(o);
    }

    public B getB(A o) {
        return AtoBMap.get(o);
    }

    @Override
    public int size() {
        return AtoBMap.size();
    }

    @Override
    public boolean isEmpty() {
        return AtoBMap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        try {
            return AtoBMap.containsKey(o) || BtoAMap.containsKey(o) || AtoBMap.containsKey(((Pair<A, B>) o).getA());
        } catch (ClassCastException cce) {
            return false;
        }
    }

    @Override
    public Iterator<Pair<A, B>> iterator() {
        return new Iterator<>() {
            final Iterator<Map.Entry<A, B>> iteratorA = AtoBMap.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return iteratorA.hasNext();
            }

            @Override
            public Pair<A, B> next() {
                return new Pair<A, B>(iteratorA.next());
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] ret = new Object[size()];

        Iterator<A> iteratorA = AtoBMap.keySet().iterator();
        Iterator<B> iteratorB = BtoAMap.keySet().iterator();

        for (int i = 0; i < ret.length; i++) {
            ret[i] = Pair.of(iteratorA.next(), iteratorB.next());
        }

        return ret;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int size = size();
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            //noinspection unchecked
            return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
        //arraycopy

        Iterator<A> iteratorA = AtoBMap.keySet().iterator();
        Iterator<B> iteratorB = BtoAMap.keySet().iterator();

        for (int i = 0; i < size; i++) {
            //noinspection unchecked
            a[i] = (T) Pair.of(iteratorA.next(), iteratorB.next());
        }
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean add(Pair<A, B> abPair) {
        if (AtoBMap.containsKey(abPair.getA()) || BtoAMap.containsKey(abPair.getB()))
            return false;
        AtoBMap.put(abPair.getA(), abPair.getB());
        BtoAMap.put(abPair.getB(), abPair.getA());
        return true;
    }

    @Override
    public boolean remove(Object o) {
        try {
            boolean success = AtoBMap.remove(o) == null;
            if (success) {
                BtoAMap.remove(o);
                return true;
            } else if (o instanceof Pair) {
                //noinspection unchecked
                Pair<A, B> po = (Pair<A, B>) o;
                if (getA((B) po.getB()) == null)
                    return false;
                else
                    return remove(po.getA());
            } else
                return false;
        } catch (ClassCastException ignored) {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) break;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Pair<A, B>> c) {
        boolean changed = false;
        for (Pair<A, B> p : c) {
            if (this.add(p)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (this.remove(o)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.removeIf(t -> !c.contains(t));
    }

    @Override
    public void clear() {
        AtoBMap.clear();
        BtoAMap.clear();
    }
}