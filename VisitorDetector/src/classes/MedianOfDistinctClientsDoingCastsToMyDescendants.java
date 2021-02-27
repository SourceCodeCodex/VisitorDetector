package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;

@PropertyComputer
public class MedianOfDistinctClientsDoingCastsToMyDescendants implements IPropertyComputer<Double, MClass> {
	private Map<String, Integer> castsPerClient;

	@Override
	public Double compute(MClass arg0) {
		castsPerClient = new HashMap<>();
		List<MMethod> allClients = arg0.myClients().getElements();
		allClients.forEach(client -> castsPerClient.putIfAbsent(getElementName(client), 0));
		for (MClass descendent : arg0.descendantsGroup().getElements()) {
			List<String> clients;
			clients = getClients(descendent);
			incrementCastsPerClient(clients);
		}
		return computeMedian();
	}

	private List<String> getClients(MClass arg0) {
		List<String> clients = new ArrayList<>();
		for (MMethod method : arg0.distinctMethodsWithCastsToMe().getElements()) {
			String client = getElementName(method);
			clients.add(client);
		}
		return clients;
	}

	private String getElementName(MMethod method) {
		try {
			return method.parentClassName() + "-" + method.toString() + "-"
					+ method.getUnderlyingObject().getSignature() + method.getUnderlyingObject().getReturnType();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void incrementCastsPerClient(List<String> clients) {
		for (String client : clients) {
			Optional<Integer> optName = Optional.ofNullable(castsPerClient.get(client));
			if (optName.isPresent())
				castsPerClient.put(client, castsPerClient.get(client) + 1);
		}
	}

	private Double computeMedian() {
		List<Integer> descendantsPerClient = new ArrayList<>();
		List<String> clients = new ArrayList<>();
		castsPerClient.forEach((client, noOfDescendants) -> {
			descendantsPerClient.add(noOfDescendants);
			clients.add(client);
		});
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
			Collections.sort(clients, (a, b) -> castsPerClient.get(a) - castsPerClient.get(b));
			descendantsPerClient.forEach(des -> System.out.println(des));
//			printCastsPerClient(clients, descendantsPerClient);
			System.out.println();
			return size % 2 != 0 ? descendantsPerClient.get(size / 2)
					: (descendantsPerClient.get(size / 2 - 1) + descendantsPerClient.get(size / 2)) / 2.0;
		}
	}
	
	private void printCastsPerClient(List<String> clients, List<Integer> casts) {
		for(int i=0; i < clients.size(); i++)
			System.out.println(clients.get(i) + " -> " + casts.get(i));
	}
}
