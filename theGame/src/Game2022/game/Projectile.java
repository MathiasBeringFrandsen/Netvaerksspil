package Game2022.game;

public class Projectile {

    pair location;
    String direction;

    public Projectile(pair location, String direction){
        this.location = location;
        this.direction = direction;
    }

    public pair getLocation() {
        return location;
    }

    public void setLocation(pair location) {
        this.location = location;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
