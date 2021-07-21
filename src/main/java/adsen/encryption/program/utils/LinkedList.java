package adsen.encryption.program.utils;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.ListIterator;

public class LinkedList<T> implements List<T> {

    /**
     * A temporary variable used for iterating over the list
     */
    ListItem<T> temp;
    private int size;
    private ListItem<T> firstItem;
    private ListItem<T> lastItem;

    public LinkedList() {
        size = 0;
    }

    /**
     * Gets the {@link ListItem} object at the given index
     *
     * @param index Index to get ListItem from
     * @return ListItem at that index
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   ({@code index < 0 || index >= size()})
     */
    private ListItem<T> listItemAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        if (index < size - index) {//get from front if closer to front
            temp = firstItem;
            for (int i = 0; i < index; i++) {
                temp = temp.nextItem;
            }
        } else { //get from back if not
            temp = lastItem;
            for (int i = 0; i < index; i++) {
                temp = temp.previousItem;
            }
        }
        return temp;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Properly calculates the size the slow way, in case I messed up in updating it, essentially.
     */
    public int checkSizeProper() {
        int properSize = 0;
        if (firstItem != null) {
            properSize++;
            temp = firstItem;
            while (temp != lastItem) {
                temp = temp.nextItem;
                properSize++;
            }
        }
        size = properSize;
        return properSize;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            ListItem<T> currentListItem = firstItem;

            @Override
            public boolean hasNext() {
                return currentListItem != lastItem;
            }

            @Override
            public T next() {
                T nextObject = currentListItem.object;
                currentListItem = currentListItem.nextItem;
                return nextObject;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        temp = firstItem;
        for (int i = 0; i < array.length; i++) {
            array[i] = temp.object;
            temp = temp.nextItem;
        }

        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            //noinspection unchecked
            return (T1[]) Arrays.copyOf(toArray(), size, a.getClass());
        //arraycopy
        for (int i = 0; i < size; i++) {
            //noinspection unchecked
            a[i] = (T1) get(i);
        }
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean add(T t) {
        size++;
        if (size == 1) {
            firstItem = new ListItem<>(t, null, null);
            lastItem = firstItem;
            return true;
        }
        lastItem = new ListItem<>(t, lastItem, null);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        boolean foundItem = false;
        temp = firstItem;
        while (temp != lastItem || foundItem) {
            foundItem = temp.object.equals(o);
            temp = temp.nextItem;
        }
        //noinspection ConstantConditions
        if (foundItem) {
            size--;
            temp.removeFromList();
        }
        //noinspection ConstantConditions
        return foundItem;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean hasAll = true;
        for (Object o : c) {
            hasAll = this.contains(o);
            if (!hasAll)
                break;
        }
        return hasAll;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        for (T t : c) {
            changed |= this.add(t);
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        int i = index;
        int previousSize = size;
        for (T t : c) {
            this.add(i, t);
            i++;
        }
        return previousSize != size;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= this.remove(o);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.removeIf(t -> !c.contains(t));
    }

    @Override
    public void clear() {
        firstItem = null;
        lastItem = null;
        size = 0;
    }

    @Override
    public T get(int index) {
        return listItemAt(index).object;
    }

    @Override
    public T set(int index, T element) {
        temp = listItemAt(index);

        T previousItem = temp.object;

        temp.object = element;

        return previousItem;
    }

    @Override
    public void add(int index, T element) {
        if (index == size) {//just adds the element on end in this case
            this.add(element);
            return;
        }
        size++;
        temp = listItemAt(index);
        new ListItem<>(element, temp.previousItem, temp);
    }

    @Override
    public T remove(int index) {
        size--;
        temp = listItemAt(index);
        temp.removeFromList();

        return temp.object;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int indexOf(Object o) {
        int index = 0;
        temp = firstItem;
        while (temp.object != o || temp != lastItem) {
            index++;
            temp = temp.nextItem;
        }
        return temp.object == o ? index : -1;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int lastIndexOf(Object o) {//same search as indexOf, but from the back instead
        int index = size;
        temp = lastItem;
        while (temp.object != o || temp != firstItem) {
            index--;
            temp = temp.previousItem;
        }
        return temp.object == o ? index : -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIterator<>() {
            ListItem<T> currentListItem = listItemAt(index);
            int currentIndex = index;
            ListItem<T> itemToReturnTo = null;

            @Override
            public boolean hasNext() {
                return currentListItem != lastItem;
            }

            @Override
            public T next() {
                currentIndex++;
                T nextObject = currentListItem.object;
                itemToReturnTo = currentListItem;
                currentListItem = currentListItem.nextItem;
                return nextObject;
            }

            @Override
            public boolean hasPrevious() {
                return currentListItem != firstItem;
            }

            @Override
            public T previous() {
                currentIndex--;
                T previousObject = currentListItem.object;
                itemToReturnTo = currentListItem;
                currentListItem = currentListItem.previousItem;
                return previousObject;
            }

            @Override
            public int nextIndex() {
                return currentIndex + 1;
            }

            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            @Override
            public void remove() {
                if (itemToReturnTo != null) {
                    currentListItem.removeFromList();
                    currentListItem = itemToReturnTo;
                }
                itemToReturnTo = null;
            }

            @Override
            public void set(T t) {
                currentListItem.object = t;
            }

            @Override
            public void add(T t) {
                currentListItem = new ListItem<>(t, currentListItem.previousItem, currentListItem.nextItem);
                itemToReturnTo = null;
            }
        };
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        ListItem<T> newFirstItem = listItemAt(fromIndex);
        ListItem<T> newLastItem = listItemAt(fromIndex);

        LinkedList<T> newList = new LinkedList<>();
        temp = newFirstItem;
        while(temp!=newLastItem){
            newList.add(temp.object);
            temp = temp.nextItem;
        }
        newList.add(lastItem.object);
        return newList;
    }

    /**
     * Swaps items at two indices
     */
    public void swap(int indexA, int indexB) {
        ListItem<T> itemA = listItemAt(indexA);
        ListItem<T> itemB = listItemAt(indexB);

        set(indexA, itemB.object);
        set(indexB, itemA.object);
    }

    /**
     * A helper class which essentially takes any object type and wraps around it a previous and next ListItem for the
     * links in the LinkedList object.
     *
     * @param <P> The original object type (not using T cos it's already being used in LinkedList class)
     */
    private static class ListItem<P> {
        private P object;
        private ListItem<P> previousItem;
        private ListItem<P> nextItem;

        private ListItem(P item, ListItem<P> previous, ListItem<P> next) {
            object = item;
            this.previousItem = previous;
            this.nextItem = next;
            if (previous != null) previous.setNext(this);
            if (next != null) next.setPrevious(this);
        }

        private void setPrevious(ListItem<P> newPrevious) {
            if (previousItem != newPrevious) {
                previousItem = newPrevious;
                if (newPrevious != null) newPrevious.nextItem = this;
            }
        }

        private void setNext(ListItem<P> newNext) {
            if (nextItem != newNext) {
                nextItem = newNext;
                if (newNext != null) newNext.previousItem = this;
            }
        }

        /**
         * Removes the item from whatever list it's currently in.
         */
        private void removeFromList() {
            if (previousItem == null) {
                nextItem.setPrevious(null);
            } else {
                previousItem.setNext(nextItem);
            }
        }
    }
}
