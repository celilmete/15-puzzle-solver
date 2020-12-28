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

        GraphNode start = puzzleGenerator(8);
        double startTime = System.currentTimeMillis();
//        expand(start, UCS);
//        System.out.println(isInExploredSet(start));


        start = graphSearch(start,UCS);
        double endTime = System.currentTimeMillis();
        System.out.println("Time taken: "+(endTime-startTime)+"ms");
        System.out.println(exploredSet.size());

//        if(start != null) {
//            while (start != null) {
//                printNode(start);
//                start = start.parent;
//            }
//        }
        System.out.println("Time taken: "+(endTime-startTime)+"ms");

    }

    // goal state den verilen derinlikte puzzle üretir ve Graphnode döndürür
    public static GraphNode puzzleGenerator(int depth){
        // 0 ile 7 arasında rastgele sayı üreticez sayılar hareketleri temsil edicek
        //7dençıkarına hamlenin tersini buluyoruz  0-u, 7-d, 1-l, 6-r, 2-ur, 5-dl, 3-ul, 4-dr
        // bu şekile verilen depth kadar random hareket ettirmiş olacagız boş taşı
        // ve istenilen depth-derinlikte bir puzzle elde etmiş olacagız
        int[][] puzzle = copy(GOAL_STATE);
        Random random =  new Random();
        int number, last;
        last = 9;
        int i = 0;
        while (i < depth){
            number = random.nextInt(8);
            if (last == 7 - number) // son hareketin tersini yapma
                continue;
            switch (number) {
                case 0 -> {
                    if (move("u", puzzle))
                        i++; // eğer taş o yöne hareket edebilmişse i yi arttır
                }
                case 1 -> {
                    if (move("l", puzzle))
                        i++; // eğer taş o yöne hareket edebilmişse i yi arttır
                }
                case 2 -> {
                    if (move("ur", puzzle))
                        i++; // eğer taş o yöne hareket edebilmişse i yi arttır
                }
                case 3 -> {
                    if (move("ul", puzzle))
                        i++; // eğer taş o yöne hareket edebilmişse i yi arttır
                }
                case 4 -> {
                    if (move("dr", puzzle))
                        i++; // eğer taş o yöne hareket edebilmişse i yi arttır
                }
                case 5 -> {
                    if (move("dl", puzzle))
                        i++; // eğer taş o yöne hareket edebilmişse i yi arttır
                }
                case 6 -> {
                    if (move("r", puzzle))
                        i++; // eğer taş o yöne hareket edebilmişse i yi arttır
                }
                case 7 -> {
                    if (move("d", puzzle))
                        i++; // eğer taş o yöne hareket edebilmişse i yi arttır
                }
            }
            last = number;
        }
        State state = new State(puzzle,0);
        return new GraphNode(null,state,state.g_n);
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

        // boş taşın yer bulundu artık mümkün olan hereketler için node oluşturuyoruz
        int[][] puzzle = copy(parentPuzzle);
        if (move("u", puzzle)) { // boş taşı yukarı kaydır
            createChildren(node, algorithm, puzzle, 1);
        }
        puzzle = copy(parentPuzzle);
        if (move("d", puzzle)) { // boş taşı aşağı kaydır
            createChildren(node, algorithm, puzzle, 1);
        }
        puzzle = copy(parentPuzzle);
        if (move("r", puzzle)) { // boş taşı sağa kaydır
            createChildren(node, algorithm, puzzle, 1);
        }
        puzzle = copy(parentPuzzle);
        if (move("l", puzzle)) { // boş taşı sola kaydır
            createChildren(node, algorithm, puzzle, 1);
        }
        puzzle = copy(parentPuzzle);
        if (move("ur", puzzle)) { // boş taşı sağ yukarı çapraz kaydır
            createChildren(node, algorithm, puzzle, 3);
        }
        puzzle = copy(parentPuzzle);
        if (move("ul", puzzle)) { // boş taşı sol yukarı çapraz kaydır
            createChildren(node, algorithm, puzzle, 3);
        }
        puzzle = copy(parentPuzzle);
        if (move("dr", puzzle)) { // boş taşı sağ aşağı çapraz kaydır
            createChildren(node, algorithm, puzzle, 3);
        }
        puzzle = copy(parentPuzzle);
        if (move("dl", puzzle)) { // boş taşı sol aşağı çapraz kaydır
            createChildren(node, algorithm, puzzle, 3);
        }

    }

    private static void createChildren(GraphNode node, int algorithm, int[][] puzzle, int realCost) {
        State state = new State(puzzle, node.state.g_n + realCost);
        GraphNode graphNode = switch (algorithm) {
            case UCS -> new GraphNode(node, state, state.g_n);
            case H_1 -> new GraphNode(node, state, state.getH1_n());
            case H_2 -> new GraphNode(node, state, state.getH2_n());
            case H_3 -> new GraphNode(node, state, state.getH3_n());
            default -> null;
        };
        if(!isInExploredSet(graphNode)) // frontierde yoksa bu state ekle
            frontier.add(graphNode);
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
        private int h3_n; // A* heuristic, h2 den daha iyi sonuç verecek bizim bulacağımız heuristic değeri
        private int[][] puzzle; // state için int array

        //default constructor
        public State() {

        }

        //algoritma ve değerle constructor
        public State(int[][] puzzle, int value) {
            this.puzzle = puzzle;
            g_n = value;
            }

        public int getH1_n() {
            int value = 0;
            for (int i = 0; i < PUZZLE_SIZE; i++) {
                for (int j = 0; j < PUZZLE_SIZE; j++) {
                    if(this.puzzle[i][j] != GOAL_STATE[i][j])
                        value += 1;
                }
            }
            return value;
        }

        //kendi h2_n değerini hesaplayan fonksiyon
        public int getH2_n() {
            int  temp, value = 0;
            for (int i = 0; i < PUZZLE_SIZE; i++) { // puzzle size büyük olmadığı sürece deep nested for loopta sorun yok
                for (int j = 0; j < PUZZLE_SIZE; j++) {
                    temp = this.puzzle[i][j];
                    if (temp == 0) continue; // boş taş için cost hesaplama
                    for (int k = 0; k < PUZZLE_SIZE; k++) {
                        for (int l = 0; l < PUZZLE_SIZE; l++) {
                            if(GOAL_STATE[k][l] == temp)
                                value += Math.abs(k-i) + Math.abs(l-j);
                        }
                    }
                }

            }
            return value;
        }

        public int getH3_n() {
            // h2_n asla gerçek cost tan büyük olamayacagı için şimdiye kadar ki cost la bi sonraki hamlenin h2_n toplamı asla o
            //hamlenin gerçek cost değerini geçemez, ve bu iki değerin toplamı gerçek cost değerine daha yakın olacagı için daha iyi sonuç verir,
            return this.g_n + getH2_n();
        }
    }
}


