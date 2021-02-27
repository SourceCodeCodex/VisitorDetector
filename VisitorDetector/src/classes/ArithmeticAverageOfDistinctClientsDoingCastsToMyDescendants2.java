package classes;

import java.util.ArrayList;
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
public class ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2 implements IPropertyComputer<Double, MClass> {
	private Map<String, Integer> castsPerClient;

	@Override
	public Double compute(MClass arg0) {
		castsPerClient = new HashMap<>();
		List<MMethod> allClients = arg0.myClients2().getElements();
		allClients.forEach(client -> castsPerClient.putIfAbsent(getElementName(client), 0));
		for (MClass descendent : arg0.descendantsGroup().getElements()) {
			List<String> clients;
			clients = getClients(descendent);
			incrementCastsPerClient(clients);
		}
		return computeArithmeticAverage();
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

	private Double computeArithmeticAverage() {
		List<Integer> descendantsPerClient = new ArrayList<>();
		castsPerClient.forEach((client, noOfDescendants) -> {
			descendantsPerClient.add(noOfDescendants);
		});
		int size = descendantsPerClient.size();
		switch (size) {
		case 0:
			return 0.0;
		case 1:
			return descendantsPerClient.get(0) * 1.0;
		default:
			castsPerClient.forEach((client, noOfDescendants) -> System.out.println(client + "-" + noOfDescendants));
			return descendantsPerClient.stream().reduce(0, (a, b) -> a + b) * 1.0 / descendantsPerClient.size();
		}
	}

}
