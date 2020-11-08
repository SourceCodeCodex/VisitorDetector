package classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import utils.PseudoMethod;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;
import visitordetector.metamodel.factory.Factory;

@RelationBuilder
public class MethodsWithCasts implements IRelationBuilder<MMethod, MClass> {
	private List<PseudoMethod> pMethods = new LinkedList<>();

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		Group<MMethod> foundMethods = new Group<>();
		try {
			List<SearchMatch> matches = this.getMatches(arg0);
			List<IMethod> methods = this.getMethods(matches);
			for (IMethod method : methods) {
				MMethod m = Factory.getInstance().createMMethod(method);
				foundMethods.add(m);
			}

		} catch (CoreException e) {
			System.err.println("MMethod - MClass -> MethodsWithCasts:" + e.getMessage());
		}
		return foundMethods;
	}

	private List<SearchMatch> getMatches(MClass arg0) throws CoreException {
		List<SearchMatch> matches = new LinkedList<>();
		List<String> uniqueResources = new ArrayList<>();
		SearchPattern pattern = SearchPattern.createPattern(arg0.getUnderlyingObject(),
				IJavaSearchConstants.REFERENCES | IJavaSearchConstants.CAST_TYPE_REFERENCE);
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		SearchRequestor requestor = new SearchRequestor() {

			@Override
			public void acceptSearchMatch(SearchMatch arg0) throws CoreException {
				PseudoMethod method = parseString(arg0.getElement().toString());
				if (!pMethods.contains(method)) {
					pMethods.add(method);
				}
				System.out.println(arg0.getElement());
				// System.out.println(method);
				String path = arg0.getResource().getProjectRelativePath().toOSString();
				if (!uniqueResources.contains(path)) {
					uniqueResources.add(path);
					matches.add(arg0);
				}
			}
		};
		SearchEngine searchEngine = new SearchEngine();
		searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
				requestor, new NullProgressMonitor());
		return matches;
	}

	private PseudoMethod parseString(String info) {
		String[] segments = info.split(" ");
		String name;
		String temp = info.substring(info.indexOf("{k"));
		name = getMethodName(temp);
		temp = info.substring(info.indexOf('(') + 1, info.indexOf(')'));
		String[] args = temp.split(" ");
		args = this.replaceComma(args);
		String containingClass = getClassName(info.substring(info.indexOf("[in")).split(" "));
		String containingPackage = getPackageName(segments);
		if (args[0].equals("")) {
			return new PseudoMethod(name, containingClass, containingPackage, null);
		}
		return new PseudoMethod(name, containingClass, containingPackage, args);
	}

	private String getMethodName(String temp) {
		int pos1 = temp.indexOf('<');
		int pos0 = temp.indexOf('.') + 1;
		int pos2 = temp.indexOf('(');
		if (pos1 != -1 && pos1 < pos2)
			return temp.substring(pos0, pos1);
		return temp.substring(pos0, pos2);
	}

	private String[] replaceComma(String[] args) {
		String[] temp = new String[args.length];
		int nr = 0;
		for (int index = 0; index < args.length; index++) {
			if (args[index].length() > 1 && args[index].charAt(args[index].length() - 1) == ',')
				args[index] = args[index].substring(0, args[index].lastIndexOf(','));
			if (args[index].contains("<") && !args[index].contains(">")) {
				String aux = args[index];
				index++;
				while (!args[index].contains(">")) {
					aux += " " + args[index];
					index++;
				}
				if (args[index].indexOf(',') == args[index].length() - 1)
					args[index] = args[index].substring(0, args[index].length() - 1);
				args[index] = aux + " " + args[index];

			}
			if (args[index].equals("...")) {
				temp[nr - 1] += "[]";
				continue;
			}
			temp[nr++] = args[index];
		}
		return Arrays.copyOf(temp, nr);
	}

	private String getClassName(String[] s) {
		int index = 1;
		String temp = "";
		while (!s[index].contains("[") && !s[index].contains(".java")) {
			temp = s[index] + "$" + temp;
			index = index + 2;
		}
		return temp.substring(0, temp.length() - 1);
	}

	private String getPackageName(String[] s) {
		int index = s.length - 5;
		if (s[index].equals("[in"))
			index -= 1;
		return s[index];
	}

	private List<IMethod> getMethods(List<SearchMatch> matches) throws JavaModelException {
		List<IMethod> methods = new LinkedList<>();
		for (SearchMatch match : matches) {
			IJavaElement element = JavaCore.create(match.getResource());
			if (element.getElementType() == IJavaElement.COMPILATION_UNIT) {
				IType[] types = ((ICompilationUnit) element).getAllTypes();
				for (IType type : types) {
					List<PseudoMethod> remainingMethods = new LinkedList<>();
					for (PseudoMethod pMethod : pMethods) {
						boolean added = false;
						if (pMethod.verifyClass(type.getFullyQualifiedName())) {
							for (IMethod method : type.getMethods()) {
								if (pMethod.verifyMethod(method)) {
									methods.add(method);
									added = true;
									break;
								}
							}
						}
						if (added == false) {
							remainingMethods.add(pMethod);
						}
					}
					pMethods = remainingMethods;
					if (pMethods.size() == 0)
						return methods;
				}
			}

		}

		return methods;
	}

}
