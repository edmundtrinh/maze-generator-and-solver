package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        this.size += 1;
        if (this.front == null & this.back == null) {
            Node<T> newNode = new Node<T>(item);
            this.front = newNode;
            this.back = newNode;
        } else {
            if (this.back.prev == null) {
                Node<T> newNode = new Node<T>(this.front, item, null);
                this.front.next = newNode;
                this.back = this.front.next;
            } else {
                Node<T> newNode = new Node<T>(this.back, item, null);
                this.back.next = newNode;
                this.back = this.back.next;
            }
        }
    }

    @Override
    public T remove() {
        T temp;
        if (this.back != null) {
            temp = this.back.data;
        } else {
            temp = null;
        }
        if (this.size > 1) {
            this.back = this.back.prev;
            this.back.next = null;
            this.size -= 1;
        } else if (this.size == 1) {
            this.front = null;
            this.back = null;
            this.size = 0;
        } else {
            throw new EmptyContainerException();
        }
        return temp;
    }

    @Override
    public T get(int index) {
        if (index < 0 | index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> temp = navigateTo(index);
        return temp.data;
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 | index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> temp = navigateBehind(index);
        if (index != 0 & index != this.size() - 1) {
            temp = temp.next;
        }
        Node<T> newNode = new Node<>(temp.prev, item, temp.next);
        if (temp.prev != null) {
            temp.prev.next = newNode;
        } else {
            this.front = newNode;
        }
        if (temp.next != null) {
            temp.next.prev = newNode;
        } else {
            this.back = newNode;
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 | index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> temp = navigateTo(index);
        Node<T> newNode = new Node<>(item);
        if (temp == null) {
            this.size -= 1;
            add(item);
        } else if (index == 0) {
            this.front = newNode;
            newNode.next = temp;
            temp.prev = newNode;
        } else if (index == this.size()) {
            newNode.prev = temp;
            temp.next = newNode;
            this.back = newNode;
        } else if (index == this.size() - 1) {
            newNode.prev = temp.prev;
            newNode.next = temp;
            temp.prev = newNode;
            this.back = temp;
        } else {
            newNode.prev = temp.prev;
            newNode.next = temp;
            temp.prev.next = newNode;
            temp.prev = newNode;
        }
        this.size += 1;
    }

    
    private Node<T> navigateTo(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> tempNode = null;
        int tempIndex;
        if (index == 0) {
            tempNode = this.front;
        } else if ((index == this.size() - 1) || (index == this.size)) {
            tempNode = this.back;
        } else if (index < (this.size()/2)) {
            tempNode = this.front;
            tempIndex = 0;
            while (tempIndex < index) {
                tempIndex += 1;
                tempNode = tempNode.next;
            }
        } else {
            tempNode = this.back;
            tempIndex = this.size() - 1;
            while (tempIndex > index) {
                tempIndex -= 1;
                tempNode = tempNode.prev;
            }
        }
        return tempNode;
    }
    
    private Node<T> navigateBehind(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> tempNode = null;
        int tempIndex;
        if (index == 0) {
            tempNode = this.front;
        } else if ((index == this.size() - 1) || (index == this.size)) {
            tempNode = this.back;
        } else if (index < (this.size()/2)) {
            tempNode = this.front;
            tempIndex = 0;
            while (tempIndex < index - 1) {
                tempIndex += 1;
                tempNode = tempNode.next;
            }
        } else {
            tempNode = this.back;
            tempIndex = this.size() - 1;
            while (tempIndex >= index) {
                tempIndex -= 1;
                tempNode = tempNode.prev;
            }
        }
        return tempNode;
    }
    
    @Override
    public T delete(int index) {
        // No need to alter
        if (index < 0 | index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        
        Node<T> temp = navigateTo(index);
        this.size -= 1;
        if (temp.prev != null) {
            temp.prev.next = temp.next;
        } else {
            this.front = temp.next;
        }
        if (temp.next != null) {
            temp.next.prev = temp.prev;
        } else {
            this.back = temp.prev;
        }
        return temp.data;
    }

    @Override
    public int indexOf(T item) {
        Node<T> temp = this.front;
        //int tempIndex = 0;
        //boolean found = false;
        int index = -1;
        for (int tempIndex = 0; tempIndex < this.size(); tempIndex++) {
            if (tempIndex > 0) {
                temp = temp.next;
            }
            if (temp.data == item || temp.data.equals(item)) {
                //found = true;
                index = tempIndex;
                tempIndex = this.size - 1;
            }
        }
        
        return index;
        /*
        while ((tempIndex < this.size() - 1) && !found) {
            if (temp.data == item) {
                found = true;
            } else {
                temp = temp.next;
                tempIndex += 1;
            }
        }
        if (temp.data != item) {
            return -1;
        } else {
            return tempIndex;
        }*/
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> temp = this.front;
        boolean found = false;
        for (int tempIndex = 0; tempIndex < this.size(); tempIndex++) {
            if (tempIndex > 0) {
                temp = temp.next;
            }
            if (temp.data == other || temp.data.equals(other)) {
                found = true;
                tempIndex = this.size() - 1;
            }
        }
        return found;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    public static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return !(current == null);
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            T temp = null;
            if (hasNext()) {
                temp = current.data;
                current = current.next;
            } else {
                throw new NoSuchElementException();
            }
            return temp;
        }
    }
}
