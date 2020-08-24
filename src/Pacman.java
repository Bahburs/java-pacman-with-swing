import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Pacman implements MouseListener,
KeyListener {

  long titleTimer = -1;
  long timer = -1;

  Board board = new Board();

  javax.swing.Timer frameTimer;

  public Pacman() {
    board.requestFocus();

    JFrame f = new JFrame();
    f.setSize(440, 500);
    f.setTitle("Pacman 0.12 by Bahburs");
    f.add(board, BorderLayout.CENTER);

    board.addMouseListener(this);
    board.addKeyListener(this);

    f.setVisible(true);
    f.setResizable(false);

    board.New = 1;

    stepFrame(true);

    frameTimer = new javax.swing.Timer(30, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stepFrame(false);
      }
    });

    frameTimer.start();

    board.requestFocus();
  }

  public void repaint() {
    if (board.player.teleport) {
      board.repaint(board.player.lastX - 20, board.player.lastY - 20, 80, 80);
      board.player.teleport = false;
    }
    board.repaint(0, 0, 600, 20);
    board.repaint(0, 420, 600, 40);
    board.repaint(board.player.x - 20, board.player.y - 20, 80, 80);
    board.repaint(board.ghost1.x - 20, board.ghost1.y - 20, 80, 80);
    board.repaint(board.ghost2.x - 20, board.ghost2.y - 20, 80, 80);
    board.repaint(board.ghost3.x - 20, board.ghost3.y - 20, 80, 80);
    board.repaint(board.ghost4.x - 20, board.ghost4.y - 20, 80, 80);
  }

  public void stepFrame(boolean New) {

    if (!board.titleScreen && !board.winScreen && !board.overScreen) {
      timer = -1;
      titleTimer = -1;
    }

    if (board.dying > 0) {
      board.repaint();
      return;
    }

    New = New || (board.New != 0);

    if (board.titleScreen) {
      if (titleTimer == -1) {
        titleTimer = System.currentTimeMillis();
      }

      long currTime = System.currentTimeMillis();
      if (currTime - titleTimer >= 5000) {
        board.titleScreen = false;
        board.demo = true;
        titleTimer = -1;
      }
      board.repaint();
      return;
    } else if (board.winScreen || board.overScreen) {
      if (timer == -1) {
        timer = System.currentTimeMillis();
      }

      long currTime = System.currentTimeMillis();
      if (currTime - timer >= 5000) {
        board.winScreen = false;
        board.overScreen = false;
        board.titleScreen = true;
        timer = -1;
      }
      board.repaint();
      return;
    }

    if (!New) {

      if (board.demo) {
        board.player.demoMove();
      } else {
        board.player.move();
      }

      board.ghost1.move();
      board.ghost2.move();
      board.ghost3.move();
      board.ghost4.move();
      board.player.updatePellet();
      board.ghost1.updatePellet();
      board.ghost2.updatePellet();
      board.ghost3.updatePellet();
      board.ghost4.updatePellet();
    }

    if (board.stopped || New) {

      frameTimer.stop();

      while (board.dying > 0) {

        stepFrame(false);
      }

      board.player.currDirection = 'L';
      board.player.direction = 'L';
      board.player.desiredDirection = 'L';
      board.player.x = 200;
      board.player.y = 300;
      board.ghost1.x = 180;
      board.ghost1.y = 180;
      board.ghost2.x = 200;
      board.ghost2.y = 180;
      board.ghost3.x = 220;
      board.ghost3.y = 180;
      board.ghost4.x = 220;
      board.ghost4.y = 180;

      board.repaint(0, 0, 600, 600);

      board.stopped = false;
      frameTimer.start();
    } else {
      repaint();
    }
  }

  public void keyPressed(KeyEvent e) {

    if (board.titleScreen) {
      board.titleScreen = false;
      return;
    } else if (board.winScreen || board.overScreen) {
      board.titleScreen = true;
      board.winScreen = false;
      board.overScreen = false;
      return;
    } else if (board.demo) {
      board.demo = false;
      board.New = 1;
      return;
    }

    switch (e.getKeyCode()) {
    case KeyEvent.VK_LEFT:
      board.player.desiredDirection = 'L';
      break;
    case KeyEvent.VK_RIGHT:
      board.player.desiredDirection = 'R';
      break;
    case KeyEvent.VK_UP:
      board.player.desiredDirection = 'U';
      break;
    case KeyEvent.VK_DOWN:
      board.player.desiredDirection = 'D';
      break;
    }

    repaint();
  }

  public void mousePressed(MouseEvent e) {
    if (board.titleScreen || board.winScreen || board.overScreen) {

      return;
    }

    int x = e.getX();
    int y = e.getY();
    if (400 <= y && y <= 460) {
      if (100 <= x && x <= 150) {

        board.New = 1;
      } else if (180 <= x && x <= 300) {

        board.clearHighScores();
      } else if (350 <= x && x <= 420) {

        System.exit(0);
      }
    }
  }

  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}

  public static void main(String[] args) {
    Pacman pacman = new Pacman();
  }
}
