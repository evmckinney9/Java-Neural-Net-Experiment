package neuralnet;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;
import com.hydrozoa.hydroneat.ConnectionGene;
import com.hydrozoa.hydroneat.Counter;
import com.hydrozoa.hydroneat.Evaluator;
import com.hydrozoa.hydroneat.FitnessGenome;
import com.hydrozoa.hydroneat.GenesisGenomeProvider;
import com.hydrozoa.hydroneat.Genome;
import com.hydrozoa.hydroneat.NEATConfiguration;
import com.hydrozoa.hydroneat.NeuralNetwork;
import com.hydrozoa.hydroneat.NodeGene;
import com.hydrozoa.hydroneat.NodeGene.TYPE;

import graphics.DisplayGraphics;
import graphics.GenomePrinter;
import spacefiles.Space;
import spacefiles.SpaceRunner;

/**
 * @author hydrozoa
 * @author Evan McKinneyai
 */
public class SpaceRunNN {
	static int counter = 0;
	static int popsize = 10;
	static boolean show = false;
	static boolean showalways = true;
	static boolean showchart = false;
	static DisplayGraphics dp;
	static boolean startbool;

	public static void main(String[] args) {
		Random r = new Random();

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(9);

		// first column and second column contains operands, the third column is for bias input
		Counter nodeInn = new Counter();
		Counter connInn = new Counter();

		Genome genome = new Genome();

		genome.addNodeGene(new NodeGene(TYPE.INPUT, nodeInn.getInnovation()));				// node id is 0
		genome.addNodeGene(new NodeGene(TYPE.INPUT, nodeInn.getInnovation()));				// node id is 1
		genome.addNodeGene(new NodeGene(TYPE.INPUT, nodeInn.getInnovation()));				// node id is 2
		genome.addNodeGene(new NodeGene(TYPE.INPUT, nodeInn.getInnovation()));				// node id is 3
		genome.addNodeGene(new NodeGene(TYPE.INPUT, nodeInn.getInnovation()));				// node id is 4
		genome.addNodeGene(new NodeGene(TYPE.INPUT, nodeInn.getInnovation()));				// node id is 5
		genome.addNodeGene(new NodeGene(TYPE.INPUT, nodeInn.getInnovation()));				// node id is 6

		genome.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInn.getInnovation()));				// node id is 7 
		genome.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInn.getInnovation()));				// node id is 8
		genome.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInn.getInnovation()));				// node id is 9
		genome.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInn.getInnovation()));				// node id is 10 //BIAS NEURON, input always = 1

		genome.addConnectionGene(new ConnectionGene(0, 7, 1f, true, connInn.getInnovation()));	// conn id is 0
		genome.addConnectionGene(new ConnectionGene(1, 9, 1f, true, connInn.getInnovation()));	// conn id is 1
		genome.addConnectionGene(new ConnectionGene(2, 7, 1f, true, connInn.getInnovation()));	// conn id is 0
		genome.addConnectionGene(new ConnectionGene(3, 9, 1f, true, connInn.getInnovation()));	// conn id is 0
		genome.addConnectionGene(new ConnectionGene(4, 7, 1f, true, connInn.getInnovation()));	// conn id is 1
		genome.addConnectionGene(new ConnectionGene(5, 9, 1f, true, connInn.getInnovation()));	// conn id is 0
		genome.addConnectionGene(new ConnectionGene(6, 7, 1f, true, connInn.getInnovation()));	// conn id is 0

		GenesisGenomeProvider provider = new GenesisGenomeProvider() {
			@Override
			public Genome generateGenesisGenome() {
				Genome g = new Genome(genome);
				for (ConnectionGene connection : g.getConnectionGenes().values()) {
					connection.setWeight((float)r.nextGaussian());
				}
				return g;
			}
		};

		NEATConfiguration conf = new NEATConfiguration(popsize);

		Evaluator eva = new Evaluator(conf, provider, nodeInn, connInn) {
			@Override
			public float evaluateGenome(Genome g) {
				NeuralNetwork net = new NeuralNetwork(g);

				//System.out.println("===========NEW NETWORK=============");

				float totalDistance = 0f;
				boolean graphics = false;
				if ((counter == 0 && show) ||showalways) {
					graphics = true;
				}
				SpaceRunner sr1 = new SpaceRunner(graphics);
				if (startbool && counter ==0) {
					dp = sr1.run();
				}
				else {
					sr1.run(dp);
				}

				Date startDate = new Date();
				Date endDate;
				int numSeconds = 0;
				float[] input;
				while(sr1.isRunning() && ((graphics && numSeconds <= 20)||(!graphics && numSeconds <= 5))) {
					input = sr1.getInputsfromGame();
					sr1.handleOutputfromNet(net.calculate(input));
					endDate = new Date(); 
					numSeconds = (int)((endDate.getTime() - startDate.getTime()) / 1000);
				}
				totalDistance = sr1.getScore();

				/*if (g.getConnectionGenes().size() > 100) { // try to favor smaller solutions
					totalDistance += 1f * (g.getConnectionGenes().size()-20);
				}*/

				//System.out.println(totalDistance);
				counter++;
				//System.out.println(counter);
				return totalDistance;
			}
		};



		//LiveXYChart fitnessChart = new LiveXYChart("average fitness over time", "generations", "fitness", 1000);
		//GenomePrinter gPrinter = new GenomePrinter();


		for (int i = 0; i < 1000; i++) {
			counter =0;
			startbool = (i==0) ? true : false;
			eva.evaluateGeneration(r);

			if (showchart) {
				//fitnessChart.addDataPoint(new Double(i), new Double(eva.getAverageGenome()));
			}

				System.out.println("Generation: "+i);
			System.out.println("\tHighest fitness: "+df.format(eva.getFittestGenome().fitness));
			System.out.println("\tAverage fitness: "+df.format(eva.getAverageGenomeFitness()));
			System.out.println("\tAmount of genomes: "+eva.getGenomeAmount());
			/*System.out.println("\tGuesses from best network: ");

			System.out.print("\t\t");
			NeuralNetwork net = new NeuralNetwork(eva.getFittestGenome().genome);
			for (int k = 0; k < input.length; k++) {
				float[] inputs = {input[k][0], input[k][1], input[k][2]};
				float[] outputs = net.calculate(inputs);

				float guess = outputs[0];
				System.out.print(df.format(guess)+", ");
			}*/
			System.out.print("\n");

			//			System.out.println("\tPrinting all genomes");
			//			int k = 0;
			//			for (FitnessGenome fitnessGenome : eva.getLastGenerationResults()) {
			//				k++;
			//				System.out.println("\t\t Index="+k+"\t N"+fitnessGenome.genome.getNodeGenes().size()+"\t C"+fitnessGenome.genome.getConnectionGenes().size()+"\t fitness="+fitnessGenome.fitness);
			//			}

			if (showchart) {
				//if (i % 500 == 0) { gPrinter.showGenome(eva.getFittestGenome().genome, ""+i);
				//}
			}
		}
	}
}