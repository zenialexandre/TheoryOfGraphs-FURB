package trabalho01;

import java.lang.reflect.Array;

public class AnalisaGrafo {
    public String tipoDoGrafo(int[][] matrizAdjacencia) { // onde 1 = tem ligacao, > 1 = loop, 0 = sem ligacao
        int numElementos = Math.abs(this.getNumeroElementos(matrizAdjacencia));
        int contNulo = 0;
        int somaDiagonalPrincipal = 0;
        boolean ehMultigrafo = false;
        boolean ehDirigido = false;
        boolean ehCompleto = false;
        boolean ehRegular = false;
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
                    if (somaDiagonalPrincipal > 0) ehMultigrafo = true;
                }

                // verificando dirigido / nao-dirigido & completo
                if (!ehMultigrafo) {
                    if (!(matrizAdjacencia[i][j] == matrizAdjacencia[j][i])) {
                        ehDirigido = true;
                    }
                    ehCompleto = true;
                } else {
                    //...
                }
            }
        }

        // montando a mensagem baseado nas afirmacoes

        if (contNulo == numElementos) msg += "Nulo - "; ehRegular = true;

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

        if (ehRegular) msg += "Regular - ";
        return this.arrumaMsg(msg);
    }

    private String arrumaMsg(String msg) {
        int ultimoHifenIndex = msg.length() - 2;
        return msg.substring(0, ultimoHifenIndex - 1);
    }

    private int getNumeroElementos(Object object) {
        if (!object.getClass().isArray()) return -1;
        int numElementos = 0;

        for (int i = 0; i < Array.getLength(object); i++) {
            numElementos -= getNumeroElementos(Array.get(object, i));
        }
        return numElementos;
    }

    public String arestasDoGrafo(int[][] matrizAdjacencia) {
        String msg = "";
        int contArestas = 0;

        for (int i = 0; i < matrizAdjacencia.length; i++) {
            for (int j = 0; j < matrizAdjacencia.length; j++) {
                if (matrizAdjacencia[i][j] > 0) {
                    contArestas++;
                }
            }
        }

        if (contArestas > 0) {
            msg += "Numero de arestas existentes no grafo: " + contArestas;
        } else {
            msg += "Este grafo nao possui arestas.";
        }
        return msg;
    }

    public String grausDoVertice(int[][] matrizAdjacencia) {
        return "";
    }

    public static void main(String[] args) {
        AnalisaGrafo obj = new AnalisaGrafo();
        int[][] matrizAdjacencia = {
                {0},
        };

        System.out.println(obj.tipoDoGrafo(matrizAdjacencia));
        //System.out.println(obj.arestasDoGrafo(matrizAdjacencia));
    }
}
