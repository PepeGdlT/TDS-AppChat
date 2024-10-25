package vista;

import javax.swing.*;

public class IconsResource {

    private static final ImageIcon iconShow = new ImageIcon(IconsResource.class.getResource("/eye_open.png"));
    private static final ImageIcon iconHide = new ImageIcon(IconsResource.class.getResource("/eye_closed.png"));
    private static final ImageIcon avatar = new ImageIcon(IconsResource.class.getResource("/avatar.png"));

    public static ImageIcon getIconShow() {
        return iconShow;
    }

    public static ImageIcon getIconHide() {
        return iconHide;
    }
    
    public static ImageIcon getAvatar() {
        return avatar;
    }
    
    

    
    
}
