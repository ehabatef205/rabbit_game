package Home_Screen;

import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

public class HomeGLEventListener extends HomeListener implements MouseListener, MouseMotionListener {

    int screen = 0;
    int pit = 1;
    int rabbit = 2;

    int hammer = 0;
    int maxWidth = 100;
    int maxHeight = 100;
    int x = maxWidth / 2, y = maxHeight / 2;
    float scaleStart = 0.7f;
    float scaleHow = 0.7f;
    float scaleCreate = 0.7f;
    float scaleExit = 0.7f;
    boolean isStart;
    boolean isHow;
    boolean isCreate;
    boolean isExit;
    float scaleEasy = 0.7f;
    float scaleMedium = 0.7f;
    float scaleHard = 0.7f;
    boolean isEasy;
    boolean isMedium;
    boolean isHard;
    float scaleBack = 0.4f;
    boolean isBack;
    float scale3Pit = 0.7f;
    float scale6Pit = 0.7f;
    float scale9Pit = 0.7f;
    boolean is3Pit;
    boolean is6Pit;
    boolean is9Pit;
    String textureNames[] = {"back.png",
            "rabbit.png",
            "start.png",
            "how.png",
            "create.png",
            "exit.png",
            "easy.png", "medium.png",
            "hard.png",
            "backbutton.png",
            "3pit.png",
            "6pit.png",
            "9pit.png"};

    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];

    int textures[] = new int[textureNames.length];

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
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
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();

        switch (screen) {
            case 0:
                DrawBackground(gl);
                DrawRabbit(gl, 25, 0, 1, 3f);
                DrawButton(gl, -25, 30, 2, scaleStart);
                DrawButton(gl, -25, 10, 3, scaleHow);
                DrawButton(gl, -25, -10, 4, scaleCreate);
                DrawButton(gl, -25, -30, 5, scaleExit);
                break;

            case 1:
                DrawBackground(gl);
                DrawRabbit(gl, 25, 0, 1, 3f);
                DrawButton(gl, -25, 20, 6, scaleEasy);
                DrawButton(gl, -25, 0, 7, scaleMedium);
                DrawButton(gl, -25, -20, 8, scaleHard);
                DrawButton(gl, -43, 45, 9, scaleBack);
                break;

            case 2:
                DrawBackground(gl);
                DrawRabbit(gl, 25, 0, 1, 3f);
                DrawButton(gl, -25, 20, 10, scale3Pit);
                DrawButton(gl, -25, 0, 11, scale6Pit);
                DrawButton(gl, -25, -20, 12, scale9Pit);
                DrawButton(gl, -43, 45, 9, scaleBack);
                break;
        }

    }

    public void DrawButton(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0), y / (maxHeight / 2.0), 0);
        gl.glScaled(0.2 * scale, 0.2 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.5f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.5f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.5f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.5f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawRabbit(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0), y / (maxHeight / 2.0), 0);
        gl.glScaled(0.25 * scale, 0.25 * scale, 1);
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

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);    // Turn Blending On

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

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getX() + " " + e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (screen) {
            case 0:
                if (e.getX() >= 115 && e.getX() <= 275 && e.getY() >= 95 && e.getY() <= 205) {
                    scaleStart = 0.6f;
                    isStart = true;
                } else if (e.getX() >= 115 && e.getX() <= 275 && e.getY() >= 250 && e.getY() <= 360) {
                    scaleHow = 0.6f;
                    isHow = true;
                } else if (e.getX() >= 115 && e.getX() <= 275 && e.getY() >= 400 && e.getY() <= 510) {
                    scaleCreate = 0.6f;
                    isCreate = true;
                } else if (e.getX() >= 115 && e.getX() <= 275 && e.getY() >= 555 && e.getY() <= 660) {
                    scaleExit = 0.6f;
                    isExit = true;
                }
                break;

            case 1:
                if (e.getX() > 115 && e.getX() < 275 && e.getY() > 175 && e.getY() < 280) {
                    scaleEasy = 0.6f;
                    isEasy = true;
                } else if (e.getX() > 115 && e.getX() < 275 && e.getY() > 330 && e.getY() < 435) {
                    scaleMedium = 0.6f;
                    isMedium = true;
                } else if (e.getX() > 115 && e.getX() < 275 && e.getY() > 480 && e.getY() < 585) {
                    scaleHard = 0.6f;
                    isHard = true;
                } else if (e.getX() > 10 && e.getX() < 100 && e.getY() > 9 && e.getY() < 65) {
                    scaleBack = 0.3f;
                    isBack = true;
                }
                break;

            case 2:
                if (e.getX() > 115 && e.getX() < 275 && e.getY() > 175 && e.getY() < 280) {
                    scale3Pit = 0.6f;
                    is3Pit = true;
                } else if (e.getX() > 115 && e.getX() < 275 && e.getY() > 330 && e.getY() < 435) {
                    scale6Pit = 0.6f;
                    is6Pit = true;
                } else if (e.getX() > 115 && e.getX() < 275 && e.getY() > 480 && e.getY() < 585) {
                    scale9Pit = 0.6f;
                    is9Pit = true;
                } else if (e.getX() > 10 && e.getX() < 100 && e.getY() > 9 && e.getY() < 65) {
                    scaleBack = 0.3f;
                    isBack = true;
                }
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (screen) {
            case 0:
                if (isStart) {
                    scaleStart = 0.7f;
                    isStart = false;
                    screen = 1;
                } else if (isHow) {
                    scaleHow = 0.7f;
                    isHow = false;
                } else if (isCreate) {
                    scaleCreate = 0.7f;
                    isCreate = false;
                } else if (isExit) {
                    scaleExit = 0.7f;
                    isExit = false;
                    int a = JOptionPane.showConfirmDialog(null, "are you sure exit!");
                    if (a == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
                break;

            case 1:
                if (isEasy) {
                    scaleEasy = 0.7f;
                    screen = 2;
                    isEasy = false;
                } else if (isMedium) {
                    scaleMedium = 0.7f;
                    isMedium = false;
                } else if (isHard) {
                    scaleHard = 0.7f;
                    isHard = false;
                } else if (isBack) {
                    scaleBack = 0.4f;
                    screen = 0;
                    isBack = false;
                }
                break;

            case 2:
                if (is3Pit) {
                    scale3Pit = 0.7f;
                    is3Pit = false;
                } else if (is6Pit) {
                    scale6Pit = 0.7f;
                    is3Pit = false;
                } else if (is9Pit) {
                    scale9Pit = 0.7f;
                    is9Pit = false;
                } else if (isBack) {
                    scaleBack = 0.4f;
                    screen = 1;
                    isBack = false;
                }
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
