package ar.edu.itba.ss;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Input {

    // Defined values
    private static int defaultIterations = 1000;
    private static Double defaultInteractionRadio = 1.0;
    private static Double MAX_PARTICLE_RADIO = 0.5;
    private static Double MIN_PARTICLE_RADIO = 0.2;
    private static Double defaultVelocityModule = 0.03;
    private static Double MIN_ANGLE = 0.0;
    private static Double MAX_ANGLE = Math.PI * 2;
    private double noise;

    private Long particlesQuantity;
    private int cellSideQuantity;
    private List<Particle> particles;
    private Boolean contornCondition;
    private int systemSideLength;
    private Double interactionRadio;

    /**
     * Empty constructor generates random inputs based in the max and min setted for each variable.
     */
    public Input(double noise, int quantity,int length){
        Random random = new Random();
        this.noise=noise;
        this.systemSideLength = length;
        this.particlesQuantity = Long.valueOf(quantity);
        this.contornCondition = true;
        this.interactionRadio = defaultInteractionRadio;
        this.cellSideQuantity = (int) Math.ceil(systemSideLength/interactionRadio) - 1 ;
        this.particles = new ArrayList<>();
        // set particles with random position and speed angle
        for (int p = 0 ; p < this.particlesQuantity ; p++ ){
            this.particles.add(new Particle(
                    random.nextDouble() * MAX_PARTICLE_RADIO + MIN_PARTICLE_RADIO,
                    null,   //TODO: Put real attributes
                    random.nextDouble() * (double) this.systemSideLength,
                    random.nextDouble() * (double) this.systemSideLength,
                    defaultVelocityModule,
                    (random.nextBoolean()?-1:1)*(random.nextDouble() * MAX_ANGLE + MIN_ANGLE)
            ));
        }
        System.out.println("Random input generated.");
        Output.generateInputFiles(this.particlesQuantity, this.systemSideLength, this.particles);
    }

    /**
     * A constructor that generates an {@link Input} instance obtaining parameters from input files.
     *
     * @param staticFileName        The static parameters file, such as side length.
     * @param dinamicFileName       The dinamic parameters file, such as velocity in one state for each particle.
     * @param noise                 The noise param.
     * @throws IOException          e.g. if one of the files cannot be founded.
     */
    public Input(String staticFileName, String dinamicFileName, double noise, int quantity,int length) throws IOException{
        this.contornCondition = true;
        this.systemSideLength = length;
        this.interactionRadio = defaultInteractionRadio;
        BufferedReader staticFileReader, dinamicFileReader;
        try{
            // Static file
            staticFileReader = new BufferedReader(new FileReader(staticFileName));
            dinamicFileReader = new BufferedReader(new FileReader(dinamicFileName));
            this.particlesQuantity = Long.valueOf(quantity);
            this.noise=noise;
            this.cellSideQuantity = (int) Math.ceil(systemSideLength/interactionRadio) - 1 ;
            this.particles = new ArrayList<>();
            dinamicFileReader.readLine();  //Discard first time notation
            while(staticFileReader.ready()){    //Only time zero for dinamic file
                String[] staticLineValues = staticFileReader.readLine().split(" ");
                String[] dinamicLineValues = dinamicFileReader.readLine().split(" ");
                Particle p =new Particle(
                        Double.valueOf(staticLineValues[0]),
                        staticLineValues[1],
                        Double.valueOf(dinamicLineValues[0]),
                        Double.valueOf(dinamicLineValues[1]),
                        Double.valueOf(dinamicLineValues[2]),
                        Double.valueOf(dinamicLineValues[3])
                );
                particles.add(p);
            }
            if (particles.size() != particlesQuantity)
                throw new IllegalArgumentException();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Long getParticlesQuantity() {
        return particlesQuantity;
    }

    public int getCellSideQuantity() {
        return cellSideQuantity;
    }

    public int getSystemSideLength() {
        return systemSideLength;
    }

    public Double getInteractionRadio() {
        return interactionRadio;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Boolean getContornCondition() {
        return contornCondition;
    }

    public int getIterationsQuantity() {
        return this.defaultIterations;
    }

    public void setIterationsQuantity(int quantity) {
        this.defaultIterations= quantity;
    }


    public double getNoise() {
        return noise;
    }

    public double getVelocityMod(){
        return this.defaultVelocityModule;
    }
}
