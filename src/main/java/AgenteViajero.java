import EstDat.LectorMatrizXML;
import EstDat.Matriz;
import EstDat.MatrizPolinomio;


/**
 * <code>AgenteViajero</code> es la representación abstracta del problema del
 * TSP leemos de una matriz simétricas (matriz de adyacencias con pesos),
 * conforme los lineamientos del DTD de la practica pasada. Para minimizar el
 * gasto de memoria utilizamos backtracking, con condiciones para reducir los
 * casos a verificar, que es con el atributo de la ruta mínima "rutaMin", vamos
 * comparando, para así solo revisar los posibles caminos de peso menor al que
 * tenemos.
 */
public class AgenteViajero {
    /**
     * Variable usada para almacenar el número de vértices de la gráfica actual.
     */
    private int nVert;
    /**
     * Variable usada para almacenar los pesos de la matriz.
     */
    private Matriz pesos = null;
    /**
     * Variable que se usa para almacenar el peso mínimo encontrado.
     */
    private double pesoMin = 0;
    /**
     * Cadena usada para escribir la ruta mínima actual.
     */
    private String rutaMin;

    /**
     * Crea un objeto <code>AgenteViajero</code> con el numero de vértices de la
     * gráfica y genera las permutaciones, así como la matriz de pesos.
     *
     * @param rutaArchivo la ruta del archivo donde se encuentra la matriz de
     * pesos de los vértices.
     *
     */
    public AgenteViajero(final String rutaArchivo) {
        rutaMin = null;
        pesoMin = 0;
        try {
            LectorMatrizXML lector = new LectorMatrizXML(rutaArchivo);
            nVert = lector.getRenglones();
            pesos = new MatrizPolinomio(
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
     * <code>creaCamino</code> es el método principal, esencialmente usamos
     * backtracking, con la condición de que el camino que se va construyendo es
     * menor al que tenemos como mínimo.
     * @param actuales la cadena que se va formando para el camino actual
     */
    public final void creaCamino(final String actuales) {
        String[] indices = actuales.split(",");
        double pesoActual = calculaPeso(indices);
        if ((indices.length >= (nVert - 1)) || (pesoActual > pesoMin)) {
            if (indices.length == (nVert - 1)) {
                if (pesoActual < pesoMin) {
                    rutaMin = actuales;
                    pesoMin = pesoActual;
                }
            }
            return;
        }
        for (int i = 1; i < nVert; i++) {
            if (actuales.indexOf(Integer.toString(i)) >= 0) {
                continue;
            }
            creaCamino(actuales + i + ",");
        }
    }

    /**
     * <code>calculaPeso</code> hace eso, calcular el costo del viaje.
     * Para ello, verifica que haya camino al cual calcularle el costo.
     * Si es vacío, termina, si no, lo revisa y busca el peso en la matriz.
     * @param indices El camino.
     * @return el costo del viaje.
     */
    public final double calculaPeso(final String[] indices) {
        double res = 0;
        if (indices[0] == "") {
            return 0;
        }
        res += pesos.getEntrada(0, Integer.parseInt(indices[0]));
        for (int i = 0; i < indices.length; i++) {
            if ((i + 1) == indices.length) {
                res += pesos.getEntrada(Integer.parseInt(indices[i]), 0);
                continue;
            }
            res += pesos.getEntrada(Integer.parseInt(indices[i]),
                    Integer.parseInt(indices[i + 1]));
        }
        return res;
    }

    /**
     * <code>creaPivote</code> genera un primer camino con el cual comparar y
     * reducir el numero de casos a revisar. El camino es recorrerlo del 0 al n
     * donde "n" es el vértice final y regresar al cero.
     */
    public final void creaPivote() {
        for (int i = 0; i < (nVert - 1); i++) {
            pesoMin += pesos.getEntrada(i, i + 1);
        }
        pesoMin += pesos.getEntrada(nVert - 2, nVert - 1);
        pesoMin += pesos.getEntrada(nVert - 1, 0);
        System.out.println("El pivote vale: " + pesoMin);
    }

    /**
     * Método main, sirve para ejecutar nuestro programa. Construye un
     * objeto de tipo <code>AgenteViajero</code> con la ruta del archivo
     * del que queremos leer, creamos un pivote, y ejecutamos el método
     * principal.
     * @param args una descripción genérica
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
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
