import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node<Item> first;

    private class Node<Item>{
        private Node<Item> next;
        private Item data;

        public Node(Item data){
            this.data = data;
            next = null;
        }
    }

    private class DequeIterator implements Iterator<Item>{

        private Node<Item> current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            Item i = current.data;

            current = current.next;
            return i;
        }

    }

    public Deque(){
        size = 0;
        first = null;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public void addFirst(Item item){
        if(item==null)
            throw new IllegalArgumentException("In addfirst");

        Node<Item> temp = new Node<>(item);

        if(size == 0){
            first = temp;
        }else{
            temp.next = first;
            first = temp;
        }
        size++;
    }

    public void addLast(Item item){
        if(item == null)
            throw new IllegalArgumentException("In AddLast");

        Node<Item> temp = new Node<>(item);

        if(size == 0){
            first = temp;
        }else{
            Node<Item> current = first;
            while(current.next!=null){
                current = current.next;
            }
            current.next = temp;
        }
        size++;
    }

    public Item removeFirst(){
        if(size == 0)
            throw new NoSuchElementException("In removeFirst");

        Item i = first.data;
        first = first.next;
        size--;
        return i;
    }

    public Item removeLast(){
        if(size==0){
            throw new NoSuchElementException("In removeLast");
        }

        Item i = null;
        if (size==1){
            i = first.data;
            first = null;
        }else{
            Node<Item> current = first;
            while(current.next.next!=null){
                current = current.next;
            }
            i = current.next.data;
            current.next = null;
        }
        size--;
        return i;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    public static void main(String args[]){
        Deque<Character> str = new Deque<>();

        System.out.println(str.size());
        str.addFirst('x');
        str.addFirst('z');
        System.out.println(str.size());
        str.addLast('m');
        System.out.println(str.size());
        System.out.println(str.removeLast());
        System.out.println(str.size());
        System.out.println(str.removeLast());
        System.out.println(str.isEmpty());

        str.addLast('y');
        str.addFirst('h');
        str.addLast('f');
        System.out.println(str.size());
        for(char a : str){
            System.out.println(a);
        }
    }

}





