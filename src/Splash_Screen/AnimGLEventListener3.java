package Splash_Screen;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;

public class AnimGLEventListener3 extends AnimListener {

    private long lastBulletFired = 0;
    private long fireRate = 500;
    private float playerSpeed = 0.5f;

    enum Directions{
        up,
        down,
        left,
        right,
        up_left,
        up_right,
        down_left,
        down_right
    }
    Directions direction = Directions.up;
    int animationIndex = 0;
    int maxWidth = 100;
    int maxHeight = 100;
    float x = maxWidth/2, y = maxHeight/2;
    
    ArrayList<SplashScreen> bullets = new ArrayList<>();
    
    String textureNames[] = {"back.png", "rabbit.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    /*
     5 means gun in array pos
     x and y coordinate for gun 
     */
    public void init(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);	
        gl.glGenTextures(textureNames.length, textures, 0);
        
        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                    GL.GL_TEXTURE_2D,
                    GL.GL_RGBA, // Internal Texel Format,
                    texture[i].getWidth(), texture[i].getHeight(),
                    GL.GL_RGBA, // External format from image,
                    GL.GL_UNSIGNED_BYTE,
                    texture[i].getPixels() // Imagedata
                    );
            } catch( IOException e ) {
              System.out.println(e);
              e.printStackTrace();
            }
        }
    }
    
    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        handleKeyPress();
        animationIndex = animationIndex % 4;
        
        
//        DrawGraph(gl);
        DrawBackground(gl, 0);
        SplashScreen splashScreen = new SplashScreen(10000);
        splashScreen.invalidate();
        if(!splashScreen.fired){
            DrawBackground(gl, 0);
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    
    public void DrawSprite(GL gl,float x, float y, int index, float scale, Directions dir){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On
        int angle = 0;
        switch(dir)
        {
            case up: angle = 0;break;
            case right: angle = -90; break;
            case down: angle = 180;break;
            case left: angle = 90; break;
            case up_left: angle = 45;break;
            case up_right: angle = -45; break;
            case down_left: angle = 135;break;
            case down_right: angle = -135; break;
            default: angle=0;
        }
        gl.glPushMatrix();
            gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
            gl.glScaled(0.1*scale, 0.1*scale, 1);
            gl.glRotated(angle, 0, 0, 1);
            //System.out.println(x +" " + y);
            gl.glBegin(GL.GL_QUADS);
            // Front Face
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 1.0f, -1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
        gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }
    
    public void DrawBackground(GL gl, int index){
        gl.glEnable(GL.GL_BLEND);	
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
            gl.glBegin(GL.GL_QUADS);
            // Front Face
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 1.0f, -1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
        gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }
    
    /*
     * KeyListener
     */    
    public void handleKeyPress() {
        if(isKeyPressed(KeyEvent.VK_SPACE)){
            if(lastBulletFired + fireRate < System.currentTimeMillis()){
                lastBulletFired = System.currentTimeMillis();
                bullets.add(new SplashScreen(10000));
            }
        }

        if (isKeyPressed(KeyEvent.VK_LEFT) && isKeyPressed(KeyEvent.VK_DOWN)) {
            if (x > 0) {
                x-=playerSpeed;
            }
            if (y > 0) {
                y-=playerSpeed;
            }
            direction = Directions.down_left;
            animationIndex++;
        }else
        if (isKeyPressed(KeyEvent.VK_RIGHT) && isKeyPressed(KeyEvent.VK_DOWN)) {
            if (x < maxWidth-10) {
                x+=playerSpeed;
            }
            if (y > 0) {
                y-=playerSpeed;
            }
            direction = Directions.down_right;
            animationIndex++;
        }else
        if (isKeyPressed(KeyEvent.VK_LEFT) && isKeyPressed(KeyEvent.VK_UP)) {
            if (x > 0) {
                x-=playerSpeed;
            }
            if (y < maxHeight-10) {
                y+=playerSpeed;
            }
            direction = Directions.up_left;
            animationIndex++;
        }else
        if (isKeyPressed(KeyEvent.VK_RIGHT) && isKeyPressed(KeyEvent.VK_UP)) {
            if (x < maxWidth-10) {
                x+=playerSpeed;
            }
            if (y < maxHeight-10) {
                y+=playerSpeed;
            }
            direction = Directions.up_right;
            animationIndex++;
        }else
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (x > 0) {
                x-=playerSpeed;
            }
            direction = Directions.left;
            animationIndex++;
        }else
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (x < maxWidth-10) {
                x+=playerSpeed;
            }
            direction = Directions.right;
            animationIndex++;
        }else
        if (isKeyPressed(KeyEvent.VK_DOWN)) {
            if (y > 0) {
                y-=playerSpeed;
            }
            direction = Directions.down;
            animationIndex++;
        }else
        if (isKeyPressed(KeyEvent.VK_UP)) {
            if (y < maxHeight-10) {
                y+=playerSpeed;
            }
            direction = Directions.up;
            animationIndex++;
        }
    }

    public BitSet keyBits = new BitSet(256);
 
    @Override 
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    } 
 
    @Override 
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    } 
 
    @Override 
    public void keyTyped(final KeyEvent event) {
        // don't care 
    } 
 
    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
}
