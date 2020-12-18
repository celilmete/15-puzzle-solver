package com.company;

import java.util.ArrayList;
import java.util.Arrays;
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

        int[][] deneme = copy(GOAL_STATE);
        printPuzzle(deneme);
        move("dl", deneme);
        printPuzzle(deneme);






    }

    // Bütün algoritmalar için kullanacağımız search fonksiyonu
    public static void graphSearch() { // implement edilecek

    }

    // puzlle daki boş taşı hareket ettirmek için fonksiyon , yönleri u,d,r,l,ur,ul,dr,dl olarak verilecek
    public static void move(String direction, int[][] puzzle) {
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

        if (direction.equals("u") && blank_tile_x - 1 >= 0 ) { // boş taşı yukarı kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x-1][blank_tile_y];
            puzzle[blank_tile_x-1][blank_tile_y] = 0;
        }
        else if (direction.equals("d") && blank_tile_x + 1 < PUZZLE_SIZE) { // boş taşı aşağı kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x+1][blank_tile_y];
            puzzle[blank_tile_x+1][blank_tile_y] = 0;
        }
        else if (direction.equals("r") && blank_tile_y + 1 < PUZZLE_SIZE) { // boş taşı sağa kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x][blank_tile_y+1];
            puzzle[blank_tile_x][blank_tile_y+1] = 0;
        }
        else if (direction.equals("l") && blank_tile_y - 1 >= 0) { // boş taşı sola kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x][blank_tile_y-1];
            puzzle[blank_tile_x][blank_tile_y-1] = 0;
        }
        else if (direction.equals("ur") && blank_tile_x - 1 >= 0 && blank_tile_y + 1 < PUZZLE_SIZE) { // boş taşı sağ yukarı çapraz kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x-1][blank_tile_y+1];
            puzzle[blank_tile_x-1][blank_tile_y+1] = 0;
        }
        else if (direction.equals("ul") && blank_tile_x - 1 >= 0 && blank_tile_y - 1 >= 0) { // boş taşı sol yukarı çapraz kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x-1][blank_tile_y-1];
            puzzle[blank_tile_x-1][blank_tile_y-1] = 0;
        }
        else if (direction.equals("dr") && blank_tile_x + 1 < PUZZLE_SIZE && blank_tile_y + 1 < PUZZLE_SIZE) { // boş taşı sağ aşağı çapraz kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x+1][blank_tile_y+1];
            puzzle[blank_tile_x+1][blank_tile_y+1] = 0;
        }
        else if (direction.equals("dl") && blank_tile_x + 1 < PUZZLE_SIZE && blank_tile_y - 1 >= 0) { // boş taşı sol aşağı çapraz kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x+1][blank_tile_y-1];
            puzzle[blank_tile_x+1][blank_tile_y-1] = 0;
        }
        else System.err.println("can not move tile that direction"); // boş taş hareket edemiyorsa hata yazdır

    }

    // bütün algoritmalar için çalışaşacak expand fonksiyonu
    public static void expand() { // implement edilecek

    }

    // bu fonksiyon verilen state i yazdıracak
    public static void printState(State state) { // implement edilecek

    }

    // int array  alır puzzle ekrana bastırır
    public static void printPuzzle(int[][] puzzle) {
        for (int i = 0; i < PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                System.out.print(puzzle[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    // puzzle kopyalamak için fonksiyon
    public static int[][] copy(int[][] puzzle) {
        int[][] copy = new int[PUZZLE_SIZE][];
        for (int i = 0; i < PUZZLE_SIZE; i++) {
            copy[i] = puzzle[i].clone();
        }
        return copy;
    }

    // iki state puzzle karşılaştıracak
    public static boolean isEqual() { // implement edilecek

        return false;
    }

    // bu graph implement etmek için node
    public static class GraphNode  implements Comparable{ // implementasyonu bitmedi

        GraphNode parent; // her node kendi parent ını tutacak
        int cost; // node un cost u, algoritmaya göre belirlenecek

        public GraphNode() {

        }


        @Override
        public int compareTo(Object o) {
            return Integer.compare(this.cost, ((GraphNode) o).cost);
        }
    }

    // graph da her node da state leri ifade etmek için kullanılacak class
    private static class State {


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


