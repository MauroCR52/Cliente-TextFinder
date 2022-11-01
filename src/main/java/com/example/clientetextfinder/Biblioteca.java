package com.example.clientetextfinder;

import com.spire.doc.Document;

import java.util.Arrays;
import java.util.Objects;

public class Biblioteca {
    public Nodo_Biblioteca head;
    private int size;




    public Biblioteca() {
        this.head = null;
        this.size = 0;
    }

    public int getSize() {
        return this.size;
    }
    public static Biblioteca biblioteca = new Biblioteca();


    public void InsertarDocumento(Documento documento) {
        Nodo_Biblioteca nodo_biblioteca = new Nodo_Biblioteca(documento, null);
        nodo_biblioteca.next = this.head;
        this.head = nodo_biblioteca;
        this.size++;
    }

    public void borrarLista(){
        if (this.head == null){
            System.out.println("Lista vacia");
        }
        else {
            this.head = null;
            this.size = 0;
        }
    }

    public void ordenar_fecha() {
        if (size > 1) {
            boolean wasChanged;
            do {
                Nodo_Biblioteca current = this.head;
                Nodo_Biblioteca previous = null;
                Nodo_Biblioteca next = this.head.next;
                wasChanged = false;
                while (next != null) if (current.getData().getFecha() < next.getData().getFecha()) {
                    wasChanged = true;
                    if (previous != null) {
                        Nodo_Biblioteca sig = next.next;
                        previous.next = next;
                        next.next = current;
                        current.next = sig;
                    } else {
                        Nodo_Biblioteca sig = next.next;
                        head = next;
                        next.next = current;
                        current.next = sig;
                    }
                    previous = next;
                    next = current.next;
                } else {
                    previous = current;
                    current = next;
                    next = next.next;
                }
            } while (wasChanged);
        }
    }

    public void listaRadix(){
        int n = 1;
        int lista[] = {Biblioteca.biblioteca.head.getData().getTamano()};
        Nodo_Biblioteca actual = Biblioteca.biblioteca.head.next;
        while (actual != null){
            lista = addX(n, lista, actual.getData().getTamano());
            n++;
            actual = actual.next;
        }
        ordenar_tamano(lista, n);
        armarLista(lista, n);
    }

    public void ordenar_tamano(int arr[], int n){

        int m = getMax(arr, n);

        for (int exp = 1; m / exp > 0; exp *= 10)
            countSort(arr, n, exp);
    }

    static int getMax(int arr[], int n)
    {
        int mx = arr[0];
        for (int i = 1; i < n; i++)
            if (arr[i] > mx)
                mx = arr[i];
        return mx;
    }
    static void countSort(int arr[], int n, int exp)
    {
        int output[] = new int[n]; // output array
        int i;
        int count[] = new int[10];
        Arrays.fill(count, 0);

        // Store count of occurrences in count[]
        for (i = 0; i < n; i++)
            count[(arr[i] / exp) % 10]++;

        // Change count[i] so that count[i] now contains
        // actual position of this digit in output[]
        for (i = 1; i < 10; i++)
            count[i] += count[i - 1];

        // Build the output array
        for (i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }

        // Copy the output array to arr[], so that arr[] now
        // contains sorted numbers according to current
        // digit
        for (i = 0; i < n; i++)
            arr[i] = output[i];
    }

    public void armarLista(int arr[], int n) {
        int i = 0;
        while (i != n){
        }
    }

    static void verdocs(){
        Nodo_Biblioteca actual = Biblioteca.biblioteca.head;
        while (actual != null){
            System.out.println(actual.getData().getTamano());
            actual = actual.next;
        }
    }


    Nodo_Biblioteca paritionLast(Nodo_Biblioteca start, Nodo_Biblioteca end)
    {
        if (start == end || start == null || end == null)
            return start;

        Nodo_Biblioteca pivot_prev = start;
        Nodo_Biblioteca curr = start;
        Documento pivot = end.data;

        while (start != end) {
            if (start.getData().getNombre().compareTo(pivot.getNombre())< 0){
                // keep tracks of last modified item
                pivot_prev = curr;
                Documento temp = curr.data;
                curr.data = start.data;
                start.data = temp;
                curr = curr.next;
            }
            start = start.next;
        }

        // Swap the position of curr i.e.
        // next suitable index and pivot
        Documento temp = curr.data;
        curr.data = pivot;
        end.data = temp;

        // Return one previous to current
        // because current is now pointing to pivot
        return pivot_prev;
    }
    public void ordenar_nombre(Nodo_Biblioteca start, Nodo_Biblioteca end){
        if(start == null || start == end|| start == end.next )
            return;

        // split list and partition recurse
        Nodo_Biblioteca pivot_prev = paritionLast(start, end);
        ordenar_nombre(start, pivot_prev);

        // if pivot is picked and moved to the start,
        // that means start and pivot is same
        // so pick from next of pivot
        if (pivot_prev != null && pivot_prev == start)
            ordenar_nombre(pivot_prev.next, end);

            // if pivot is in between of the list,
            // start from next of pivot,
            // since we have pivot_prev, so we move two nodes
        else if (pivot_prev != null
                && pivot_prev.next != null)
            ordenar_nombre(pivot_prev.next.next, end);

    }
    public static int[] addX(int n, int arr[], int x)
    {
        int i;
        // create a new array of size n+1
        int newarr[] = new int[n + 1];

        // insert the elements from
        // the old array into the new array
        // insert all elements till n
        // then insert x at n+1
        for (i = 0; i < n; i++)
            newarr[i] = arr[i];
        newarr[n] = x;
        return newarr;
    }

    public void borrar(String nombre){
        Nodo_Biblioteca temp = Biblioteca.biblioteca.head;
        Nodo_Biblioteca prev = null;

        if (temp != null && temp.getData().getNombre().equals(nombre)) {
            head = temp.next; // Changed head
            return;
        }

        // Search for the key to be deleted, keep track of
        // the previous node as we need to change temp.next
        while (temp != null && !Objects.equals(temp.getData().getNombre(), nombre)) {
            prev = temp;
            temp = temp.next;
        }

        // If key was not present in linked list
        if (temp == null)
            return;

        // Unlink the node from linked list
        prev.next = temp.next;
    }

}
