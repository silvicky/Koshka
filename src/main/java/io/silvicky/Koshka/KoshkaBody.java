package io.silvicky.Koshka;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KoshkaBody extends JWindow {
    List<ImageIcon> images;
    int windowWidth,windowHeight;
    int windowX,windowY;
    int screenWidth,screenHeight;
    int imageCount;
    int curImage;
    Timer timer;
    JLabel imageLabel;
    public KoshkaBody() {
        setAlwaysOnTop(true);
        setBackground(new Color(0,0,0,0));
        images=new ArrayList<>();
        GraphicsConfiguration gc=getGraphicsConfiguration();
        Insets insets=getInsets();
        windowWidth=images.get(0).getIconWidth();
        windowHeight=images.get(0).getIconHeight();
        screenWidth= (int) gc.getBounds().getWidth();
        screenHeight= (int) gc.getBounds().getHeight();
        setSize(windowWidth,windowHeight);
        imageLabel=new JLabel();
        imageLabel.addMouseListener(new FormListener());
        getContentPane().add(imageLabel, BorderLayout.CENTER);
        imageLabel.setSize(windowWidth,windowHeight);
        imageLabel.setBackground(new Color(0,0,0,0));
        imageLabel.setVisible(true);
        imageLabel.setOpaque(true);
        windowX=100;
        windowY=100;
        setVisible(true);
        timer = new Timer(20, e ->
        {
            setLocation(windowX,windowY);
            imageLabel.setIcon(images.get(curImage));
            migrate();
        });
        timer.setRepeats(true);
        timer.start();
    }
    void migrate()
    {

    }
    void imageClicked() {

    }
    class FormListener implements MouseListener {
        FormListener() {}
        public void mouseClicked(MouseEvent evt) {
            if (evt.getSource() == imageLabel) {
                KoshkaBody.this.imageClicked();
            }
        }

        public void mouseEntered(MouseEvent evt) {
        }

        public void mouseExited(MouseEvent evt) {
        }

        public void mousePressed(MouseEvent evt) {
        }

        public void mouseReleased(MouseEvent evt) {
        }
    }
    public static void main(String[] args) throws IOException {
        KoshkaBody koshkaBody=new KoshkaBody();
    }
}
