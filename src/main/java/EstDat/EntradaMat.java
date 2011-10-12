/* ----------------------------------------------------------------------
 * EntradaMat.java
 * version 3.0
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
 * Clase para manupilar entradas de matrices.
 * Cada Entrada posee sus propios indices de columna y
 * renglon, asi como su propio contenido.
 *
 * @author Jose Galaviz jgc@fciencias.unam.mx
 * @version 1.0
 * octubre 2003
 */
public class EntradaMat {
   private int idxren;
   private int idxcol;
   private double conte;

   /**
    * Construye una instancia a partir de sus datos.
    * @param ire es el indice del renglon.
    * @param ico es el indice de columna.
    * @param entr es el valor de la entrada de la matriz.
    */
   public EntradaMat(int ire, int ico, double entr) {
      idxren = ire;
      idxcol = ico;
      conte = entr;
   }

   /**
    * Regresa el indice del renglon.
    * @return el indice del renglon de la entrada de la
    * matriz.
    */
   public int getIdxRenglon() {
      return idxren;
   }

   /**
    * Regresa el indice de la columna.
    * @return el indice de la columna de la entrada de la
    * matriz.
    */
   public int getIdxColumna() {
      return idxcol;
   }

   /**
    * Regresa el contenido de la entrada.
    * @return contenido de la entrada de la matriz.
    */
   public double getContenido() {
      return conte;
   }

   /**
    * Indica si dos entradas poseen los mismos indices.
    * @return <code>true</code> si la instancia que llama y
    * el paramero dicen tener los mismos indices de columna
    * y renglon (i.e. son la misma entrada aunque tengan
    * diferente contenido).
    */
   public boolean colision(EntradaMat entr) {
      return ((idxren == entr.idxren) && (idxcol == entr.idxcol));
   }
} // Fin de EntradaMat.java
