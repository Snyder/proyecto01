package proyecto01;

import java.util.LinkedList;

/**
 * Clase que obtiene las permutaciones de una lista de caracteres
 * @author Fabiola Garcìa
 */
public class Permutaciones {

    private LinkedList<String> permutaciones;

    /**
     * Constructor
     */
    public Permutaciones() {
        this.permutaciones = new LinkedList<String>();
    }

    /**
     * Funci&acuteon que obtiene las permutaciones de la lista.
     * @param acumulada cadena de caracteres acumulados.
     * @param sobrantes caracteres a permutar.
     */
    public void permuta(String acumulada, LinkedList<Character> sobrantes) {
        if (sobrantes.size() == 1) {
            this.permutaciones.add(acumulada + sobrantes.get(0));

            System.out.println(acumulada + sobrantes.get(0));
        }
        for (int i = 0; i < sobrantes.size(); i++) {
            Character sig = sobrantes.remove(i);
            permuta(acumulada + sig, sobrantes);
            sobrantes.add(i, sig);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LinkedList<Character> caracteres = new LinkedList<Character>();
        caracteres.add('1');
        caracteres.add('2');
        caracteres.add('3');
        caracteres.add('4');
        caracteres.add('5');
        Permutaciones p = new Permutaciones();
        p.permuta("", caracteres);
    }
}
