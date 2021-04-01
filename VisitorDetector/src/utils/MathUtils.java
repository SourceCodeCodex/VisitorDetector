package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;

public class MathUtils {
	private static Map<String, Integer> castsPerClient;

	public static Double computeArithmeticAverage(List<MMethod> allClients, List<MClass> descendants) {
		prepareCastsPerClient(allClients, descendants);
		return computeArithmeticAverage(getDescendatsPerClient());
	}

	public static Double computeMedian(List<MMethod> allClients, List<MClass> descendants) {
		prepareCastsPerClient(allClients, descendants);
		return computeMedian(getDescendatsPerClient());
	}

	public static void prepareCastsPerClient(List<MMethod> allClients, List<MClass> descendants) {
		castsPerClient = new HashMap<>();
		allClients.forEach(client -> castsPerClient.putIfAbsent(Utils.getElementName(client), 0));
		for (MClass descendent : descendants) {
			List<String> clients;
			clients = getClients(descendent);
			incrementCastsPerClient(clients);
		}

	}

	private static List<String> getClients(MClass arg0) {
		List<String> clients = new ArrayList<>();
		for (MMethod method : arg0.distinctMethodsWithCastsToMe().getElements()) {
			String client = Utils.getElementName(method);
			clients.add(client);
		}
		return clients;
	}

	private static void incrementCastsPerClient(List<String> clients) {
		for (String client : clients) {
			Optional<Integer> optName = Optional.ofNullable(castsPerClient.get(client));
			if (optName.isPresent())
				castsPerClient.put(client, castsPerClient.get(client) + 1);
		}
	}

	public static Double computeArithmeticAverage(List<Integer> descendantsPerClient) {
		int size = descendantsPerClient.size();
		switch (size) {
		case 0:
			return 0.0;
		case 1:
			return descendantsPerClient.get(0) * 1.0;
		default:
//			castsPerClient.forEach((client, noOfDescendants) -> System.out.println(client + "-" + noOfDescendants));
			return descendantsPerClient.stream().reduce(0, (a, b) -> a + b) * 1.0 / descendantsPerClient.size();
		}
	}

	private static List<Integer> getDescendatsPerClient() {
		List<Integer> descendantsPerClient = new ArrayList<>();
		castsPerClient.forEach((client, noOfDescendants) -> {
			descendantsPerClient.add(noOfDescendants);
		});
		return descendantsPerClient;
	}

	private static List<String> getClients() {
		List<String> clients = new ArrayList<>();
		castsPerClient.forEach((client, noOfDescendants) -> {
			clients.add(client);
		});
		return clients;
	}

	public static Double computeMedian(List<Integer> descendantsPerClient) {
		int size = descendantsPerClient.size();

		switch (size) {
		case 0:
			return 0.0;
		case 1:
			return descendantsPerClient.get(0) * 1.0;
		case 2:
			return (descendantsPerClient.get(0) + descendantsPerClient.get(1)) / 2.0;
		default:
			Collections.sort(descendantsPerClient);
			descendantsPerClient.forEach(des -> System.out.println(des));
			System.out.println();
			return size % 2 != 0 ? descendantsPerClient.get(size / 2)
					: (descendantsPerClient.get(size / 2 - 1) + descendantsPerClient.get(size / 2)) / 2.0;
		}
	}

	private static void printCastsPerClient(List<String> clients, List<Integer> casts) {
		for (int i = 0; i < clients.size(); i++)
			System.out.println(clients.get(i) + " -> " + casts.get(i));
	}

}
