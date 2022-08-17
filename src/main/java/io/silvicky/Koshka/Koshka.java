package io.silvicky.Koshka;

import io.silvicky.String.ExpressionErr;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;

import static java.lang.Math.random;

public class Koshka extends KoshkaTemplate{
    int vX,vY;
    int lastFrame;
    int framesInThisState;
    public Koshka(GraphicsConfiguration gc) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        setGender();
        init(gc);
    }
    public Koshka(GraphicsConfiguration gc,String val) throws IOException, UnsupportedAudioFileException, LineUnavailableException, ExpressionErr {
        setGender(val);
        init(gc);
    }
    int ran()
    {
        return (int) (random()*10000);
    }
    public void migrate() {
        int sran;
        switch(curImage) {
            case 0:
            case 1:
                curImage = 1 - curImage;
                windowX -= 10;
                if (windowX < minX) {
                    windowX = minX;
                    if (ran() < 2000) {
                        curImage = 2;
                        setDelay(500);
                    } else curImage = 6;
                    break;
                }
                if (ran() < 100) play(clips[0]);
                sran=ran();
                if (sran < 1)
                {
                    curImage = 17;
                    lastFrame=0;
                    framesInThisState=0;
                    setDelay(500);
                    break;
                }
                else if(sran<20)
                {
                    curImage = 19;
                    lastFrame=0;
                    framesInThisState=0;
                    setDelay(2000);
                    break;
                }
                else if(sran<40)
                {
                    curImage=29;
                    lastFrame=0;
                    framesInThisState=0;
                    setDelay(2000);
                    break;
                }
                break;
            case 2:
            case 3:
                curImage = 5 - curImage;
                windowY -= 10;
                if (windowY >= minY && ran() < 100) {
                    startFall(false);
                    break;
                }
                if (windowY < minY) {
                    windowY = minY;
                    startFall(false);
                    break;
                }
                if (ran() < 100) play(clips[0]);
                break;
            case 4:
            case 5:
                curImage = 9 - curImage;
                windowY -= 10;
                if (windowY >= minY && ran() < 100) {
                    startFall(true);
                    break;
                }
                if (windowY < minY) {
                    windowY = minY;
                    startFall(true);
                    break;
                }
                if (ran() < 100) play(clips[0]);
                break;
            case 6:
            case 7:
                curImage = 13 - curImage;
                windowX += 10;
                if (windowX > maxX) {
                    windowX = maxX;
                    if (ran() < 2000) {
                        curImage = 4;
                        setDelay(500);
                    } else curImage = 0;
                    break;
                }
                if (ran() < 100) play(clips[0]);
                sran=ran();
                if (sran < 1)
                {
                    curImage = 17;
                    lastFrame=6;
                    framesInThisState=0;
                    setDelay(500);
                }
                else if(sran<20)
                {
                    curImage = 22;
                    lastFrame=6;
                    framesInThisState=0;
                    setDelay(2000);
                    break;
                }
                else if(sran<40)
                {
                    curImage=30;
                    lastFrame=6;
                    framesInThisState=0;
                    setDelay(2000);
                    break;
                }
                break;
            case 16:
                windowX+=vX;
                windowY+=vY;
                vY+=10;
                if(windowY>maxY)
                {
                    windowY=maxY;
                    if(vX>0)curImage=6;
                    else curImage=0;
                    play(clips[2]);
                    setDelay(200);
                }
                break;
            case 17:
                play(clips[0]);
            case 18:
                curImage=35-curImage;
                framesInThisState++;
                if(framesInThisState>=20)
                {
                    curImage=lastFrame;
                    setDelay(200);
                }
                break;
            case 19:
            case 20:
                curImage=39-curImage;
                framesInThisState++;
                if(framesInThisState>=10)
                {
                    curImage=21;
                    break;
                }
                break;
            case 21:
                curImage=0;
                setDelay(200);
                break;
            case 22:
            case 23:
                curImage=45-curImage;
                framesInThisState++;
                if(framesInThisState>=10)
                {
                    curImage=24;
                    break;
                }
                break;
            case 24:
                curImage=6;
                setDelay(200);
                break;
            case 25:
            case 26:
                if(ran()<1000)play(clips[3]);
                curImage=51-curImage;
                framesInThisState++;
                if(framesInThisState>=15)
                {
                    lastFrame=25;
                    curImage=29;
                    break;
                }
                break;
            case 27:
            case 28:
                if(ran()<1000)play(clips[3]);
                curImage=55-curImage;
                framesInThisState++;
                if(framesInThisState>=15)
                {
                    lastFrame=27;
                    curImage=30;
                    break;
                }
                break;
            case 29:
                if(lastFrame==0)curImage=25;
                else
                {
                    curImage=0;
                    setDelay(200);
                }
                break;
            case 30:
                if(lastFrame==6)curImage=27;
                else
                {
                    curImage=6;
                    setDelay(200);
                }
                break;
        }
    }
    void startFall(boolean side) {
        curImage=16;
        vX=side?-2:2;
        vY=10;
        play(clips[1]);
        setDelay(50);
    }
    public void mouseClick(){
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
            case 25:
            case 26:
                curImage=0;
                setDelay(200);
                break;
            case 27:
            case 28:
                curImage=6;
                setDelay(200);
                break;
            default:
                play(clips[0]);
        }
    }
    public void mouseEnter()
    {
        if(curImage>=0&&curImage<=7)
        {
            curImage+=8;
            setDelay(50);
        }
    }
    public void mouseExit()
    {
        if(curImage>=8&&curImage<=15)
        {
            curImage-=8;
            setDelay(200);
        }
    }
}
