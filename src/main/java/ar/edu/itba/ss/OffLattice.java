package ar.edu.itba.ss;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OffLattice {

    // Program Arguments: "./NeighborDetection/resources/sample_input_static.txt" "./NeighborDetection/resources/sample_input_dinamic.txt"
    public static void main(String[] args) throws IOException {

        CommandLine cmd = getOptions(args);
        Input input;
        if (cmd.getOptionValue('s')!=null && cmd.getOptionValue('d')!= null) {
            input = new Input(cmd.getOptionValue('s'), cmd.getOptionValue('d'),
                    Double.valueOf(cmd.getOptionValue('n')),
                    Integer.valueOf(cmd.getOptionValue('N')),
                    Integer.valueOf(cmd.getOptionValue('L')));
        }else
            input=new Input(Double.valueOf(cmd.getOptionValue('n')),
                            Integer.valueOf(cmd.getOptionValue('N')),
                            Integer.valueOf(cmd.getOptionValue('L')));
        if ( cmd.getOptionValue('i') != null)
            input.setIterationsQuantity(Integer.valueOf(cmd.getOptionValue('i')));

        System.out.println("N="+input.getParticlesQuantity()+" ; L="+ input.getSystemSideLength()+ " ; n="+ input.getNoise()+ " ; i="+ input.getIterationsQuantity());
        System.out.println("Starting simulation");
        Map<Particle, List<Particle>> results;
        int time = 0;
        long timeMilis= System.currentTimeMillis();
        try {
            for(time =0;time<input.getIterationsQuantity();time++) {
                Grid grid = new Grid(input.getCellSideQuantity(), input.getSystemSideLength());
                grid.setParticles(input.getParticles(), time);
                results=NeighborDetection.getNeighbors(grid, grid.getUsedCells(), input.getInteractionRadio(), input.getContornCondition(), time);
                updateParticles(input, results,time);
                }
        }catch (OutOfMemoryError o){
            System.out.println("Out of memory. Printing "+ time +" iteration");
        }
        long end = System.currentTimeMillis();
        System.out.println("Simulation finished. Duration: "+ ((end-timeMilis)/1000.0)+ " seconds");

        double sumVx =0;
        double sumVy =0;
        for (Particle p: input.getParticles()){
            sumVx += p.getState(input.getIterationsQuantity()-1).getVx();
            sumVy += p.getState(input.getIterationsQuantity()-1).getVy();
        }
        double sumV = Math.sqrt(Math.pow(sumVx,2)+Math.pow(sumVy,2));
        double va = sumV/(input.getVelocityMod()*input.getParticlesQuantity());
        System.out.println("Va: " + va);
        Output.generatePositionOutput(input.getParticles(), input.getIterationsQuantity());
        System.out.println("Files generated");

    }

    private static void updateParticles(Input input, Map<Particle, List<Particle>> neighbors, int time) {

        //TODO: get noise from input
        input.getParticles().stream().forEach(particle -> {
            particle.move(input.getSystemSideLength(),input.getNoise(),neighbors.get(particle),time);
        });
    }

    private static CommandLine getOptions(String[] args){


        Options options = new Options();

        Option staticInput = new Option("s", "staticInput", true, "static file path");
        staticInput.setRequired(false);
        options.addOption(staticInput);

        Option dinamicInput = new Option("d", "dinamicInput", true, "dinamic file");
        dinamicInput.setRequired(false);
        options.addOption(dinamicInput);

        Option noise = new Option("n", "noise", true, "noise");
        noise.setRequired(true);
        options.addOption(noise);

        Option iterations = new Option("i", "iterations", true, "iterations");
        iterations.setRequired(false);
        options.addOption(iterations);

        Option L = new Option("L", "length", true, "length");
        L.setRequired(true);
        options.addOption(L);

        Option quantity = new Option("N", "quantity", true, "quantity(N)");
        quantity.setRequired(true);
        options.addOption(quantity);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd=null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
        return cmd;
    }

}
