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

    public static void generatePositionOutput(List<Particle> result, int time) {
        if (result == null) return; //TODO: Throw exception
        try{
            FileWriter fileWriter = new FileWriter(FILENAME2);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (int state = 0 ; state < time ; state++ ){
                bufferedWriter.write(Integer.valueOf(result.size()).toString());
                bufferedWriter.newLine();
                for (Particle particle: result){
                    printToFile(bufferedWriter,particle, state);
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

    public static void printToFile(BufferedWriter bufferedWriter, Particle particle, Integer state) throws IOException {
        bufferedWriter.newLine();
        String print = particle.getId() + " " + particle.getStates().get(state).getX()
                + " " + particle.getStates().get(state).getY()
                + " " + particle.getStates().get(state).getVx()
                + " " + particle.getStates().get(state).getVy();
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
}
