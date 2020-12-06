package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;

@PropertyComputer
public class AverageOfDescendantsCastsForAClient implements IPropertyComputer<Double, MClass> {
	private Map<String, Integer> descendantsCastsPerClient;

	@Override
	public Double compute(MClass arg0) {
		descendantsCastsPerClient = new HashMap<>();
		for (MClass subtype : arg0.subGroup().getElements()) {
			List<String> clients = getClients(subtype);
			incrementDescendantsCastsPerClient(clients);
		}
		return computeMedian();
	}

	private List<String> getClients(MClass arg0) {
		List<String> clients = new ArrayList<>();
		for (MMethod method : arg0.methodsWithCasts().getElements()) {
			String client = method.parentClassName();
			if (!clients.contains(client)) {
				clients.add(client);
			}
		}
		return clients;
	}

	private void incrementDescendantsCastsPerClient(List<String> clients) {
		for (String client : clients) {
			descendantsCastsPerClient.put(client, descendantsCastsPerClient.getOrDefault(client, 0) + 1);
		}
	}

	private Double computeMedian() {
		List<Integer> descendantsPerClient = new ArrayList<>();
		descendantsCastsPerClient.forEach((client, noOfDescendants) -> {
			descendantsPerClient.add(noOfDescendants);
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
			// for (Integer i : descendantsPerClient) {
			// 	System.out.println(i);
			// }
			// System.out.println();
			return size % 2 != 0 ? descendantsPerClient.get(size / 2)
					: (descendantsPerClient.get(size / 2 - 1) + descendantsPerClient.get(size / 2)) / 2.0;
		}
	}
}
