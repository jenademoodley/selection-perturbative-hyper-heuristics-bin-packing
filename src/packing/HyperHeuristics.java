package packing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import BinPacking.BinPacking;

public class HyperHeuristics {

	static long seed;
	static Random rangen;

	static double fitness = 0;

	public static void main(String[] args) {

		// menu
		Scanner sc = new Scanner(System.in);
		seed = System.currentTimeMillis();
		rangen = new Random(seed);

		System.out.println(
				"Selection Perturbative Hyper-Heuristics using HyFlex for the One-Dimensional Bin-Packing Problem");
		System.out.println(
				"=================================================================================================");
		System.out.println(
				"Do you wish to enter a Seed? (Number Only) (Default is System Current Time in Milliseconds)\n1 - Yes\n2 - No");
		int choice = sc.nextInt();

		while (choice < 1 || choice > 2) {
			System.out.println(
					"\nUnavailable Option\nDo you wish to enter a Seed? (Number Only) (Default is System Current Time in Milliseconds)\n1 - Yes\n2 - No");
			choice = sc.nextInt();
		}

		if (choice == 1) {
			System.out.println("\nEnter Seed");
			seed = sc.nextLong();
			rangen = new Random(seed);
		}

		System.out.println("\nChoose Problem Instance (Number Only)");
		System.out.println("1 - bpp1");
		System.out.println("2 - bpp2");
		System.out.println("3 - bpp3");
		System.out.println("4 - bpp4");
		System.out.println("5 - bpp5");

		int instance = sc.nextInt();

		while (instance < 1 || instance > 5) {
			System.out.println("\nUnavailable Option\nChoose Problem Instance (Number Only)");
			System.out.println("1 - bpp1");
			System.out.println("2 - bpp2");
			System.out.println("3 - bpp3");
			System.out.println("4 - bpp4");
			System.out.println("5 - bpp5");
			instance = sc.nextInt();
		}

		System.out.println();
		System.out.println("Heuristic Selectors");
		System.out.println("H1 - Random Selection");
		System.out.println("H2 - Random Gradient");
		System.out.println("H3 - Tournament Selection");

		System.out.println("\nMove Acceptors");
		System.out.println("M1 - Accept all moves");
		System.out.println("M2 - Accept only improving moves");

		System.out.println("\nChoose a Hyper-Heuristic (Number Only)");
		System.out.println("1 - Hyper-Heuristic 1 (H1, M1)");
		System.out.println("2 - Hyper-Heuristic 2 (H1, M2)");
		System.out.println("3 - Hyper-Heuristic 3 (H2, M1)");
		System.out.println("4 - Hyper-Heuristic 4 (H2, M2)");
		System.out.println("5 - Hyper-Heuristic 5 (H3, M1)");
		System.out.println("6 - Hyper-Heuristic 6 (H3, M2)");
		int hyper = sc.nextInt();

		while (hyper < 1 || hyper > 6) {
			System.out.println("\nChoose a Hyper-Heuristic (Number Only)");
			System.out.println("1 - Hyper-Heuristic 1 (H1, M1)");
			System.out.println("2 - Hyper-Heuristic 2 (H1, M2)");
			System.out.println("3 - Hyper-Heuristic 3 (H2, M1)");
			System.out.println("4 - Hyper-Heuristic 4 (H2, M2)");
			System.out.println("5 - Hyper-Heuristic 5 (H3, M1)");
			System.out.println("6 - Hyper-Heuristic 6 (H3, M2)");
			hyper = sc.nextInt();
		}

		int heuristic = 0;
		int move = 0;

		switch (hyper) {
		case 1:
			heuristic = 1;
			move = 1;
			break;

		case 2:
			heuristic = 1;
			move = 2;
			break;

		case 3:
			heuristic = 2;
			move = 1;
			break;

		case 4:
			heuristic = 2;
			move = 2;
			break;

		case 5:
			heuristic = 3;
			move = 1;
			break;

		case 6:
			heuristic = 3;
			move = 2;
			break;

		default:
			heuristic = 1;
			move = 1;
			break;
		}

		BufferedWriter bw = null;
		FileWriter fw = null;
		String FILENAME = "Outputs//output";
		int t = 1;
		try {

			File f = new File(FILENAME + t + ".txt");

			// if file doesn't exists, then create it

			while (f.exists()) {
				t++;
				f = new File(FILENAME + t + ".txt");
			}
			f.createNewFile();

			// true = append file
			fw = new FileWriter(f.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			bw.write("Problem Instance: bpp" + instance);
			bw.newLine();

			bw.write("Heuristic Selector: ");
			if (heuristic == 1)
				bw.write("Random Selection");

			else if (heuristic == 2)
				bw.write("Random Gradient");

			else if (heuristic == 3)
				bw.write("Tournament Selection");

			bw.newLine();

			bw.write("Move Acceptor: ");

			if (move == 1)
				bw.write("Accept all moves");
			else
				bw.write("Accept only improving moves");

			bw.newLine();

			bw.write("Seed: " + seed);
			bw.newLine();
			bw.newLine();
			// create instance
			System.out.println("\nSeed: " + seed);
			BinPacking BPP = new BinPacking(seed);

			long start = System.currentTimeMillis();
			// initial solution
			instance--;

			BPP.loadInstance(instance);
			BPP.initialiseSolution(0);

			// original fitness
			fitness = BPP.getFunctionValue(0);
			System.out.println("Initial Solution Fitness: " + fitness);
			bw.write("Initial Solution Fitness: " + fitness);
			bw.newLine();
			// run heuristics
			applyHyperHeuristic(heuristic, move, instance, BPP, bw);

			long end = System.currentTimeMillis();
			System.out.println("Time Taken (in milliseconds): " + (end - start));
			bw.write("Time Taken (in milliseconds): " + (end - start));

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

		System.out.println("\nPush Enter to Continue");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sc.close();
	}

	// SELECTION METHODS
	// random selection and random gradient
	public static int randomSelection() {
		int heuristic = rangen.nextInt(8);
		return heuristic;
	}

	public static double[] getAllParticipants(int instance) {
		BinPacking bpp = new BinPacking(seed);
		bpp.loadInstance(instance);
		int num = bpp.getNumberOfHeuristics();
		double[] solutions = new double[num];

		for (int i = 0; i < num; i++) {
			bpp.initialiseSolution(0);
			bpp.applyHeuristic(i, 0, 0);
			solutions[i] = bpp.getFunctionValue(0);
		}

		return solutions;
	}

	// tournament selection
	public static int tournamentSelection(int instance, double[] solutions) {

		int num = solutions.length;

		int[] tournament = new int[3];

		for (int i = 0; i < 3; i++)
			tournament[i] = rangen.nextInt(num);

		double heuristic = solutions[tournament[0]];
		int h = 0;

		for (int i = 1; i < tournament.length; i++) {
			if (solutions[tournament[i]] < heuristic) {
				heuristic = tournament[i];
				h = i;
			}
		}
		return h;
	}

	public static void applyHyperHeuristic(int H, int M, int instance, BinPacking BPP, BufferedWriter bw)
			throws IOException {
		fitness = BPP.getFunctionValue(0);
		// select heuristic
		int heuristicValue = 0;

		int iterations = 0;

		// apply heuristic
		int i = 0;
		double currentFitness = 0;
		String solution = BPP.solutionToString(0);
		double previousFitness = BPP.getFunctionValue(0);

		double[] solutions = null;

		if (H == 3)
			solutions = getAllParticipants(instance);

		while (iterations < 100) {

			if (H == 1)
				heuristicValue = randomSelection();

			else if (H == 2) {
				if (currentFitness == previousFitness)
					i++;

				else
					i = 0;

				if (i == 10) {
					heuristicValue = randomSelection();
					i = 0;
				}
				previousFitness = currentFitness;
			}

			else
				heuristicValue = tournamentSelection(instance, solutions);

			BPP.applyHeuristic(heuristicValue, 0, 1);
			currentFitness = BPP.getFunctionValue(1);

			if (M == 1) {
				BPP.copySolution(1, 0);
				if (currentFitness < fitness) {
					fitness = currentFitness;
					solution = BPP.solutionToString(0);
				}
			}

			else {
				if (currentFitness < fitness) {
					fitness = currentFitness;
					BPP.copySolution(1, 0);
					solution = BPP.solutionToString(0);
				}
			}
			iterations++;
		}

		// display evolved solution

		System.out.println("Fitness of Solution after Hyper-Heuristic: " + fitness);
		bw.write("Fitness of Solution after Hyper-Heuristic: " + fitness);
		bw.newLine();

		int objective = getObjectiveValue(solution);

		System.out.println("Objective Value of Solution: " + objective);
		bw.write("Objective Value of Solution: " + objective);

		bw.newLine();
		bw.newLine();
		System.out.println();

		System.out.println(solution);
		bw.write(solution);
		bw.newLine();

	}

	public static int getObjectiveValue(String sol) {
		int lines = 0;
		for (int i = 0; i < sol.length(); i++) {
			if (sol.charAt(i) == '\n')
				lines++;
		}

		return lines - 1;
	}

}
