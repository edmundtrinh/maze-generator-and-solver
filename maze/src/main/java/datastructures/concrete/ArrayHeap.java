package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.
    private int numElements;
    private int arrSize;

    public ArrayHeap() {
        this.numElements = 0;
        this.arrSize = 25;
        heap = makeArrayOfT(this.arrSize);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    private void resizeHeap() {
        this.arrSize *= 2;
        T[] temp = makeArrayOfT(this.arrSize);
        for (int i = 0; i < this.size(); i++) {
            temp[i] = heap[i];
        }
        heap = temp;
    }
    
    private int parent(int i) {
        return (i-1)/NUM_CHILDREN;
    }
    
    private int childHelper(int nodeIndex, int childNumber) {
        return NUM_CHILDREN*nodeIndex + childNumber + 1;
    }
    
    private int child(int nodeIndex, int childNumber) { // childNumber is 0 based indexing
        int temp = childHelper(nodeIndex, childNumber);
        while (temp > (this.arrSize - 1)) {
            this.resizeHeap();
        }
        return temp;
    }
    
    private void swapIndices(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    private void percolateDown(int i) {

        int smallestChildIndex;
        
        int tempIndex = child(i, 0);
        if (heap[tempIndex] != null) {
            smallestChildIndex = tempIndex;
        } else {
            return;
        }
        
        for (int j = 0; j < NUM_CHILDREN; j++) {
            tempIndex = child(i, j);
            if ((heap[tempIndex] != null)
                    && (heap[smallestChildIndex].compareTo(heap[tempIndex]) > 0)) {
                smallestChildIndex = tempIndex;   
            }
        }
        
        if (heap[i].compareTo(heap[smallestChildIndex]) > 0) {
            this.swapIndices(i, smallestChildIndex);
            this.percolateDown(smallestChildIndex);
        }
    }
    
    private void percolateUp(int i) {
        if ((i > 0) && (heap[i].compareTo(heap[this.parent(i)]) < 0)) {
            swapIndices(i, this.parent(i));
            this.percolateUp(this.parent(i));
        }
    }
    
    @Override
    public T removeMin() {
        if (this.size() == 0) {
            throw new EmptyContainerException();
        }
        T temp = heap[0];
        heap[0] = heap[this.size() - 1];
        heap[this.size() - 1] = null;
        this.numElements--;
        if (this.size() != 0) {
            this.percolateDown(0);
        }
        return temp;
    }

    @Override
    public T peekMin() {
        if (this.size() == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        heap[this.size()] = item;
        this.numElements++;
        this.percolateUp(this.size() - 1);
        if (this.size() > (this.arrSize/2)) {
            this.resizeHeap();
        }
    }

    @Override
    public int size() {
        return this.numElements;
    }

    @Override
    public void remove(T item) {
        throw new UnsupportedOperationException();
    }
}
