package java_splash;
import java.awt.*;
import java.awt.SplashScreen;
import java.io.File;
import javax.swing.JOptionPane;

public final class ScreenSplash {

  final SplashScreen splash ;
  //texto que se muestra a medida que se va cargando el screensplah
  final String[] texto = {"configuration", "library",
                          "files XYZ","forms","iconos","properties",
                          "XML files", "X-files", "anonymous",
                          "database" ,"server","Conexion Establecida con GpBOX",
                          ""};

  public ScreenSplash() {
	 splash = SplashScreen.getSplashScreen();
  }

   public void animar()
   {
        if (splash != null)
        {
            Graphics2D g = splash.createGraphics();
            for(int i=1; i<texto.length; i++)
            {                       
                //se pinta texto del array
                g.setColor( new Color(4,52,101));//color de fondo
	        g.fillRect(203, 378,280,12);//para tapar texto anterior
                g.setColor(Color.white);//color de texto	        
                g.drawString("Loading "+texto[i-1]+"...", 203, 388);                
                g.setColor(Color.blue);//color de barra de progeso
                g.fillRect(204, 358,(i*307/texto.length), 12);//la barra de progreso
                //se pinta una linea segmentada encima de la barra verde
               // float dash1[] = {2.0f};
              //  BasicStroke dashed = new BasicStroke(9.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,5.0f, dash1, 0.0f);
                //g.setStroke(dashed);
                g.setColor(Color.blue);//color de barra de progeso
                g.setColor( new Color(4,52,101));
                g.drawLine(205,364, 510, 364);                
                //se actualiza todo
                splash.update();
		try {
		 Thread.sleep(321);
		} catch(InterruptedException e) { }
            }
	   splash.close();
	}
        //una vez terminada la animación se muestra la aplicación principal
        VentanaLogin n = new VentanaLogin();
                      n.setVisible(true); 
        
      
   }
    
}
