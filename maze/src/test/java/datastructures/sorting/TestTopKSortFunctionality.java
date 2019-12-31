package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testTooLargeK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(10, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testZeroK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(0, list);
        assertEquals(0, top.size());
    }
    
    @Test(timeout=SECOND)
    public void testDuplicates() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 25; i++) {
            list.add(i % 5);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(4, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testSizeEqualsK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(-1, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testTopStrings() {
        IList<String> list = new DoubleLinkedList<>();
        List<String> arrList = new ArrayList<String>();
        list.add("asdfasdf");
        list.add("qwerqwer");
        list.add("tyuityui");
        list.add("zxcvzxcv");
        list.add("bnmbnm");
        list.add("ghjkghjk");
        list.add("rtyurtyu");
        list.add("hjklhjkl");
        arrList.add("asdfasdf");
        arrList.add("qwerqwer");
        arrList.add("tyuityui");
        arrList.add("zxcvzxcv");
        arrList.add("bnmbnm");
        arrList.add("ghjkghjk");
        arrList.add("rtyurtyu");
        arrList.add("hjklhjkl");
        Collections.sort(arrList);
        IList<String> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(arrList.get(arrList.size() - 5 + i), top.get(i));
        }
    }
    
}
