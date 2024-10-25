package vista;

import javax.swing.*;

public class IconsResource {

    private static final ImageIcon iconShow = new ImageIcon(IconsResource.class.getResource("/eye_open.png"));
    private static final ImageIcon iconHide = new ImageIcon(IconsResource.class.getResource("/eye_closed.png"));

    public static ImageIcon getIconShow() {
        return iconShow;
    }

    public static ImageIcon getIconHide() {
        return iconHide;
    }
    

    
    
}
