package Home_Screen;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        new Main(new HomeGLEventListener());
    }
    public Main(HomeListener aListener) {
        GLCanvas glcanvas;
        Animator animator;

        HomeListener listener = aListener;
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(15);
        animator.add(glcanvas);
        animator.start();
        glcanvas.addMouseListener(listener);

        setTitle("Rabbit Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
}
