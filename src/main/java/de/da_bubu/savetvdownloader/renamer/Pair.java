package de.da_bubu.savetvdownloader.renamer;


public class Pair<T,H> {

    private T head;
    private H tail;
    
    
    public Pair(T head, H tail) {
        this.head = head;
        this.tail = tail;
    }


    
    public T getHead() {
        return head;
    }


    
    public H getTail() {
        return tail;
    }
    
}
