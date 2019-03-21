package ar.edu.itba.ss;
import sun.security.krb5.internal.PAData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Output {
    private final static String FILENAME = "output.txt";
    private final static String FILENAME2 = "positions.xyz";
    private final static String STATIC_FILE = "sample_input_static.txt";
    private final static String DINAMIC_FILE = "sample_input_dinamic.txt";

    public static void generateOutput(Map<Particle, List<Particle>> neighbors){
        if (neighbors == null) return; //TODO: Throw exception
        try{
            FileWriter fileWriter = new FileWriter(FILENAME);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for(Map.Entry<Particle, List<Particle>> entry : neighbors.entrySet()){
                bufferedWriter.write("[ " + entry.getKey().getId() + "  ");
                entry.getValue().stream().forEach(particle -> {
                    try {
                        bufferedWriter.write(particle.getId() + " ");
                    }catch (IOException e){
                        // TODO: Handle IO Execption
                    }
                });
                bufferedWriter.write("]");
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        }catch (IOException e){
            // TODO: Handle IO Execption
        }
    }

    public static void generatePositionOutput(List<Particle> particles){
        if (particles == null) return; //TODO: Throw exception
        try{
            FileWriter fileWriter = new FileWriter(FILENAME2);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(Integer.valueOf(particles.size()).toString());
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            for (int state = 0 ; state < particles.get(0).getStates().size() ; state++){
                for (int i = 0 ; i < particles.size() ; i++){
                    bufferedWriter.write(particles.get(i).getId() + " " + particles.get(i).getStates().get(state).getX()
                            + " " + particles.get(i).getStates().get(state).getY() + " "
                            + " " + particles.get(i).getRadio() + " ");
                    if (i < particles.size() - 1)
                        bufferedWriter.newLine();
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        }catch (IOException e){
            // TODO: Handle IO Execption
        }
    }

    public static void printGrid(Grid grid){
        System.out.println("Side Cells Quantity: " + grid.getSideCellsQuantity());
        System.out.println("Side Length: " + grid.getSideLength());
        System.out.println("Cell Side Length: " + Double.valueOf(grid.getSideLength()) / Double.valueOf(grid.getSideCellsQuantity()));
        for (int i = 0 ; i < grid.getSideCellsQuantity() ; i++){
            for (int j = 0 ; j < grid.getSideCellsQuantity() ; j++){
                System.out.print("|"+String.format("%02d", grid.getCell(i, j).getParticlesQuantity())+"|");
            }
            System.out.println("");
        }
    }

    public static void printResult(Map<Particle, List<Particle>> result){
        System.out.println("Result: ");
        result.forEach((particle, particles) -> {
            System.out.print("["+particle.getId()+" ");
            particles.forEach(neighbor -> System.out.print(neighbor.getId() + " "));
            System.out.println("]");
        });
    }

    public static void printParticlesInfo(List<Particle> particles, int state){
        particles.forEach(particle -> {
            System.out.println("ID: " + particle.getId() + " | Radio: " + particle.getRadio() + " | Location: (" +
                    particle.getStates().get(state).getX() + "," + particle.getStates().get(state).getY() + ")");
        });
    }

    public static void generatePositionOutput(List<Map<Particle,List<Particle>>> result, Particle selectedParticle) {
        if (result == null) return; //TODO: Throw exception
        try{
            FileWriter fileWriter = new FileWriter(FILENAME2);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (int state = 0 ; state < result.size() ; state++ ){
                Set<Particle> particles = result.get(state).keySet();
                bufferedWriter.write(Integer.valueOf(result.get(state).size()).toString());
                bufferedWriter.newLine();
                printToFile(bufferedWriter, selectedParticle,255,0,0, 0.1, state);
                for (Particle particle: result.get(state).get(selectedParticle)){
                    printToFile(bufferedWriter,particle,255,255,255, 0.4, state);
                    particles.remove(particle);
                }
                particles.remove(selectedParticle);
                for (Particle particle: particles){
                    printToFile(bufferedWriter,particle,0,255,255, 0.9, state);
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        }catch (IOException e){
            // TODO: Handle IO Execption
        }
    }

    public static void printToFile(BufferedWriter bufferedWriter, Particle particle, Integer r,Integer g, Integer b, Double transparency, Integer state) throws IOException {
        bufferedWriter.newLine();
        String print = particle.getId() + " " + particle.getStates().get(state).getX()
                + " " + particle.getStates().get(state).getY()
                + " " + particle.getRadio() + " " + r.toString() + " " + g.toString() + " " + b.toString() + " " + transparency.toString();
        bufferedWriter.write(print);
    }

    public static void generateInputFiles(Long n, int l, List<Particle> particles){
        try{
            FileWriter fileWriter = new FileWriter(STATIC_FILE);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.valueOf(n));
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(l));
            bufferedWriter.newLine();
            for (int i = 0 ; i < particles.size() ; i++){
                bufferedWriter.write(particles.get(i).getRadio() + " " + particles.get(i).getProperty());
                if (i != particles.size() - 1 ){
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        }catch(IOException e){
            System.out.println(e);
        }

        try{
            FileWriter fileWriter = new FileWriter(DINAMIC_FILE);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int i = 0 ; i < particles.get(0).getStates().size() ; i++){
                bufferedWriter.write(String.valueOf(i));
                bufferedWriter.newLine();
                for (int p = 0 ; p < particles.size() ; p++){
                    bufferedWriter.write(
                        particles.get(p).getStates().get(i).getX() + " " +
                            particles.get(p).getStates().get(i).getY() + " " +
                            particles.get(p).getStates().get(i).getVx() + " " +
                            particles.get(p).getStates().get(i).getVy()
                        );
                    if (p != particles.size() - 1){
                        bufferedWriter.newLine();
                    }
                }
                if (i != particles.get(0).getStates().size() - 1){
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void printExecutionData(long duration, Input input){
        System.out.println("M: " + input.getCellSideQuantity());
        System.out.println("N: " + input.getParticlesQuantity());
        System.out.println("Rc: " + input.getInteractionRadio());
        System.out.println("L: " + input.getSystemSideLength());
        System.out.println("Contorn: " + input.getContornCondition());
        System.out.println("Duration: " + duration);
    }
}
