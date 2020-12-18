package com.company;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Main {

    // kullanılacak algoritmalar
    private static final int UCS = 1; // uniform cost search
    private static final int H_1 = 2; // A* with heuristic h_1
    private static final int H_3 = 4; // A* with heuristic h_2
    private static final int H_2 = 3; // A* with heuristic h_3
    private static final int PUZZLE_SIZE = 4; // puzzle boyutu

    private static final int[][] GOAL_STATE = {
            {1,2,3,4},
            {12,13,14,5},
            {11,0,15,6},
            {10,9,8,7}
    };

    public static void main(String[] args) {

        //frontier i priority que olarak oluşturdum
        PriorityQueue<GraphNode> frontier = new PriorityQueue<GraphNode>(); // explored set i array list
        ArrayList<GraphNode> exploredSet = new ArrayList<GraphNode>(); //  olarak oluşturdum





    }

    // Bütün algoritmalar için kullanacağımız search fonksiyonu
    public void graphSearch() { // implement edilecek

    }

    // puzlle daki boş taşı hareket ettirmek için fonksiyon
    public int[][] move(String direction, int[][] puzzle) { //implement edilecek
        int blank_tile_x = -1;
        int blank_tile_y =  -1;

        for(int i=0; i<PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                if (puzzle[i][j] == 0) {
                    blank_tile_x = i;
                    blank_tile_y = j;
                }
            }
        }

        if (direction.equals("up") && (blank_tile_x - 1) >= 0 ) {

        }

        return null;
    }

    // bütün algoritmalar için çalışaşacak expand fonksiyonu
    public void expand() { // implement edilecek

    }

    // bu fonksiyon verilen state i yazdıracak
    public void printState(State state) { // implement edilecek

    }

    // iki state puzzle karşılaştıracak
    public boolean isEqual() { // implement edilecek

        return false;
    }

    // bu graph implement etmek için node
    public class GraphNode  implements Comparable{

        int cost; // node un cost u, algoritmaya göre belirlenecek

        public GraphNode() {

        }


        @Override
        public int compareTo(Object o) {
            if (((GraphNode)o).cost > this.cost)
        }
    }

    // graph da her node da state leri ifade etmek için kullanılacak class
    private class State {


        private int g_n; // uniform cost search için g(n) değeri, şimdiye kadar yapılan toplam cost
        private int h1_n; // A* heuristic, yanlış dizilmiş taşların sayısı
        private int h2_n; // A* heuristic, taşların kendi yerlerine city-block uzaklıkları
        private int h3_n; // A* heuristic, h2 den daha iyi sonuç verecek bizim bulacağımız heuristic değeri
        private int[][] state; // state için int array

        public int getG_n() {
            return g_n;
        }

        public int getH1_n() {
            return h1_n;
        }

        public int getH2_n() {
            return h2_n;
        }

        public int getH3_n() {
            return h3_n;
        }

        public int[][] getState() {
            return state;
        }

    }

}


