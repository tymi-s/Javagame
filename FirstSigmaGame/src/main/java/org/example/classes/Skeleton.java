package org.example.classes;
import jakarta.persistence.*;


public class Skeleton
{
    /// /////////////////////////////////////PARAMETRY SZKIELETÓW:
    ///
    private int HP;
    private int damage;
    private int moveSpeed;
    private float x,y,targetX,targetY;// target to wspolrzedne gracza
    private boolean active;



    ///  ////////////////////////////////////////////////// //////////////////////GETTERY I SETTERY:
    public boolean getActive(){
        return active;
    }
    public void setActive(boolean active){
        this.active = active;
    }
    public int getHP() {
        return HP;
    }
    public void setHP(int HP) {
        this.HP = HP;
    }
    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public int getMoveSpeed() {
        return moveSpeed;
    }
    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
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
}
