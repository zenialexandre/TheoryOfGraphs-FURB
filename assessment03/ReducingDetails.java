/**
 * @author Alexandre Zeni
 */

package assessment03;

import java.util.ArrayList;
import java.util.Scanner;

public class ReducingDetails {

    private int nVertices;
    private int mEdges;
    private int uVertice;
    private int vVertice;
    private int cCost;
    private int minimumCost;
    private int uniqueId = 0;
    private int uniqueIdGenerated = 300;
    private final ArrayList<ArrayList<Integer>> graphInfos = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();
    private final ArrayList<DisjointSet> sets = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public static class Edge {
        public int origin;
        public int destiny;
        public int cost;

        public Edge(int origin, int destiny, int cost) {
            this.origin = origin;
            this.destiny = destiny;
            this.cost = cost;
        }
    }

    public static class DisjointSet {
        public int uniqueId;
        public ArrayList<Integer> vertices = new ArrayList<>();
        public DisjointSet(int uniqueId, int vertice) {
            this.uniqueId = uniqueId;
            this.vertices.add(vertice);
        }
    }

    public void build() {
        this.requestInput();
        this.createEdgesList();
        this.execKruskalAlgo();
    }

    private void requestInput() {
        this.verticesAndEdges();
        this.graphInfo();
        scanner.close();
    }

    private void execKruskalAlgo() {
        for (int i = 1; i <= nVertices; i++) {
            this.makeSet(i);
        }
        this.sortEdgesByCost();
        this.iterateEdges();
        System.out.println("Custo minimo: " + this.minimumCost);
    }

    private void createEdgesList() {
        for (ArrayList<Integer> list : graphInfos) {
            Edge genericEdge = new Edge(list.get(0), list.get(1), list.get(2));
            edges.add(genericEdge);
        }
    }

    private void makeSet(int vertice) {
        DisjointSet set = new DisjointSet(uniqueId++, vertice);
        sets.add(set);
    }

    private int findSet(int vertice) {
        for (DisjointSet set : sets) {
            for (int innerVertice : set.vertices) {
                if (vertice == innerVertice) {
                    return set.uniqueId;
                }
            }
        }
        return -1;
    }

    private void union(int origin, int destiny) {
        uniqueIdGenerated++;

        for (int i = 0; i < sets.size(); i++) {
            if (sets.get(i).uniqueId == this.findSet(origin)) {
               DisjointSet newSet = new DisjointSet(uniqueId + uniqueIdGenerated, origin);

                for (int j = 0; j < sets.size(); j++) {
                    if (sets.get(j).uniqueId == this.findSet(destiny)) {
                        newSet.vertices.addAll(sets.get(j).vertices);
                        sets.remove(sets.get(j));
                    }
                }
                sets.add(newSet);
                sets.remove(sets.get(i));
            }
        }
    }

    private void sortEdgesByCost() {
        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < edges.size(); j++) {
                if (edges.get(i).cost < edges.get(j).cost) {
                    Edge temp = edges.get(i);
                    edges.set(i, edges.get(j));
                    edges.set(j, temp);
                }
            }
        }
    }

    private void iterateEdges() {
        for (Edge edge : edges) {
            if (this.findSet(edge.origin) != this.findSet(edge.destiny)) {
                this.minimumCost += edge.cost;
                this.union(edge.origin, edge.destiny);
            }
        }
    }

    private void verticesAndEdges() {
        System.out.println("Insira a seguir o numero de vertices do grafo: ");
        nVertices = scanner.nextInt();
        this.validateVerticesInput();

        System.out.println("Insira a seguir o numero de arestas do grafo: ");
        mEdges = scanner.nextInt();
        this.validateEdgesInput();
    }

    private void validateVerticesInput() {
        if (!(nVertices >= 1 && nVertices <= 500)) {
            while (nVertices < 1 || nVertices > 500) {
                System.out.println("Insira um numero de vertices valido (1 <= N <= 500): ");
                nVertices = scanner.nextInt();
            }
        }
    }

    private void validateEdgesInput() {
        if (!(mEdges >= 1 && mEdges <= 124750)) {
            while (mEdges < 1 || mEdges > 124750) {
                System.out.println("Insira um numero de arestas valido (1 <= M <= 124750): ");
                mEdges = scanner.nextInt();
            }
        }
    }

    private void graphInfo() {
        for (int i = 0; i < mEdges; i++) {
            ArrayList<Integer> internalList = new ArrayList<>();
            this.clearValues();
            this.origin(internalList);
            this.destiny(internalList);
            this.cost(internalList);
            graphInfos.add(internalList);
        }
    }

    private void origin(ArrayList<Integer> internalList) {
        System.out.println("Insira o vertice de origem: ");
        uVertice = scanner.nextInt();
        this.validateOriginInput(internalList);
    }

    private void validateOriginInput(ArrayList<Integer> internalList) {
        if (!(uVertice >= 1 && uVertice != vVertice)) {
            while (uVertice < 1 || uVertice == vVertice) {
                System.out.println("Insira um vertice de origem valido (1 <= U != V): ");
                uVertice = scanner.nextInt();
            }
        }
        internalList.add(uVertice);
    }

    private void destiny(ArrayList<Integer> internalList) {
        System.out.println("Insira o vertice de destino: ");
        vVertice = scanner.nextInt();
        this.validateDestinyInput(internalList);
    }

    private void validateDestinyInput(ArrayList<Integer> internalList) {
        if (!(vVertice <= nVertices && vVertice != uVertice)) {
            while (vVertice > nVertices || vVertice == uVertice) {
                System.out.println("Insira um vertice de destino valido (V <= N, V != U): ");
                vVertice = scanner.nextInt();
            }
        }
        internalList.add(vVertice);
    }

    private void cost(ArrayList<Integer> internalList) {
        System.out.println("Insira um custo para a ligacao entre os dois vertices: ");
        cCost = scanner.nextInt();
        this.validateCostInput(internalList);
    }

    private void validateCostInput(ArrayList<Integer> internalList) {
        if (!(cCost >= 1 && cCost <= 500)) {
            while (cCost < 1 || cCost > 500) {
                System.out.println("Insira um custo valido (1 <= C <= 500): ");
                cCost = scanner.nextInt();
            }
        }
        internalList.add(cCost);
    }

    private void clearValues() {
        uVertice = 0;
        vVertice = 0;
    }

    public static void main(String[] args) {
        ReducingDetails obj = new ReducingDetails();
        obj.build();
    }
}
