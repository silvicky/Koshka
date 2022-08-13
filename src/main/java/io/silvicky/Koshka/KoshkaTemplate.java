package io.silvicky.Koshka;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class KoshkaTemplate extends JWindow{
    ImageIcon[] images;
    Clip[] clips;
    int windowWidth,windowHeight;
    int windowX,windowY;
    int minX,minY,maxX,maxY;
    int curImage;
    double delayRatio=1;
    Timer timer;
    JLabel imageLabel;
    String readLine(BufferedReader in) throws IOException {
        String tmp="#";
        while(tmp.startsWith("#"))tmp=in.readLine();
        return tmp;
    }
    void readImages() throws IOException {
        int imageCount;
        BufferedReader in=new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/images/images.txt")));
        imageCount=Integer.parseInt(readLine(in));
        windowWidth=Integer.parseInt(readLine(in));
        windowHeight=Integer.parseInt(readLine(in));
        images=new ImageIcon[imageCount];
        int rot;
        boolean isFlipped;
        String fileName;
        for(int i=0;i<imageCount;i++)
        {
            fileName=readLine(in);
            rot=Integer.parseInt(readLine(in));
            isFlipped=Boolean.parseBoolean(readLine(in));
            BufferedImage result=new BufferedImage(windowWidth,windowHeight,2);
            Graphics2D g=GraphicsEnvironment.getLocalGraphicsEnvironment().createGraphics(result);
            AffineTransform tx=AffineTransform.getScaleInstance(isFlipped?-1:1,1);
            if(isFlipped)tx.translate(-windowWidth,0);
            tx.rotate(Math.toRadians(rot),(double)(windowWidth)/2, (double)(windowHeight)/2);
            g.drawImage(ImageIO.read(getClass().getResourceAsStream("/images/"+fileName)),new AffineTransformOp(tx,AffineTransformOp.TYPE_NEAREST_NEIGHBOR),0,0);
            images[i]=new ImageIcon(result);
        }
    }
    void readAudio() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        int clipCount;
        BufferedReader in=new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/audio/audio.txt")));
        clipCount= Integer.parseInt(readLine(in));
        clips=new Clip[clipCount];
        for(int i=0;i<clipCount;i++)
        {
            clips[i]= AudioSystem.getClip();
            clips[i].open(AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream("/audio/"+readLine(in)))));
        }
    }
    void init(GraphicsConfiguration gc) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        setAlwaysOnTop(true);
        setBackground(new Color(0,0,0,0));
        readImages();
        readAudio();
        Insets insets=Toolkit.getDefaultToolkit().getScreenInsets(gc);
        setSize(windowWidth,windowHeight);
        minX=(int)gc.getBounds().getMinX()+insets.left;
        minY=(int)gc.getBounds().getMinY()+insets.top;
        maxX=(int)gc.getBounds().getMaxX()-windowWidth-insets.right;
        maxY=(int)gc.getBounds().getMaxY()-windowHeight-insets.bottom;
        imageLabel=new JLabel();
        KoshkaTemplate.FormListener formListener=new KoshkaTemplate.FormListener();
        imageLabel.addMouseListener(formListener);
        getContentPane().add(imageLabel, BorderLayout.CENTER);
        imageLabel.setSize(windowWidth,windowHeight);
        imageLabel.setBackground(new Color(0,0,0,0));
        imageLabel.setVisible(true);
        imageLabel.setOpaque(true);
        windowX=maxX;
        windowY=maxY;
        curImage=0;
        setVisible(true);
        timer = new Timer((int) (200/delayRatio), e ->
        {
            migrate();
            setLocation(windowX,windowY);
            imageLabel.setIcon(images[curImage]);
            setSize(windowWidth,windowHeight+1);
            repaint();
            setSize(windowWidth,windowHeight);
            repaint();
        });
        timer.setRepeats(true);
        timer.start();
    }
    void setDelay(int ms)
    {
        timer.setDelay((int) (ms/delayRatio));
    }
    void play(Clip c) {
        c.stop();
        c.setFramePosition(0);
        c.start();
    }
    abstract void migrate();
    class FormListener implements MouseListener {
        FormListener() {}
        public void mouseClicked(MouseEvent evt) {
            if (evt.getSource() == imageLabel) {
                mouseClick();
            }
        }

        public void mouseEntered(MouseEvent evt) {
            if (evt.getSource() == imageLabel) {
                mouseEnter();
            }
        }

        public void mouseExited(MouseEvent evt) {
            if (evt.getSource() == imageLabel) {
                mouseExit();
            }
        }

        public void mousePressed(MouseEvent evt) {
            if (evt.getSource() == imageLabel) {

            }
        }

        public void mouseReleased(MouseEvent evt) {
            if (evt.getSource() == imageLabel) {

            }
        }
    }
    abstract void mouseClick();
    abstract void mouseEnter();
    abstract void mouseExit();
    void exit()
    {
        timer.stop();
        dispose();
    }
}
