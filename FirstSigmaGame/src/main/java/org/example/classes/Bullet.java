package org.example.classes;

public class Bullet {

    /// //////////////////////////////////////////////////////////////////PARAMETRY POCISKOW:
    private float x, y;
    private float speedX, speedY;
    private int damage;
    private boolean isActive;
    private float speed;

    /// //////////////////////////////////////////////////////////////////METODY:
    public Bullet(float startX, float startY, float targetX, float targetY, int damage, float speed) {
        this.x = startX;
        this.y = startY;
        this.damage = damage;
        this.speed = speed;
        this.isActive = true;

        float distance = (float) Math.sqrt(Math.pow(targetX - startX, 2) + Math.pow(targetY - startY, 2));
        this.speedX = (targetX - startX) / distance * speed;
        this.speedY = (targetY - startY) / distance * speed;

    }
    public void positionUpdate(){
        this.x += speedX;
        this.y += speedY;
    }

    /// /////////////////////////////////////////////////////////////////////////////////////GETTERY I SETTERY
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
    public float getSpeedX() {
        return speedX;
    }
    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }
    public float getSpeedY() {
        return speedY;
    }
    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }
    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
