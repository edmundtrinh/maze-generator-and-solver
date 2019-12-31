package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    private IDictionary<T, Integer> refMap;
    private int id;
    private int arrSize;

    // rank = -rank - 1;
    
    public ArrayDisjointSet() {
        pointers = new int[10];
        id = 0;
        refMap = new ChainedHashDictionary<T, Integer>();
    }

    private void resizeArr() {
        int[] temp = new int[pointers.length * 2];
        for (int i = 0; i < pointers.length; i++) {
            temp[i] = pointers[i];
        }
        pointers = temp;
    }
    
    @Override
    public void makeSet(T item) {
        if (refMap.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        this.arrSize++;
        if (pointers.length < this.arrSize) {
            resizeArr();
        }
        refMap.put(item, this.id);
        pointers[this.id] = -1;
        this.id++;
    }

    @Override
    public int findSet(T item) {
        if (!refMap.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        IList<Integer> pathCompressList = new DoubleLinkedList<Integer>();
        int temp = refMap.get(item);
        while (pointers[temp] > -1) {
            pathCompressList.add(temp);
            temp = pointers[temp];
        }
        for (int i : pathCompressList) {
            pointers[i] = temp;
        }
        return temp;
    }

    @Override
    public void union(T item1, T item2) {
        if (!refMap.containsKey(item1) || !refMap.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int temp1 = findSet(item1);
        int temp2 = findSet(item2);
        if (temp1 == temp2) {
            throw new IllegalArgumentException();
        }
        if (temp1 < temp2) {
            pointers[temp2] = temp1;
            pointers[temp1]--;
        } else {
            pointers[temp1] = temp2;
            pointers[temp2]--;
        }
    }
}
