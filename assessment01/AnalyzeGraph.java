/**
* @author Alexandre Zeni
*/
package assessment01;

import java.lang.reflect.Array;
import java.util.*;

public class AnalyzeGraph {

    private boolean isDirected = false;

    public String tipoDoGrafo(int[][] adjacencyMatrix) {
        if (this.checkAdjacencyMatrix(adjacencyMatrix)) {
            int elementsNumber = this.getElementsNumber(adjacencyMatrix);
            int nullCount = 0;
            int mainDiagonalSum = 0;
            boolean isMultigraph = false;
            boolean isComplete = false;
            boolean isRegular = false;
            boolean hasLoop = false;
            boolean isBipartite = false;
            String msg = "O tipo do grafo inserido seria: ";

            for (int i = 0; i < adjacencyMatrix.length; i++) {
                for (int j = 0; j < adjacencyMatrix[0].length; j++) {
                    if (adjacencyMatrix[i][j] == 0) {
                        nullCount++;
                    }
                    
                    if (adjacencyMatrix[i][j] > 1) {
                        isMultigraph = true;
                    } else if (i == j) {
                        mainDiagonalSum += adjacencyMatrix[i][j];
                        if (mainDiagonalSum > 0) isMultigraph = true; hasLoop = true;
                    }
                    
                    if (!this.checkSymmetry(adjacencyMatrix, i, j)) isDirected = true;
                    
                    if (!isMultigraph) {
                        if (nullCount == 1 && this.checkNull(nullCount, elementsNumber)) {
                            isComplete = true;
                        }
                    }

                    // verificando regular
                    if (this.checkRegular(adjacencyMatrix)) isRegular = true;
                }
            }
            // verificando bipartido
            if ((!isMultigraph || isMultigraph && !hasLoop) && !isDirected && !this.checkNull(nullCount, elementsNumber)) {
                if (this.checkBipartite(adjacencyMatrix)) isBipartite = true;
            }

            // verificando completo
            if (!isMultigraph && !isDirected && !this.checkNull(nullCount, elementsNumber)) {
                if (this.checkComplete(adjacencyMatrix)) isComplete = true;
            }

            // montando a mensagem baseado nas afirmacoes
            if (this.checkNull(nullCount, elementsNumber)) msg += "Nulo - ";
            if (isMultigraph) msg += "Multigrafo - "; else if (!isMultigraph) msg += "Simples - ";
            if (isDirected) msg += "Dirigido - "; else msg += "Nao-Dirigido - ";
            if (isComplete) msg += "Completo - ";
            if (isBipartite) msg += "Bipartido - ";
            if (isRegular) msg += "Regular - ";
            return this.fixMessage(msg);
        } else {
            return "Matriz inserida invalida para analise. Nao deve ser nula em elementos e tamanho deve ser N x N.";
        }
    }

    public String arestasDoGrafo(int[][] adjacencyMatrix) {
        if (this.checkAdjacencyMatrix(adjacencyMatrix)) {
            String msg = "\n";
            ArrayList<String[]> connections = new ArrayList<>();
            int edgesSum = 0;
            int edgesNum = 0;

            for (int i = 0; i < adjacencyMatrix.length; i++) {
                for (int j = 0; j < adjacencyMatrix[0].length; j++) {
                    if (adjacencyMatrix[i][j] > 0) {
                        edgesSum += adjacencyMatrix[i][j];
                        
                        i++;
                        j++;
                        String[] edges = { "v" + i + ", v" + j };
                        connections.add(edges);
                        i--;
                        j--;
                    }
                }
            }

            if (!isDirected) edgesNum = edgesSum / 2; else edgesNum = edgesSum;
            if (edgesNum > 0) {
                msg += "Numero de arestas existentes no grafo: " + edgesNum;
                msg += "\nConjunto de arestas: ";

                for (String[] edgesIn : connections) {
                    msg += "{" + Arrays.toString(edgesIn) + "} , ";
                }
                msg = this.fixMessage(msg);
            } else {
                msg += "Este grafo nao possui arestas.";
            }
            return msg;
        } else {
            return "";
        }
    }

    public String grausDoVertice(int[][] adjacencyMatrix) {
        if (this.checkAdjacencyMatrix(adjacencyMatrix)) {
            List<Integer> edgesFrequency = this.getEdgesFrequency(adjacencyMatrix);
            List<Integer> degreeSequence = this.getDegreeSequenceNoDirected(adjacencyMatrix);
            String msg = "\nListagem de graus de cada vertice:\n";
            String degreeMsg = "";

            if (isDirected) {
                List<Integer> directedDegreeSequence = new ArrayList<>();
                int[] sumInputsOutputs = this.getDegreeSequenceDirected(adjacencyMatrix);
                for (int k = 0; k < sumInputsOutputs.length; k++) {
                    k++;
                    degreeMsg += "v" + k + " -> ";
                    k--;
                    degreeMsg += sumInputsOutputs[k] + "\n";
                    directedDegreeSequence.add(sumInputsOutputs[k]);
                }
                Collections.sort(directedDegreeSequence);
                msg += degreeMsg + "\nSequencia de graus: " + directedDegreeSequence;

            } else {
                for (int k = 0; k < adjacencyMatrix.length + 1; k++) {
                    degreeMsg += "v" + k + " -> " + Collections.frequency(edgesFrequency, k) + "\n";
                }
                msg += degreeMsg.substring(7, degreeMsg.length() - 1) + "\n\nSequencia de graus: " + degreeSequence;
            }
            return msg;
        } else {
            return "";
        }
    }

    private boolean checkAdjacencyMatrix(int[][] adjacencyMatrix) {
        return adjacencyMatrix != null && adjacencyMatrix.length == adjacencyMatrix[0].length;
    }

    private boolean checkSymmetry(int[][] adjacencyMatrix, int i, int j) {
        return adjacencyMatrix[i][j] == adjacencyMatrix[j][i];
    }

    private boolean checkNull(int nullCount, int elementsNumber) {
        return nullCount == elementsNumber;
    }

    private boolean checkComplete(int[][] adjacencyMatrix) {
        int vertices = adjacencyMatrix[0].length;
        ArrayList<Integer> degreeCompl = new ArrayList<>();

        for (int degree : this.getDegreeSequenceNoDirected(adjacencyMatrix)) {
            if (degree == vertices - 1) degreeCompl.add(degree);
        }
        return degreeCompl.size() == this.getDegreeSequenceNoDirected(adjacencyMatrix).size();
    }

    private boolean checkRegular(int[][] adjacencyMatrix) {
        int frequencyCount = 0;
        boolean boolReturn = false;

        if (!isDirected) {
            for (int degree : this.getDegreeSequenceNoDirected(adjacencyMatrix)) {
                frequencyCount = Collections.frequency(this.getDegreeSequenceNoDirected(adjacencyMatrix), degree);
            }
            boolReturn = frequencyCount == this.getDegreeSequenceNoDirected(adjacencyMatrix).size();
        } else {
            for (int grau : this.getDegreeSequenceDirected(adjacencyMatrix)) {
                frequencyCount = Collections.frequency(this.getDegreeSequenceDirectedAsList(this.getDegreeSequenceDirected(adjacencyMatrix)), grau);
            }
            boolReturn = frequencyCount == this.getDegreeSequenceDirected(adjacencyMatrix).length;
        }
        return boolReturn;
    }

    private boolean checkBipartite(int[][] adjacencyMatrix) {
        ArrayList<Integer> group1 = new ArrayList<>();
        ArrayList<Integer> group2 = new ArrayList<>();

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (!(adjacencyMatrix[i][i] > 0)) {
                for (int j = 0; j < adjacencyMatrix.length; j++) {
                    if (group1.isEmpty() || checkInvalidGroupInsert(i, group1, adjacencyMatrix)) {
                        group1.add(i);
                    } else if (group2.isEmpty() || checkInvalidGroupInsert(i, group2, adjacencyMatrix)) {
                        group2.add(i);
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean checkInvalidGroupInsert(int i, ArrayList<Integer> group, int[][] adjacencyMatrix) {
        for (int j = 0; j < adjacencyMatrix.length; j++)
            if (adjacencyMatrix[i][j] > 0 && group.contains(j)) return false;
        return true;
    }

    private String fixMessage(String msg) {
        int lastHyphenIndex = msg.length() - 2;
        return msg.substring(0, lastHyphenIndex - 1);
    }

    private int getElementsNumber(Object object) {
        if (!object.getClass().isArray()) return -1;
        int elementsNumber = 0;

        for (int i = 0; i < Array.getLength(object); i++) {
            elementsNumber += getElementsNumber(Array.get(object, i));
        }
        return Math.abs(elementsNumber);
    }

    private List<Integer> getEdgesFrequency(int[][] adjacencyMatrix) {
        List<Integer> edgesFrequency = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[0].length; j++) {
                if (adjacencyMatrix[i][j] > 0) {
                    i++;
                    edgesFrequency.add(i);
                    i--;
                }
            }
        }
        return edgesFrequency;
    }

    private List<Integer> getDegreeSequenceNoDirected(int[][] adjacencyMatrix) {
        List<Integer> degreeSequence = new ArrayList<>();

        for (int i = 0; i < adjacencyMatrix.length + 1; i++) {
            degreeSequence.add(Collections.frequency(this.getEdgesFrequency(adjacencyMatrix), i));
        }
        degreeSequence.remove(0);
        Collections.sort(degreeSequence);
        return degreeSequence;
    }

    private int[] getDegreeSequenceDirected(int[][] adjacencyMatrix) {
        int[] sumInputOutput = new int[adjacencyMatrix.length];
        int[] sumOfInputs = new int[adjacencyMatrix.length];
        int[] sumOfOutputs = new int[adjacencyMatrix[0].length];

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            int sumInput = 0;
            for (int j = 0; j < adjacencyMatrix[0].length; j++) {
                sumInput += adjacencyMatrix[i][j];
                sumOfInputs[i] = sumInput;
            }
        }

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            int sumOutput = 0;
            for (int j = 0; j < adjacencyMatrix[0].length; j++) {
                sumOutput += adjacencyMatrix[j][i];
                sumOfOutputs[i] = sumOutput;
            }
        }
        for (int i = 0; i < sumInputOutput.length; i++) sumInputOutput[i] = sumOfInputs[i] + sumOfOutputs[i];
        return sumInputOutput;
    }

    private List<Integer> getDegreeSequenceDirectedAsList(int[] sumInputOutput) {
        List<Integer> sumInputOutputList = new ArrayList<>();
        for (int i = 0; i < sumInputOutput.length; i++) {
            sumInputOutputList.add(sumInputOutput[i]);
        }
        return sumInputOutputList;
    }

    public static void main(String[] args) {
        AnalyzeGraph obj = new AnalyzeGraph();
        int[][] adjacencyMatrix = {
                {0, 0},
                {0, 0}
        };

        System.out.println(obj.tipoDoGrafo(adjacencyMatrix));
        System.out.println(obj.arestasDoGrafo(adjacencyMatrix));
        System.out.println(obj.grausDoVertice(adjacencyMatrix));
    }
}
