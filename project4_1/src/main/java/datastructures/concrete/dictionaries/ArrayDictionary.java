package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    private int arrSize;
    private int keyIndex;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {   
        this.pairs = makeArrayOfPairs(5);
        this.size = 0;
        this.arrSize = 5;
        this.keyIndex = -1;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) { 
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }
    
    private int getIndexOf(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return this.keyIndex;
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return pairs[this.keyIndex].getValue();
    }

    @Override
    public void put(K key, V value) {
        if (this.arrSize == this.size) {
            Pair<K, V>[] tempArray = makeArrayOfPairs(this.arrSize * 2);
            this.arrSize *= 2;
            for (int i = 0; i < this.size(); i++) {
                tempArray[i] = this.pairs[i];
            }
        
            this.pairs = tempArray;
        }
        if (containsKey(key)) {
            this.pairs[getIndexOf(key)].setValue(value);
        } else {
            this.pairs[this.size] = new Pair<K, V>(key, value);        
            this.size += 1;
        }
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        V value = this.pairs[this.keyIndex].getValue();
        for (int i = this.keyIndex; i < this.size - 1; i++) {
            this.pairs[i] = this.pairs[i+1];
        }
        this.pairs[this.size - 1] = null;
        this.size -= 1;
        return value;
    }

    @Override
    public boolean containsKey(K key) {
        for (int index = 0; index < this.size; index++) {
            if (this.pairs[index].getKey() == key 
                    || (key != null && key.equals(this.pairs[index].getKey())) 
                    || (key == null && this.pairs[index].getKey().equals(key))) {
                this.keyIndex = index;
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
            return this.key;
        }
        
        public V getValue() {
            return this.value;
        }
        
        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new ArrayDictionaryIterator<>(this.pairs, this.size);
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        // You should not need to change this field, or add any new fields.
        private int currIndex;
        private int pairsSize;
        private Pair<K, V> currPair;
        private Pair<K, V>[] pairs;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            // You do not need to make any changes to this constructor.
            this.currIndex = 0;
            this.pairs = pairs;
            this.pairsSize = size;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return (currIndex < this.pairsSize) && !(pairs[currIndex] == null);
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public KVPair<K, V> next() {
            if (hasNext()) {
                currPair = this.pairs[currIndex];
                currIndex++;
            } else {
                throw new NoSuchElementException();
            }
            return new KVPair<K, V>(currPair.getKey(), currPair.getValue());
        }
    }
}
