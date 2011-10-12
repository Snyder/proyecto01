package proyecto01;

import EstDat.Matriz;
import java.util.LinkedList;

/**
 * Describe class AgenteViajero here.
 *
 * @author <a href="mailto:snyder@ciencias.unam.mx">Snyder</a>
 * @version 1.0
 */
public class AgenteViajero {

    private int nVert;
    private final Matriz pesos;
    private long pesoMin;
    private LinkedList<Integer> rutaMin;
    private String permutaciones;

    /**
     * Crea un objeto <code>AgenteViajero</code> con el numero de vertices de la
     * grafica y genera las permutaciones, asi como la matriz de pesos.
     *
     * @param numeroVertices la cantidad de vertices que tiene la grafica
     * @param rutaArchivo la ruta del archivo donde se encuentra la matriz de
     * pesos de los vertices.
     *
     */
    public AgenteViajero(int numeroVertices, String rutaArchivo) {
        this.nVert = numeroVertices;
        rutaMin = null;
        pesoMin = 0;
        //falta pesos (creado automaticamente)
        //falta permutaciones (creado automaticamente)
    }

}
