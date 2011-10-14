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
    //private LinkedList<Integer> rutaMin;

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
        double pesoActual = this.calculaPeso(actuales);
        if ((actuales.length() >= this.nVert - 1) || pesoActual > this.pesoMin) {
            if (actuales.length() == this.nVert - 1) {
                if (pesoActual < this.pesoMin) {
                    this.rutaMin = actuales;
                    this.pesoMin = pesoActual;
                }
            }
            return;
        }
        for (int i = 1; i < this.nVert; i++) {
            if (actuales.indexOf(""+i) >= 0) {
                continue;
            }
            creaCamino(actuales + i);
        }
    }

    public double calculaPeso(String lista) {
        double res = 0;
        char[] indices =lista.toCharArray();
        switch(lista.length()) {
        case 0:
            return 0;
        case 1:
            return 2*(this.pesos.getEntrada(0, Character.getNumericValue(indices[0])));
        case 2 :
            res += this.pesos.getEntrada(0, Character.getNumericValue(indices[0]));
            res += this.pesos.getEntrada(Character.getNumericValue(indices[0]),
                                         Character.getNumericValue(indices[1]));
            return res;
        default:
            res += this.pesos.getEntrada(0, Character.getNumericValue(indices[0]));
            for (int i = 0; i < indices.length - 1; i++) {
                res += this.pesos.getEntrada(Character.getNumericValue(indices[i]),
                                         Character.getNumericValue(indices[i+1]));
            }
            res += this.pesos.getEntrada(Character.getNumericValue(indices[indices.length - 1]),0);
            return res;
        }
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
        AgenteViajero test = new AgenteViajero("data/XML-MAd/09mad.xml");
        test.creaPivote();
        test.creaCamino("");
        System.out.println(test.pesoMin);
        System.out.println(test.rutaMin);

    }

}
