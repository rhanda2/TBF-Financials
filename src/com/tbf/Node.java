package com.tbf;

public class Node<T> {

    private T item;
    private Node<T> next;

    public Node(T item) {
        this.item = item;
        this.next = null;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public boolean hasNext() {
        return this.next != null;
    }
}
