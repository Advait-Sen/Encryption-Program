package adsen.encryption.program.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MinCharTreeHeap implements Collection<CharTree> {
    private CharTree[] items;
    private int maxSize;
    private int size;

    public MinCharTreeHeap() {
        items = new CharTree[10];
        maxSize = 10;
        size = 0;
    }

    public MinCharTreeHeap(CharTree[] collection) {
        size = 0;
        maxSize = collection.length * 2 + 1;
        items = collection;
        this.addAll(Arrays.asList(collection));
    }

    private int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    private boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < size;
    }

    private boolean hasRightChild(int index) {
        return getRightChildIndex(index) < size;
    }

    private boolean hasParent(int index) {
        return index > 0;
    }

    private CharTree leftChild(int index) {
        return items[getLeftChildIndex(index)];
    }

    private CharTree rightChild(int index) {
        return items[getRightChildIndex(index)];
    }

    private CharTree parent(int index) {
        return items[getParentIndex(index)];
    }

    private void swap(int indexOne, int indexTwo) {
        CharTree temp = items[indexOne];
        items[indexOne] = items[indexTwo];
        items[indexTwo] = temp;
    }

    private void ensureExtraCapacity() {
        if (size == maxSize) {
            items = Arrays.copyOf(items, maxSize * 2);
            maxSize *= 2;
        }
    }

    public CharTree peek() {
        if (size == 0) throw new IllegalStateException();
        return items[0];
    }

    public CharTree pop() {
        if (size == 0) throw new IllegalStateException();
        CharTree item = items[0];
        if (size == 1) {
            clear();
            return item;
        }
        items[0] = items[size - 1];
        size--;
        heapifyUp();
        return item;
    }

    public int size() {
        return size;
    }

    public CharTree get(int index) {
        return items[index];
    }

    public boolean add(CharTree item) {
        ensureExtraCapacity();
        if (size == 0) {
            items[0] = item;
            size = 1;
            return false;
        }

        items[size] = item;
        size++;
        heapifyDown();
        return false;
    }

    public void clear() {
        this.size = 0;
        this.maxSize = 10;
        items = new CharTree[maxSize];
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        if (size == 0) return 1;
        return Arrays.hashCode(items);
    }

    private void heapifyDown() {
        int index = size - 1;
        while (hasParent(index) && parent(index).compareTo(items[index]) > 0) {
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    private void heapifyUp() {
        int index = 0;
        while (hasLeftChild(index)) {
            int smallerChildIndex = getLeftChildIndex(index);
            if (hasRightChild(index) && rightChild(index).compareTo(leftChild(index)) < 0) {
                smallerChildIndex = getRightChildIndex(index);
            }

            if (items[index].compareTo(items[smallerChildIndex]) < 0)
                break;

            swap(index, smallerChildIndex);
            index = smallerChildIndex;
        }
    }

    @Override
    public String toString(){
        return Arrays.toString(Arrays.copyOf(items, size));
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof CharTree)) return false;
        for (CharTree item : items) {
            if (item == o) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends CharTree> collection) {
        collection.forEach(this::add);
        return false;
    }

    @Override
    public Iterator<CharTree> iterator() {
        return new Iterator<CharTree>() {
            int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < size;
            }

            @Override
            public CharTree next() {
                return items[pos++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(items, size);
    }

    @Override
    public <T> T[] toArray(T[] array) {
        //noinspection unchecked
        return (T[]) Arrays.copyOf(items, size, array.getClass());
    }

    @Override
    public boolean remove(Object o) {
        throw new IllegalArgumentException();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return collection.stream().allMatch(this::contains);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new IllegalArgumentException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new IllegalArgumentException();
    }
}
