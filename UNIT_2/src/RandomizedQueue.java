import java.util.*;

public class RandomizedQueue<Item> implements Iterable<Item> {
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

    private class RQueueIterator implements Iterator<Item>{
        private Node<Item> current = first;

        @Override
        public boolean hasNext() {
            return current!=null;
        }

        @Override
        public Item next() {
            Item i = current.data;
            current = current.next;
            return i;
        }
    }

    public RandomizedQueue(){
        size = 0;
        first = null;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public int size(){
        return size;
    }

    public void enqueue(Item item){
        if(item == null)
            throw new IllegalArgumentException("In Enqueue");
        Node<Item> temp = new Node<>(item);
        if(size==0){
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

    public Item dequeue(){
        Item i = null;
        if(size == 0)
            throw new NoSuchElementException("In Dequeue");
        else if(size==1){
            i = first.data;
            first = null;
        }
        else {
            Node<Item> current = first;
            Node<Item> currentFront = first;
            int stopIndex = new Random().nextInt(size);
//            int stopIndex = 2;
            for(int j =0; j<stopIndex; j++){
                if(j==stopIndex-1)
                    currentFront = current;
                current = current.next;
            }

            i = current.data;
            currentFront.next = current.next;
        }
        size--;
        return i;
    }

    public Item sample(){
        Node<Item> current = first;
        int index = new Random().nextInt(size);
        for(int i = 0; i<index; i++){
            current = current.next;
        }
        return current.data;

    }



    @Override
    public Iterator<Item> iterator() {
        return new RQueueIterator();
    }

    public static void main(String[] args){
        RandomizedQueue<Character> rq = new RandomizedQueue<>();
        System.out.println(rq.size());
        rq.enqueue('h');
        System.out.println(rq.dequeue());
        System.out.println(rq.isEmpty());
        rq.enqueue('z');
        rq.enqueue('x');
        rq.enqueue('m');
        System.out.println(rq.dequeue());
        rq.enqueue('l');
        System.out.println(rq.sample());
        System.out.println(rq.sample());

        for (char a:rq){
            System.out.println(a);
        }
    }
}
