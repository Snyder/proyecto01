package proyecto01;

import java.util.LinkedList;

/**
 * Clase que obtiene las permutaciones de una lista de caracteres
 * @author Fabiola Garcìa
 */
public class Permutaciones {

    private String permutaciones;

    /**
     * Constructor
     */
    public Permutaciones() {
        this.permutaciones = "";
    }

    public String getPermutaciones() {
        return this.permutaciones;
    }

    /**
     * Funcion que obtiene las permutaciones de la lista.
     * @param acumulada cadena de caracteres acumulados.
     * @param sobrantes caracteres a permutar.
     */
    public void permuta(String acumulada, LinkedList<Integer> vertices) {
        if (vertices.size() == 1) {
            this.permutaciones += acumulada + vertices.get(0) + ",";
        }
        for (int i = 0; i < vertices.size(); i++) {
            Integer sig = vertices.remove(i);
            permuta(acumulada + sig + ",", vertices);
            vertices.add(i, sig);
        }
    }

    public void creaPermutaciones(int nVertices) {
        LinkedList<Integer> vertices = new LinkedList<Integer>();
        for (int i = 1; i < nVertices; i++) {
            vertices.add(i);
        }
        this.permuta("", vertices);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Permutaciones p = new Permutaciones();
        p.creaPermutaciones(6);
        System.out.println(p.permutaciones);

    }
}
