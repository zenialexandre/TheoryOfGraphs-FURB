/**
* @author Alexandre Zeni
*/
package trabalho01;

import java.lang.reflect.Array;
import java.util.*;

public class AnalisaGrafo {

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
            for (int j = 0; j < matrizAdjacencia[0].length; j++) {
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
                    }
                }

                // verificando regular
                if (this.verifRegular(matrizAdjacencia)) ehRegular = true;
            }
        }
        // verificando bipartido
        if ((!ehMultigrafo || ehMultigrafo && !temLoop) && !ehDirigido && !this.verifNulo(contNulo, numElementos)) {
            if (this.verifBipartido(matrizAdjacencia)) ehBipartido = true;
        }

        // verificando completo
        if (!ehMultigrafo && !ehDirigido && !this.verifNulo(contNulo, numElementos)) {
            if (this.verifCompleto(matrizAdjacencia)) ehCompleto = true;
        }

        // montando a mensagem baseado nas afirmacoes
        if (this.verifNulo(contNulo, numElementos)) msg += "Nulo - ";
        if (ehMultigrafo) msg += "Multigrafo - "; else if (!ehMultigrafo) msg += "Simples - ";
        if (ehDirigido) msg += "Dirigido - "; else msg += "Nao-Dirigido - ";
        if (ehCompleto) msg += "Completo - ";
        if (ehBipartido) msg += "Bipartido - ";
        if (ehRegular) msg += "Regular - ";

        return this.arrumaMsg(msg);
    }

    public String arestasDoGrafo(int[][] matrizAdjacencia) {
        String msg = "\n";
        ArrayList<String[]> conexoes = new ArrayList<>();
        int somaArestas = 0;
        int numArestas = 0;

        for (int i = 0; i < matrizAdjacencia.length; i++) {
            for (int j = 0; j < matrizAdjacencia[0].length; j++) {
                if (matrizAdjacencia[i][j] > 0) {
                    //contando as arestas
                    somaArestas += matrizAdjacencia[i][j];

                    //conexoes
                    i++;
                    j++;
                    String[] arestas = { "v" + i + ", v" + j };
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

    public String grausDoVertice(int[][] matrizAdjacencia) {
        List<Integer> freqArestas = this.getFrequenciaArestas(matrizAdjacencia);
        List<Integer> seqGraus = this.getSequenciaDeGrausNaoDirigido(matrizAdjacencia);
        String msg = "\nListagem de graus de cada vertice:\n";
        String grausMsg = "";

        if (ehDirigido) { // entradas = linhas / saidas = colunas
            List<Integer> seqGrausDirigido = new ArrayList<>();
            int[] somaEntradasSaidas = this.getSequenciaDeGrausDirigido(matrizAdjacencia);
            for (int k = 0; k < somaEntradasSaidas.length; k++) {
                k++;
                grausMsg += "v" + k + " -> ";
                k--;
                grausMsg += somaEntradasSaidas[k] + "\n";
                seqGrausDirigido.add(somaEntradasSaidas[k]);
            }
            Collections.sort(seqGrausDirigido);
            msg += grausMsg + "\nSequencia de graus: " + seqGrausDirigido;

        } else {
            for (int k = 0; k < matrizAdjacencia.length + 1; k++) {
                grausMsg += "v" + k + " -> " + Collections.frequency(freqArestas, k) + "\n";
            }
            msg += grausMsg.substring(7, grausMsg.length() - 1) + "\n\nSequencia de graus: " + seqGraus;
        }
        return msg;
    }

    private boolean verifSimetria(int[][] matrizAdjacencia, int i, int j) {
        return matrizAdjacencia[i][j] == matrizAdjacencia[j][i];
    }

    private boolean verifNulo(int contNulo, int numElementos) {
        return contNulo == numElementos;
    }

    private boolean verifCompleto(int[][] matrizAdjacencia) {
        int vertices = matrizAdjacencia[0].length;

        for (int grau : this.getSequenciaDeGrausNaoDirigido(matrizAdjacencia)) { // verificando se cada grau da sequencia eh igual a numVertices - 1
            if (grau == vertices - 1) return true;
        }
        return false;
    }

    private boolean verifRegular(int[][] matrizAdjacencia) {
        int contFrequencia = 0;

        if (!ehDirigido) {
            for (int grau : this.getSequenciaDeGrausNaoDirigido(matrizAdjacencia)) {
                contFrequencia = Collections.frequency(this.getSequenciaDeGrausNaoDirigido(matrizAdjacencia), grau);
            }
        } else {
            for (int grau : this.getSequenciaDeGrausDirigido(matrizAdjacencia)) {
                contFrequencia = Collections.frequency(this.getSeqGrausDirigidoComoList(this.getSequenciaDeGrausDirigido(matrizAdjacencia)), grau);
            }
        }
        return contFrequencia == this.getSequenciaDeGrausNaoDirigido(matrizAdjacencia).size();
    }

    private boolean verifBipartido(int[][] matrizAdjacencia) {
        boolean retorno = false;
        ArrayList<Integer> conjunto1 = new ArrayList<>();
        ArrayList<Integer> conjunto2 = new ArrayList<>();

        for (int i = 0; i < matrizAdjacencia.length; i++) {
            for (int j = 0; j < matrizAdjacencia[0].length; j++) {
                if (i < j) { // pega a parte debaixo da diagonal principal
                    if (matrizAdjacencia[i][j] > 0) {
                        conjunto1.add(i);
                        conjunto2.add(j);
                    }
                }
            }
        }
        if (!Objects.equals(conjunto2.get(0), conjunto1.get(conjunto1.size() - 1))) retorno = true;
        return retorno;
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

    private List<Integer> getFrequenciaArestas(int[][] matrizAdjacencia) {
        List<Integer> freqArestas = new ArrayList<>();
        for (int i = 0; i < matrizAdjacencia.length; i++) {
            for (int j = 0; j < matrizAdjacencia[0].length; j++) {
                if (matrizAdjacencia[i][j] > 0) {
                    i++;
                    freqArestas.add(i);
                    i--;
                }
            }
        }
        return freqArestas;
    }

    private List<Integer> getSequenciaDeGrausNaoDirigido(int[][] matrizAdjacencia) {
        List<Integer> seqGraus = new ArrayList<>();

        for (int i = 0; i < matrizAdjacencia.length + 1; i++) {
            seqGraus.add(Collections.frequency(this.getFrequenciaArestas(matrizAdjacencia), i));
        }
        seqGraus.remove(0);
        Collections.sort(seqGraus);
        return seqGraus;
    }

    private int[] getSequenciaDeGrausDirigido(int[][] matrizAdjacencia) {
        int[] somaEntradasSaidas = new int[matrizAdjacencia.length];
        int[] somaDasEntradas = new int[matrizAdjacencia.length];
        int[] somaDasSaidas = new int[matrizAdjacencia[0].length];

        for (int i = 0; i < matrizAdjacencia.length; i++) {
            int somaEntradas = 0;
            for (int j = 0; j < matrizAdjacencia[0].length; j++) {
                somaEntradas += matrizAdjacencia[i][j];
                somaDasEntradas[i] = somaEntradas;
            }
        }

        for (int i = 0; i < matrizAdjacencia.length; i++) {
            int somaSaidas = 0;
            for (int j = 0; j < matrizAdjacencia[0].length; j++) {
                somaSaidas += matrizAdjacencia[j][i];
                somaDasSaidas[i] = somaSaidas;
            }
        }
        for (int i = 0; i < somaEntradasSaidas.length; i++) somaEntradasSaidas[i] = somaDasEntradas[i] + somaDasSaidas[i];
        return somaEntradasSaidas;
    }

    private List<Integer> getSeqGrausDirigidoComoList(int[] somaEntradasSaidas) {
        List<Integer> somaEntradasSaidasList = new ArrayList<>();
        for (int i = 0; i < somaEntradasSaidas.length; i++) {
            somaEntradasSaidasList.add(somaEntradasSaidas[i]);
        }
        return somaEntradasSaidasList;
    }

    public static void main(String[] args) {
        AnalisaGrafo obj = new AnalisaGrafo();
        int[][] matrizAdjacencia = {
                {1, 0, 1, 0},
                {1, 1, 2, 0},
                {0, 0, 0, 1},
                {1, 0, 1, 1}
        };

        System.out.println(obj.tipoDoGrafo(matrizAdjacencia));
        System.out.println(obj.arestasDoGrafo(matrizAdjacencia));
        System.out.println(obj.grausDoVertice(matrizAdjacencia));
    }
}
