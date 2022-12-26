import Texture.TextureReader;
import com.sun.opengl.util.j2d.TextRenderer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Random;

public class RabbitGLEventListener extends RabbitListener {
    //till passed by the prior screen
    String screen = "Home_Screen";
    String levels = "";
    String difficulty = "";
    int level = 0;
    float[][] level1 = {{0.5f, 1.0f}, {1.0f, 1.5f}, {1.5f, 1.0f}};
    float[][] level2 = {{1.5f, 1.5f}, {1.0f, 1.5f}, {0.5f, 1.5f}, {1.5f, 1.1f}, {1.0f, 1.1f}, {0.5f, 1.1f}};
    float[][] level3 = {{1.5f, 1.7f}, {1.0f, 1.7f}, {0.5f, 1.7f}, {1.5f, 1.3f}, {1.0f, 1.3f}, {0.5f, 1.3f}, {1.5f, 0.9f}, {1.0f, 0.9f}, {0.5f, 0.9f}};
    float rabbit_displacement = 0.18f;
    int rabbit_position = 0;
    int Hitanimation = 0;
    int rabbit_TTl = 0;
    int pit = 2;
    int lives = 3;
    int rabbit = 3;
    int bang = 6;
    int hammer = 0;
    int game_over = 5;
    int xPosition;
    int yPosition;

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
    float scaleLevel1 = 0.7f;
    float scaleLevel2 = 0.7f;
    float scaleLevel3 = 0.7f;
    boolean isLevel1;
    boolean isLevel2;
    boolean isLevel3;
    float scaleSound = 0.4f;
    boolean isSound;
    float scaleHome = 0.7f;
    boolean isHome;
    float scaleAgain = 0.7f;
    boolean isAgain;
    float scaleRestart = 0.7f;
    boolean isRestart;
    float scaleResume = 0.7f;
    boolean isResume;
    int sound = 18;

    int score = 0;

    //to be adjusted with frame rate
    int timer = 1800;
    Random rand = new Random();
    TextRenderer textRenderer = new TextRenderer(new Font("sanaSerif", Font.BOLD, 10));
    TextRenderer textRenderer1 = new TextRenderer(new Font("sanaSerif", Font.BOLD, 40));

    TextRenderer ren = new TextRenderer(new Font("sanaSerif", Font.BOLD, 30));
    String highScore = "";
    String textureNames[] = {
            "hammer.png",
            "hammer2.png",
            "pit.png",
            "rabbit.png",
            "back.png",
            "game_over.png",
            "boom.png",
            "start.png",
            "how.png",
            "create.png",
            "exit.png",
            "easy.png",
            "medium.png",
            "hard.png",
            "backbutton.png",
            "level1.png",
            "level2.png",
            "level3.png",
            "sound.png",
            "mute.png",
            "name.png",
            "how_to_play.png",
            "Home.png",
            "again.png",
            "menu.png",
            "restart.png",
            "resume.png",
    };

    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];

    int textures[] = new int[textureNames.length];

    AudioInputStream audioStream, audio, gameover, hammerSound, whistle;

    Clip clip, clip1, clip2, clip3, clip4;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
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
        try {
            audioStream = AudioSystem.getAudioInputStream(new File("song//song.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            changeVolume(clip);
            clip.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        DrawBackground(gl);
        if (highScore == "") {
            {
                highScore = GetHighScore();
            }
        }
        switch (screen) {
            case "Home_Screen":
                Home_Screen(gl);
                drawtime();
                break;

            case "Difficulty_Screen":
                Difficulty_Screen(gl);
                break;

            case "Level_Screen":
                Level_Screen(gl);
                break;

            case "Create_By_Screen":
                Create_By_Screen(gl);
                break;

            case "How_To_Play":
                How_To_Play(gl);
                break;

            case "Level":
                if (lives == 0)
                    timer = 0;
                if (timer > 0) {
                    switch (level) {
                        case 1:
                            drawaction(gl, level1);
                            DrawButton(gl, 40, 45, 24, scaleBack);
                            break;
                        case 2:
                            drawaction(gl, level2);
                            DrawButton(gl, 40, 45, 24, scaleBack);
                            break;
                        case 3:
                            drawaction(gl, level3);
                            DrawButton(gl, 40, 45, 24, scaleBack);
                            break;
                    }
                    if (sound == 18) {
                        clip.stop();
                    }
                    DrawHammer(gl, xPosition, yPosition, hammer, 0.8f);
                    timer--;
                    rabbit_TTl--;
                    if (rabbit_TTl < -15)
                        switch (difficulty) {
                            case "Easy":
                                rabbit_TTl = 30;
                                soundWhistle();
                                rabbit_position = rand.nextInt(level * 3);
                                break;

                            case "Medium":
                                rabbit_TTl = 20;
                                soundWhistle();
                                rabbit_position = rand.nextInt(level * 3);
                                break;

                            case "Hard":
                                rabbit_TTl = 10;
                                soundWhistle();
                                rabbit_position = rand.nextInt(level * 3);
                                break;
                        }
                    DrawScore();
                } else {
                    screen = "Game_Over";
                    try {
                        gameover = AudioSystem.getAudioInputStream(new File("song//gameover.wav"));
                        clip2 = AudioSystem.getClip();
                        clip2.open(gameover);
                        changeVolume(clip2);
                        if (sound == 18) {
                            clip2.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    storeHighScore();
                }
                break;

            case "Game_Over":
                DrawGameOver(gl, game_over, 2.5f);
                DrawScore_game_over();
                DrawButton(gl, -30, -30, 22, scaleHome);
                DrawButton(gl, 30, -30, 23, scaleAgain);
                break;

            case "Pause_Screen":
                DrawRabbit(gl, 25, 0, 3, 3f);
                DrawButton(gl, -25, 20, 26, scaleResume);
                DrawButton(gl, -25, 0, 25, scaleRestart);
                DrawButton(gl, -25, -20, 22, scaleHome);
                DrawButton(gl, 43, 45, sound, scaleSound);
                break;
        }
    }

    public void changeVolume(Clip c) {
        FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        double gain = 0.05;
        float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }

    public String GetHighScore() {
        FileReader readFile = null;
        BufferedReader reader = null;
        try {
            readFile = new FileReader("highScore.dat");
            reader = new BufferedReader(readFile);
            return reader.readLine();
        } catch (Exception e) {
            return "0";
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void storeHighScore() {
        if (score > Integer.parseInt(highScore)) {
            File scoreFile = new File("highScore.dat");
            if (!scoreFile.exists()) {
                try {
                    scoreFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileWriter writerFile = null;
            BufferedWriter writer = null;
            try {
                writerFile = new FileWriter(scoreFile);
                writer = new BufferedWriter(writerFile);
                writer.write("" + score);
                highScore = "" + score;
            } catch (Exception e) {

            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    public void soundWhistle() {
        try {
            whistle = AudioSystem.getAudioInputStream(new File("song//whistle.wav"));
            clip3 = AudioSystem.getClip();
            clip3.open(whistle);
            clip3.loop(0);
            changeVolume(clip3);
            if (sound == 19) {
                clip3.stop();
            } else {
                clip3.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawtime() {
        ren.beginRendering(800, 800);
        ren.setColor(Color.white);
        ren.draw("High Score : " + highScore, 300, 760);
        ren.setColor(Color.WHITE);
        ren.endRendering();
    }

    public void Home_Screen(GL gl) {
        DrawRabbit(gl, 25, 0, 3, 3f);
        DrawButton(gl, -25, 30, 7, scaleStart);
        DrawButton(gl, -25, 10, 8, scaleHow);
        DrawButton(gl, -25, -10, 9, scaleCreate);
        DrawButton(gl, -25, -30, 10, scaleExit);
        DrawButton(gl, 43, 45, sound, scaleSound);
    }

    public void Difficulty_Screen(GL gl) {
        DrawRabbit(gl, 25, 0, 3, 3f);
        DrawButton(gl, -25, 20, 11, scaleEasy);
        DrawButton(gl, -25, 0, 12, scaleMedium);
        DrawButton(gl, -25, -20, 13, scaleHard);
        DrawButton(gl, -43, 45, 14, scaleBack);
    }

    public void Level_Screen(GL gl) {
        DrawRabbit(gl, 25, 0, 3, 3f);
        DrawButton(gl, -25, 20, 15, scaleLevel1);
        DrawButton(gl, -25, 0, 16, scaleLevel2);
        DrawButton(gl, -25, -20, 17, scaleLevel3);
        DrawButton(gl, -43, 45, 14, scaleBack);
    }

    public void Create_By_Screen(GL gl) {
        DrawRabbit(gl, 25, 0, 3, 3f);
        DrawName(gl, -20, 0, 20, 2.5f);
        DrawButton(gl, -43, 45, 14, scaleBack);
    }

    public void How_To_Play(GL gl) {
        DrawRabbit(gl, 25, 0, 3, 3f);
        DrawName(gl, -20, 0, 21, 2.5f);
        DrawButton(gl, -43, 45, 14, scaleBack);
    }

    public void DrawButton(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / 50.0, y / 50.0, 0);
        gl.glScaled(0.2 * scale, 0.2 * scale, 1);
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
        gl.glTranslated(x / 50.0, y / 50.0, 0);
        gl.glScaled(0.25 * scale, 0.25 * scale, 1);
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

    public void DrawName(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / 50.0, y / 50.0, 0);
        gl.glScaled(0.25 * scale, 0.25 * scale, 1);
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
        textRenderer1.setColor(Color.WHITE);
        textRenderer1.draw("Your Score Is " + score, 240, 340);
        textRenderer1.setColor(Color.WHITE);
        textRenderer1.endRendering();
    }

    private void DrawScore() {
        textRenderer.beginRendering(300, 300);
        textRenderer.setColor(Color.WHITE);
        textRenderer.draw("Score : " + score, 20, 280);
        textRenderer.draw("Timer : " + timer / 60, 20, 265);
        textRenderer.draw("lives : " + lives, 20, 250);
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

    public void hit_Checker(double x, double y, float[][] level) {
        for (int index = 0; index < level.length; index++) {
            if (index == rabbit_position) {
                if (rabbit_TTl > 0) {
                    int xrabbit = (int) (784 - (level[rabbit_position][0] * 392));
                    int yrabbit = (int) (((level[rabbit_position][1]) * 380));
                    if (45 + xrabbit > x && xrabbit - 45 < x && yrabbit + 10 > y && yrabbit - 90 < y) {
                        rabbit_TTl = 0;
                        Hitanimation = 10;
                        score++;
                        try {
                            audio = AudioSystem.getAudioInputStream(new File("song//alert.wav"));
                            clip1 = AudioSystem.getClip();
                            clip1.open(audio);
                            changeVolume(clip1);
                            if (sound == 19) {
                                clip1.stop();
                            } else {
                                clip1.start();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                int x_pit = (int) (784 - (level[index][0] * 392));
                int y_pit = (int) (((level[index][1]) * 380));
                if (50 + x_pit > x && x_pit - 45 < x && y_pit - 25 < y && y_pit + 10 > y) {
                    lives--;
                    try {
                        hammerSound = AudioSystem.getAudioInputStream(new File("song//hammer.wav"));
                        clip4 = AudioSystem.getClip();
                        clip4.open(hammerSound);
                        changeVolume(clip4);
                        if (sound == 19) {
                            clip4.stop();
                        } else {
                            clip4.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void DrawFixed(GL gl, int index, float x_axis, float y_axis, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(1 - x_axis, 1 - y_axis, 0);
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


    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);

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
        double x = e.getX(), y = e.getY();
        switch (screen) {
            case "Home_Screen":
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
                } else if (e.getX() >= 685 && e.getX() <= 770 && e.getY() >= 10 && e.getY() <= 65) {
                    scaleSound = 0.3f;
                    isSound = true;
                }
                break;

            case "Difficulty_Screen":
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

            case "Level_Screen":
                if (e.getX() > 115 && e.getX() < 275 && e.getY() > 175 && e.getY() < 280) {
                    scaleLevel1 = 0.6f;
                    isLevel1 = true;
                } else if (e.getX() > 115 && e.getX() < 275 && e.getY() > 330 && e.getY() < 435) {
                    scaleLevel2 = 0.6f;
                    isLevel2 = true;
                } else if (e.getX() > 115 && e.getX() < 275 && e.getY() > 480 && e.getY() < 585) {
                    scaleLevel3 = 0.6f;
                    isLevel3 = true;
                } else if (e.getX() > 10 && e.getX() < 100 && e.getY() > 9 && e.getY() < 65) {
                    scaleBack = 0.3f;
                    isBack = true;
                }
                break;

            case "Create_By_Screen":

            case "How_To_Play":
                if (e.getX() > 10 && e.getX() < 100 && e.getY() > 9 && e.getY() < 65) {
                    scaleBack = 0.3f;
                    isBack = true;
                }
                break;

            case "Level":
                hammer = 1;
                if (x > 665 && x < 745 && y > 10 && y < 60) {
                    scaleBack = 0.3f;
                    isBack = true;
                } else {
                    switch (level) {
                        case 1:
                            hit_Checker(x, y, level1);
                            break;
                        case 2:
                            hit_Checker(x, y, level2);
                            break;
                        case 3:
                            hit_Checker(x, y, level3);
                            break;
                    }
                }
                break;

            case "Game_Over":
                if (e.getX() > 115 && e.getX() < 275 && e.getY() > 555 && e.getY() < 660) {
                    scaleHome = 0.6f;
                    isHome = true;
                } else if (e.getX() > 545 && e.getX() < 710 && e.getY() > 555 && e.getY() < 660) {
                    scaleAgain = 0.6f;
                    isAgain = true;
                }
                break;

            case "Pause_Screen":
                if (e.getX() > 115 && e.getX() < 275 && e.getY() > 175 && e.getY() < 280) {
                    scaleResume = 0.6f;
                    isResume = true;
                } else if (e.getX() > 115 && e.getX() < 275 && e.getY() > 330 && e.getY() < 435) {
                    scaleRestart = 0.6f;
                    isRestart = true;
                } else if (e.getX() > 115 && e.getX() < 275 && e.getY() > 480 && e.getY() < 585) {
                    scaleHome = 0.6f;
                    isHome = true;
                } else if (e.getX() >= 685 && e.getX() <= 770 && e.getY() >= 10 && e.getY() <= 65) {
                    scaleSound = 0.3f;
                    isSound = true;
                }
                break;
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (screen) {
            case "Home_Screen":
                if (isStart) {
                    scaleStart = 0.7f;
                    isStart = false;
                    screen = "Difficulty_Screen";
                } else if (isHow) {
                    scaleHow = 0.7f;
                    isHow = false;
                    screen = "How_To_Play";
                } else if (isCreate) {
                    scaleCreate = 0.7f;
                    isCreate = false;
                    screen = "Create_By_Screen";
                } else if (isExit) {
                    scaleExit = 0.7f;
                    isExit = false;
                    int a = JOptionPane.showConfirmDialog(null, "are you sure exit!");
                    if (a == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                } else if (isSound) {
                    scaleSound = 0.4f;
                    isSound = false;
                    if (sound == 18) {
                        sound = 19;
                        clip.stop();
                    } else {
                        sound = 18;
                        clip.start();
                    }
                }
                break;

            case "Difficulty_Screen":
                if (isEasy) {
                    scaleEasy = 0.7f;
                    screen = "Level_Screen";
                    difficulty = "Easy";
                    isEasy = false;
                } else if (isMedium) {
                    scaleMedium = 0.7f;
                    screen = "Level_Screen";
                    difficulty = "Medium";
                    isMedium = false;
                } else if (isHard) {
                    scaleHard = 0.7f;
                    screen = "Level_Screen";
                    difficulty = "Hard";
                    isHard = false;
                } else if (isBack) {
                    scaleBack = 0.4f;
                    screen = "Home_Screen";
                    difficulty = "";
                    isBack = false;
                }
                break;

            case "Level_Screen":
                if (isLevel1) {
                    scaleLevel1 = 0.7f;
                    screen = "Level";
                    levels = "Level 1";
                    level = 1;
                    isLevel1 = false;
                } else if (isLevel2) {
                    scaleLevel2 = 0.7f;
                    screen = "Level";
                    levels = "Level 2";
                    level = 2;
                    isLevel2 = false;
                } else if (isLevel3) {
                    scaleLevel3 = 0.7f;
                    screen = "Level";
                    levels = "Level 3";
                    level = 3;
                    isLevel3 = false;
                } else if (isBack) {
                    scaleBack = 0.4f;
                    screen = "Difficulty_Screen";
                    levels = "";
                    isBack = false;
                }
                break;

            case "Create_By_Screen":

            case "How_To_Play":
                if (isBack) {
                    scaleBack = 0.4f;
                    screen = "Home_Screen";
                    isBack = false;
                }
                break;

            case "Level":
                hammer = 0;
                if (isBack) {
                    scaleBack = 0.4f;
                    screen = "Pause_Screen";
                    isBack = false;
                    if (sound == 18) {
                        clip.start();
                    }
                }
                break;

            case "Game_Over":
                if (isHome) {
                    scaleHome = 0.7f;
                    screen = "Home_Screen";
                    difficulty = "";
                    level = 0;
                    score = 0;
                    lives = 3;
                    timer = 1800;
                    rabbit_TTl = 0;
                    hammer = 0;
                    levels = "";
                    if (sound == 18) {
                        clip.start();
                    }
                    clip2.stop();
                    isHome = false;
                } else if (isAgain) {
                    scaleAgain = 0.7f;
                    screen = "Level";
                    score = 0;
                    hammer = 0;
                    lives = 3;
                    rabbit_TTl = 0;
                    timer = 1800;
                    clip2.stop();
                    isAgain = false;
                }
                break;

            case "Pause_Screen":
                if (isResume) {
                    scaleResume = 0.7f;
                    screen = "Level";
                    isResume = false;
                } else if (isRestart) {
                    scaleRestart = 0.7f;
                    screen = "Level";
                    score = 0;
                    lives = 3;
                    rabbit_TTl = 0;
                    hammer = 0;
                    timer = 1800;
                    isRestart = false;
                } else if (isHome) {
                    scaleHome = 0.7f;
                    screen = "Home_Screen";
                    difficulty = "";
                    level = 0;
                    score = 0;
                    hammer = 0;
                    lives = 3;
                    timer = 1800;
                    rabbit_TTl = 0;
                    levels = "";
                    if (sound == 18) {
                        clip.start();
                    }
                    isHome = false;
                } else if (isSound) {
                    scaleSound = 0.4f;
                    isSound = false;
                    if (sound == 18) {
                        sound = 19;
                        clip.stop();
                    } else {
                        sound = 18;
                        clip.start();
                    }
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
        double x = e.getX();
        double y = e.getY();

        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();

        xPosition = (int) ((x / width) * 100) + 6;
        yPosition = ((int) (((height - y) / height) * 100));
    }
}
