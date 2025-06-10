package org.example.classes;

import org.example.ui.MenuPanel.DifficultyLevel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/// /////////////////////// CAŁA LOGISTYKA GRY:
public class GameEngine {

    ///////////////////////////////////// ////////////////////////////////////////////////////////PARAMETRY OBIEKTÓW I GRY:

    /// /////////////////POSTAĆ:
    Player player;
    private int score;

    ////////////POCISKI:
    List<Bullet> bullets;

    ///  /////////////////////SZKIELETY:
    List<Skeleton> skeletons;
    private long lastSkeletonSpawn;
    private int SkeletonSpawnRate = 2000; // CZESCOSC SPAWNU SZKIELETOW 2000 = 2 SEC

    /// /////////////////////////////GRA:
    private boolean gameRunning;
    private Random random;
    private InputHandler inputHandler;
    private DifficultyLevel currentDifficulty = DifficultyLevel.EASY;

    /// ////////////////////////////////////////////////////////////////EKRAN:
    private int screenWidth = 800;
    private int screenHeight = 600;


    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////METODY:
    /// ///////////////////////////////////////////KONSTRUKTOR:
    public GameEngine() {
        this.player = new Player();
        this.bullets = new ArrayList<>();
        this.skeletons = new ArrayList<>();
        this.score = 0;
        this.gameRunning = true;
        this.random = new Random();

        initializePlayer();
    }


    /// ////////////////////////////////////////////////////////////////INICJALIZACJA:
    public void setDifficulty(DifficultyLevel difficulty) {
        this.currentDifficulty = difficulty;


        switch (difficulty) {
            case EASY:
                SkeletonSpawnRate = 1500; // co 2 sekundy
                break;
            case HARD:
                SkeletonSpawnRate = 750; // co 1 sekundę
                break;
            case VERY_HARD:
                SkeletonSpawnRate = 250;  // co 0.5 sekundy
                break;
            case INSANE:
                SkeletonSpawnRate = 1;  // co 0.1 sekundy
                break;
        }

        System.out.println("Poziom trudności ustawiony na: " + difficulty +
                " (spawn rate: " + SkeletonSpawnRate + "ms)");
    }
    public void update() {
        if (gameRunning) {
            updatePlayer();
            updateBullets();
            updateSkeletons();
            spawnSkeletons();
            checkCollisions();
            removeDeadObjects();
        } else {
            return;
        }
    }
    private void updateSkeletons() {
        for (Skeleton skeleton : skeletons) {
            // odleglosc
            float deltaX = player.getX() - skeleton.getX();
            float deltaY = player.getY() - skeleton.getY();
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            // ruch
            if (distance > 0) {
                float moveX = (deltaX / distance) * skeleton.getMoveSpeed();
                float moveY = (deltaY / distance) * skeleton.getMoveSpeed();

                skeleton.setX(skeleton.getX() + moveX);
                skeleton.setY(skeleton.getY() + moveY);
            }
        }
    }
    private void updatePlayer() {
        if (inputHandler != null) {
            inputHandler.update();
        }
        if (player.getX() < 0) {
            player.setX(0);
        }
        if (player.getY() < 0) {
            player.setY(0);
        }
        if (player.getX() > screenWidth) {
            player.setX(screenWidth);
        }
        if (player.getY() > screenHeight) {
            player.setY(screenHeight);
        }
    }
    private void updateBullets() {
        for (Bullet bullet : bullets) {
            bullet.positionUpdate();
            if (bullet.getX() > screenWidth || bullet.getX() < 0 || bullet.getY() > screenHeight || bullet.getY() < 0) {
                bullet.setActive(false);
            }
        }
    }
    private void initializePlayer() {
        player.setX(screenWidth / 2);
        player.setY(screenHeight / 2);

        player.setMaxHP(100);
        player.setAlive(true);
        player.setAtackSpeed(100);
        player.setCurrentHP(100);
        player.setMoveSpeed(3);
        player.setDamage(25);
        player.setLastShotTime(0); // Reset ostatniego czasu strzału
    }
    private void spawnSkeletons() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSkeletonSpawn > SkeletonSpawnRate) {
            spawnRandomSkeleton();
            lastSkeletonSpawn = currentTime;
        }
    }
    private void spawnRandomSkeleton() {
        Skeleton skeleton = new Skeleton();
        int screenSide = random.nextInt(4);
        float x, y;

        switch (screenSide) {
            //góra
            case 0:
                x = random.nextFloat() * screenWidth;
                y = -50;
                break;
            //prawo
            case 1:
                x = screenWidth + 50;
                y = random.nextFloat() * screenHeight;
                break;
            //lewo
            case 2:
                x = -50;
                y = random.nextFloat() * screenHeight;
                break;
            //dół
            default:
                x = random.nextFloat() * screenWidth;
                y = screenHeight + 50;
                break;
        }

        skeleton.setX(x);
        skeleton.setY(y);
        skeleton.setHP(20);
        skeleton.setDamage(1);
        skeleton.setMoveSpeed(1);
        skeleton.setActive(true);
        skeletons.add(skeleton);
    }
    public void resetGame() {

        bullets.clear();
        skeletons.clear();

        score = 0;
        gameRunning = true;
        lastSkeletonSpawn = 0;


        initializePlayer();

        System.out.println("Gra została zresetowana na poziomie: " + currentDifficulty);
    }



    //////////////////////////////////////////////////////////////////////////////////INNE METODY:
    // obrażenia i dodanie punktów
    private void checkCollisions() {
        //obrażenia szkieleta
        for (Bullet bullet : bullets) {
            if (!bullet.isActive()) continue;
            for (Skeleton skeleton : skeletons) {
                if (isColliding(bullet.getX(), bullet.getY(), skeleton.getX(), skeleton.getY(), 20)) {
                    skeleton.setHP(skeleton.getHP() - bullet.getDamage());//obrażenia
                    bullet.setActive(false);//usuniece pocisku
                }

                // dodanie punktów
                if (skeleton.getHP() <= 0) {
                    score += 15;
                    skeleton.setActive(false);
                }

            }
        }
        // obrażenia gracza
        for (Skeleton skeleton : skeletons) {
            if (isColliding(player.getX(), player.getY(), skeleton.getX(), skeleton.getY(), 25)) {
                player.setCurrentHP(player.getCurrentHP() - skeleton.getDamage());
                if (player.getCurrentHP() <= 0) {
                    gameRunning = false;
                    player.setAlive(false);
                }
            }
        }
    }
    private boolean isColliding(float x1, float y1, float x2, float y2, float radius) {
        float distance = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance < radius;
    }
    private void removeDeadObjects() {
        bullets.removeIf(bullet -> !bullet.isActive());
        skeletons.removeIf(skeleton -> skeleton.getHP() <= 0);
    }
    public void shoot(float targetX, float targetY) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - player.getLastShotTime() > (1000 / player.getAtackSpeed())) {
            Bullet bullet = new Bullet(player.getX(), player.getY(), targetX, targetY, player.getDamage(), 5.0f);

            bullets.add(bullet);
            player.setLastShotTime(currentTime);
        }
    }



/// //////////////////////////////////////////////////////////////////////////GETTERY I SETTERY:
    public List<Skeleton> getSkeletons() { return skeletons; }
    public List<Bullet> getBullets() { return bullets; }
    public Player getPlayer() { return player; }
    public int getScore() { return score; }
    public boolean isGameRunning() { return gameRunning; }
    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }
    public void stopGame() {
        this.gameRunning = false;
    }
    public void startGame() {
        this.gameRunning =true;
    }
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }
    public DifficultyLevel getDifficulty() {
        return currentDifficulty;
    }
}