package org.example.classes;
import jakarta.persistence.*;

/// ////////////////////////////////////////////////////////////ZMIENNE DO BAZY DANYCH:
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "player_name")
    private String playerName;

//////////////////////////////////////////////////////////////////////// DANEM URZYTKOWNIKA:

    private String playerSurname;
    private int playerAge;
    private String playerGender;
    private String playerPassword;
    private String playerScore;


    ///////////////////////////////////////////////////////////////PARAMETRY POSTACI W GRZE:
    private int atackSpeed;
    private int moveSpeed;
    private int damage;

    private int currentHP,MaxHP;

    /// ///////////////////////////////////////////////////PARAMETRY RYUCHU I STRZELANIA:
    private long lastShotTime;
    private float x,y,rotation;
    private boolean isAlive;

    //////////////////////////////////////////////////////////////////// KONSTRUKTOWY I METODY:
    public Player(){}
    public Player(String name,String surname, int age, String gender, String password, String score, int atackSpeed, int moveSpeed, int damage) {
        this.playerName = name;
        this.playerSurname = surname;
        this.playerAge = age;
        this.playerGender = gender;
        this.playerPassword = password;
        this.playerScore = score;
        this.atackSpeed = atackSpeed;
        this.moveSpeed = moveSpeed;
        this.damage = damage;
    }

    public boolean isAlive() {
        return isAlive;
    }



    /// /////////////////////////////////////////////////////////////////// GETERY I SETERY:
    public long getLastShotTime() {
        return lastShotTime;
    }
    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
    }
    public int getAtackSpeed() {
        return atackSpeed;
    }
    public int getMaxHP() {
        return MaxHP;
    }
    public void setMaxHP(int maxHP) {
        MaxHP = maxHP;
    }
    public void setCurrentHP(int currentHP) {this.currentHP = currentHP;}
    public void setAtackSpeed(int atackSpeed) {
        this.atackSpeed = atackSpeed;
    }
    public int getMoveSpeed() {
        return moveSpeed;
    }
    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public int getCurrentHP() {
        return currentHP;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public void setPlayerSurname(String playerSurname) {
        this.playerSurname = playerSurname;
    }
    public void setPlayerAge(int playerAge) {
        this.playerAge = playerAge;
    }
    public void setPlayerGender(String playerGender) {
        this.playerGender = playerGender;
    }
    public void setPlayerScore(String playerScore) {
        this.playerScore = playerScore;
    }
    public void setPlayerPassword(String playerPassword) {
        this.playerPassword = playerPassword;
    }

}
