package ar.edu.itba.ss;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Input {

    // Defined values
    private static int defaultSystemSideLength = 20;
    private static Double defaultInteractionRadio = 1.0;
    private static Double defaultParticleRadio = 0.25;
    private static int MAX_SYSTEM_SIDE_LENGTH = 40;
    private static int MIN_SYSTEM_SIDE_LENGTH = 10;
    private static Double MAX_INTERACTION_RADIO = 3.0;
    private static Double MIN_INTERACTION_RADIO = 1.1;
    private static int MAX_PARTICLE_QUANTITY = Integer.valueOf(10000);
    private static int MIN_PARTICLE_QUANTITY = Integer.valueOf(10000);
    private static int MAX_CELL_SIDE_QUANTITY = 100;
    private static int MIN_CELL_SIDE_QUANTITY = 10;
    private static Double MAX_PARTICLE_RADIO = 0.6;
    private static Double MIN_PARTICLE_RADIO = 0.5;
    private static Double MIN_VELOCITY = 0.1;
    private static Double MAX_VELOCITY = 20.0;

    private Long particlesQuantity;
    private int cellSideQuantity;
    private List<Particle> particles;
    private Boolean contornCondition;
    private int systemSideLength;
    private Double interactionRadio;
    private Particle selectedParticle;

    /**
     * Empty constructor generates random inputs based in the max and min setted for each variable.
     */
    public Input(){
        Random random = new Random();
        this.systemSideLength = defaultSystemSideLength;
        this.particlesQuantity = (long) random.nextInt(MAX_PARTICLE_QUANTITY - MIN_PARTICLE_QUANTITY + 1) + MIN_PARTICLE_QUANTITY;
        this.contornCondition = true;
        this.interactionRadio = defaultInteractionRadio;
        this.cellSideQuantity = 19;
        this.particles = new ArrayList<>();
        for (int i = 0 ; i < this.particlesQuantity ; i++){
            this.particles.add(new Particle(
                    defaultParticleRadio,
                    null,   //TODO: Put real attributes
                    random.nextDouble() * (double) this.systemSideLength,
                    random.nextDouble() * (double) this.systemSideLength,
                    random.nextDouble() * MAX_VELOCITY + MIN_VELOCITY,
                    random.nextDouble() * MAX_VELOCITY + MIN_VELOCITY
            ));
        }
        this.selectedParticle = this.particles.get(random.nextInt(this.particles.size()));

        Output.generateInputFiles(this.particlesQuantity, this.systemSideLength, this.particles);
    }

    /**
     * A constructor that generates an {@link Input} instance obtaining parameters from input files.
     *
     * @param staticFileName        The static parameters file, such as side length.
     * @param dinamicFileName       The dinamic parameters file, such as velocity in one state for each particle.
     * @param contornCondition      If the contorn condition is on.
     * @throws IOException          e.g. if one of the files cannot be founded.
     */
    public Input(String staticFileName, String dinamicFileName, Boolean contornCondition, Long particleId) throws IOException{
        this.contornCondition = contornCondition;
        this.systemSideLength = defaultSystemSideLength;
        this.interactionRadio = defaultInteractionRadio;
        BufferedReader staticFileReader, dinamicFileReader;
        try{
            // Static file
            staticFileReader = new BufferedReader(new FileReader(staticFileName));
            dinamicFileReader = new BufferedReader(new FileReader(dinamicFileName));
            this.particlesQuantity = Long.valueOf(staticFileReader.readLine());
            if (particleId ==null){
                Random r = new Random();
                particleId = r.nextLong() % particlesQuantity;
            }
            particleId= Long.valueOf(10);
            this.cellSideQuantity = Integer.valueOf(staticFileReader.readLine());
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
                if( particleId != null && p.getId()==particleId.longValue())
                    this.selectedParticle = p;
                particles.add(p);
            }
            if (particles.size() != particlesQuantity)
                throw new IllegalArgumentException();
            // TODO: Validar L/M > Rc
//            TODO: More than one state per particle
//            while(dinamicFileReader.ready()){
//                 dinamicFileReader.readLine();  //Discard time notation
//                 particles.add(new Particle())
//            }
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

    public Particle getSelectedParticle() {
        return selectedParticle;
    }
}
