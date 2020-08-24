import java.awt. * ;
import javax.swing.JPanel;
import java.lang.Math;
import java.util. * ;
import java.io. * ;

class Mover {

  int frameCount = 0;

  boolean[][] state;

  int gridSize;
  int max;
  int increment;

  public Mover() {
    gridSize = 20;
    increment = 4;
    max = 400;
    state = new boolean[20][20];
    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < 20; j++) {
        state[i][j] = false;
      }
    }
  }

  public void updateState(boolean[][] state) {
    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < 20; j++) {
        this.state[i][j] = state[i][j];
      }
    }
  }

  public boolean isValidDest(int x, int y) {

    if ((((x) % 20 == 0) || ((y) % 20) == 0) && 20 <= x && x < 400 && 20 <= y && y < 400 && state[x / 20 - 1][y / 20 - 1]) {
      return true;
    }
    return false;
  }
}

class Player extends Mover {

  char direction;
  char currDirection;
  char desiredDirection;

  int pelletsEaten;

  int lastX;
  int lastY;

  int x;
  int y;

  int pelletX;
  int pelletY;

  boolean teleport;

  boolean stopped = false;

  public Player(int x, int y) {

    teleport = false;
    pelletsEaten = 0;
    pelletX = x / gridSize - 1;
    pelletY = y / gridSize - 1;
    this.lastX = x;
    this.lastY = y;
    this.x = x;
    this.y = y;
    currDirection = 'L';
    desiredDirection = 'L';
  }

  public char newDirection() {
    int random;
    char backwards = 'U';
    int newX = x,
    newY = y;
    int lookX = x,
    lookY = y;
    Set < Character > set = new HashSet < Character > ();
    switch (direction) {
    case 'L':
      backwards = 'R';
      break;
    case 'R':
      backwards = 'L';
      break;
    case 'U':
      backwards = 'D';
      break;
    case 'D':
      backwards = 'U';
      break;
    }
    char newDirection = backwards;
    while (newDirection == backwards || !isValidDest(lookX, lookY)) {
      if (set.size() == 3) {
        newDirection = backwards;
        break;
      }
      newX = x;
      newY = y;
      lookX = x;
      lookY = y;
      random = (int)(Math.random() * 4) + 1;
      if (random == 1) {
        newDirection = 'L';
        newX -= increment;
        lookX -= increment;
      } else if (random == 2) {
        newDirection = 'R';
        newX += increment;
        lookX += gridSize;
      } else if (random == 3) {
        newDirection = 'U';
        newY -= increment;
        lookY -= increment;
      } else if (random == 4) {
        newDirection = 'D';
        newY += increment;
        lookY += gridSize;
      }
      if (newDirection != backwards) {
        set.add(newDirection);
      }
    }
    return newDirection;
  }

  public boolean isChoiceDest() {
    if (x % gridSize == 0 && y % gridSize == 0) {
      return true;
    }
    return false;
  }

  public void demoMove() {
    lastX = x;
    lastY = y;
    if (isChoiceDest()) {
      direction = newDirection();
    }
    switch (direction) {
    case 'L':
      if (isValidDest(x - increment, y)) {
        x -= increment;
      } else if (y == 9 * gridSize && x < 2 * gridSize) {
        x = max - gridSize * 1;
        teleport = true;
      }
      break;
    case 'R':
      if (isValidDest(x + gridSize, y)) {
        x += increment;
      } else if (y == 9 * gridSize && x > max - gridSize * 2) {
        x = 1 * gridSize;
        teleport = true;
      }
      break;
    case 'U':
      if (isValidDest(x, y - increment)) y -= increment;
      break;
    case 'D':
      if (isValidDest(x, y + gridSize)) y += increment;
      break;
    }
    currDirection = direction;
    frameCount++;
  }

  public void move() {
    int gridSize = 20;
    lastX = x;
    lastY = y;

    if (x % 20 == 0 && y % 20 == 0 ||

    (desiredDirection == 'L' && currDirection == 'R') || (desiredDirection == 'R' && currDirection == 'L') || (desiredDirection == 'U' && currDirection == 'D') || (desiredDirection == 'D' && currDirection == 'U')) {
      switch (desiredDirection) {
      case 'L':
        if (isValidDest(x - increment, y)) x -= increment;
        break;
      case 'R':
        if (isValidDest(x + gridSize, y)) x += increment;
        break;
      case 'U':
        if (isValidDest(x, y - increment)) y -= increment;
        break;
      case 'D':
        if (isValidDest(x, y + gridSize)) y += increment;
        break;
      }
    }

    if (lastX == x && lastY == y) {
      switch (currDirection) {
      case 'L':
        if (isValidDest(x - increment, y)) x -= increment;
        else if (y == 9 * gridSize && x < 2 * gridSize) {
          x = max - gridSize * 1;
          teleport = true;
        }
        break;
      case 'R':
        if (isValidDest(x + gridSize, y)) x += increment;
        else if (y == 9 * gridSize && x > max - gridSize * 2) {
          x = 1 * gridSize;
          teleport = true;
        }
        break;
      case 'U':
        if (isValidDest(x, y - increment)) y -= increment;
        break;
      case 'D':
        if (isValidDest(x, y + gridSize)) y += increment;
        break;
      }
    } else {
      currDirection = desiredDirection;
    }

    if (lastX == x && lastY == y) stopped = true;

    else {
      stopped = false;
      frameCount++;
    }
  }

  public void updatePellet() {
    if (x % gridSize == 0 && y % gridSize == 0) {
      pelletX = x / gridSize - 1;
      pelletY = y / gridSize - 1;
    }
  }
}

class Ghost extends Mover {

  char direction;

  int lastX;
  int lastY;

  int x;
  int y;

  int pelletX,
  pelletY;

  int lastPelletX,
  lastPelletY;

  public Ghost(int x, int y) {
    direction = 'L';
    pelletX = x / gridSize - 1;
    pelletY = x / gridSize - 1;
    lastPelletX = pelletX;
    lastPelletY = pelletY;
    this.lastX = x;
    this.lastY = y;
    this.x = x;
    this.y = y;
  }

  public void updatePellet() {
    int tempX,
    tempY;
    tempX = x / gridSize - 1;
    tempY = y / gridSize - 1;
    if (tempX != pelletX || tempY != pelletY) {
      lastPelletX = pelletX;
      lastPelletY = pelletY;
      pelletX = tempX;
      pelletY = tempY;
    }

  }

  public boolean isChoiceDest() {
    if (x % gridSize == 0 && y % gridSize == 0) {
      return true;
    }
    return false;
  }

  public char newDirection() {
    int random;
    char backwards = 'U';
    int newX = x,
    newY = y;
    int lookX = x,
    lookY = y;
    Set < Character > set = new HashSet < Character > ();
    switch (direction) {
    case 'L':
      backwards = 'R';
      break;
    case 'R':
      backwards = 'L';
      break;
    case 'U':
      backwards = 'D';
      break;
    case 'D':
      backwards = 'U';
      break;
    }

    char newDirection = backwards;

    while (newDirection == backwards || !isValidDest(lookX, lookY)) {

      if (set.size() == 3) {
        newDirection = backwards;
        break;
      }

      newX = x;
      newY = y;
      lookX = x;
      lookY = y;

      random = (int)(Math.random() * 4) + 1;
      if (random == 1) {
        newDirection = 'L';
        newX -= increment;
        lookX -= increment;
      } else if (random == 2) {
        newDirection = 'R';
        newX += increment;
        lookX += gridSize;
      } else if (random == 3) {
        newDirection = 'U';
        newY -= increment;
        lookY -= increment;
      } else if (random == 4) {
        newDirection = 'D';
        newY += increment;
        lookY += gridSize;
      }
      if (newDirection != backwards) {
        set.add(newDirection);
      }
    }
    return newDirection;
  }

  public void move() {
    lastX = x;
    lastY = y;

    if (isChoiceDest()) {
      direction = newDirection();
    }

    switch (direction) {
    case 'L':
      if (isValidDest(x - increment, y)) x -= increment;
      break;
    case 'R':
      if (isValidDest(x + gridSize, y)) x += increment;
      break;
    case 'U':
      if (isValidDest(x, y - increment)) y -= increment;
      break;
    case 'D':
      if (isValidDest(x, y + gridSize)) y += increment;
      break;
    }
  }
}

public class Board extends JPanel {

  Image pacmanImage = Toolkit.getDefaultToolkit().getImage("img/pacman.jpg");
  Image pacmanUpImage = Toolkit.getDefaultToolkit().getImage("img/pacmanup.jpg");
  Image pacmanDownImage = Toolkit.getDefaultToolkit().getImage("img/pacmandown.jpg");
  Image pacmanLeftImage = Toolkit.getDefaultToolkit().getImage("img/pacmanleft.jpg");
  Image pacmanRightImage = Toolkit.getDefaultToolkit().getImage("img/pacmanright.jpg");
  Image ghost10 = Toolkit.getDefaultToolkit().getImage("img/ghost10.jpg");
  Image ghost20 = Toolkit.getDefaultToolkit().getImage("img/ghost20.jpg");
  Image ghost30 = Toolkit.getDefaultToolkit().getImage("img/ghost30.jpg");
  Image ghost40 = Toolkit.getDefaultToolkit().getImage("img/ghost40.jpg");
  Image ghost11 = Toolkit.getDefaultToolkit().getImage("img/ghost11.jpg");
  Image ghost21 = Toolkit.getDefaultToolkit().getImage("img/ghost21.jpg");
  Image ghost31 = Toolkit.getDefaultToolkit().getImage("img/ghost31.jpg");
  Image ghost41 = Toolkit.getDefaultToolkit().getImage("img/ghost41.jpg");
  Image titleScreenImage = Toolkit.getDefaultToolkit().getImage("img/titlescreen.jpg");
  Image gameOverImage = Toolkit.getDefaultToolkit().getImage("img/gameover.jpg");
  Image winScreenImage = Toolkit.getDefaultToolkit().getImage("img/winScreen.jpg");

  Player player = new Player(200, 300);
  Ghost ghost1 = new Ghost(180, 180);
  Ghost ghost2 = new Ghost(200, 180);
  Ghost ghost3 = new Ghost(220, 180);
  Ghost ghost4 = new Ghost(220, 180);

  long timer = System.currentTimeMillis();

  int dying = 0;

  int currScore;
  int highScore;

  boolean clearHighScores = false;

  int numLives = 2;

  boolean[][] state;

  boolean[][] pellets;

  int gridSize;
  int max;

  boolean stopped;
  boolean titleScreen;
  boolean winScreen = false;
  boolean overScreen = false;
  boolean demo = false;
  int New;

  int lastPelletEatenX = 0;
  int lastPelletEatenY = 0;

  Font font = new Font("Monospaced", Font.BOLD, 12);

  public Board() {
    initHighScores();
    currScore = 0;
    stopped = false;
    max = 400;
    gridSize = 20;
    New = 0;
    titleScreen = true;
  }

  public void initHighScores() {
    File file = new File("scores/highscores.txt");
    Scanner sc;
    try {
      sc = new Scanner(file);
      highScore = sc.nextInt();
      sc.close();
    } catch(Exception e) {}
  }

  public void updateScore(int score) {
    PrintWriter out;
    try {
      out = new PrintWriter("scores/highscores.txt");
      out.println(score);
      out.close();
    } catch(Exception e) {}
    highScore = score;
    clearHighScores = true;
  }

  public void clearHighScores() {
    PrintWriter out;
    try {
      out = new PrintWriter("scores/highscores.txt");
      out.println("0");
      out.close();
    } catch(Exception e) {}
    highScore = 0;
    clearHighScores = true;
  }

  public void reset() {
    numLives = 2;
    state = new boolean[20][20];
    pellets = new boolean[20][20];

    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < 20; j++) {
        state[i][j] = true;
        pellets[i][j] = true;
      }
    }

    for (int i = 5; i < 14; i++) {
      for (int j = 5; j < 12; j++) {
        pellets[i][j] = false;
      }
    }
    pellets[9][7] = false;
    pellets[8][8] = false;
    pellets[9][8] = false;
    pellets[10][8] = false;

  }

  public void updateMap(int x, int y, int width, int height) {
    for (int i = x / gridSize; i < x / gridSize + width / gridSize; i++) {
      for (int j = y / gridSize; j < y / gridSize + height / gridSize; j++) {
        state[i - 1][j - 1] = false;
        pellets[i - 1][j - 1] = false;
      }
    }
  }

  public void drawLives(Graphics g) {
    g.setColor(Color.BLACK);

    g.fillRect(0, max + 5, 600, gridSize);
    g.setColor(Color.YELLOW);
    for (int i = 0; i < numLives; i++) {

      g.fillOval(gridSize * (i + 1), max + 5, gridSize, gridSize);
    }

    g.setColor(Color.YELLOW);
    g.setFont(font);
    g.drawString("Reset", 100, max + 5 + gridSize);
    g.drawString("Clear High Scores", 180, max + 5 + gridSize);
    g.drawString("Exit", 350, max + 5 + gridSize);
  }

  public void drawBoard(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 600, 600);
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 420, 420);

    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 20, 600);
    g.fillRect(0, 0, 600, 20);
    g.setColor(Color.WHITE);
    g.drawRect(19, 19, 382, 382);
    g.setColor(Color.BLUE);

    g.fillRect(40, 40, 60, 20);
    updateMap(40, 40, 60, 20);
    g.fillRect(120, 40, 60, 20);
    updateMap(120, 40, 60, 20);
    g.fillRect(200, 20, 20, 40);
    updateMap(200, 20, 20, 40);
    g.fillRect(240, 40, 60, 20);
    updateMap(240, 40, 60, 20);
    g.fillRect(320, 40, 60, 20);
    updateMap(320, 40, 60, 20);
    g.fillRect(40, 80, 60, 20);
    updateMap(40, 80, 60, 20);
    g.fillRect(160, 80, 100, 20);
    updateMap(160, 80, 100, 20);
    g.fillRect(200, 80, 20, 60);
    updateMap(200, 80, 20, 60);
    g.fillRect(320, 80, 60, 20);
    updateMap(320, 80, 60, 20);

    g.fillRect(20, 120, 80, 60);
    updateMap(20, 120, 80, 60);
    g.fillRect(320, 120, 80, 60);
    updateMap(320, 120, 80, 60);
    g.fillRect(20, 200, 80, 60);
    updateMap(20, 200, 80, 60);
    g.fillRect(320, 200, 80, 60);
    updateMap(320, 200, 80, 60);

    g.fillRect(160, 160, 40, 20);
    updateMap(160, 160, 40, 20);
    g.fillRect(220, 160, 40, 20);
    updateMap(220, 160, 40, 20);
    g.fillRect(160, 180, 20, 20);
    updateMap(160, 180, 20, 20);
    g.fillRect(160, 200, 100, 20);
    updateMap(160, 200, 100, 20);
    g.fillRect(240, 180, 20, 20);
    updateMap(240, 180, 20, 20);
    g.setColor(Color.BLUE);

    g.fillRect(120, 120, 60, 20);
    updateMap(120, 120, 60, 20);
    g.fillRect(120, 80, 20, 100);
    updateMap(120, 80, 20, 100);
    g.fillRect(280, 80, 20, 100);
    updateMap(280, 80, 20, 100);
    g.fillRect(240, 120, 60, 20);
    updateMap(240, 120, 60, 20);

    g.fillRect(280, 200, 20, 60);
    updateMap(280, 200, 20, 60);
    g.fillRect(120, 200, 20, 60);
    updateMap(120, 200, 20, 60);
    g.fillRect(160, 240, 100, 20);
    updateMap(160, 240, 100, 20);
    g.fillRect(200, 260, 20, 40);
    updateMap(200, 260, 20, 40);

    g.fillRect(120, 280, 60, 20);
    updateMap(120, 280, 60, 20);
    g.fillRect(240, 280, 60, 20);
    updateMap(240, 280, 60, 20);

    g.fillRect(40, 280, 60, 20);
    updateMap(40, 280, 60, 20);
    g.fillRect(80, 280, 20, 60);
    updateMap(80, 280, 20, 60);
    g.fillRect(320, 280, 60, 20);
    updateMap(320, 280, 60, 20);
    g.fillRect(320, 280, 20, 60);
    updateMap(320, 280, 20, 60);

    g.fillRect(20, 320, 40, 20);
    updateMap(20, 320, 40, 20);
    g.fillRect(360, 320, 40, 20);
    updateMap(360, 320, 40, 20);
    g.fillRect(160, 320, 100, 20);
    updateMap(160, 320, 100, 20);
    g.fillRect(200, 320, 20, 60);
    updateMap(200, 320, 20, 60);

    g.fillRect(40, 360, 140, 20);
    updateMap(40, 360, 140, 20);
    g.fillRect(240, 360, 140, 20);
    updateMap(240, 360, 140, 20);
    g.fillRect(280, 320, 20, 40);
    updateMap(280, 320, 20, 60);
    g.fillRect(120, 320, 20, 60);
    updateMap(120, 320, 20, 60);
    drawLives(g);
  }

  public void drawPellets(Graphics g) {
    g.setColor(Color.YELLOW);
    for (int i = 1; i < 20; i++) {
      for (int j = 1; j < 20; j++) {
        if (pellets[i - 1][j - 1]) g.fillOval(i * 20 + 8, j * 20 + 8, 4, 4);
      }
    }
  }

  public void fillPellet(int x, int y, Graphics g) {
    g.setColor(Color.YELLOW);
    g.fillOval(x * 20 + 28, y * 20 + 28, 4, 4);
  }

  public void paint(Graphics g) {

    if (dying > 0) {

      g.drawImage(pacmanImage, player.x, player.y, Color.BLACK, null);
      g.setColor(Color.BLACK);

      if (dying == 4) g.fillRect(player.x, player.y, 20, 7);
      else if (dying == 3) g.fillRect(player.x, player.y, 20, 14);
      else if (dying == 2) g.fillRect(player.x, player.y, 20, 20);
      else if (dying == 1) {
        g.fillRect(player.x, player.y, 20, 20);
      }

      long currTime = System.currentTimeMillis();
      long temp;
      if (dying != 1) temp = 100;
      else temp = 2000;

      if (currTime - timer >= temp) {
        dying--;
        timer = currTime;

        if (dying == 0) {
          if (numLives == -1) {

            if (demo) numLives = 2;
            else {

              if (currScore > highScore) {
                updateScore(currScore);
              }
              overScreen = true;
            }
          }
        }
      }
      return;
    }

    if (titleScreen) {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 600, 600);
      g.drawImage(titleScreenImage, 0, 0, Color.BLACK, null);

      New = 1;
      return;
    } else if (winScreen) {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 600, 600);
      g.drawImage(winScreenImage, 0, 0, Color.BLACK, null);
      New = 1;
      return;
    } else if (overScreen) {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 600, 600);
      g.drawImage(gameOverImage, 0, 0, Color.BLACK, null);
      New = 1;
      return;
    }

    if (clearHighScores) {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 600, 18);
      g.setColor(Color.YELLOW);
      g.setFont(font);
      clearHighScores = false;
      if (demo) g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME -----\t High Score: " + highScore, 20, 10);
      else g.drawString("Score: " + (currScore) + "\t High Score: " + highScore, 20, 10);
    }

    boolean oops = false;

    if (New == 1) {
      reset();
      player = new Player(200, 300);
      ghost1 = new Ghost(180, 180);
      ghost2 = new Ghost(200, 180);
      ghost3 = new Ghost(220, 180);
      ghost4 = new Ghost(220, 180);
      currScore = 0;
      drawBoard(g);
      drawPellets(g);
      drawLives(g);

      player.updateState(state);

      player.state[9][7] = false;
      ghost1.updateState(state);
      ghost2.updateState(state);
      ghost3.updateState(state);
      ghost4.updateState(state);

      g.setColor(Color.YELLOW);
      g.setFont(font);
      if (demo) g.drawString("PRESS ANY KEY TO START A GAME -----\t High Score: " + highScore, 20, 10);
      else g.drawString("Score: " + (currScore) + "\t High Score: " + highScore, 20, 10);
      New++;
    } else if (New == 2) {
      New++;
    } else if (New == 3) {
      New++;
      timer = System.currentTimeMillis();
      return;
    } else if (New == 4) {

      long currTime = System.currentTimeMillis();
      if (currTime - timer >= 5000) {
        New = 0;
      } else return;
    }

    g.copyArea(player.x - 20, player.y - 20, 80, 80, 0, 0);
    g.copyArea(ghost1.x - 20, ghost1.y - 20, 80, 80, 0, 0);
    g.copyArea(ghost2.x - 20, ghost2.y - 20, 80, 80, 0, 0);
    g.copyArea(ghost3.x - 20, ghost3.y - 20, 80, 80, 0, 0);
    g.copyArea(ghost4.x - 20, ghost4.y - 20, 80, 80, 0, 0);

    if (player.x == ghost1.x && Math.abs(player.y - ghost1.y) < 10) oops = true;
    else if (player.x == ghost2.x && Math.abs(player.y - ghost2.y) < 10) oops = true;
    else if (player.x == ghost3.x && Math.abs(player.y - ghost3.y) < 10) oops = true;
    else if (player.x == ghost4.x && Math.abs(player.y - ghost4.y) < 10) oops = true;
    else if (player.y == ghost1.y && Math.abs(player.x - ghost1.x) < 10) oops = true;
    else if (player.y == ghost2.y && Math.abs(player.x - ghost2.x) < 10) oops = true;
    else if (player.y == ghost3.y && Math.abs(player.x - ghost3.x) < 10) oops = true;
    else if (player.y == ghost4.y && Math.abs(player.x - ghost4.x) < 10) oops = true;

    if (oops && !stopped) {

      dying = 4;

      numLives--;
      stopped = true;
      drawLives(g);
      timer = System.currentTimeMillis();
    }

    g.setColor(Color.BLACK);
    g.fillRect(player.lastX, player.lastY, 20, 20);
    g.fillRect(ghost1.lastX, ghost1.lastY, 20, 20);
    g.fillRect(ghost2.lastX, ghost2.lastY, 20, 20);
    g.fillRect(ghost3.lastX, ghost3.lastY, 20, 20);
    g.fillRect(ghost4.lastX, ghost4.lastY, 20, 20);

    if (pellets[player.pelletX][player.pelletY] && New != 2 && New != 3) {
      lastPelletEatenX = player.pelletX;
      lastPelletEatenY = player.pelletY;

      player.pelletsEaten++;

      pellets[player.pelletX][player.pelletY] = false;

      currScore += 50;

      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 600, 20);
      g.setColor(Color.YELLOW);
      g.setFont(font);
      if (demo) g.drawString("PRESS ANY KEY TO START A GAME -----\t High Score: " + highScore, 20, 10);
      else g.drawString("Score: " + (currScore) + "\t High Score: " + highScore, 20, 10);

      if (player.pelletsEaten == 173) {

        if (!demo) {
          if (currScore > highScore) {
            updateScore(currScore);
          }
          winScreen = true;
        } else {
          titleScreen = true;
        }
        return;
      }
    }

    if (pellets[ghost1.lastPelletX][ghost1.lastPelletY]) fillPellet(ghost1.lastPelletX, ghost1.lastPelletY, g);
    if (pellets[ghost2.lastPelletX][ghost2.lastPelletY]) fillPellet(ghost2.lastPelletX, ghost2.lastPelletY, g);
    if (pellets[ghost3.lastPelletX][ghost3.lastPelletY]) fillPellet(ghost3.lastPelletX, ghost3.lastPelletY, g);
    if (pellets[ghost4.lastPelletX][ghost4.lastPelletY]) fillPellet(ghost4.lastPelletX, ghost4.lastPelletY, g);

    if (ghost1.frameCount < 5) {

      g.drawImage(ghost10, ghost1.x, ghost1.y, Color.BLACK, null);
      g.drawImage(ghost20, ghost2.x, ghost2.y, Color.BLACK, null);
      g.drawImage(ghost30, ghost3.x, ghost3.y, Color.BLACK, null);
      g.drawImage(ghost40, ghost4.x, ghost4.y, Color.BLACK, null);
      ghost1.frameCount++;
    } else {

      g.drawImage(ghost11, ghost1.x, ghost1.y, Color.BLACK, null);
      g.drawImage(ghost21, ghost2.x, ghost2.y, Color.BLACK, null);
      g.drawImage(ghost31, ghost3.x, ghost3.y, Color.BLACK, null);
      g.drawImage(ghost41, ghost4.x, ghost4.y, Color.BLACK, null);
      if (ghost1.frameCount >= 10) ghost1.frameCount = 0;
      else ghost1.frameCount++;
    }

    if (player.frameCount < 5) {

      g.drawImage(pacmanImage, player.x, player.y, Color.BLACK, null);
    } else {

      if (player.frameCount >= 10) player.frameCount = 0;

      switch (player.currDirection) {
      case 'L':
        g.drawImage(pacmanLeftImage, player.x, player.y, Color.BLACK, null);
        break;
      case 'R':
        g.drawImage(pacmanRightImage, player.x, player.y, Color.BLACK, null);
        break;
      case 'U':
        g.drawImage(pacmanUpImage, player.x, player.y, Color.BLACK, null);
        break;
      case 'D':
        g.drawImage(pacmanDownImage, player.x, player.y, Color.BLACK, null);
        break;
      }
    }

    g.setColor(Color.WHITE);
    g.drawRect(19, 19, 382, 382);

  }
}
