package adsen.encryption.program.utils;

import java.util.Arrays;

public class MaxHeap<T extends Comparable<T>> {
    private T[] items;
    private int maxSize;
    private int size;

    private int getLeftChildIndex(int parentIndex){
        return 2 * parentIndex + 1;
    }
    private int getRightChildIndex(int parentIndex){
        return 2 * parentIndex + 2;
    }
    private int getParentIndex(int childIndex){
        return (childIndex-1) / 2;
    }

    private boolean hasLeftChild(int index){
        return getLeftChildIndex(index) < size;
    }
    private boolean hasRightChild(int index){
        return getRightChildIndex(index) < size;
    }
    private boolean hasParent(int index){
        return index > 0;
    }

    private T leftChild(int index){
        return items[getLeftChildIndex(index)];
    }
    private T rightChild(int index){
        return items[getRightChildIndex(index)];
    }
    private T parent(int index){
        return items[getParentIndex(index)];
    }

    private void swap(int indexOne, int indexTwo){
        T temp = items[indexOne];
        items[indexOne] = items[indexTwo];
        items[indexTwo] = temp;
    }

    private void ensureExtraCapacity(){
        if(size==maxSize){
            items = Arrays.copyOf(items, maxSize * 2);
            maxSize *= 2;
        }
    }

    public T peek(){
        if(size==0) throw new IllegalStateException();
        return items[0];
    }

    public T pop(){
        if(size==0) throw new IllegalStateException();
        T item = items[0];
        items[0] = items[size-1];
        size--;
        heapifyDown();
        return item;
    }

    public void add(T item){
        ensureExtraCapacity();
        items[size-1] = item;
        size++;
        heapifyUp();
    }

    private void heapifyUp(){
        int index = size-1;
        while(hasParent(index) && parent(index).compareTo(items[index])<0){
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    private void heapifyDown(){
        int index = 0;
        while(hasLeftChild(index)){
            int smallerChildIndex = getLeftChildIndex(index);
            if(hasRightChild(index) && rightChild(index).compareTo(leftChild(index))>0){
                smallerChildIndex = getRightChildIndex(index);
            }

            if(items[index].compareTo(items[smallerChildIndex])>0)
                break;

            swap(index, smallerChildIndex);
            index = smallerChildIndex;
        }
    }
}
