package com.anura.view;

import com.anura.UI;
import com.anura.controller.KeyHandler;
import com.anura.model.CollisionChecker;
import com.anura.model.guientity.Entity;
import com.anura.model.guientity.Player;
import object.SuperObject;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    private final int originalTileSize = 16; // 16x16 tile
    private final int scale = 3;  // multiplier scale for tiles

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    // visible area on screen
    private final int maxScreenColumns = 16;
    private final int maxScreenRows = 12;

    // Window mode
    public final int screenWidth = tileSize * maxScreenColumns; // 768 px
    public final int screenHeight = tileSize * maxScreenRows; // 576 px

    // FPS (adjust as needed)
    int FPS = 60;

    // WORLD SETTINGS
    public final int maxWorldColumns = 64;
    public final int maxWorldRows = 50;
    public final int worldWidth = tileSize * maxWorldColumns;
    public final int worldHeight = tileSize * maxWorldRows;

    // Tile manager
    public TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);

    // GAME THREAD
    public Thread gameThread;

    //UI
    public UI ui = new UI(this);

    //KEY HANDLER
    KeyHandler keyH = new KeyHandler();

    //PLAYER instantiation
    public Player player = new Player(this, keyH);

    //OBJECT Instantiation
    public SuperObject obj[] = new SuperObject[4];
    public AssetSetter setter = new AssetSetter(this);

    //NPC
    public Entity npc[] = new Entity[10];

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);   //improves the game rendering performance
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setUpGame(){
        setter.setObject();setter.setNPC();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {

        double drawInterval = 1_000_000_000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        // create core game loop
        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
        }
    }

    public void update(){
        //PLAYER
        player.update();
        //NPC
        for (int i =0; i < npc.length; i++){
            if (npc[i] != null){
                npc[i].update();
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        //TILE
        tileM.draw(g2);  // make sure this is above player

        //OBJECT
        for(int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }
        //NPC
        for(int i = 0; i < obj.length; i++) {
            if (npc[i] != null) {
                npc[i].draw(g2);
            }
        }

        //PLAYER
        player.draw(g2, this);

        //UI
        ui.draw(g2);

        g2.dispose();  // keeps from building up memory usage
    }

}
