/**
* @author Alexandre Zeni
*/
package trabalho01;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnalisaGrafo {
// regular, sequencia de graus só numeros iguais
    private boolean ehDirigido = false;

    public String tipoDoGrafo(int[][] matrizAdjacencia) {
        int numElementos = this.getNumeroElementos(matrizAdjacencia);
        int contNulo = 0;
        int somaDiagonalPrincipal = 0;
        boolean ehMultigrafo = false;
        boolean ehCompleto = false;
        boolean ehRegular = false;
        boolean temLoop = false;
        boolean ehBipartido = false;
        String msg = "O tipo do grafo inserido seria: ";


        for (int i = 0; i < matrizAdjacencia.length; i++) {
            for (int j = 0; j < matrizAdjacencia.length; j++) {
                // verficando grafo nulo
                if (matrizAdjacencia[i][j] == 0) {
                    contNulo++;
                }

                // verificando grafo simples / multigrafo
                if (matrizAdjacencia[i][j] > 1) {
                    ehMultigrafo = true;
                } else if (i == j) {
                    somaDiagonalPrincipal += matrizAdjacencia[i][j];
                    if (somaDiagonalPrincipal > 0) ehMultigrafo = true; temLoop = true;
                }

                // verificando dirigido / nao-dirigido
                if (!this.verifSimetria(matrizAdjacencia, i, j)) ehDirigido = true;

                // verificando completo / graus devem ser = numVertices - 1
                if (!ehMultigrafo) {
                    if (contNulo == 1 && this.verifNulo(contNulo, numElementos)) { // cobre o caso do K1, unico nulo completo
                        ehCompleto = true;
                    } else if (this.verifCompleto(matrizAdjacencia)) {
                        ehCompleto = true;
                    }
                }

                // verificando bipartido
                if (matrizAdjacencia[0].length % 2 == 0) ehBipartido = true;

                // verificando regular
                if (this.verifRegular(matrizAdjacencia)) ehRegular = true;
            }
        }

        // montando a mensagem baseado nas afirmacoes

        if (this.verifNulo(contNulo, numElementos)) msg += "Nulo - ";

        if (ehMultigrafo) {
            msg += "Multigrafo - ";
        } else if (!ehMultigrafo) {
            msg += "Simples - ";
        }

        if (ehDirigido) {
            msg += "Dirigido - ";
        } else {
            msg += "Nao-Dirigido - ";
        }

        if (ehCompleto) msg += "Completo - ";
        if (ehBipartido) msg += "Bipartido - ";
        if (ehRegular) msg += "Regular - ";

        return this.arrumaMsg(msg);
    }

    private boolean verifSimetria(int[][] matrizAdjacencia, int i, int j) {
        return matrizAdjacencia[i][j] == matrizAdjacencia[j][i];
    }

    private boolean verifNulo(int contNulo, int numElementos) {
        return contNulo == numElementos;
    }

    private boolean verifCompleto(int[][] matrizAdjacencia) {
        int vertices = matrizAdjacencia[0].length;

        for (int grau : this.getSequenciaDeGraus(matrizAdjacencia)) { // verificando se cada grau da sequencia eh igual a numVertices - 1
           if (grau == vertices - 1) return true;
        }
        return false;
    }

    private boolean verifRegular(int[][] matrizAdjacencia) {
        int contFrequencia = 0;

        for (int grau : this.getSequenciaDeGraus(matrizAdjacencia)) {
            contFrequencia = Collections.frequency(this.getSequenciaDeGraus(matrizAdjacencia), grau);
        }
        return contFrequencia == this.getSequenciaDeGraus(matrizAdjacencia).size();
    }

    private String arrumaMsg(String msg) {
        int ultimoHifenIndex = msg.length() - 2;
        return msg.substring(0, ultimoHifenIndex - 1);
    }

    private int getNumeroElementos(Object object) {
        if (!object.getClass().isArray()) return -1;
        int numElementos = 0;

        for (int i = 0; i < Array.getLength(object); i++) {
            numElementos += getNumeroElementos(Array.get(object, i));
        }
        return Math.abs(numElementos);
    }

    public String arestasDoGrafo(int[][] matrizAdjacencia) {
        String msg = "\n";
        ArrayList<String[]> conexoes = new ArrayList<>();
        int somaArestas = 0;
        int numArestas = 0;

        // solucao p/ nao dirigidos e dirigidos (quantidade de arestas)
        for (int i = 0; i < matrizAdjacencia.length; i++) {
            for (int j = 0; j < matrizAdjacencia.length; j++) {
                if (matrizAdjacencia[i][j] > 0) {
                    //contando as arestas
                    somaArestas += matrizAdjacencia[i][j];

                    //conexoes
                    i++;
                    j++;
                    String[] arestas = { i + ", " + j };
                    conexoes.add(arestas);
                    i--;
                    j--;
                }
            }
        }
        if (!ehDirigido) numArestas = somaArestas / 2; else numArestas = somaArestas;

        if (numArestas > 0) {
            msg += "Numero de arestas existentes no grafo: " + numArestas;
            msg += "\nConjunto de arestas: ";

            for (String[] arestas : conexoes) {
                msg += "{" + Arrays.toString(arestas) + "} , ";
            }
            msg = this.arrumaMsg(msg);
        } else {
            msg += "Este grafo nao possui arestas.";
        }
        return msg;
    }

    //p/ descobrir o grau -> conta aparicoes / 2 = grau
    public String grausDoVertice(int[][] matrizAdjacencia) {
        List<Integer> freqArestas = this.getFrequenciaArestas(matrizAdjacencia);
        List<Integer> seqGraus = this.getSequenciaDeGraus(matrizAdjacencia);
        String introducaoMsg = "\nListagem de graus de cada vertice:\n";
        String grausMsg = "";

        // graus de cada vertice
        if (ehDirigido) {
            // somaDeEntradas + somaDeSaidas

        } else {
            for (int k = 0; k < matrizAdjacencia.length + 1; k++) {
                grausMsg += k + " -> " + Collections.frequency(freqArestas, k) + "\n";
            }
        }
        return introducaoMsg + grausMsg.substring(7, grausMsg.length() - 1) + "\n\nSequencia de graus: " + seqGraus;
    }

    private List<Integer> getFrequenciaArestas(int[][] matrizAdjacencia) {
        List<Integer> freqArestas = new ArrayList<>();
        for (int i = 0; i < matrizAdjacencia.length; i++) {
            for (int j = 0; j < matrizAdjacencia.length; j++) {
                if (matrizAdjacencia[i][j] > 0) {
                    i++;
                    freqArestas.add(i);
                    i--;
                }
            }
        }
        return freqArestas;
    }

    private List<Integer> getSequenciaDeGraus(int[][] matrizAdjacencia) {
        List<Integer> seqGraus = new ArrayList<>();

        for (int i = 0; i < matrizAdjacencia.length + 1; i++) {
            seqGraus.add(Collections.frequency(this.getFrequenciaArestas(matrizAdjacencia), i));
        }
        seqGraus.remove(0);
        Collections.sort(seqGraus);
        return seqGraus;
    }

    public static void main(String[] args) {
        AnalisaGrafo obj = new AnalisaGrafo();
        int[][] matrizAdjacencia = {
                {0, 1},
                {1, 0}
        };

        System.out.println(obj.tipoDoGrafo(matrizAdjacencia));
        System.out.println(obj.arestasDoGrafo(matrizAdjacencia));
        System.out.println(obj.grausDoVertice(matrizAdjacencia));
    }
}
