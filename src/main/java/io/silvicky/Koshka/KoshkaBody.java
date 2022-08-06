package io.silvicky.Koshka;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;

import static java.lang.Math.random;

public class KoshkaBody extends JWindow {
    ImageIcon[] images;
    Clip[] clips;
    int windowWidth,windowHeight;
    int windowX,windowY;
    int vX,vY;
    int screenWidth,screenHeight;
    int imageCount;
    int clipCount;
    int curImage;
    int state;
    int delay;
    double delayRatio=1;
    Timer timer;
    JLabel imageLabel;
    String readLine(BufferedReader in) throws IOException {
        String tmp="#";
        while(tmp.startsWith("#"))tmp=in.readLine();
        return tmp;
    }
    void readImages() throws IOException {
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
            tx.rotate(Math.toRadians(rot),windowWidth/2, windowHeight/2);
            g.drawImage(ImageIO.read(getClass().getResourceAsStream("/images/"+fileName)),new AffineTransformOp(tx,AffineTransformOp.TYPE_NEAREST_NEIGHBOR),0,0);
            images[i]=new ImageIcon(result);
        }
    }
    void readAudio() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        BufferedReader in=new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/audio/audio.txt")));
        clipCount= Integer.parseInt(readLine(in));
        clips=new Clip[clipCount];
        for(int i=0;i<clipCount;i++)
        {
            clips[i]=AudioSystem.getClip();
            clips[i].open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/audio/"+readLine(in))));
        }
    }
    public KoshkaBody() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        setAlwaysOnTop(true);
        setBackground(new Color(0,0,0,0));
        readImages();
        readAudio();
        GraphicsConfiguration gc=getGraphicsConfiguration();
        Insets insets=getInsets();
        windowWidth=images[0].getIconWidth();
        windowHeight=images[0].getIconHeight();
        screenWidth= (int) gc.getBounds().getWidth();
        screenHeight= (int) gc.getBounds().getHeight();
        setSize(windowWidth,windowHeight);
        imageLabel=new JLabel();
        FormListener formListener=new FormListener();
        imageLabel.addMouseListener(formListener);
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
            try {
                migrate();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
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
    int ran()
    {
        return (int) (random()*1000);
    }
    class AudioListener implements LineListener {
        private boolean done = false;
        @Override public synchronized void update(LineEvent event) {
            LineEvent.Type eventType = event.getType();
            if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                done = true;
                notifyAll();
            }
        }
        public synchronized void waitUntilDone() throws InterruptedException {
            while (!done) { wait(); }
        }
    }
    void play(Clip c) throws InterruptedException {
        c.stop();
        c.setFramePosition(0);
        c.start();
    }
    void migrate() throws InterruptedException {
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
                    setDelay(500);
                    break;
                }
                if(ran()<10)play(clips[0]);
                break;
            case 2:
            case 3:
                curImage=5-curImage;
                windowY-=10;
                if(windowY>=0&&ran()<10)
                {
                    startFall(false);
                    break;
                }
                if(windowY<0)
                {
                    windowY=0;
                    startFall(false);
                    break;
                }
                if(ran()<10)play(clips[0]);
                break;
            case 4:
            case 5:
                curImage=9-curImage;
                windowY-=10;
                if(windowY>=0&&ran()<10)
                {
                    startFall(true);
                    break;
                }
                if(windowY<0)
                {
                    windowY=0;
                    startFall(true);
                    break;
                }
                if(ran()<10)play(clips[0]);
                break;
            case 6:
            case 7:
                curImage=13-curImage;
                windowX+=10;
                if(windowX>screenWidth-windowWidth)
                {
                    windowX=screenWidth-windowWidth;
                    curImage=4;
                    setDelay(500);
                    break;
                }
                if(ran()<10)play(clips[0]);
                break;
            case 16:
                windowX+=vX;
                windowY+=vY;
                vY+=10;
                if(windowY>screenHeight-windowHeight)
                {
                    windowY=screenHeight-windowHeight;
                    if(vX>0)curImage=6;
                    else curImage=0;
                    play(clips[2]);
                    setDelay(200);
                }
        }
    }
    class FormListener implements MouseListener {
        FormListener() {}
        public void mouseClicked(MouseEvent evt) {
            if (evt.getSource() == imageLabel) {
                try {
                    click();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void mouseEntered(MouseEvent evt) {
            if (evt.getSource() == imageLabel) {
                enter();
            }
        }

        public void mouseExited(MouseEvent evt) {
            if (evt.getSource() == imageLabel) {
                exit();
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
    private void startFall(boolean side) throws InterruptedException {
        curImage=16;
        delay=50;
        vX=side?-2:2;
        vY=10;
        play(clips[1]);
        setDelay(50);
    }
    void click() throws InterruptedException {
        switch (curImage)
        {
            case 2:
            case 3:
            case 10:
            case 11:
                startFall(false);
                break;
            case 4:
            case 5:
            case 12:
            case 13:
                startFall(true);
                break;
            default:
                play(clips[0]);
        }
    }
    void enter()
    {
        if(curImage>=0&&curImage<=7)
        {
            curImage+=8;
            delay=50;
        }
    }
    void exit()
    {
        if(curImage>=8&&curImage<=15)
        {
            curImage-=8;
            delay=200;
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
        KoshkaBody koshkaBody=new KoshkaBody();
    }
}
