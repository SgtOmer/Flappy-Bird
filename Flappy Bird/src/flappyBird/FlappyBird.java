package flappyBird;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener{

	public static FlappyBird flappyBird;
	
	public final int WIDTH = 800, HEIGHT = 800;
	
	public Renderer renderer;
	
	public Rectangle bird;
	
	public ArrayList<Rectangle> columns;
	
	public Random ran;
	
	public int ticks, yMotion, score;
	
	boolean gameOver, started;

	public FlappyBird(){
		JFrame jframe = new JFrame();
		
		Timer timer = new Timer(20, this);
		
		renderer = new Renderer();
		ran = new Random();
		
		jframe.add(renderer);
		jframe.setTitle("Flappy Bird");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.addMouseListener(this);
		jframe.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
		jframe.setResizable(false);
		jframe.pack();
		jframe.setVisible(true);
		
		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20 ,20);
		columns = new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
	}
	
	
	public void addColumn(boolean start){
		int space = 300;
		int width = 100;
		int height = 50 + ran.nextInt(300);
		
		if (start){
			columns.add(new Rectangle(WIDTH + width +columns.size()*300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width +(columns.size()-1)*300, 0, width, HEIGHT - height - space));
		} else {
			columns.add(new Rectangle(columns.get(columns.size()-1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, width, HEIGHT - height - space));
		}
	}
	
	public void paintColumn(Graphics g, Rectangle column){
		g.setColor(Color.green.darker().darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}

	public void repaint(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);
		
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);
		
		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, 20, 20);
		
		for (Rectangle col : columns){
			paintColumn(g, col);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));
		
		if (!started){
			g.drawString("Click to start!", 100, HEIGHT / 2 - 50);
		}
		
		if (gameOver){
			g.drawString("Game Over!", 100, HEIGHT / 2 - 50);
		}
		
		if (!gameOver && started){
			g.drawString(String.valueOf(score), WIDTH / 2 -25, 100);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		int speed = 10;
		
		if (started){		
			ticks++;
			
			for (int i = 0 ; i < columns.size() ; i++){
				Rectangle col = columns.get(i);
				col.x -= speed;
				
				if (col.x +col.width < 0){
					columns.remove(i);
					if (col.y == 0)
						addColumn(false);
				}
			}
			
			if (ticks % 2 ==0 && yMotion<15){
				yMotion +=2;
			}
			
			bird.y += yMotion;
			
			for (Rectangle col : columns){
				
				if (bird.x + bird.width / 2 > col.x + col.width / 2 -10 && bird.x + bird.width / 2 < col.x + col.width / 2 +10 && col.y==0)
					score++;
				
				if (col.intersects(bird)){
					gameOver = true;
					
					bird.x = col.x - bird.width;
				}
			}
			
			if (bird.y > HEIGHT - 120 || bird.y < 0){
				gameOver = true;
			}
			
			if (gameOver){				
				bird.y = HEIGHT - 120 - bird.height;
			}
		
		}
		renderer.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public void jump(){
		if (gameOver){
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20 ,20);
			columns.clear();
			yMotion = 0;
			score = 0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver = false;			
		}
		
		if (!started){
			started = true;
		} else if (!gameOver) {
			if (yMotion > 0)
				yMotion = 0;
			yMotion -= 10;
		}
	}
	
	public static void main(String[] args) {
		flappyBird = new FlappyBird();
	}

}
