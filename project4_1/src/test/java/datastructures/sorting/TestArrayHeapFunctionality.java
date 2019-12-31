package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testLargeSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 400; i++) {
            heap.insert(i);
        }
        assertEquals(400, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testLargeReverseSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 400; i > 0; i--) {
            heap.insert(i);
        }
        assertEquals(400, heap.size());
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
    
    @Test(timeout=SECOND)
    public void testSortOrderString() {
        IPriorityQueue<String> heap = this.makeInstance();
        List<String> test = new ArrayList<>();
        test.add("what");
        test.add("the");
        test.add("flamingo");
        test.add("gum");
        test.add("reddit");
        test.add("junkmail");
        test.add("red");
        test.add("electric");
        test.add("music");
        test.add("mouse");
        Collections.sort(test);
        heap.insert("what");
        heap.insert("the");
        heap.insert("flamingo");
        heap.insert("gum");
        heap.insert("reddit");
        heap.insert("junkmail");
        heap.insert("red");
        heap.insert("electric");
        heap.insert("music");
        heap.insert("mouse");
        for (int i = 0; i < 10; i++) {
            assertEquals(test.get(i), heap.removeMin());
        }
        assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testSortOrderStringReverse() {
        IPriorityQueue<String> heap = this.makeInstance();
        List<String> test = new ArrayList<>();
        for (int i = 4; i >= 0; i--) {
            test.add("" + i);
            heap.insert("" + i);
        }
        Collections.sort(test);
        for (int i = 0; i < 5; i++) {
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
    
    @Test(expected = IllegalArgumentException.class)
    public void insertNull() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(null);
    }
    
    @Test(expected = EmptyContainerException.class)
    public void emptyContainerRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.removeMin();
    }
    
    @Test(expected = EmptyContainerException.class)
    public void emptyContainerPeek() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.peekMin();
    }
    
    
}
