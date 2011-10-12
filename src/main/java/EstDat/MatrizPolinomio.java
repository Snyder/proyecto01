/* ----------------------------------------------------------------------
 * MatrizPolinomio.java
 * version 1.0
 * Copyright (C) 2004  José Galaviz Casas,
 * Facultad de Ciencias,
 * Universidad Nacional Autónoma de México, Mexico.
 *
 * Este programa es software libre; se puede redistribuir
 * y/o modificar en los términos establecidos por la
 * Licencia Pública General de GNU tal como fue publicada
 * por la Free Software Foundation en la versión 2 o
 * superior.
 *
 * Este programa es distribuido con la esperanza de que
 * resulte de utilidad, pero SIN GARANTÍA ALGUNA; de hecho
 * sin la garantía implícita de COMERCIALIZACIÓN o
 * ADECUACIÓN PARA PROPÓSITOS PARTICULARES. Véase la
 * Licencia Pública General de GNU para mayores detalles.
 *
 * Con este programa se debe haber recibido una copia de la
 * Licencia Pública General de GNU, de no ser así, visite el
 * siguiente URL:
 * http://www.gnu.org/licenses/gpl.html
 * o escriba a la Free Software Foundation Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * ----------------------------------------------------------------------
 */
package EstDat;

/**
 * Clase para representar matrices eficientemente usando un
 * polinomio de direccionamiento sobre un arreglo
 * unidimensional.
 *
 * @since 2.0
 * @author José Galaviz &lt;jgc@fciencias.unam.mx&gt;
 * @version 1.0 <br>
 * octubre 2003
 */
public class MatrizPolinomio implements Matriz {
   // la matriz, un arreglo lineal
   double[] mat;
   String tipomat;
   int tipocod;
   int columnas;
   int renglones;
   double omision;

   /**
    * Constructor.
    * @param ren número de renglones en la matriz.
    * @param col número de columnas en la matriz.
    * @param valdefault valor en el que se establecen las
    * entradas de la matriz no establecidas explícitamente
    * con el método <code>setEntrada</code>.
    * @param tipo es una cadena que identifica el tipo de
    * matriz. Los valores posibles estan especificados en la
    * clase <code>ManejadorMatrizXML</code>:
    * <UL>
    * <LI><code>ManejadorMatrizXML.TRISD</code>:
    * Triangular superior con diagonal.
    * <LI><code>ManejadorMatrizXML.TRISN</code>:
    * Triangular superior sin diagonal.
    * <LI><code>ManejadorMatrizXML.TRIID</code>:
    * Triangular inferior con diagonal.
    * <LI><code>ManejadorMatrizXML.TRIIN</code>:
    * Triangular inferior sin diagonal.
    * <LI><code>ManejadorMatrizXML.SIMETRICA</code>:
    * Simétrica
    * <LI><code>ManejadorMatrizXML.REGULAR</code>:
    * Regular (cualquiera)
    * </UL>
    */
   public MatrizPolinomio(int ren, int col, double valdefault, String tipo) {
      creaMatriz(ren, col, valdefault, tipo);
   }

   /**
    * Para inicializar la matriz a su valor default
    */
   private void inicializa(double valor) {
      if (mat != null) {
         for (int i = 0; i < mat.length; mat[i++] = valor) {
            ;
         }
      }
   }

   /**
    * Establece un código para el tipo de matriz.
    */
   private void estableceTipo(String tipo) {
      if (tipo.equals(ManejadorMatrizXML.TRISD)) {
         tipocod = 1;
      } else if (tipo.equals(ManejadorMatrizXML.TRISN)) {
         tipocod = 2;
      } else if (tipo.equals(ManejadorMatrizXML.TRIID)) {
         tipocod = 3;
      } else if (tipo.equals(ManejadorMatrizXML.TRIIN)) {
         tipocod = 4;
      } else if (tipo.equals(ManejadorMatrizXML.SIMETRICA)) {
         tipocod = 5;
      } else if (tipo.equals(ManejadorMatrizXML.REGULAR)) {
         tipocod = 6;
      }
   }

   private boolean indiceValido(int ren, int col) {
      if ((ren >= 0) && (ren < renglones) && (col >= 0)
            && (col < columnas)) {
         switch (tipocod) {
         case 1:
            return (ren <= col); // sd

         case 2:
            return (ren < col); // sn

         case 3:
            return (ren >= col); // id

         case 4:
            return (ren > col); // in

         case 5:
         default:
            return true;
         }
      } else {
         return false;
      }
   }

   /**
    * Crea una matriz de dimensiones y tipo dados.
    * @param ren número de renglones en la matriz.
    * @param col número de columnas en la matriz.
    * @param valdefault valor en el que se establecen las
    * entradas de la matriz no establecidas explícitamente
    * con el método <code>setEntrada</code>.
    * @param tipo es una cadena que identifica el tipo de
    * matriz. Los valores posibles estan especificados en la
    * clase <code>ManejadorMatrizXML</code>:
    * <UL>
    * <LI><code>ManejadorMatrizXML.TRISD</code>:
    * Triangular superior con diagonal.
    * <LI><code>ManejadorMatrizXML.TRISN</code>:
    * Triangular superior sin diagonal.
    * <LI><code>ManejadorMatrizXML.TRIID</code>:
    * Triangular inferior con diagonal.
    * <LI><code>ManejadorMatrizXML.TRIIN</code>:
    * Triangular inferior sin diagonal.
    * <LI><code>ManejadorMatrizXML.SIMETRICA</code>:
    * Simétrica
    * <LI><code>ManejadorMatrizXML.REGULAR</code>:
    * Regular (cualquiera)
    * </UL>
    */
   public void creaMatriz(int ren, int col, double valdefault, String tipo) {
      if (tipo.equals(ManejadorMatrizXML.REGULAR)) {
         mat = new double[ren * col];
      } else if (tipo.equals(ManejadorMatrizXML.TRIID)
            || tipo.equals(ManejadorMatrizXML.TRISD)
            || tipo.equals(ManejadorMatrizXML.SIMETRICA)) {
         mat = new double[(ren * (ren + 1)) / 2];
      } else if (tipo.equals(ManejadorMatrizXML.TRIIN)
            || tipo.equals(ManejadorMatrizXML.TRISN)) {
         mat = new double[(ren * (ren - 1)) / 2];
      } else {
         mat = null;
      }

      inicializa(valdefault);
      tipomat = new String(tipo);
      estableceTipo(tipo);
      columnas = col;
      renglones = ren;
      omision = valdefault;
   }

   /**
    * Establece la entrada indicada de la matriz.
    * @param ren índice del renglón de la entrada a
    * establecer.
    * @param col índice de la columna de la entrada a
    * establecer.
    * @param valor valor a colocar en la entrada.
    * @throws IndexOutOfBoundsException si los valores de
    * índice de renglón o columna son erróneos de acuerdo
    * con las dimensiones establecidas para la matriz y/o de
    * acuerdo con el tipo de matriz especificado (por
    * ejemplo, si el tipo es triangular inferior sin
    * diagonal entonces debe cumplirse que <br><code>ren</code>>
    * > <code>col</code>).<br> Si la matriz es simétrica se
    * tratará como triangular inferior con diagonal.
    */
   public void setEntrada(int ren, int col, double valor)
      throws IndexOutOfBoundsException {
      if (indiceValido(ren, col)) {
         switch (tipocod) {
         case 1:
            mat[((col * (col + 1)) / 2) + ren] = valor; // sd

            break;

         case 2:
            mat[((col * (col - 1)) / 2) + ren] = valor; // sn

            break;

         case 3:
            mat[((ren * (ren + 1)) / 2) + col] = valor; // id

            break;

         case 4:
            mat[((ren * (ren - 1)) / 2) + col] = valor; // in

            break;

         case 5:

            if (ren >= col) { // sim
               mat[((ren * (ren + 1)) / 2) + col] = valor;
            } else {
               mat[((col * (col + 1)) / 2) + ren] = valor;
            }

            break;

         default:
            mat[(ren * columnas) + col] = valor; // reg
         }
      } else {
         throw new IndexOutOfBoundsException("Índice inválido");
      }
   }

   /**
    * Regresa la cadena con el tipo de matriz. Véase el
    * método constructor para los posibles valores.
    * @return una cadena con el tipo de matriz de acuerdo
    * con la clase <code>ManejadorMatrizXML</code>.
    */
   public String getTipo() {
      return tipomat;
   }

   /**
    * Regresa el valor por omisión, aquel que se encuentra
    * en las entradas no establecidas explícitamente.
    * @return el valor por omisión.
    */
   public double getDefault() {
      return omision;
   }

   /**
    * Regresa el número de renglones en la matriz.
    * @return un entero no negativo con el número de
    * renglones de la matriz.
    */
   public int getRenglones() {
      return renglones;
   }

   /**
    * Regresa el número de columnas en la matriz.
    * @return un entero no negativo con el número de
    * columnas de la matriz.
    */
   public int getColumnas() {
      return columnas;
   }

   /**
    * Regresa el valor almacenado en una entrada de la
    * matriz.
    * @param ren es el índice del renglón de la entrada
    * requerida.
    * @param col es el índice de la columna de la entrada
    * requerida.
    * @return el valor almacenado en la entrada
    * especificada.
    * @throws IndexOutOfBoundsException si los índices
    * especificados son incorrectos. En este caso sólo se
    * considera que no rebasen las dimansiones especificadas
    * para la matriz.
    */
   public double getEntrada(int ren, int col)
      throws IndexOutOfBoundsException {
      if (indiceValido(ren, col)) {
         switch (tipocod) {
         case 1:
            return mat[((col * (col + 1)) / 2) + ren]; // sd

         case 2:
            return mat[((col * (col - 1)) / 2) + ren]; // sn

         case 3:
            return mat[((ren * (ren + 1)) / 2) + col]; // id

         case 4:
            return mat[((ren * (ren - 1)) / 2) + col]; // in

         case 5:
            return (ren >= col)
                  ? // sim
                  mat[((ren * (ren + 1)) / 2) + col]
                  : mat[((col * (col + 1)) / 2) + ren];

         default:
            return mat[(ren * columnas) + col]; // reg
         }
      } else if ((ren >= 0) && (ren < renglones) && (col >= 0)
            && (col < columnas)) {
         return omision;
      } else {
         throw new IndexOutOfBoundsException("Índice inválido");
      }
   }

   /**
    * Despliega la matriz en la salida estándar.
    * @throws IndexOutOfBoundsException
    */
   public void despliega()
      throws IndexOutOfBoundsException {
      int i;
      int j;

      for (i = 0; i < renglones; i++) {
         for (j = 0; j < columnas; j++) {
            System.out.print(" " + getEntrada(i, j) + "\t");
         }

         System.out.println();
      }
   }

   /**
    * Programa de ejemplo
    */
   public static void main(String[] args) {
      if (args.length != 1) {
         System.err.println("Uso: java MatrizPolinomio <archivo>");
         System.exit(1);
      }

      try {
         LectorMatrizXML lector = new LectorMatrizXML(args[0]);
         int i;

         System.out.println("Default " + lector.getDefault());
         System.out.println("Renglones " + lector.getRenglones());
         System.out.println("Columnas " + lector.getColumnas());
         System.out.println("Tipo " + lector.getTipo());
         System.out.println("Entradas " + lector.getEntradas());

         for (i = 0; i < lector.getEntradas(); i++) {
            System.out.print(
                  "i= " + i + "\tRenglón: " + lector.getRenglon());
            System.out.print("\tColumna: " + lector.getColumna());
            System.out.println("\tContenido: " + lector.getEntrada());

            if (lector.haySiguiente()) {
               lector.siguienteEntrada();
            }
         }

         lector.reinicia();
         System.out.println();

         MatrizPolinomio matricilla = new MatrizPolinomio(
               lector.getRenglones(), lector.getColumnas(),
               lector.getDefault(), lector.getTipo());

         for (i = 0; i < lector.getEntradas(); i++) {
            matricilla.setEntrada(lector.getRenglon(),
                  lector.getColumna(), lector.getEntrada());

            if (lector.haySiguiente()) {
               lector.siguienteEntrada();
            }
         }

         matricilla.despliega();
      } catch (IndexOutOfBoundsException exio) {
         exio.printStackTrace();
         System.err.println("Se salió");
      } catch (Exception ex) {
         System.err.println("Oops!");
      }
   }
} // Fin de MatrizPolinomio.java
