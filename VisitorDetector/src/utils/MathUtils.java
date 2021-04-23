package utils;

import java.util.Collections;
import java.util.List;

import visitordetector.metamodel.entity.MMethod;

public class MathUtils {
	public static Double computeArithmeticAverage(List<Integer> descendantsPerClient, List<MMethod> allCLients) {
		int size = descendantsPerClient.size();
		switch (size) {
		case 0:
			return 0.0;
		case 1:
			return descendantsPerClient.get(0) * 1.0;
		default:
			for (int i = 0; i < descendantsPerClient.size(); i++) {
				if (descendantsPerClient.get(i) > 1) {
					System.out.println(allCLients.get(i) + " - " + allCLients.get(i).parentClassName() + " - "
							+ descendantsPerClient.get(i));
				}
			}
			return descendantsPerClient.stream().reduce(0, (a, b) -> a + b) * 1.0 / descendantsPerClient.size();
		}
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
}
