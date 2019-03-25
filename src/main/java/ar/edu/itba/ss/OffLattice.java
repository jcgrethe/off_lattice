package ar.edu.itba.ss;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OffLattice {

    // Program Arguments: "./NeighborDetection/resources/sample_input_static.txt" "./NeighborDetection/resources/sample_input_dinamic.txt"
    public static void main(String[] args) throws IOException {

        for (double density=7; density<=10; density+=1){
            List<Double> lVa=new LinkedList<>();
            double prom=0;
            //System.out.println("Noise: "+noise);

            CommandLine cmd = getOptions(args);
            Input input;
            int n = (int)(density*Math.pow(Integer.valueOf(cmd.getOptionValue('L')),2));
            double d = n/Math.pow(Integer.valueOf(cmd.getOptionValue('L')),2);
            System.out.println("density: "+ d);
            for (int x=0;x<10;x++) {

                if (cmd.getOptionValue('s') != null && cmd.getOptionValue('d') != null) {
                    input = new Input(cmd.getOptionValue('s'), cmd.getOptionValue('d'),
                            Double.valueOf(cmd.getOptionValue('n')),
                            Integer.valueOf(cmd.getOptionValue('N')),
                            Integer.valueOf(cmd.getOptionValue('L')));
                } else
                    input = new Input(Double.valueOf(cmd.getOptionValue('n')),
                            n,
                            Integer.valueOf(cmd.getOptionValue('L')));

                List<Map<Particle, List<Particle>>> results = new LinkedList<>();

                try {
                    for (int time = 0; time < input.getIterationsQuantity(); time++) {
                        Grid grid = new Grid(input.getCellSideQuantity(), input.getSystemSideLength());
                        grid.setParticles(input.getParticles(), time);
                        results.add(NeighborDetection.getNeighbors(grid, grid.getUsedCells(), input.getInteractionRadio(), input.getContornCondition(), time));
                        updateParticles(input, ((LinkedList<Map<Particle, List<Particle>>>) results).getLast(), time);
                    }
                } catch (OutOfMemoryError o) {
                    System.out.println("Out of memory. Printing " + results.size() + " iteration");
                }

                double sumVx = 0;
                double sumVy = 0;
                for (Particle p : input.getParticles()) {
                    sumVx += p.getState(input.getIterationsQuantity() - 1).getVx();
                    sumVy += p.getState(input.getIterationsQuantity() - 1).getVy();
                }
                double sumV = Math.sqrt(Math.pow(sumVx, 2) + Math.pow(sumVy, 2));
                double va = sumV / (input.getVelocityMod() * input.getParticlesQuantity());
                //System.out.println("Va: " + va);
                lVa.add(va);
                prom+=va;
            }
            double auxerror = 0.0;
            for(Double va: lVa){
                auxerror+=Math.pow(Math.abs(va-prom/10),2);
            }
            double error= Math.sqrt(auxerror/10);
            System.out.println("prom: "+ prom/10);
            System.out.println("error: " + error);
        }
        //Output.generatePositionOutput(results);


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
