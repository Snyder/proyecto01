package proyecto01;

import EstDat.Matriz;
import EstDat.LectorMatrizXML;
import EstDat.MatrizPolinomio;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 * <code>AgenteViajero</code> es la representacion abstracta del problema del TSP,
 * leemos de una matriz simetricas (matriz de adyacencias con pesos), conforme los lineamientos
 * del dtd de la practica pasada.
 * Para minimizar el gasto de memoria utilizamos backtracking, con condiciones para reducir los
 * casos a verificar, que es con el atributo de la ruta minima "rutaMin", vamos comparando, 
 * para asi solo revisar los posibles caminos de peso menor al que tenemos.
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
        } catch (IndexOutOfBoundsException exio) {
            exio.printStackTrace();
            System.err.println("Se salió");
        } catch (Exception ex) {
            System.err.println("Oops!");
        }
    }

    /**
    * <code>creaCamino</code> es el metodo principal, esencialmente usamos
    * backtracking, con la condicion de que el camino que sa va construyendo es
    * menor al que tenemos como min.
    */
    public void creaCamino (String actuales) {
        String[] indices = actuales.split(",");
        double pesoActual = this.calculaPeso(indices);
        if ((indices.length >= this.nVert - 1) || pesoActual > this.pesoMin) {
            if (indices.length == this.nVert - 1) {
                if (pesoActual < this.pesoMin) {
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

    /**
    * <code>calculaPeso</code> hace eso, calcular el costo del viaje.
    * Para ello, verifica que haya camino al cual calcularle el costo.
    * Si es vacio, termina, si no, lo revisa y busca el peso en la matriz.
    * @param indices El camino.
    * @return el costo del viaje.
    */
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

    /**
    * <code>creaPivote</code> genera un primer camino con el cual comparar y 
    * reducir el numero de casos a revisar. El camino es recorrerlo del 0 al n
    * donde "n" es el vertice final y regresar al cero.
    */
    public void creaPivote() {
        for (int i = 0; i < nVert - 1; i++) {
            this.pesoMin += this.pesos.getEntrada(i, i+1);
        }
        this.pesoMin += this.pesos.getEntrada(this.nVert - 2, this.nVert - 1);
        this.pesoMin += this.pesos.getEntrada(this.nVert - 1, 0);
        System.out.println("El pivote vale: " + this.pesoMin);
    }

    /**
    * Metodo main, sirve para ejecutar nuestro programa. Construe un
    * objeto de tipo <code>AgenteViajero</code> con la ruta del archivo
    * del que queremos leer, creamos un pivote, y ejecutamos el metodo principal.
    */
    public static void main(String[] args) {
    	if(args.length == 0) {
    	    System.err.println("Argumento invalido.");
    	    System.exit(1);
    	}
        AgenteViajero test = new AgenteViajero(args[0]);
        test.creaPivote();
        test.creaCamino("");
        System.out.println("El costo minimo del viaje es = " + test.pesoMin);
        System.out.println("La ruta que lo consigue es :\t" + test.rutaMin);

    }

}
