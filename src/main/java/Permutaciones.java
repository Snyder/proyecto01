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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LinkedList<Integer> caracteres = new LinkedList<Integer>();
        caracteres.add(0);
        caracteres.add(1);
        caracteres.add(2);
        caracteres.add(3);
        caracteres.add(4);
        Permutaciones p = new Permutaciones();
        p.permuta("", caracteres);
        System.out.println(p.permutaciones);

    }
}
