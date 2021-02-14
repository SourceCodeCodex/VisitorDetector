package utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import visitordetector.metamodel.entity.MMethod;

public class Utils {
	public static List<MMethod> removeDuplicates(List<MMethod> methodsWithDuplicates,
			List<MMethod> methodsWithoutDuplicates) throws JavaModelException {
		boolean found;
		for (MMethod methodOne : methodsWithDuplicates) {
			found = false;
			for (MMethod methodTwo : methodsWithoutDuplicates) {
				if (compareMethods(methodOne, methodTwo)) {
					found = true;
					break;
				}
			}
			if (!found)
				methodsWithoutDuplicates.add(methodOne);
		}
		return methodsWithoutDuplicates;
	}

	public static List<IMethod> removeDuplicates(List<IMethod> methodsWithDuplicates) throws JavaModelException {
		List<IMethod> methodsWithoutDuplicates = new ArrayList<>();
		boolean found;
		for (IMethod methodOne : methodsWithDuplicates) {
			found = false;
			for (IMethod methodTwo : methodsWithoutDuplicates) {
				if (compareMethods(methodOne, methodTwo)) {
					found = true;
					break;
				}
			}
			if (!found)
				methodsWithoutDuplicates.add(methodOne);
		}
		return methodsWithoutDuplicates;
	}

	public static boolean compareMethods(IMethod mOne, IMethod mTwo) throws JavaModelException {
		IType parent1 = (IType) (mOne.getParent());
		IType parent2 = (IType) (mTwo.getParent());
		if (!parent1.getFullyQualifiedName().equals(parent2.getFullyQualifiedName()))
			return false;
		if (!mOne.getElementName().equals(mTwo.getElementName()))
			return false;
		if (!mOne.getSignature().equals(mTwo.getSignature()))
			return false;
		return true;
	}

	private static boolean compareMethods(MMethod mOne, MMethod mTwo) throws JavaModelException {
		if (!mOne.parentClassName().equals(mTwo.parentClassName()))
			return false;
		IMethod methodOne = mOne.getUnderlyingObject();
		IMethod methodTwo = mTwo.getUnderlyingObject();
		if (!methodOne.getElementName().equals(methodTwo.getElementName()))
			return false;
		if (!methodOne.getSignature().equals(methodTwo.getSignature()))
			return false;
		return true;
	}
}
