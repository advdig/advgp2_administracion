package system.tray;
import conexion.conexion_facturacion;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
/**
 * 
 * 
 */
public class Tray_clave {

    private JFrame miframe;
    private PopupMenu popup = new PopupMenu();
    private Image image =new ImageIcon(getClass().getResource("/java_splash/03.jpg")).getImage() ;
    private final TrayIcon trayIcon = new TrayIcon(image, "Monitoreo Claves Factura", popup);
    //para el Timer
    private Timer timer;    
    private boolean band;

 public Tray_clave( JFrame frame)
 {
    this.miframe = frame;
    //comprueba si SystemTray es soportado en el sistema
    if (SystemTray.isSupported())
    {
      //obtiene instancia SystemTray
      SystemTray systemtray = SystemTray.getSystemTray();
      //acciones del raton sobre el icono en la barra de tareas
    timer = new Timer();           
           timer.schedule(new MyTimerTask(),0,9000 );
      MouseListener mouseListener = new MouseListener() {

        public void mouseClicked(MouseEvent evt) {            
            //Si se presiono el boton izquierdo y la aplicacion esta minimizada
            if( evt.getButton() == MouseEvent.BUTTON1 && miframe.getExtendedState()==JFrame.ICONIFIED )
                MensajeTrayIcon("Advantech Digital", MessageType.WARNING);
        }

        public void mouseEntered(MouseEvent evt) {}

        public void mouseExited(MouseEvent evt) {}

        public void mousePressed(MouseEvent evt) {}

        public void mouseReleased(MouseEvent evt) {}
    };

    //ACCIONES DEL MENU POPUP
    //Salir de aplicacion
    ActionListener exitListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {            
           
        }
    };
    //Restaurar aplicacion
    ActionListener RestaurarListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {            
            miframe.setVisible(true);
            miframe.setExtendedState( JFrame.NORMAL );
            miframe.repaint();
            band = true;
        }
    };
    //Se crean los Items del menu PopUp y se añaden
    

    MenuItem ItemRestaurar = new MenuItem("Restaurar");
    ItemRestaurar.addActionListener(RestaurarListener);
    popup.add(ItemRestaurar);
    trayIcon.setImageAutoSize(true);
    trayIcon.addMouseListener(mouseListener);

    //Añade el TrayIcon al SystemTray
    try {
        systemtray.add(trayIcon);
    } catch (AWTException e) {
        System.err.println( "Error:" + e.getMessage() );
    }
  } else {
     System.err.println( "Error: SystemTray no es soportado" );
  }

    //Cuando se minimiza JFrame, se oculta para que no aparesca en la barra de tareas
     miframe.addWindowListener(new WindowAdapter(){
        @Override
        public void windowIconified(WindowEvent e){
          miframe.setVisible(false);//Se oculta JFrame
           //Se inicia una tarea cuando se minimiza
           band = false;
         
        }
    });

    }

    //Muestra una burbuja con la accion que se realiza
    public void MensajeTrayIcon(String texto, MessageType tipo)
    {
        trayIcon.displayMessage("Alerta :", texto, tipo);
    }

    //clase interna que manejara una accion en segundo plano
    class MyTimerTask extends TimerTask {
        public void run() {
            
            if(band)//Termina Timer
                timer.cancel();
            else //realiza acción
                monitoreo();
        }
        //Una accion a realiza cuando la aplicacion a sido minimizada
       
        
        public void monitoreo()
        {
            try {
               int clave=0;
                conexion_facturacion he = new conexion_facturacion("root","manager");
                he.conectar();
                   Statement st_d = he.coneccion.createStatement();
                   ResultSet rid = st_d.executeQuery("SELECT count(*) FROM adv_facturacion.clave_acceso where estado=0 and tipo='contingencia';");


                   while (rid.next()) {
                   
                       clave=rid.getInt(1);
                   
                   
                   }
                    MensajeTrayIcon("Quedan Disponibles : "+clave + " claves para facturacion ", MessageType.INFO);
                   
                   
                    rid.close();
                    st_d.close();
                    he.coneccion.close();
                    
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tray_clave.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Tray_clave.class.getName()).log(Level.SEVERE, null, ex);
            }
             
             
             
             
   
        
        }

    }

}
