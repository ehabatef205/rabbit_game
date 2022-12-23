import Texture.TextureReader;
import com.sun.opengl.util.j2d.TextRenderer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class RabbitGLEventListener extends RabbitListener{

    int pit = 2;
    int rabbit = 3;
    int hammer = 0, hammer2 = 1;
    int game_over = 5;
    int xPosition ;
    int yPosition;
    int maxWidth = 100;
    int maxHeight = 100;
    int x = maxWidth / 2, y = maxHeight / 2;
    int score = 0;
    int timer = 1000;
    TextRenderer textRenderer = new TextRenderer(new Font("sanaSerif", Font.BOLD, 10));
    TextRenderer textRenderer1 = new TextRenderer(new Font("sanaSerif", Font.BOLD, 40));
    String textureNames[] = {"hammer.png", "hammer2.png" ,"pit.png", "rabbit.png","back.png", "game_over.png"};

    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];

    int textures[] = new int[textureNames.length];

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
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

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();


        DrawBackground(gl);

        //level 1
        /*
        DrawPit(gl, x, y, pit, 0.5f, 1.0f,1);
        DrawPit(gl, x, y, pit, 1.0f, 1.5f,1);
        DrawPit(gl, x, y, pit, 1.5f, 1.0f,1);
        */

        //level 2
        /*DrawPit(gl , x , y , pit , 1.5f , 1.2f , 1);
        DrawPit(gl , x , y , pit , 1.0f , 1.2f , 1);
        DrawPit(gl , x , y , pit , 0.5f , 1.2f , 1);
        DrawPit(gl , x , y , pit , 1.5f , 0.8f , 1);
        DrawPit(gl , x , y , pit , 1.0f , 0.8f , 1);
        DrawPit(gl , x , y , pit , 0.5f , 0.8f , 1);*/

        //level 3
        /*DrawPit(gl , x , y , pit , 1.5f , 1.4f , 1);
        DrawPit(gl , x , y , pit , 1.0f , 1.4f , 1);
        DrawPit(gl , x , y , pit , 0.5f , 1.4f , 1);
        DrawPit(gl , x , y , pit , 1.5f , 1.0f , 1);
        DrawPit(gl , x , y , pit , 1.0f , 1.0f , 1);
        DrawPit(gl , x , y , pit , 0.5f , 1.0f , 1);
        DrawPit(gl , x , y , pit , 1.5f , 0.6f , 1);
        DrawPit(gl , x , y , pit , 1.0f , 0.6f , 1);
        DrawPit(gl , x , y , pit , 0.5f , 0.6f , 1);*/

        //DrawRabbit(gl, x, y, rabbit, 0.5f , 0.8f , 1);
        //draw hammer
        //DrawHammer(gl , xPosition , yPosition , hammer, 0.8f);

        //draw score
        //DrawScore();

        //draw game_over
        DrawGameOver(gl, x , y , game_over ,2.5f);
        DrawScore_game_over();
    }

    private void DrawGameOver(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 1, y / (maxHeight / 2.0) - 0.6, 0);
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
    private void DrawScore_game_over(){
        textRenderer1.beginRendering(800, 800);
        textRenderer1.setColor(Color.blue);
        textRenderer1.draw("Your Score Is " + score, 240, 340);
        textRenderer1.draw("Play Again?", 270, 280);
        textRenderer1.setColor(Color.WHITE);
        textRenderer1.endRendering();
    }

    private void DrawScore(){
        textRenderer.beginRendering(300, 300);
        textRenderer.setColor(Color.blue);
        textRenderer.draw("Score : " + score, 20, 280);
        textRenderer.draw("Timer : " + timer, 20, 265);
        textRenderer.setColor(Color.WHITE);
        textRenderer.endRendering();
    }

    private void DrawHammer(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 1, y / (maxHeight / 2.0) - 1, 0);
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

    public void DrawPit(GL gl , int x , int y , int index ,float x_axis , float y_axis ,  float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - x_axis, y / (maxHeight / 2.0) - y_axis, 0);
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

    public void DrawRabbit(GL gl , int x , int y , int index ,float x_axis , float y_axis , float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - x_axis, y / (maxHeight / 2.0) - y_axis, 0);
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

    public void DrawBackground(GL gl){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);	// Turn Blending On

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

    @Override
    public void mousePressed(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();

        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();

        xPosition = (int) ((x / width) * 100) + 6;
        yPosition = ((int) (((height-y) / height) * 100));
        hammer = 1;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();

        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();

        xPosition = (int) ((x / width) * 100) + 6;
        yPosition = ((int) (((height-y) / height) * 100));
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
        yPosition = ((int) (((height-y) / height) * 100));
    }
}
