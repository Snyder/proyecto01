/* ----------------------------------------------------------------------
 * ManejadorMatrizXML.java
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

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

/**
 * Manejador SAX para matrices bidimensionales acordes a la
 * definicion de documento <code>matriz.dtd</code>. Las
 * instancias de esta clasepueden ser usadas como segundo
 * argumento en la llamada al metodo <code>parse</code> de
 * una instancia de <code>SAXParser</code>. </p>
 * Esta clase provee de servicios elementales para leer,
 * para utilizar servicios de alto nivel se puede utilizar
 * la clase <code>LectorMatrizXML</code>. Para hacer uso de
 * esta ultima el usuario no necesita saber nada acerca del
 * parser SAX.
 *
 * @see LectorMatrizXML
 *
 * @since 2.0
 * @author Jose Galaviz <jgc@fciencias.unam.mx>
 * @version 1.0<br>
 * octubre 2003
*/

public class ManejadorMatrizXML extends DefaultHandler {

   // cadenas que pueden aparecer en el archivo XML a
   // parsear.
   private static final String MATRIZSTR = "Matriz";
   private static final String RENGLOSTR = "renglon";
   private static final String COLUMNSTR = "columna";
   private static final String DESCRISTR = "descripcion";
   private static final String INDICESTR = "indice";
   private static final String NRENGSSTR = "renglones";
   private static final String NCOLUSSTR = "columnas";
   private static final String DEFAULSTR = "omision";
   private static final String TIPOMASTR = "tipo";

   // CADENAS DE TIPO
   // Tipos de matriz aceptados que pueden especificarse en
   // el archivo XML.
   /**
    * Triangular superior con diagonal
    */
   public static final String TRISD    = "Triangular Superior (c/diagonal)";
   /**
    * Triangular superior sin diagonal
    */
   public static final String TRISN    = "Triangular Superior (s/diagonal)";
   /**
    * Triangular inferior con diagonal
    */
   public static final String TRIID    = "Triangular Inferior (c/diagonal)";
   /**
    * Triangular inferior sin diagonal
    */
   public static final String TRIIN    = "Triangular Inferior (s/diagonal)";
   /**
    * Simetrica
    */
   public static final String SIMETRICA= "Simetrica";
   /**
    * Regular, es decir cualquier matriz.
    */
   public static final String REGULAR  = "Regular";

   // Variable para indicar que es lo que mas recientemente
   // se esta leyendo del archivo XML.
   private byte              whatisreading;
   // La variable "whatisreading" puede tener alguno de los
   // siguientes valores.
   private static final byte MATRIZbyte = 0;
   private static final byte RENGLObyte = 1;
   private static final byte COLUMNbyte = 2;
   private static final byte DESCRIbyte = 3;
   private static final byte INDICERbyte= 4;
   private static final byte INDICECbyte= 5;
   private static final byte NRENGSbyte = 6;
   private static final byte NCOLUSbyte = 7;
   private static final byte DEFAULbyte = 8;
   private static final byte TIPOMAbyte = 9;

   // Si ya se ha perseado el archivo XML ready es
   // verdadero, en otro caso es falso.
   private boolean       ready;

   // El lugar a donde se mandan todos los mensajes de error
   // y de avance.
   private static Writer salida;

   // Para almacenar las cadenas de contenido entre "tags".
   private String        chardata;

   // Contadores de renglones y de columnas,
   // respectivamente. Valores efectivos.
   private int           rcount, ccount;

   // Para almacenar el valor por omision.
   private double        valordefault;

   // Para almacenar el tipo de matriz. Las CADENAS DE TIPO
   // especificadas arriba son los valores posibles para
   // esta variable.
   private String        tipomatriz;

   // numero de renglones y de columnas especificados en el
   // archivo XML.
   private int           nrens, ncols;

   // indice de columna y de renglon de entrada
   // actual.
   private int           icol, iren;

   // Vector para almacenar los datos de la matriz. En la
   // i-esima posicion de cada uno de estos vectores se
   // almacenan:
   // 1. El indice del renglon.
   // 2. El indice de la columna y
   // 3. El valor de la entrada
   // de la i-esima entrada leida del archivo XML.
   private Vector<EntradaMat>        contenido;


/*
  ------------------------------------------------------------------------------
                        Metodos invocados por el usuario
  ------------------------------------------------------------------------------
 */

/**
 * Crea una instancia del manejador SAX para analizar
 * sintacticamente un archivo XML con una matriz
 * especificada segun el <i>Document Type Definition</i>
 * (DTD) <code>matriz.dtd</code>.
 * <b>Importante:</b> La instancia de
 * <code>SAXParserFactory</code> que se debe usar para la
 * creacion del <code>SAXParser</code> debe validar y estar
 * capacitada para identificar el espacio de nombres.
 * Es decir, debe ponerse <code>true</code> como parametro
 * de llamada en los metodos <code>setValidating</code> y
 * <code> setNamespaceAware</code>, antes de
 * <code>newSAXParser</code>.
 */
   public ManejadorMatrizXML ()
   {
      try {
         salida = new OutputStreamWriter(System.err, "UTF8");
      }
      catch (IOException ioe) {
         // I/O error
         ioe.printStackTrace();
      }

      rcount = ccount = 0;
      chardata  = new String();
      contenido = new Vector<EntradaMat>();
      ready = false;
   }

   /**
    * Regresa los datos leidos del archivo XML.
    * @return un arreglo de <code>EntradaMat</code>. En el
    * lugar i-esimo del arreglo se almacena la infirmacion
    * de renglon, columna y contenido de la i-esima entrada
    * de la matriz, leida del archivo XML.
    */
   public EntradaMat[] getEntradas ()
   {
      if (ready){
         EntradaMat[] datos = new EntradaMat[contenido.size()];
         int          i;

         for (i=0; i<contenido.size(); i++)
            datos[i] = (EntradaMat)contenido.elementAt(i);
         return datos;
      }
      else
         return null;
   }

   /**
    * Regresa el tipo de matriz leido.
    * @return una cadena especificando el tipo de matriz
    * especificado en el archivo XML. Si aun no se ha hecho
    * "parsing" regresa null.
    */
   public String getTipo ()
   {
      if (ready)
         return tipomatriz;
      else
         return null;
   }

   /**
    * Regresa el valor por omision declarado en el archivo
    * XML para las entradas no especificadas.
    * @return el valor por omision especificado en el
    * archivo XML. Si aun no se ha hecho
    * "parsing" regresa -1.
    */
   public double getDefault ()
   {
      if (ready)
         return valordefault;
      else
         return -1;
   }

   /**
    * Regresa el numero de renglones de la matriz,
    * especificado en el archivo XML.
    * @return el numero de renglones definido en el
    * archivo XML. Si aun no se ha hecho
    * "parsing" regresa -1.
    */
   public int getNRens ()
   {
      if (ready)
         return nrens;
      else
         return -1;
   }

   /**
    * Regresa el numero de columnas de la matriz,
    * especificado en el archivo XML.
    * @return el numero de columnas definido en el
    * archivo XML. Si aun no se ha hecho
    * "parsing" regresa -1.
    */
   public int getNCols ()
   {
      if (ready)
         return ncols;
      else
         return -1;
   }



/*
  ------------------------------------------------------------------------------
                       Metodos invocados por el parser.
  ------------------------------------------------------------------------------
 */

   /**
    * Establece el localizador de documento. Metodo usado
    * por el parser.
    */
    public void setDocumentLocator(Locator l)
    {
        try {
          salida.write("LOCATOR");
          salida.write("\n SYS ID: " + l.getSystemId() );
          nl();
          salida.flush();
        } catch (IOException e) {
        } catch (SAXException sxe) {
        }
    }

   /**
    * Maneja el evento de inicio de documento. Metodo usado
    * por el parser.
    */
    public void startDocument()
    throws SAXException
    {
        emit("Leyendo datos de archivo XML ...");
        nl();
    }

   /**
    * Maneja el evento de fin de documento. Metodo usado
    * por el parser.
    */
    public void endDocument()
    throws SAXException
    {
        ready = true;
        nl();
        emit("Lectura completa. "+rcount+" de "+nrens+" renglones leidos.");
        ready=true;
        try {
            nl();
            salida.flush();
        } catch (IOException e) {
            throw new SAXException("error de E/S", e);
        }
    }

   /**
    * Maneja los eventos de inicio de elemento. Es decir el
    * encuentro con un "tag" de inicio. Metodo usado por el
    * parser.
    */
    public void startElement(String namespaceURI,
                             String lName, // local name
                             String qName, // qualified name
                             Attributes attrs)
    throws SAXException
    {
        String eName = lName; // nombre del elemento
        if ("".equals(eName)) eName = qName; // namespaceAware = false
        // inicia Matriz
        if (eName.equals(MATRIZSTR))      {
           emit ("Inicia matriz.");
           nl();
           whatisreading=MATRIZbyte;
           if (attrs != null) {
              nrens = Integer.parseInt(attrs.getValue(NRENGSSTR));
              ncols = Integer.parseInt(attrs.getValue(NCOLUSSTR));
              if (attrs.getValue(DEFAULSTR) != null)
                 valordefault = Double.parseDouble(attrs.getValue(DEFAULSTR));
              else valordefault = 0.0;
              if (attrs.getValue(TIPOMASTR) != null) {
                 if (attrs.getValue(TIPOMASTR).equalsIgnoreCase("TRSD"))
                    tipomatriz = new String(TRISD);
                 else if (attrs.getValue(TIPOMASTR).equalsIgnoreCase("TRSN"))
                    tipomatriz = new String(TRISN);
                 else if (attrs.getValue(TIPOMASTR).equalsIgnoreCase("TRID"))
                    tipomatriz = new String(TRIID);
                 else if (attrs.getValue(TIPOMASTR).equalsIgnoreCase("TRIN"))
                    tipomatriz = new String(TRIIN);
                 else if (attrs.getValue(TIPOMASTR).equalsIgnoreCase("SIM"))
                    tipomatriz = new String(SIMETRICA);
                 else
                    tipomatriz = new String(REGULAR);
              }
              else
                 tipomatriz = new String(REGULAR);
              emit ("Renglones: "+nrens);
              nl();
              emit ("Columnas: "+ncols);
              nl();
              emit ("Valor por omision: "+valordefault);
              nl();
              emit ("Tipo de matriz: "+tipomatriz);
              nl();
           }
           else throw new SAXException ("Faltan atributos obligatorios");
        //   inicia un renglon
        } else if (eName.equals(RENGLOSTR))   {
           whatisreading=RENGLObyte;
           iren = Integer.parseInt(attrs.getValue(0));
           rcount++;
        //    inicia una columna
        } else if (eName.equals(COLUMNSTR))   {
           whatisreading = COLUMNbyte;
           icol = Integer.parseInt(attrs.getValue(0));
           ccount++;
        //    inicia la descripcion opcional
        } else if (eName.equals(DESCRISTR))
           whatisreading = DESCRIbyte;
    }

   /**
    * Maneja los eventos de fin de elemento. Es decir el
    * encuentro con un "tag" de termino. Metodo usado por el
    * parser.
    */
    public void endElement(String namespaceURI,
                           String sName, // simple name
                           String qName  // qualified name
                          )
       throws SAXException
    {
       int  i, j;
       if (sName.equals(COLUMNSTR)) { // fin de columna
          // anadir el elemento a los vectores
          // se forza a que la matriz sea correcta de
          // acuerdo con el tipo declarado en el archivo
          // XML. Las simetricas se tratan como triangulares
          // inferiores con diagonal.
          /*
          if ( ((tipomatriz.equals(TRISD)) && (iren >  icol)) ||
               ((tipomatriz.equals(TRISN)) && (iren >= icol)) ||
               ((tipomatriz.equals(TRIID)) && (iren <  icol)) ||
               ((tipomatriz.equals(TRIIN)) && (iren <= icol)) ||
               ((tipomatriz.equals(SIMETRICA)) && (iren <  icol)))
             contenido.add(new EntradaMat(icol,iren,
                                          Double.parseDouble(chardata)));
          */
          if ( ((tipomatriz.equals(TRISD) ||
                 tipomatriz.equals(TRISN))     && (iren >  icol)) ||
               ((tipomatriz.equals(TRIID) ||
                 tipomatriz.equals(TRIIN) ||
                 tipomatriz.equals(SIMETRICA)) && (iren <  icol)) )
             contenido.add(new EntradaMat(iren,icol,
                                          Double.parseDouble(chardata)));

          else if ( tipomatriz.equals(REGULAR)   ||
                    tipomatriz.equals(SIMETRICA) ||
                    tipomatriz.equals(TRISD)     ||
                    tipomatriz.equals(TRIID)     ||
                    (iren != icol)
                  )
             contenido.add(new EntradaMat(iren,icol,
                                          Double.parseDouble(chardata)));
             // se limpia la cadena de lectura
             chardata = new String();
       } else if (sName.equals(MATRIZSTR)) {// fin de Matriz
          // se recorre el contenido buscando entradas
          // duplicadas para eliminarlas.
          for (i=0; i<contenido.size()-1; i++){
             for (j=i+1; j<contenido.size(); j++){
                if (((EntradaMat)contenido.elementAt(i)).colision((EntradaMat)contenido.elementAt(j))){
                   contenido.removeElementAt(j);
                   // como los indices se recorren hay que
                   // decrementar j para que el for, al
                   // incrementar haga que se compare en el
                   // mismo lugar que se acaba de eliminar
                   j--;
                }
             }
          }
          // se recorta el vector para que tenga el tamano
          // justo necesario
          contenido.trimToSize();
          // se indica que se ha terminado
          ready = true;
       }
    }

   /**
    * encuentro con cadenas de caracteres (cualquier cosa
    * entre "tags").
    */
    public void characters(char buf[], int offset, int len)
    throws SAXException
    {
       String s = new String(buf, offset, len);
       // Lo que sea leido se acumula hasta encontrar un
       // "tag" de finalizacion.
       chardata = chardata.concat(s);
    }

   /**
    * Manejo de espacios en blanco
    */
    public void ignorableWhitespace(char buf[], int offset, int len)
    throws SAXException
    {
        // se ignoran
    }

   /**
    * Metodo vacio.
    */
    public void processingInstruction(String target, String data)
    throws SAXException
    {
    }

    // Manejo de errores

    /**
     * Errores de validacion.
     */
    public void error(SAXParseException e)
    throws SAXParseException
    {
        throw e;
    }

   /**
    * "Warnings".
    */
    public void warning(SAXParseException err)
    throws SAXParseException
    {
        System.out.println("** Warning"
            + ", line " + err.getLineNumber()
            + ", uri " + err.getSystemId());
        System.out.println("   " + err.getMessage());
    }


/*
  ------------------------------------------------------------------------------
                       Metodos privados.
  ------------------------------------------------------------------------------
 */
   /**
    * Metodo privado. Solo para emitir cadenas a la salida
    * indicada.
    */
    private void emit(String s)
    throws SAXException
    {
        try {
            salida.write(s);
            salida.flush();
        } catch (IOException e) {
            throw new SAXException("error de E/S", e);
        }
    }


   /**
    * Metodo privado. Solo para emitir cambio de linea en la
    * salida indicada.
    */
    private void nl()
    throws SAXException
    {
        String lineEnd =  System.getProperty("line.separator");
        try {
            salida.write(lineEnd);
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

/*
  ------------------------------------------------------------------------------
                         Programa de ejemplo.
  ------------------------------------------------------------------------------
 */

   public static void main (String[] argv)
   {
      int         i;
      EntradaMat[]  loleido;

      if (argv.length != 1) {
         System.err.println("Uso: java ManejadorMatrizXML <archivo>");
         System.exit(1);
      }

      ManejadorMatrizXML handler = new ManejadorMatrizXML();
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setValidating(true);
      factory.setNamespaceAware(true);
      try {
         SAXParser saxParser = factory.newSAXParser();
         saxParser.parse( new File(argv[0]), handler);
         loleido = handler.getEntradas();
         for (i=0; i<loleido.length; i++){
            System.out.print("ren: "+loleido[i].getIdxRenglon());
            System.out.print("\tcol: "+loleido[i].getIdxColumna());
            System.out.println("\tE: "+loleido[i].getContenido());
         }
      } catch (SAXParseException spe) {
         System.out.println("\n** Parsing error"
                            + ", line " + spe.getLineNumber()
                            + ", uri " + spe.getSystemId());
         System.out.println("   " + spe.getMessage() );
         Exception  x = spe;
         if (spe.getException() != null)
            x = spe.getException();
         x.printStackTrace();
      } catch (SAXException sxe) {
         Exception  x = sxe;
         if (sxe.getException() != null)
            x = sxe.getException();
         x.printStackTrace();
      } catch (ParserConfigurationException pce) {
         pce.printStackTrace();
      } catch (IOException ioe) {
         ioe.printStackTrace();
      }
      System.exit(0);
   }

} // ManejadorMatrizXML termina aqui.
