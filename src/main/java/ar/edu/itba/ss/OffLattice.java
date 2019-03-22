package ar.edu.itba.ss;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OffLattice {

    // Program Arguments: "./NeighborDetection/resources/sample_input_static.txt" "./NeighborDetection/resources/sample_input_dinamic.txt"
    public static void main(String[] args) throws IOException {

        CommandLine cmd = getOptions(args);
        Input input;
        if (cmd.getOptionValue('s')!=null && cmd.getOptionValue('d')!= null) {
            input = new Input(cmd.getOptionValue('s'), cmd.getOptionValue('d'), true , Long.valueOf(cmd.getOptionValue("id")));
        }else
            input=new Input();


        List<Map<Particle, List<Particle>>> results=new LinkedList<>();

        for(int time=0;time<input.getIterationsQuantity();time++) {
            Grid grid = new Grid(input.getCellSideQuantity(), input.getSystemSideLength());
            grid.setParticles(input.getParticles(), time);
            results.add(NeighborDetection.getNeighbors(grid, grid.getUsedCells(), input.getInteractionRadio(), input.getContornCondition(), time));
            updateParticles(input, results.get(results.size() - 1));
        }

        if(input.getSelectedParticle()!=null)
            Output.generatePositionOutput(results,input.getSelectedParticle());
        else
            Output.generatePositionOutput(input.getParticles());

    }

    private static void updateParticles(Input input, Map<Particle, List<Particle>> neighbors) {

        //TODO: get noise from input
        input.getParticles().stream().parallel().forEach(particle -> {
            particle.move(input.getSystemSideLength(),0,neighbors.get(particle));
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

        Option M = new Option("M", true, "M");
        M.setRequired(true);
        options.addOption(M);

        Option id = new Option("id", true, "id");
        id.setRequired(false);
        options.addOption(id);

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
