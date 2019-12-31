package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private int dictSize = 0; // Dictionary Array Size
    private int numKeys = 0;  // What else could this possibly be?
    private double lambda;    // numKeys/dictSize
    private boolean resizeDictFlag; // Flag that allows resizing begin when true
    
    
    public ChainedHashDictionary() {
        this.dictSize = 11;
        chains = this.makeArrayOfChains(dictSize);
        resizeDictFlag = true;
    }
    
    private int newArrayOfChainsdictSize() {
        this.dictSize *= 2;
        return this.dictSize;
    }
    
    /**
     * This method will return a new, empty array of the given dictSize
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }
    
    private int getHash(K key) {
        int hash;
        if (key == null) {
            hash = 0;
        } else {
            hash = key.hashCode();
        }
        return hash;
    }
    
    private int getIndex(int hash) {
        int index = hash % this.dictSize;
        if (index < 0) {
            index *= -1;
        }
        return index;
    }
    
    @Override
    public V get(K key) {
        int hash = getHash(key);
        int index = getIndex(hash);
        if (chains[index] != null && chains[index].containsKey(key)) {
            return chains[index].get(key);
        } else {
            throw new NoSuchKeyException();
        }
    }

    private void resizeDictCHD() {
        resizeDictFlag = false;
        this.numKeys = 0;
        IDictionary<K, V>[] temp = chains;
        int olddictSize = this.dictSize;
        chains = this.makeArrayOfChains(newArrayOfChainsdictSize());
        
        for (int i = 0; i < olddictSize; i++) {
            if (temp[i] != null) {
                Iterator<KVPair<K, V>> iter = temp[i].iterator();
                KVPair<K, V> tempPair;
                for (int j = 0; j < temp[i].size(); j++) {
                    if (iter.hasNext()) {
                        tempPair = iter.next();
                        this.put(tempPair.getKey(), tempPair.getValue());
                    }
                }
            }
        }
        resizeDictFlag = true;
        this.lambda = (double) numKeys / (double) this.dictSize;
    }
    
    @Override
    public void put(K key, V value) {
        numKeys++;
        int hash = getHash(key);
        int index = getIndex(hash);
        boolean added = false;
        if (chains[index] == null) {
            chains[index] = new ArrayDictionary<K, V>();
        }
        if (chains[index].containsKey(key)) {
            numKeys--;
        }
        this.lambda = (double) numKeys / (double) this.dictSize;
        while (this.lambda > 1 && resizeDictFlag) {
            resizeDictCHD();
            this.put(key, value);
            added = true;
        }
        if (!added) {
            chains[index].put(key, value);
        }
    }

    @Override
    public V remove(K key) {
        int hash = getHash(key);
        int index = getIndex(hash);
        if (chains[index] != null && chains[index].containsKey(key)) {
            numKeys--;
            return chains[index].remove(key);
        } else {
            throw new NoSuchKeyException();
        }
    }

    @Override
    public boolean containsKey(K key) {
        int hash = getHash(key);
        int index = getIndex(hash);
        if (chains[index] != null) {
            return chains[index].containsKey(key);
        }
        return false;
    }

    @Override
    public int size() {
        return numKeys;
    }

    public int dictSize() {
        return dictSize;
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
<<<<<<< HEAD
     * 2. Think about what exactly your *invariants* are. Once you've
     *    decided, write them down in a comment somewhere to help you
     *    remember.
     *
     * 3. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
=======
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant*
     *    is something that must *always* be true once the constructor is
     *    done setting up the class AND must *always* be true both before and
     *    after you call any method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
>>>>>>> e9154deef026c8fbe3e89b0544561f18888df89f
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int iteratorIndex; // which bucket iterator is the iterator using
        private Iterator<KVPair<K, V>> bucketIter;
        private int nextCount = 0;
        private int nextIterCount = 0;
        private long count = 0;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.iteratorIndex = -1;
            if (hasNextIterator()) {
                nextIterator();
            }
        }
        
        // retrieves next bucket iterator
        private void nextIterator() {
            nextIterCount++;
            if (iteratorIndex == chains.length - 1) {
                bucketIter = null;
            } else {
                iteratorIndex++;
                if ((chains[iteratorIndex] != null) && (chains[iteratorIndex].size() != 0)) {
                    this.bucketIter = chains[iteratorIndex].iterator();
                } else {
                    nextIterator();
                }
            }
        }
        
        private boolean hasNextIterator() {
            boolean temp = false;
            for (int i = iteratorIndex + 1; i < chains.length; i++) {
                if ((chains[i] != null) && (chains[i].size() != 0)) {
                    temp = true;
                    i = chains.length;
                }
                count++;
            }
            return temp;
        }
        
        @Override
        public boolean hasNext() {
            if (bucketIter == null) {
                return false;
            } else if (bucketIter.hasNext()) {
                return true;
            } else {
                return hasNextIterator();
            }
        }

        @Override
        public KVPair<K, V> next() {
            nextCount++;
            if (hasNext() && bucketIter.hasNext()) {
                return bucketIter.next();
            } else if (hasNext() && !bucketIter.hasNext()) {
                nextIterator();
                return bucketIter.next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
