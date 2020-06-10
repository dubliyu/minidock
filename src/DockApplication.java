import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import sun.awt.image.OffScreenImageSource;
import sun.awt.image.ToolkitImage;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DockApplication{

    String name;
    String exe;
    String accent;
    Process me;
    Image ico;
    Boolean UseGenericImage;

    public DockApplication(String ApplicationName, String ExecutablePath){
        name = ApplicationName;
        exe = ExecutablePath;

        SetIcon();
    }

    private void SetIcon(){
        File f = new File(exe);

        sun.awt.shell.ShellFolder sf = null;
        try {
            if(f.exists() && f.isFile()){
                sf = sun.awt.shell.ShellFolder.getShellFolder(f);
                java.awt.Image i = sf.getIcon(true);
                ico = SwingFXUtils.toFXImage((BufferedImage) i, null);
                GenerateAccent(i);
                UseGenericImage = false;
            }
            else{
                UseGenericImage = true;
            }
        } catch (FileNotFoundException e) {
            UseGenericImage = true;
        }
    }

    private void GenerateAccent(java.awt.Image i){
        java.awt.Image scaled = i.getScaledInstance(1,1,  java.awt.Image.SCALE_AREA_AVERAGING );
        BufferedImage buffered = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        buffered.getGraphics().drawImage(scaled, 0, 0 , null);

        int clr = buffered.getRGB(0, 0);
        int red =   (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue =   clr & 0x000000ff;
        accent = "rgba("+red+","+green+","+blue+",1)";
    }

    public boolean Run(){
        Runtime rt = Runtime.getRuntime();

        try{
            me = rt.exec(exe);
            return  true;
        }
        catch(IOException e){
            System.out.println("The following Application failed to launch: " + name);
            return false;
        }
    }

    public boolean IsAlive(){
        return me.isAlive();
    }

    public void Close(){
        me.destroy();
    }
}
