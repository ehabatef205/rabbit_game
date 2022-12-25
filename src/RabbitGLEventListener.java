import Texture.TextureReader;
import com.sun.opengl.util.j2d.TextRenderer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;

public class RabbitGLEventListener extends RabbitListener {
    //till passed by the prior screen
    int level = 2;
    float[][] level1 = {{0.5f, 1.0f}, {1.0f, 1.5f}, {1.5f, 1.0f}};
    float[][] level2 = {{1.5f, 1.5f}, {1.0f, 1.5f}, {0.5f, 1.5f}, {1.5f, 1.1f}, {1.0f, 1.1f}, {0.5f, 1.1f}};
    float[][] level3 = {{1.5f, 1.7f}, {1.0f, 1.7f}, {0.5f, 1.7f}, {1.5f, 1.3f}, {1.0f, 1.3f}, {0.5f, 1.3f}, {1.5f, 0.9f}, {1.0f, 0.9f}, {0.5f, 0.9f}};
    float rabbit_displacement = 0.18f;
    int rabbit_position = 0;
    int Hitanimation = 0;
    int rabbit_TTl = 0;
    int pit = 2;
    int lives=3;
    int rabbit = 3;
    int bang = 6;
    int hammer = 0;
    int game_over = 5;
    int xPosition;
    int yPosition;


    int score = 0;

    //to be adjusted with frame rate
    int timer = 1800;
    Random rand = new Random();
    TextRenderer textRenderer = new TextRenderer(new Font("sanaSerif", Font.BOLD, 10));
    TextRenderer textRenderer1 = new TextRenderer(new Font("sanaSerif", Font.BOLD, 40));
    String textureNames[] = {"hammer.png", "hammer2.png", "pit.png", "rabbit.png", "back.png", "game_over.png", "boom.png"};

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

//              mipmapsFromPNG(gl, new GLU(), texture[i]);
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
        DrawBackground(gl);
        if (lives==0)
            timer=0;
        if (timer > 0) {
            if (rabbit_TTl == 30) {
                rabbit_position = rand.nextInt(level * 3);
            }
            switch (level) {
                case 1:
                    drawaction(gl, level1);
                    break;
                case 2:
                    drawaction(gl, level2);
                    break;
                case 3:
                    drawaction(gl, level3);
                    break;
            }
            DrawHammer(gl, xPosition, yPosition, hammer, 0.8f);
            timer--;
            if (rabbit_TTl==1)
                lives--;
            rabbit_TTl--;
            if (rabbit_TTl < -15)
                rabbit_TTl =30;
            DrawScore();
        } else {
            //draw game_over
            DrawGameOver(gl, game_over, 2.5f);
            DrawScore_game_over();
        }

    }

    void drawaction(GL gl, float[][] level) {
        for (int i = 0; i < level.length; i++) {
            DrawFixed(gl, pit, level[i][0], level[i][1], 1f);
        }
        if (Hitanimation > 0) {
            Hitanimation--;
            DrawFixed(gl, bang, level[rabbit_position][0], level[rabbit_position][1] - rabbit_displacement, 1);
        }
        if (rabbit_TTl > 0)
            DrawFixed(gl, rabbit, level[rabbit_position][0], level[rabbit_position][1] - rabbit_displacement, 1);
    }

    private void DrawGameOver(GL gl, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(0, 1 - 0.6, 0);
        gl.glScaled(0.2 * scale, 0.2 * scale, 1);
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

    private void DrawScore_game_over() {
        textRenderer1.beginRendering(800, 800);
        textRenderer1.setColor(Color.blue);
        textRenderer1.draw("Your Score Is " + score, 240, 340);
        textRenderer1.draw("Play Again?", 270, 280);
        textRenderer1.setColor(Color.WHITE);
        textRenderer1.endRendering();
    }

    private void DrawScore() {
        textRenderer.beginRendering(300, 300);
        textRenderer.setColor(Color.blue);
        textRenderer.draw("Score : " + score, 20, 280);
        textRenderer.draw("Timer : " + timer / 30, 20, 265);
        textRenderer.draw("lives : "+lives,20,250);
        textRenderer.setColor(Color.WHITE);
        textRenderer.endRendering();
    }

    private void DrawHammer(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (100 / 2.0) - 1, y / (100 / 2.0) - 1, 0);
        gl.glScaled(0.15 * scale, 0.15 * scale, 1);
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

    public void hit_Checker(double x, double y,float[][] level) {
        if (rabbit_TTl > 0) {
            int xrabbit = (int) (784-(level[rabbit_position][0] * 392));
            int yrabbit = (int) (((level[rabbit_position][1] ) * 380) );
            if (45 + xrabbit > x && xrabbit-45 < x&&yrabbit>y&&yrabbit-90<y) {
                rabbit_TTl = 0;
                Hitanimation = 10;
                score++;
            }
            else {
                lives--;
            }

        }
    }

    public void DrawFixed(GL gl, int index, float x_axis, float y_axis, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(1 - x_axis, 1 - y_axis, 0);
        gl.glScaled(0.2 * scale, 0.2 * scale, 1);
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
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);    // Turn Blending On

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

    }

    //// 784 X max  761 Y max
    @Override
    public void mousePressed(MouseEvent e) {
        double x=e.getX(),y=e.getY();

        switch (level) {
            case 1:
                hit_Checker(x,y, level1);
                break;
            case 2:
                hit_Checker(x,y, level2);
                break;
            case 3:
                hit_Checker(x,y, level3);
                break;
        }
        hammer = 1;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        hammer = 0;
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
        double x = e.getX();
        double y = e.getY();

        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();

        xPosition = (int) ((x / width) * 100) + 6;
        yPosition = ((int) (((height - y) / height) * 100));
    }
}
