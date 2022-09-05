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
// verificacao p/ completo e bipartido ainda nao estao 100%
    private boolean ehDirigido = false;

    public String tipoDoGrafo(int[][] matrizAdjacencia) {
        int numElementos = Math.abs(this.getNumeroElementos(matrizAdjacencia));
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
                // verficiando grafo nulo
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

                // verificando dirigido / nao-dirigido & completo
                if (!ehMultigrafo) {
                    if (!this.verifSimetria(matrizAdjacencia, i, j)) {
                        ehDirigido = true;
                    }

                    // verificando completo
                    if (contNulo == 1 && this.verifNulo(contNulo, numElementos)) { // cobre o caso do K1, unico nulo completo
                        ehCompleto = true;
                    } else if (this.verifCompleto(matrizAdjacencia, i, j)) {
                        ehCompleto = true;
                    }
                } else {
                    if (temLoop) {
                        ehDirigido = true;
                    } else if (!temLoop && this.verifSimetria(matrizAdjacencia, i, j)) {
                        ehDirigido = false;
                    }
                }

                // verificando bipartido
                if (!ehMultigrafo) {

                } else if (ehMultigrafo && !temLoop) {

                }
            }
        }

        // montando a mensagem baseado nas afirmacoes

        if (this.verifNulo(contNulo, numElementos)) msg += "Nulo - "; ehRegular = true;

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

        if (ehCompleto) {
            msg += "Completo - ";
            ehRegular = true;
        }
        if (ehBipartido) msg += "Bipartido - ";
        if (ehRegular) msg += "Regular - ";

        return this.arrumaMsg(msg);
    }

    private boolean verifSimetria(int[][] matrizAdjacente, int i, int j) {
        return matrizAdjacente[i][j] == matrizAdjacente[j][i];
    }

    private boolean verifNulo(int contNulo, int numElementos) {
        return contNulo == numElementos;
    }

    private boolean verifCompleto(int[][] matrizAdjacente, int i, int j) {
        return matrizAdjacente[i][j] > 0 && matrizAdjacente[j][i] > 0;
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
        return numElementos;
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
        List<Integer> freqArestas = new ArrayList<>();
        List<Integer> seqGraus = new ArrayList<>();
        String introducaoMsg = "\nListagem de graus de cada vertice:\n";
        String grausMsg = "";
        String sequenciaMsg = "";

        // graus de cada vertice
        if (ehDirigido) {

        } else {
            for (int i = 0; i < matrizAdjacencia.length; i++) {
                for (int j = 0; j < matrizAdjacencia.length; j++) {
                    if (matrizAdjacencia[i][j] > 0) {
                        i++;
                        freqArestas.add(i);
                        i--;
                    }
                }
            }

            for (int k = 0; k < matrizAdjacencia.length + 1; k++) {
                grausMsg += k + " -> " + Collections.frequency(freqArestas, k) + "\n";
                seqGraus.add(Collections.frequency(freqArestas, k));
            }
        }
        seqGraus.remove(0);
        Collections.sort(seqGraus);
        return introducaoMsg + grausMsg.substring(7, grausMsg.length() - 1) + "\n\nSequencia de graus: " + seqGraus;
    }

    public static void main(String[] args) {
        AnalisaGrafo obj = new AnalisaGrafo();
        int[][] matrizAdjacencia = {
                {0, 1, 1},
                {1, 0, 0},
                {1, 0, 0}
        };

        System.out.println(obj.tipoDoGrafo(matrizAdjacencia));
        System.out.println(obj.arestasDoGrafo(matrizAdjacencia));
        System.out.println(obj.grausDoVertice(matrizAdjacencia));
    }
}
