package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=10*SECOND)
    public void testPlaceholder() {
        assertTrue(true);
    }
    
    @Test(timeout=10*SECOND)
    public void testLargeSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 400000; i++) {
            heap.insert(i);
        }
        assertEquals(400000, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=10*SECOND)
    public void testLargeReverseSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 400000; i > 0; i--) {
            heap.insert(i);
        }
        assertEquals(400000, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testSortOrderInteger() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(5);
        heap.insert(9);
        heap.insert(2);
        heap.insert(1);
        heap.insert(8);
        heap.insert(7);
        heap.insert(3);
        heap.insert(6);
        heap.insert(4);
        heap.insert(0);
        for (int i = 0; i < 10; i++) {
            assertEquals(i, heap.removeMin());
        }
        assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=10*SECOND)
    public void testSortOrderString() {
        IPriorityQueue<String> heap = this.makeInstance();
        List<String> test = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            test.add("" + i);
            heap.insert("" + i);
        }
        Collections.sort(test);
        for (int i = 0; i < 500000; i++) {
            assertEquals(test.get(i), heap.removeMin());
        }
        assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=10*SECOND)
    public void testSortOrderStringReverse() {
        IPriorityQueue<String> heap = this.makeInstance();
        List<String> test = new ArrayList<>();
        for (int i = 499999; i >= 0; i--) {
            test.add("" + i);
            heap.insert("" + i);
        }
        Collections.sort(test);
        for (int i = 0; i < 500000; i++) {
            assertEquals(test.get(i), heap.removeMin());
        }
        assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testDuplicateStrings() {
        IPriorityQueue<String> heap = this.makeInstance();
        List<String> test = new ArrayList<>();
        test.add("what");
        test.add("the");
        test.add("what");
        test.add("the");
        test.add("what");
        test.add("the");
        test.add("what");
        test.add("the");
        test.add("what");
        test.add("the");
        Collections.sort(test);
        heap.insert("what");
        heap.insert("the");
        heap.insert("what");
        heap.insert("the");
        heap.insert("what");
        heap.insert("the");
        heap.insert("what");
        heap.insert("the");
        heap.insert("what");
        heap.insert("the");
        for (int i = 0; i < 10; i++) {
            assertEquals(test.get(i), heap.removeMin());
        }
        assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=500*SECOND)
    public void testExtremeUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 50000000; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5000, list);
        assertEquals(5000, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(49995000 + i, top.get(i));
        }
    }
    
}
