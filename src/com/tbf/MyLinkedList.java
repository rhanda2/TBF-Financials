package com.tbf;

import java.util.*;

/**
 * This class models a sorted list ADT. It uses a linked list implementation and is sorted according
 * to the composed comparator.
 * @param <T>
 */
public class MyLinkedList<T> implements Iterable<T> {

    private Node<T> head;
    private int size;
    private Comparator comparator;

    public MyLinkedList(){
        this.head = null;
        this.size = 0;
        this.comparator = null;
    }

    public MyLinkedList(Comparator comparator) {
        this.head = null;
        this.size = 0;
        this.comparator = comparator;
    }

    /**
     * Return the node at a particular index.
     * @param index
     * @return
     */
    private Node<T> getNodeATIndex(int index) {
        if(index < 0 || index >= this.size) {
            throw new IllegalArgumentException("Index is out of bounds.");
        }

        Node<T> curr = this.head;

        for(int i=0; i<index; i++) {
            curr = curr.getNext();
        }

        return curr;
    }

    /**
     * Return the item at a particular index
     * @param index
     * @return
     */
    public T getElementAtIndex(int index){
        return getNodeATIndex(index).getItem();
    }

    /**
     * Inserts element at the head
     * @param item
     */
    private void insertAtHead(T item) {
        Node<T> newHead = new Node<>(item);
        newHead.setNext(this.head);
        this.head = newHead;
        this.size++;
    }

    /**
     * inserts element at the tail
     * @param item
     */
    private void insertAtTail(T item) {
        if(this.isEmpty()) {
            insertAtHead(item);
            return;
        }
        Node<T> curr = this.head;
        while(curr.getNext() != null) {
            curr = curr.getNext();
        }
        Node<T> newTail = new Node<>(item);
        curr.setNext(newTail);
        this.size++;
    }

    /**
     * Inserts at the particular index. Private because breaks functionality.
     * @param item
     * @param index
     */
    private void insertAtIndex(T item, int index) {
        if(index < 0 || index > this.size) {
            throw new IllegalArgumentException("index is out of bounds.");
        }
        if(index == 0) {
            this.insertAtHead(item);
            return;
        } else if(index == this.size) {
            insertAtTail(item);
            return;
        } else {
            Node<T> newNode = new Node(item);
            Node<T> prevNode = this.getNodeATIndex(index-1);
            Node<T> currNode = prevNode.getNext();
            newNode.setNext(currNode);
            prevNode.setNext(newNode);
            this.size++;
            return;
        }
    }

    /**
     * Adds while maintaining the sorting comparator.
     * @param item
     */
    public void add(T item) {

        if(this.comparator == null) {
            this.insertAtTail(item);
            return;
        }

        if(this.size == 0) {
            this.insertAtHead(item);
            return;
        }
        Node<T> curr = this.head;
        int currIndex = 0;

        while(( this.comparator.compare(curr.getItem(), item) < 0) && currIndex < this.size) {
            if(curr.hasNext()){
                curr = curr.getNext();
                currIndex++;
            } else {
                insertAtTail(item);
                return;
            }
        }
        if(currIndex < size){
            insertAtIndex(item, currIndex);
        } else {
            insertAtTail(item);
        }
        return;
    }

    /**
     * Adds a whole MyLinkedList to the new MyLinkedList
     * @param list
     */
    public void addAllFromAnotherMyLinkedList(MyLinkedList<T> list) {
        for(Object item : list) {
            this.add((T) item);
            this.size++;
        }
    }

    /**
     * Removes element at a particular index.
     * @param index
     * @return
     */
    public T removeFromIndex(int index) {
        if(index == 0) {
            T item = this.head.getItem();
        }

        Node<T> prev = getNodeATIndex(index - 1);
        Node<T> curr = prev.getNext();

        prev.setNext(curr.getNext());
        this.size--;
        return curr.getItem();
    }

    /**
     * Removes from head.
     * @return
     */
    private T removeFromHead() {
        if(this.isEmpty()) {
            throw new IllegalStateException("The list is empty cannot remove from it.");
        }
        T item = this.head.getItem();
        this.head = this.head.getNext();
        this.size--;
        return item;
    }

    /**
     * Returns the size.
     * @return
     */
    public int getSize(){
        return this.size;
    }

    /**
     * Returns true if the list is empty.
     * @return
     */
    public boolean isEmpty() {
        return (this.size == 0);
    }

    public String toString() {
        if(this.isEmpty()) {
            return "[empty]";
        }
        StringBuilder sb = new StringBuilder();
        Node<T> curr = this.head;
        while(curr != null) {
            sb.append(curr.getItem() + ", ");
            curr = curr.getNext();
        }
        return sb.toString();
    }

    @Override
    public Iterator iterator() {
        return new Iterator<T>() {
            Node<T> current = head;

            public boolean hasNext() {
                if (current == null)
                    return false;
                else
                    return true;
            }

            public T next() {
                T item = current.getItem();
                current = current.getNext();
                return item;
            }

            public void remove() {
                throw new UnsupportedOperationException("not implemented");
            }
        };
    }

}