package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import visitordetector.metamodel.entity.MMethod;

public class Utils {
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

	public static String getElementName(MMethod method) {
		try {
			return method.parentClassName() + "-" + method.toString() + "-"
					+ method.getUnderlyingObject().getSignature() + method.getUnderlyingObject().getReturnType();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getElementName(IMethod method) {
		try {
			IType parent = (IType) method.getParent();
			return parent.getFullyQualifiedName() + "-" + method.toString() + "-" + method.getSignature()
					+ method.getReturnType();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getParentFullyQualifiedName(IMethod method) {
		IType parent = (IType) method.getParent();
		return parent.getFullyQualifiedName();
	}

	public static List<IMethod> removeAbstractMethods(List<IMethod> methods) {
		return methods.stream().filter(method -> {
			try {
				return !Flags.isAbstract(method.getFlags());
			} catch (JavaModelException e) {
				// System.err.println("removeAbstractMethods->" + method.getElementName());
			}
			return false;
		}).collect(Collectors.toList());
	}

	public static boolean isInAInnerMethod(ASTNode node, MethodDeclaration methodSearchingFor) {
		try {
			ASTNode astNode = node.getParent();
			while (astNode.getNodeType() != ASTNode.METHOD_DECLARATION) {
				astNode = astNode.getParent();
			}
			MethodDeclaration parent = (MethodDeclaration) astNode;
			return !(methodSearchingFor.toString().equals(parent.toString()));
		} catch (Exception e) {
			System.err.println("isInAInnerMethod->" + node);
		}
		return true;
	}

	public static boolean visitMethod(IMethod method, Expression expr, IType analyzedType, String fullyQualifiedName)
			throws JavaModelException {
		if (Flags.isStatic(method.getFlags()))
			return false;
		if (expr == null) {
			if (getParentFullyQualifiedName(method).equals(analyzedType.getFullyQualifiedName()))
				return true;
			return false;
		}
		IJavaElement element = expr.resolveTypeBinding().getJavaElement();
		if (containsType(element.toString(), fullyQualifiedName))
			return true;
		return false;
	}

	public static boolean containsType(String type1Info, String type2Info) {
		if (!type1Info.contains(type2Info))
			return false;
		int pos = type1Info.indexOf(type2Info) + type2Info.length();
		char ch = type1Info.charAt(pos);
		return !Character.isLetterOrDigit(ch) && ch != '$';
	}

}
