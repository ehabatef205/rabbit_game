/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Splash_Screen;


/**
 * @author moham
 */
public class SplashScreen {
    long start, lifeTime;
    boolean fired;

    public SplashScreen(int lifetime) {
        this.fired = true;
        start = System.currentTimeMillis();
        lifeTime = lifetime;
    }

    public void invalidate() {
        fired = start + lifeTime > System.currentTimeMillis();
//        int dX = (initX - x) * (initX - x);
//        int dY = (initY - y) * (initY - y);
//        this.fired = dX + dY < 15000;
    }
}
