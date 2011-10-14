package proyecto01;

import EstDat.Matriz;
import EstDat.LectorMatrizXML;
import EstDat.MatrizPolinomio;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 * Describe class AgenteViajero here.
 *
 * @author <a href="mailto:snyder@ciencias.unam.mx">Snyder</a>
 * @version 1.0
 */
public class AgenteViajero {

    private int nVert;
    private Matriz pesos = null;
    private double pesoMin = 0;
    private String rutaMin;

    /**
     * Crea un objeto <code>AgenteViajero</code> con el numero de vertices de la
     * grafica y genera las permutaciones, asi como la matriz de pesos.
     *
     * @param numeroVertices la cantidad de vertices que tiene la grafica
     * @param rutaArchivo la ruta del archivo donde se encuentra la matriz de
     * pesos de los vertices.
     *
     */
    public AgenteViajero(String rutaArchivo) {
        this.rutaMin = null;
        this.pesoMin = 0;
        try {
            LectorMatrizXML lector = new LectorMatrizXML(rutaArchivo);
            this.nVert = lector.getRenglones();
            this.pesos = new MatrizPolinomio(
                       lector.getRenglones(), lector.getColumnas(),
                       lector.getDefault(), lector.getTipo());
            for (int i = 0; i < lector.getEntradas(); i++) {
                pesos.setEntrada(lector.getRenglon(),
                                      lector.getColumna(), lector.getEntrada());
                if (lector.haySiguiente()) {
                    lector.siguienteEntrada();
                }
            }
            pesos.despliega();
        } catch (IndexOutOfBoundsException exio) {
            exio.printStackTrace();
            System.err.println("Se salió");
        } catch (Exception ex) {
            System.err.println("Oops!");
        }
    }

    public void creaCamino (String actuales) {
        String[] indices = actuales.split(",");
        double pesoActual = this.calculaPeso(indices);
        if ((indices.length >= this.nVert - 1) || pesoActual > this.pesoMin) {
            if (indices.length == this.nVert - 1) {
                if (pesoActual < this.pesoMin) {
                    System.out.println("cambio de pivote" + pesoActual );

                    this.rutaMin = actuales;
                    this.pesoMin = pesoActual;
                }
            }
            return;
        }
        for (int i = 1; i < this.nVert; i++) {
            if (actuales.indexOf(Integer.toString(i, 10)) >= 0) {
                continue;
            }
            creaCamino(actuales + i + ",");
        }
    }

    public double calculaPeso(String[] indices) {
        double res = 0;
        if (indices[0] == "") {
            return 0;
        }
        res += this.pesos.getEntrada(0, Integer.parseInt(indices[0]));
        for (int i = 0; i < indices.length; i++) {
            if ((i + 1) == indices.length) {
                res += this.pesos.getEntrada(Integer.parseInt(indices[i]), 0);
                continue;
            }
            res += this.pesos.getEntrada(Integer.parseInt(indices[i]),
                                         Integer.parseInt(indices[i + 1]));
        }
        return res;
    }

    public void creaPivote() {
        for (int i = 0; i < nVert - 1; i++) {
            this.pesoMin += this.pesos.getEntrada(i, i+1);
        }
        this.pesoMin += this.pesos.getEntrada(this.nVert - 2, this.nVert - 1);
        this.pesoMin += this.pesos.getEntrada(this.nVert - 1, 0);
        System.out.println("el pivote vale: " + this.pesoMin);
    }

    public static void main(String[] args) {
        AgenteViajero test = new AgenteViajero("data/XML-MAd/12mad.xml");
        test.creaPivote();
        test.creaCamino("");
        System.out.println(test.pesoMin);
        System.out.println(test.rutaMin);

    }

}
