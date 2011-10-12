/* ----------------------------------------------------------------------
 * Matriz.java
 * version 1.0
 * Copyright (C) 2004  Jose Galaviz Casas,
 * Facultad de Ciencias,
 * Universidad Nacional Autonoma de Mexico, Mexico.
 *
 * Este programa es software libre; se puede redistribuir
 * y/o modificar en los terminos establecidos por la
 * Licencia Publica General de GNU tal como fue publicada
 * por la Free Software Foundation en la version 2 o
 * superior.
 *
 * Este programa es distribuido con la esperanza de que
 * resulte de utilidad, pero SIN GARANTIA ALGUNA; de hecho
 * sin la garantia implicita de COMERCIALIZACION o
 * ADECUACION PARA PROPOSITOS PARTICULARES. Vease la
 * Licencia Publica General de GNU para mayores detalles.
 *
 * Con este programa se debe haber recibido una copia de la
 * Licencia Publica General de GNU, de no ser asi, visite el
 * siguiente URL:
 * http://www.gnu.org/licenses/gpl.html
 * o escriba a la Free Software Foundation Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * ----------------------------------------------------------------------
 */


package EstDat;

/**
 * Se definen las firmas de los
 * metodos asociados con la manipulacion de matrices
 * numericas. Se suponen entradas reales en las matrices.
 *
 * @since 2.0
 * @author Jose Galaviz <jgc@fciencias.unam.mx>
 * @version 1.0<br>
 * octubre 2003
 */

public interface Matriz {

   /**
    * Crea una matriz de dimensiones y tipo dados.
    * @param ren numero de renglones en la matriz.
    * @param col numero de columnas en la matriz.
    * @param valdefault valor en el que se establecen las
    * entradas de la matriz no establecidas explicitamente
    * con el metodo <code>setEntrada</code>.
    * @param tipo es una cadena que identifica el tipo de
    * matriz. Los valores posibles estan especificados en la
    * clase <code>ManejadorMatrizXML</code>:
    * <UL>
    * <LI><code>ManejadorMatrizXML.TRISD</code>: Triangular superior con diagonal.
    * <LI><code>ManejadorMatrizXML.TRISN</code>: Triangular superior sin diagonal.
    * <LI><code>ManejadorMatrizXML.TRIID</code>: Triangular inferior con diagonal.
    * <LI><code>ManejadorMatrizXML.TRIIN</code>: Triangular inferior sin diagonal.
    * <LI><code>ManejadorMatrizXML.SIMETRICA</code>: Simetrica
    * <LI><code>ManejadorMatrizXML.REGULAR</code>: Regular (cualquiera)
    * </UL>
    */
   public void creaMatriz (int ren, int col, double valdefault, String tipo);

   /**
    * Establece la entrada indicada de la matriz.
    * @param ren indice del renglon de la entrada a
    * establecer.
    * @param col indice de la columna de la entrada a
    * establecer.
    * @param valor valor a colocar en la entrada.
    * @throws IndexOutOfBoundsException si los valores de
    * indice de renglon o columna son erroneos de acuerdo
    * con las dimensiones establecidas para la matriz y/o de
    * acuerdo con el tipo de matriz especificado (por
    * ejemplo, si el tipo es triangular inferior sin
    * diagonal entonces debe cumplirse que <br><code>ren</code>>
    * > <code>col</code>).<br> Si la matriz es simetrica se
    * tratara como triangular inferior con diagonal.
    */
   public void   setEntrada   (int ren, int col, double valor)
      throws IndexOutOfBoundsException;

   /**
    * Regresa la cadena con el tipo de matriz. Vease el
    * metodo constructor para los posibles valores.
    * @return una cadena con el tipo de matriz de acuerdo
    * con la clase <code>ManejadorMatrizXML</code>.
    */
   public String getTipo      ();

   /**
    * Regresa el valor por omision, aquel que se encuentra
    * en las entradas no establecidas explicitamente.
    * @return el valor por omision.
    */
   public double getDefault   ();

   /**
    * Regresa el numero de renglones en la matriz.
    * @return un entero no negativo con el numero de
    * renglones de la matriz.
    */
   public int    getRenglones ();

   /**
    * Regresa el numero de columnas en la matriz.
    * @return un entero no negativo con el numero de
    * columnas de la matriz.
    */
   public int    getColumnas  ();

   /**
    * Regresa el valor almacenado en una entrada de la
    * matriz.
    * @param ren es el indide del renglon de la entrada
    * requerida.
    * @param col es el indide de la columna de la entrada
    * requerida.
    * @return el valor almacenado en la entrada
    * especificada.
    * @throws IndexOutOfBoundsException si los indices
    * especificados son incorrectos. En este caso solo se
    * considera que no rebasen las dimansiones especificadas
    * para la matriz.
    */
   public double getEntrada   (int ren, int col)
      throws IndexOutOfBoundsException;

   /**
    * Despliega la matriz en la salida estandar.
    * @throws IndexOutOfBoundsException
    */
   public void despliega ()
      throws IndexOutOfBoundsException;
}// Fin de Matriz.java
