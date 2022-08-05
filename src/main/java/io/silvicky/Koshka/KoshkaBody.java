package io.silvicky.Koshka;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class KoshkaBody extends JWindow {
    ImageIcon[] images;
    int windowWidth,windowHeight;
    int windowX,windowY;
    int screenWidth,screenHeight;
    int imageCount;
    int curImage;
    int state;
    int delay;
    double delayRatio=10;
    final String imagesCfg="./target/classes/images/images.txt";
    final String curDir="./target/classes/images/";
    Timer timer;
    JLabel imageLabel;
    void readImages() throws IOException {
        BufferedReader in=new BufferedReader(new FileReader(imagesCfg));
        imageCount=Integer.parseInt(in.readLine());
        windowWidth=Integer.parseInt(in.readLine());
        windowHeight=Integer.parseInt(in.readLine());
        images=new ImageIcon[imageCount];
        int rot;
        boolean isFlipped;
        String fileName;
        for(int i=0;i<imageCount;i++)
        {
            fileName=in.readLine();
            rot=Integer.parseInt(in.readLine());
            isFlipped=Boolean.parseBoolean(in.readLine());
            BufferedImage result=new BufferedImage(windowWidth,windowHeight,2);
            Graphics2D g=GraphicsEnvironment.getLocalGraphicsEnvironment().createGraphics(result);
            AffineTransform tx=AffineTransform.getScaleInstance(isFlipped?-1:1,1);
            if(isFlipped)tx.translate(-windowWidth,0);
            tx.rotate(Math.toRadians(rot),windowWidth/2, windowHeight/2);
            g.drawImage(ImageIO.read(new File(curDir+fileName)),new AffineTransformOp(tx,AffineTransformOp.TYPE_NEAREST_NEIGHBOR),0,0);
            images[i]=new ImageIcon(result);
        }
    }
    public KoshkaBody() throws IOException {
        setAlwaysOnTop(true);
        setBackground(new Color(0,0,0,0));
        readImages();
        GraphicsConfiguration gc=getGraphicsConfiguration();
        Insets insets=getInsets();
        windowWidth=images[0].getIconWidth();
        windowHeight=images[0].getIconHeight();
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
        windowX=screenWidth-windowWidth;
        windowY=screenHeight-windowHeight;
        curImage=0;
        delay=200;
        setVisible(true);
        timer = new Timer((int) (200/delayRatio), e ->
        {
            setLocation(windowX,windowY);
            imageLabel.setIcon(images[curImage]);
            repaint();
            migrate();
        });
        timer.setRepeats(true);
        timer.start();
    }
    void setDelay(int ms)
    {
        timer.setDelay((int) (ms/delayRatio));
    }
    void migrate()
    {
        switch(curImage)
        {
            case 0:
            case 1:
                curImage=1-curImage;
                windowX-=10;
                if(windowX<0)
                {
                    windowX=0;
                    curImage=2;
                    setDelay(1000);
                }
                break;
            case 2:
            case 3:
                curImage=5-curImage;
                windowY-=10;
                if(windowY<0)
                {
                    windowY=screenHeight-windowHeight;
                    curImage=6;
                    setDelay(200);
                }
                break;
            case 4:
            case 5:
                curImage=9-curImage;
                windowY-=10;
                if(windowY<0)
                {
                    windowY=screenHeight-windowHeight;
                    curImage=0;
                    setDelay(200);
                }
                break;
            case 6:
            case 7:
                curImage=13-curImage;
                windowX+=10;
                if(windowX+windowWidth>screenWidth)
                {
                    windowX=screenWidth-windowWidth;
                    curImage=4;
                    setDelay(1000);
                }
                break;
        }
    }
    void imageClicked() {
        System.exit(0);
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
    public static void main(String[] args) throws IOException, InterruptedException {
        KoshkaBody koshkaBody=new KoshkaBody();
    }
}
