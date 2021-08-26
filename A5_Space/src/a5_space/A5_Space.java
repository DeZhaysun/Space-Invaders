
package a5_space;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

/*
A5_Space
Creator: Jason Tang
Date: Jan. 7, 2019
Classic game of Space Invaders, with 5 aliens
*/
public class A5_Space extends JPanel implements KeyListener {

    String s;//where the input of the keyCorBulletoard characters are stored
    boolean pressedLeft, pressedRight, fire; //the 2 directions controlled by keyboard input and the fire
    Image alien, ship,background;
    int xCorShip = 300; // moving the ship, the horizontal location of the ship
    boolean alienAlive[] = {true, true, true, true, true}; //whether the aliens are alive or not
    int xCorAlien[] = {0, 100, 200, 300, 400}; //array for the 5 aliens
    int yCorAlien[] = {100, 100, 100, 100, 100};//height of the aliens
    boolean alienRight = true; //whether the alien is moving right
    int win = 0; //whether the user has won or not
    int style = Font.BOLD; //font style
    Font font = new Font ("Arial", style, 20); //font of the string output
    int xCorBullet[] = new int [20];//horizontal location of the bullets
    int yCorBullet[] = new int [20];//the vertical location of the bullets
    boolean bulletUp[] = new boolean [20]; //when the bullet needs to go bulletUp
    int bulletsLeft = 19; //how many bullets the user has left
    int score = 0; //how many aliens the player has killed
    boolean bulletAlive[] = new boolean [20]; //whether the bullet has hit an alien or not
    
    public A5_Space() { // add the listener
        for (int i = 0; i <= 19; i++){ //give all the bullets the horizontal position of zero
            xCorBullet [i] = 0;
        }
        for (int i = 0; i <= 19; i++){ //give all the bullets the vertical position of 700 so the bullets are not in the visible frame
            yCorBullet [i] = 700;
        }
        for (int i = 0; i <= 19; i++){ //none of the bullets have to go up when starting the program
            bulletUp [i] = false;
        }
        for (int i = 0; i <= 19; i++){ //all the bullets first appear so the bullets are all alive
            bulletAlive[i] = true;
        }
        this.addKeyListener(this); //add the keyboard input
        setFocusable(true); // need this to set the focus of the keyCorBulletoard
        try { //open the alien image
            alien = ImageIO.read(new File("alien.png"));
        } catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
        try { //open the ship image
            ship = ImageIO.read(new File("ship.png"));
        } catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
        try { //open the background image
            background = ImageIO.read(new File("background.jpg"));
        } catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
        background = background.getScaledInstance(700,670,Image.SCALE_DEFAULT); //scale the background image to fill the frame
        ship = ship.getScaledInstance(80, 80, Image.SCALE_DEFAULT); //scale the ship image
        alien = alien.getScaledInstance(70, 60, Image.SCALE_DEFAULT); //scale the alien image
    }

    public void keyTyped(KeyEvent e) {
        s = s + e.getKeyChar(); //save the keyCorBulletoard input
    }

    public void keyPressed(KeyEvent e) { //determining 
        char key = (char) e.getKeyCode(); //convert keyCorBulletoard input to char

        if (key == e.VK_LEFT || key == 'A') { //when the user wants to go left
            pressedLeft = true;
        } else if (key == e.VK_RIGHT || key == 'D') { //when the user wants to go alienRight
            pressedRight = true;
        } else if (key == ' ') { //when the space bar is pressed and the user wants to fire
             fire = true;
        } else if (key == e.VK_ESCAPE || key == 'Q') //quit the program
        {
            System.exit(0);
        }

        repaint(); //repaint the graphics after input
    }

    public void keyReleased(KeyEvent e) {
        pressedLeft = pressedRight = fire = false; //when the key is no longer inputted
        repaint(); //repaint the graphics after no input
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); //clear screen
        Dimension s = this.getSize(); //gets the size of the current window
        //setbackground to the background image
        g.drawImage(background,0,0,null);
        g.setFont(font); //set the font
        g.setColor(Color.white);
        g.drawString("SPACE INVADERS: By Jason Tang", 160,80);
        
        if (win == 1) {//when the user wins
            g.drawString("You Win", 270, 100); //draw the string
            g.drawString("Score: 5",270,120);
        } 
        else if (win == 2){ //when the user loses
            g.drawString("Game Over: You Lose", 200, 100); //draw the string
            g.drawString("Score: "+score,270,120);
            
        }
        else { //when the user hasn't lost or won
            g.drawString("Ammo: " + (bulletsLeft+1),580,20); //show the bullets left
            g.drawString("Score: " + score,594,40); //show how many aliens the user has killed  
            g.drawString("Arrow Keys or AD to move.",0,20);
            g.drawString("Space to fire.",0,40);
            
            //moving the ship
            if (pressedLeft == true) {//when the user wants to go left
                xCorShip = xCorShip - 4; //x coordinate decreases
            }
            if (pressedRight == true) { //when the user wants to go right
                xCorShip = xCorShip + 4; //x coordinate increases
            }
            //setting the border for the ship
            if (xCorShip < 0) { //when the ship hits the left border
                xCorShip = 0; //set the position
            } else if (xCorShip > s.width - 80) { //when the ship hits the right border
                xCorShip = s.width - 80; //reset the position
            }
            
            //firing a shot
            if (fire == true){ //when the user fires
                if(bulletsLeft <= -1){ //when you run out of bullets, the bullets left gets reset
                }
                else{ //when there are bullets left
                    xCorBullet[bulletsLeft] = xCorShip+35; //set the position of the current bullet
                    yCorBullet[bulletsLeft] = 580;//set the vertical position of the current bullet
                    bulletUp[bulletsLeft] = true; //the bullet starts going up
                    bulletsLeft--; //move onto the next bullet
                }
                fire = false; //reset the fire boolean
            }
            
            //bullet goes bulletUp
            for (int i = 0; i <= 19; i++){ //go through all the bullets
                if (bulletUp[i] == true){ //when the bullet's up boolean is true
                    yCorBullet[i] = yCorBullet[i] - 6; //the bullets vertical coordinate starts decreasing
                }
            }
            
            //collision detection            
            for (int i = 0; i <= 19; i++){ //go through all the bullets
                for (int j = 0; j <= 4; j++){ //go though all the aliens
                    if (xCorBullet[i] + 20 > xCorAlien[j] && yCorBullet[i] < yCorAlien[j] + 60 && yCorBullet [i] + 20 > yCorAlien[j]  && xCorBullet[i] < xCorAlien[j] + 70 ){ //when the bullets and aliens collide
                        alienAlive[j] = false; //the alien is not alive
                        bulletAlive[i] = false; //the bullet disappears
                        yCorAlien[j] = -1000; //set the coordinate impossible far where the bullet cannot hit the virtual alien
                    }
                }
            }
            
            //aliens going left or alienRight
            if (alienRight == true) { //the alien goes right
                for (int i = 0; i <= 4; i++) { //all the aliens
                    xCorAlien[i] = xCorAlien[i] + 2; //increase the x coordinate of the aliens
                }
            } 
            else if (alienRight == false) { //the alien goes left
                for (int i = 0; i <= 4; i++) { //all the aliens 
                    xCorAlien[i] = xCorAlien[i] - 2; //decrease the x position of the aliens
                }
            }

            //aliens bouncing and descending
            for (int i = 4; i >=0;i--){
                if (alienRight == true && xCorAlien[i] >= s.width - 70 && alienAlive[i]==true) { //when the rightmost alien hits the right border
                    alienRight = false; //the aliens starts to move left
                    for (int j = 0; j <= 4; j++){
                        yCorAlien[j] = yCorAlien[j] + 60; //the aliens descends
                    }
                }
            }
            for (int i = 0; i <= 4; i++){
                if (alienRight == false && xCorAlien[i] <= 0 && alienAlive[i] ==true) { //when the leftmost alien hits the left border
                    alienRight = true; //the aliens starts moving right
                    for (int j = 0; j <= 4; j++){
                        yCorAlien[j] = yCorAlien[j] + 60; //the aliens descends
                    }
                }
            }
            
            //determine the score
            score = 0; //reset the score so it can be count again
            for (int i = 0; i <=4; i++){//count how many aliens are dead
                if (alienAlive[i] == false){ //when the alien is dead
                    score++; //score increases
                }
            }
            
            //draw the aliens
            for (int i = 0; i <= 4; i++){
                if (alienAlive[i]){//if the alien is alive
                    g.drawImage(alien, xCorAlien[i],yCorAlien[i],null); //draw the alien in its corresponding position
                }
            }
            
            //draw the bullet
            g.setColor(Color.green); //make the bullet green
            for (int i = 0; i <= 19; i++){
                if (bulletAlive[i]){ //when the bullet has not hit an alien
                    g.fillOval(xCorBullet[i],yCorBullet[i],12,12);//draw the bullet in its corresponding position
                }
            }
            
            g.drawImage(ship, xCorShip, 583, null);//draw the space ship
            
            delay(10); //5 milisecond delay
            repaint(); //redraw the frame
            
            //win conditions
            if (alienAlive[0] == false && alienAlive[1] == false && alienAlive[2] == false && alienAlive[3] == false && alienAlive[4] == false){ //when all the aliens are killed
                win = 1;
            }
            else if (yCorAlien[0] > 550 || yCorAlien[1] > 550 || yCorAlien[2] > 550 || yCorAlien[3] > 550 || yCorAlien[4] > 550){ //when the alien(s) reach below the ship
                win = 2;
            }
        }
    }

    public static void delay(int mili) //the delay method
    {
        try {
            Thread.sleep(mili);
        } catch (InterruptedException e) {
            System.out.println("ERROR IN SLEEPING");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("A5_Space"); //create new frame called A4_NameMove
        frame.getContentPane().add(new A5_Space());
        frame.setSize(700, 700); //set frame size
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}







