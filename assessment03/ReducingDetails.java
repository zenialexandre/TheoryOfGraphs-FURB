/**
 * @author Alexandre Zeni
 */

package assessment03;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ReducingDetails {

    private int nVertices;
    private int mEdges;
    private int uVertice;
    private int vVertice;
    private int cCost;
    private ArrayList<ArrayList<Integer>> graphInfos;
    private ArrayList<Integer> originVertices;
    private ArrayList<ArrayList<Integer>> sortedEdges;
    private final Scanner scanner = new Scanner(System.in);

    public void build() {
        this.requestInput();
        this.execKruskalAlgo();
    }

    private void requestInput() {
        this.verticesAndEdges();
        this.graphInfo();
        scanner.close();
    }

    private void execKruskalAlgo() {
        int minimumCost = 0;

        for (int i = 0; i < nVertices; i++) {
            this.makeSet(i);
        }
        System.out.println(originVertices.toString());
        this.sortEdgesByCost();

        System.out.println("Custo minimo: " + minimumCost);
    }

    private void makeSet(int originVertice) {
        originVertices = new ArrayList<>();
        originVertices.add(originVertice);
    }

    private void findSet() {

    }

    private void union() {

    }

    private void sortEdgesByCost() {

    }

    private void addEdge(ArrayList<Integer> info) {
        sortedEdges.add(info);
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
        graphInfos = new ArrayList<>();

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
