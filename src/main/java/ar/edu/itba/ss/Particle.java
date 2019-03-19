package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;

public class Particle {

    private static Long ID_ACUM = Long.valueOf(0);

    private Long id;
    private Double radio;
    private String property;
    private List<State> states = new ArrayList<>();

    public Particle(Double radio, String property, Double x, Double y, Double vx, Double vy) {
        this.id = ID_ACUM++;
        this.radio = radio;
        this.property = property;
        states.add(new State(x, y, vx, vy));
    }

    public Particle(Double radio, String property) {
        this.radio = radio;
        this.property = property;
    }

    public Double getRadio() {
        return radio;
    }

    public Object getProperty() {
        return property;
    }

    public List<State> getStates() {
        return states;
    }

    public void addState(Double x, Double y, Double vx, Double vy){
        states.add(new State(x,y,vx,vy));
    }

    public void move(double size) {
        State lastState = states.get(states.size()-1);
        final double auxX = (lastState.x + (Math.cos(lastState.speedAngle) * lastState.speedModule)) % size;
        final double auxY = (lastState.y + (Math.sin(lastState.speedAngle) * lastState.speedModule)) % size;
        double x = auxX < 0 ? auxX + size : auxX;
        double y = auxY < 0 ? auxY + size : auxY;

        states.add(new State(x,y,lastState));
    }

    public State getLastState(){
       return states.get(states.size()-1);
    }

    public Long getId() {
        return id;
    }

    class State{
        private final Double x;
        private final Double y;

        private final Double vx;
        private final Double vy;

        private final Double speedModule;
        private final Double speedAngle;

        public State(Double x, Double y, Double vx, Double vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.speedModule = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
            this.speedAngle = Math.atan2(vy,vx);
        }

        public State(Double x, Double y, State previous) {
            this.x = x;
            this.y = y;
            this.vx = previous.vx;
            this.vy = previous.vy;
            this.speedModule = previous.speedModule;
            this.speedAngle = previous.speedAngle;
        }

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }

        public Double getVx() {
            return vx;
        }

        public Double getVy() {
            return vy;
        }

        public Double getSpeedModule() {
            return speedModule;
        }

        public Double getSpeedAngle() {
            return speedAngle;
        }
    }


}
