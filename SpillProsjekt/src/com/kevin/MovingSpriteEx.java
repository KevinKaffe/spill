package com.kevin;

import java.awt.EventQueue;
import javax.swing.JFrame;


public class MovingSpriteEx extends JFrame {


public static MovingSpriteEx ex2;
private MovingSpriteEx ex;
private Board b;
    public MovingSpriteEx() {
        
        initUI();
        ex2 = this;
    }
    
    private void initUI() {
        
    	b = new Board();
        add(b);
        //remove(b);
        setSize(840-36, 720);
        setResizable(false);
        
        setTitle("Bomb A Wall!");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void makeNewBoard()
    {
    	remove(b);
    	b=new Board();
    	add(b);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                MovingSpriteEx ex = new MovingSpriteEx();
                ex.setVisible(true);
            }
        });
    }
}