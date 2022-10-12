package Game2022.game;

public class Chest {
    private pair location;

    public Chest(pair location){
        this.location = location;
    }

    public pair getLocation() {
        return location;
    }

    public void setLocation(pair location) {
        this.location = location;
    }
}
