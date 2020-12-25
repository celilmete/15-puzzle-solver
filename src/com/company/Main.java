package com.company;

import java.util.*;

public class Main {

    // kullanılacak algoritmalar
    private static final int UCS = 0; // uniform cost search
    private static final int H_1 = 1; // A* with heuristic h_1
    private static final int H_2 = 2; // A* with heuristic h_2
    private static final int H_3 = 3; // A* with heuristic h_3
    private static final int PUZZLE_SIZE = 4; // puzzle boyutu

    private static final int[][] GOAL_STATE = {
            {1, 2, 3, 4},
            {12, 13, 14, 5},
            {11, 0, 15, 6},
            {10, 9, 8, 7}
    };

    //frontier i priority que olarak oluşturdum
    static PriorityQueue<GraphNode> frontier = new PriorityQueue<GraphNode>(Comparator.comparing(GraphNode::getCost)); // explored set i array list
    static ArrayList<GraphNode> exploredSet = new ArrayList<GraphNode>(); //  olarak oluşturdum

    public static void main(String[] args) {
        //
        int[][] puzzle = {
                {1,2,3,4},
                {12,13,0,5},
                {11,15,14,6},
                {10,9,8,7}};
        State state = new State(puzzle,0,UCS);
        GraphNode start = new GraphNode(null,state,state.g_n);


        start = graphSearch(start,UCS);
        if(start != null) {
            while (start.parent != null) {
                printNode(start);
                start = start.parent;
            }
        }

    }

    public static int[][] puzzleGenerator(int depth){// implement edilecek

        return null;
    }

    // Bütün algoritmalar için kullanacağımız search fonksiyonu
    public static GraphNode graphSearch(GraphNode startNode, int algorithm) { // implement edilecek
        if(isEqual(startNode.state.puzzle,GOAL_STATE))
            return startNode;
        expand(startNode,algorithm);
        GraphNode node;
        while (frontier.peek() != null) {
            node = frontier.poll();
            if(isEqual(GOAL_STATE,node.state.puzzle))
                return node;
            else {
                System.out.println(".");
                expand(node,algorithm);
            }
        }
        return null;
    }

    // puzlle daki boş taşı hareket ettirmek için fonksiyon , yönleri u,d,r,l,ur,ul,dr,dl olarak verilecek
    public static boolean move(String direction, int[][] puzzle) {
        int blank_tile_x = -1;
        int blank_tile_y = -1;

        for (int i = 0; i < PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                if (puzzle[i][j] == 0) {
                    blank_tile_x = i;
                    blank_tile_y = j;
                }
            }
        }
        // bu fonksiyon eğer verilen hareketi yapabiliyorsa yapıp true yoksa false döndürüyor
        if (direction.equals("u") && blank_tile_x - 1 >= 0) { // boş taşı yukarı kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x - 1][blank_tile_y];
            puzzle[blank_tile_x - 1][blank_tile_y] = 0;
        } else if (direction.equals("d") && blank_tile_x + 1 < PUZZLE_SIZE) { // boş taşı aşağı kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x + 1][blank_tile_y];
            puzzle[blank_tile_x + 1][blank_tile_y] = 0;
        } else if (direction.equals("r") && blank_tile_y + 1 < PUZZLE_SIZE) { // boş taşı sağa kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x][blank_tile_y + 1];
            puzzle[blank_tile_x][blank_tile_y + 1] = 0;
        } else if (direction.equals("l") && blank_tile_y - 1 >= 0) { // boş taşı sola kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x][blank_tile_y - 1];
            puzzle[blank_tile_x][blank_tile_y - 1] = 0;
        } else if (direction.equals("ur") && blank_tile_x - 1 >= 0 && blank_tile_y + 1 < PUZZLE_SIZE) { // boş taşı sağ yukarı çapraz kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x - 1][blank_tile_y + 1];
            puzzle[blank_tile_x - 1][blank_tile_y + 1] = 0;
        } else if (direction.equals("ul") && blank_tile_x - 1 >= 0 && blank_tile_y - 1 >= 0) { // boş taşı sol yukarı çapraz kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x - 1][blank_tile_y - 1];
            puzzle[blank_tile_x - 1][blank_tile_y - 1] = 0;
        } else if (direction.equals("dr") && blank_tile_x + 1 < PUZZLE_SIZE && blank_tile_y + 1 < PUZZLE_SIZE) { // boş taşı sağ aşağı çapraz kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x + 1][blank_tile_y + 1];
            puzzle[blank_tile_x + 1][blank_tile_y + 1] = 0;
        } else if (direction.equals("dl") && blank_tile_x + 1 < PUZZLE_SIZE && blank_tile_y - 1 >= 0) { // boş taşı sol aşağı çapraz kaydır
            puzzle[blank_tile_x][blank_tile_y] = puzzle[blank_tile_x + 1][blank_tile_y - 1];
            puzzle[blank_tile_x + 1][blank_tile_y - 1] = 0;
        } else
            return false;//System.err.println("can not move tile that direction"); // boş taş hareket edemiyorsa hata yazdır
        return true;
    }

    // bütün algoritmalar için çalışaşacak expand fonksiyonu
    public static void expand(GraphNode node, int algorithm) { // implement devam ediyor
        exploredSet.add(node);
        int[][] parentPuzzle = node.state.puzzle;
        int blank_tile_x = -1;
        int blank_tile_y = -1;

        for (int i = 0; i < PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                if (parentPuzzle[i][j] == 0) {
                    blank_tile_x = i;
                    blank_tile_y = j;
                }
            }
        }
        switch (algorithm) {// her bir hareket için yeni bir node oluşturup frontiere ekliyoruz
            case UCS -> {
                int[][] puzzle = copy(parentPuzzle);
                if (move("u", puzzle)) { // boş taşı yukarı kaydır
                    State state = new State(puzzle, node.state.g_n + 1, algorithm);
                    GraphNode graphNode = new GraphNode(node, state, state.g_n);
                    if(!isInExploredSet(graphNode))
                        frontier.add(graphNode);
                }
                puzzle = copy(parentPuzzle);
                if (move("d", puzzle)) { // boş taşı aşağı kaydır
                    State state = new State(puzzle, node.state.g_n + 1, algorithm);
                    GraphNode graphNode = new GraphNode(node, state, state.g_n);
                    if(!isInExploredSet(graphNode))
                        frontier.add(graphNode);
                }
                puzzle = copy(parentPuzzle);
                if (move("r", puzzle)) { // boş taşı sağa kaydır
                    State state = new State(puzzle, node.state.g_n + 1, algorithm);
                    GraphNode graphNode = new GraphNode(node, state, state.g_n);
                    if(!isInExploredSet(graphNode))
                        frontier.add(graphNode);
                }
                puzzle = copy(parentPuzzle);
                if (move("l", puzzle)) { // boş taşı sola kaydır
                    State state = new State(puzzle, node.state.g_n + 1, algorithm);
                    GraphNode graphNode = new GraphNode(node, state, state.g_n);
                    if(!isInExploredSet(graphNode))
                        frontier.add(graphNode);
                }
                puzzle = copy(parentPuzzle);
                if (move("ur", puzzle)) { // boş taşı sağ yukarı çapraz kaydır
                    State state = new State(puzzle, node.state.g_n + 3, algorithm);
                    GraphNode graphNode = new GraphNode(node, state, state.g_n);
                    if(!isInExploredSet(graphNode))
                        frontier.add(graphNode);
                }
                puzzle = copy(parentPuzzle);
                if (move("ul", puzzle)) { // boş taşı sol yukarı çapraz kaydır
                    State state = new State(puzzle, node.state.g_n + 3, algorithm);
                    GraphNode graphNode = new GraphNode(node, state, state.g_n);
                    if(!isInExploredSet(graphNode))
                        frontier.add(graphNode);
                }
                puzzle = copy(parentPuzzle);
                if (move("dr", puzzle)) { // boş taşı sağ aşağı çapraz kaydır
                    State state = new State(puzzle, node.state.g_n + 3, algorithm);
                    GraphNode graphNode = new GraphNode(node, state, state.g_n);
                    if(!isInExploredSet(graphNode))
                        frontier.add(graphNode);
                }
                puzzle = copy(parentPuzzle);
                if (move("dl", puzzle)) { // boş taşı sol aşağı çapraz kaydır
                    State state = new State(puzzle, node.state.g_n + 3, algorithm);
                    GraphNode graphNode = new GraphNode(node, state, state.g_n);
                    if(!isInExploredSet(graphNode))
                        frontier.add(graphNode);
                }
            }
            case H_1 -> System.out.println("not yet");
            case H_2 -> System.out.println("not yet");
            case H_3 -> System.out.println("not yet");
        }

    }

    // bu fonksiyon verilen state i yazdıracak
    public static void printNode(GraphNode node) { // implement edilecek
        System.out.println("Cost is: " + node.cost);
        printPuzzle(node.state.puzzle);
        System.out.println("");
    }

    // Bir node explored sette var mı yok mu kontrol eden fonksiyon yok is false dönüyor
    public static boolean isInExploredSet(GraphNode node) {

        for (GraphNode item:exploredSet) {
            if(isEqual(item,node))
                return true;
        }

        return false;
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

    // iki state eşit mi değil mi karşılaştıracak
    public static boolean isEqual(GraphNode node1, GraphNode node2) {
        return isEqual(node1.state.puzzle,node2.state.puzzle);
    }

    // iki puzzle eşit mi onu kontrol eder
    public static boolean isEqual(int[][] first, int[][] second) {
        for (int i = 0; i < PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                if (first[i][j] != second[i][j])
                    return false;
            }
        }
        return true;
    }

    // bu graph implement etmek için node
    public static class GraphNode implements Comparable { // implementasyonu bitmedi

        GraphNode parent; // her node kendi parent ını tutacak
        int cost; // node un cost u, algoritmaya göre belirlenecek
        private State state;

        public GraphNode() {
            state = new State();
            cost = 0;
        }

        public GraphNode(GraphNode parent, State state, int cost) {
            this.parent = parent;
            this.state = state;
            this.cost = cost;
        }

        public int getCost() {
            return cost;
        }

        @Override
        public int compareTo(Object o) {
            return Integer.compare(this.cost, ((GraphNode) o).cost);
        }
    }

    // graph da her node da state leri ifade etmek için kullanılacak class
    private static class State { //implementasyonu devam ediyor


        private int g_n; // uniform cost search için g(n) değeri, şimdiye kadar yapılan toplam cost
        private int h1_n; // A* heuristic, yanlış dizilmiş taşların sayısı
        private int h2_n; // A* heuristic, taşların kendi yerlerine city-block uzaklıkları
        private int h3_n; // A* heuristic, h2 den daha iyi sonuç verecek bizim bulacağımız heuristic değeri
        private int[][] puzzle; // state için int array

        //default constructor
        public State() {

        }

        //algoritma ve değerle constructor
        public State(int[][] puzzle, int value, int algorithm) {
            this.puzzle = puzzle;
            switch (algorithm) {
                case UCS:
                    g_n = value;
                    break;
                case H_1:
                    h1_n = value;
                    break;
                case H_2:
                    h2_n = value;
                    break;
            }
        }

    }

}


