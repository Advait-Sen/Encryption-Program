package adsen.encryption.program.utils;


import java.util.*;

/**
 * This file is purely academic, because I felt that it was a cool idea to have it and that it was an interesting challenge.
 * It basically maps one set to another, with no duplicate keys on any side, allowing for quick 2-way lookups.
 */
public class DoubleWayMapping<A, B> implements Collection<Pair<A, B>> {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final float loadFactor;
    int[] itemAHashes;
    int[] itemBHashes;
    Pair<A, B>[][] items;
    private int size = 0;
    private int capacity = 4;


    public DoubleWayMapping() {
        loadFactor = DEFAULT_LOAD_FACTOR;
        itemAHashes = new int[capacity];
        itemBHashes = new int[capacity];
        items = new Pair[capacity][capacity];
    }

    public DoubleWayMapping(float loadFactor) {
        if (loadFactor > 1)
            throw new IllegalArgumentException("'loadFactor' parameter must not exceed 1, found '" + loadFactor + "'");
        this.loadFactor = loadFactor;
        itemAHashes = new int[capacity];
        itemBHashes = new int[capacity];
        items = new Pair[capacity][capacity];
    }

    /**
     * The number of value-value mappings in this Mapping
     *
     * @return the number of value-value entries in this map
     */
    public int size() {
        return size;
    }

    private void resize() {
        capacity = (int) (((float) size + 1) / (1 - loadFactor));
        itemAHashes = Arrays.copyOf(itemAHashes, capacity);
        itemBHashes = Arrays.copyOf(itemBHashes, capacity);
        items = (Pair<A, B>[][]) Utils.resize(items, capacity, capacity);
    }

    public boolean add(Pair<A, B> item) {//todo
        if (contains(item))
            return false;
        if (((float) size + 1) / ((float) capacity) > loadFactor) {
            resize();
        }
        size++;


        return true;
    }

    public boolean add(A a, B b) {
        return add((Pair<A, B>) Pair.of(a, b));
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Pair) {
            Pair oPair = (Pair) o;
            return items[oPair.getA().hashCode()][oPair.getB().hashCode()] == oPair;
        }
        return false;
    }

    @Override
    public Iterator<Pair<A, B>> iterator() {//todo
        return null;
    }

    @Override
    public Object[] toArray() {//todo
        return new Pair[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {//todo
        return null;
    }

    @Override
    public boolean remove(Object o) {//todo
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {//todo
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Pair<A, B>> c) {//todo
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {//todo
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {//todo
        return false;
    }

    @Override
    public void clear() {//todo

    }

    @Override
    public boolean equals(Object o) {//todo
        return false;
    }

    @Override
    public int hashCode() {//todo
        return 0;
    }

}