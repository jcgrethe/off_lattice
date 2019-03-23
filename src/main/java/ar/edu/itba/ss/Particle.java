package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Particle {

    private static Long ID_ACUM = Long.valueOf(0);

    private Long id;
    private Double radio;
    private String property;
    private List<State> states = new ArrayList<>();

    public Particle(Double radio, String property, Double x, Double y, Double speedModule, Double speedAngle) {
        this.id = ID_ACUM++;
        this.radio = radio;
        this.property = property;
        states.add(new State(x, y, speedModule, speedAngle));
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

    public void move(double size, double noise, List<Particle> neighbors,int time) {
        State lastState = getLastState(time);
        final double auxX = (lastState.x + lastState.vx ) % size;
        final double auxY = (lastState.y + lastState.vy) % size;
        double x = auxX < 0 ? auxX + size : auxX;
        double y = auxY < 0 ? auxY + size : auxY;

        double angle = newAngle(noise, -noise, neighbors,time);
        states.add(new State(x,y,lastState.speedModule,angle));
    }

    private double newAngle(double maxNoise, double minNoise, List <Particle> neighbors, int time ) {
        double randomNoise = minNoise + (new Random().nextDouble() * (maxNoise - minNoise));
        return average(neighbors,time) + randomNoise;
    }

    private double average(List<Particle> neighbors,int time){
        Double avgSin = 0.0;
        for (Particle particle : neighbors)
            avgSin += Math.sin(particle.getLastState(time).getSpeedAngle());
        avgSin += Math.sin(getLastState(time).getSpeedAngle());
        avgSin/=(neighbors.size() + 1);

        Double avgCos = 0.0;
        for (Particle particle : neighbors)
            avgCos += Math.cos(particle.getLastState(time).getSpeedAngle());
        avgCos += Math.cos(getLastState(time).getSpeedAngle());
        avgCos/=(neighbors.size() + 1);

        return Math.atan2(avgSin, avgCos);
    }

    public State getLastState(int time){
       return states.get(time);
    }

    public Long getId() {
        return id;
    }

    class State{
        private final Double x;
        private final Double y;

        private final double vx;
        private final double vy;
        private final Double speedModule;
        private Double speedAngle;


        public State(Double x, Double y, double speedModule, double speedAngle) {
            this.x = x;
            this.y = y;
            this.speedAngle = speedAngle;
            this.speedModule = speedModule;
            this.vx = Math.cos(speedAngle) * speedModule;
            this.vy = Math.sin(speedAngle) * speedModule;
        }

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }

        public Double getSpeedModule() {
            return speedModule;
        }

        public Double getSpeedAngle() {
            return speedAngle;
        }

        public double getVx() {
            return vx;
        }

        public double getVy() {
            return vy;
        }
    }


}
