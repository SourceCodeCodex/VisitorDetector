package classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
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
import utils.StringParser;
import utils.Utils;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;
import visitordetector.metamodel.factory.Factory;

@RelationBuilder
public class MyClients2 implements IRelationBuilder<MMethod, MClass> {
	private List<PseudoMethod> pMethods;
	private List<String> pFields;
	private List<SearchMatch> fieldsMatches;

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		pMethods = new LinkedList<>();
		pFields = new ArrayList<>();
		fieldsMatches = new ArrayList<>();
		Group<MMethod> clients = new Group<>();
		List<SearchMatch> methodsMatches;
		try {
			methodsMatches = this.getMethodsMatches(arg0);
			List<IMethod> methods = getMethods(methodsMatches);
			// List<SearchMatch> fieldsMatches = this.getFieldsMatches(arg0);
			List<IField> fields = this.getFields(fieldsMatches, arg0.getUnderlyingObject());
			methodsMatches = getMethodsMatchesUsingTheseFields(fields);
			List<IMethod> fieldMethods = getMethods(methodsMatches);
			methods.addAll(fieldMethods);
			methods = Utils.removeDuplicates(methods);
			for (IMethod method : methods) {
				ASTParser parser = ASTParser.newParser(AST.JLS10);
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setSource(method.getCompilationUnit());
				parser.setResolveBindings(true);
				CompilationUnit cUnit = (CompilationUnit) parser.createAST(null);
				boolean found = visitCompilationUnit(cUnit, method, arg0.getUnderlyingObject());
				if (found)
					clients.add(Factory.getInstance().createMMethod(method));
			}
		} catch (CoreException e) {
			System.err.println("MMethod - MClass -> MyClientsGroup2:" + e.getMessage());
		}
		return clients;
	}

	private boolean visitCompilationUnit(CompilationUnit cUnit, IMethod methodSearchingFor, IType baseType) {
		AtomicBoolean found = new AtomicBoolean(false);
		AtomicBoolean visited = new AtomicBoolean(false);
		cUnit.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration methodDeclaration) {
				if (!visited.get()) {
					try {
						IJavaElement element = methodDeclaration.resolveBinding().getJavaElement();
						if (element != null
								&& StringParser.replaceKey(element.toString()).equals(methodSearchingFor.toString())) {
							boolean result = visitMethodBody(methodDeclaration.getBody(), baseType, methodSearchingFor);
							visited.set(true);
							if (result)
								found.set(true);
						}
					} catch (NullPointerException e) {
						System.err.println("visitCompilationUnit -> " + e);
					}
				}
				return super.visit(methodDeclaration);
			}
		});
		return found.get();
	}

	private boolean visitMethodBody(Block methodBody, IType baseType, IMethod methodSearchingFor) {
		if (methodBody == null || methodBody.statements().size() == 0)
			return false;
		AtomicBoolean found = new AtomicBoolean(false);
		methodBody.accept(new ASTVisitor() {
			public boolean visit(MethodInvocation methodInvocation) {
				if (!found.get()) {
					try {
						List<IJavaElement> elements = visitMethodInvocation(methodInvocation, methodSearchingFor);
						boolean result = checkMethodInvocation(elements, baseType, methodSearchingFor);
						if (result)
							found.set(true);
					} catch (Exception e) {
						System.err.println("visitMethodBody -> " + e);
					}
				}
				return super.visit(methodInvocation);
			}
		});
		if (found.get())
			return found.get();
		methodBody.accept(new ASTVisitor() {
			public boolean visit(SuperMethodInvocation methodInvocation) {
				if (!found.get()) {
					try {
						List<IJavaElement> elements = visitMethodInvocation(methodInvocation, methodSearchingFor);
						boolean result = checkMethodInvocation(elements, baseType, methodSearchingFor);
						if (result)
							found.set(true);
					} catch (Exception e) {
						System.err.println("visitMethodBody -> " + e);
					}
				}
				return super.visit(methodInvocation);
			}
		});
		return found.get();
	}

	private boolean checkMethodInvocation(List<IJavaElement> elements, IType baseType, IMethod methodSearchingFor) {
		AtomicBoolean found = new AtomicBoolean(false);
		IJavaElement firstElement = elements.get(0);
		if (firstElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT
				|| firstElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT) {
			int i = getFirstElement(elements);
			firstElement = elements.get(i);
			elements = elements.subList(i, elements.size());
		}
		if (firstElement.getElementType() == IJavaElement.TYPE) {
			IType type = (IType) firstElement;
			if (StringParser.replaceKey(type.toString()).equals(baseType.toString()) && elements.size() > 2) {
				found.set(true);
			}
		} else if (elements.size() == 1) {
			IMethod method = (IMethod) firstElement;
			try {
				if (method.getDeclaringType().toString().equals(baseType.toString())
						&& !Flags.isStatic(method.getFlags())) {
					found.set(true);
				}
			} catch (JavaModelException e) {
				System.err.println("MyClientsGroup2 -> visitMethodBody:" + e.getMessage());
			}
		} else if (firstElement.getElementType() == IJavaElement.FIELD) {
			String fieldInfo = getFieldInfo((IField) firstElement);
			if (checkMethodInvocationForSearchedType(fieldInfo, elements, baseType)) {
				found.set(true);
			}
		} else if (firstElement.getElementType() == IJavaElement.LOCAL_VARIABLE) {
			ILocalVariable variable = (ILocalVariable) firstElement;
			if (checkMethodInvocationForSearchedType(variable.toString(), elements, baseType)) {
				found.set(true);
			}
		} else if (firstElement.getElementType() == IJavaElement.METHOD) {
			if (checkChainingForSearchedType(firstElement, elements, baseType))
				found.set(true);
		}
		return found.get();
	}

	private boolean checkChainingForSearchedType(IJavaElement element, List<IJavaElement> elements, IType baseType) {
		if (element.getElementType() == IJavaElement.METHOD) {
			IMethod method = (IMethod) element;
			try {
				if (method.getReturnType().contains(baseType.getElementName()) && elements.size() > 1
						&& elements.get(1).getElementType() == IJavaElement.METHOD) {
					return true;
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		} else if (element.getElementType() == IJavaElement.FIELD) {
			String fieldInfo = getFieldInfo((IField) element);
			return checkMethodInvocationForSearchedType(fieldInfo, elements, baseType);
		}
		if (elements.size() > 1)
			return checkChainingForSearchedType(elements.get(1), elements.subList(1, elements.size()), baseType);
		return false;
	}

	private String getFieldInfo(IField field) {
		String fieldInfo = field.toString();
		return fieldInfo.substring(fieldInfo.lastIndexOf(field.getElementName()));
	}

	private int getFirstElement(List<IJavaElement> elements) {
		int i = 0;
		while (elements.get(i).getElementType() == IJavaElement.PACKAGE_FRAGMENT
				|| elements.get(i).getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT)
			i++;
		return i;
	}

	private boolean checkMethodInvocationForSearchedType(String element, List<IJavaElement> elements, IType baseType) {
		String fullyQualifiedName = baseType.getFullyQualifiedName().replace('.', '/');
		int i = 1;
		if (element.contains(fullyQualifiedName)) {
			IJavaElement secondElement = elements.get(i);
			if (secondElement.getElementType() == IJavaElement.METHOD)
				return true;
			while (elements.get(i).getElementType() == IJavaElement.LOCAL_VARIABLE)
				i++;
			secondElement = elements.get(i);
			if (secondElement.getElementType() == IJavaElement.METHOD)
				return true;
			if (secondElement.getElementType() == IJavaElement.FIELD) {
				String fieldInfo = getFieldInfo((IField) secondElement);
				return checkMethodInvocationForSearchedType(fieldInfo, elements.subList(i, elements.size()), baseType);
			}
		} else {
			while (i < elements.size()) {
				IJavaElement secondElement = elements.get(i);
				if (secondElement.getElementType() == IJavaElement.FIELD) {
					String fieldInfo = getFieldInfo((IField) secondElement);
					return checkMethodInvocationForSearchedType(fieldInfo, elements.subList(i, elements.size()),
							baseType);
				} else if (secondElement.getElementType() == IJavaElement.METHOD) {
					IMethod method = (IMethod) secondElement;
					try {
						if (method.getReturnType().contains(baseType.getElementName()) && i < (elements.size() - 1)
								&& elements.get(i + 1).getElementType() == IJavaElement.METHOD)
							return true;
					} catch (JavaModelException e) {
						e.printStackTrace();
					}
				}
				i++;
			}
		}
		return false;
	}

	private List<IJavaElement> visitMethodInvocation(ASTNode methodInvocation, IMethod methodSearchingFor) {
		List<IJavaElement> elements = new ArrayList<>();
		List<String> params = new ArrayList<>();
		methodInvocation.accept(new ASTVisitor() {
			public boolean visit(SimpleName name) {
				try {
					IJavaElement element = name.resolveBinding().getJavaElement();
					if (element != null && !isAParameter(params, name.toString())) {
						elements.add(element);
						if (element.getElementType() == IJavaElement.METHOD) {
							params.clear();
							String methoInvocationInfo = methodInvocation.toString();
							String methodInfo = methoInvocationInfo
									.substring(methoInvocationInfo.indexOf(name.toString()));
							try {
								params.add(methodInfo.substring(methodInfo.indexOf('('),
										getParametersClosingPar(methodInfo) + 1));
							} catch (Exception e) {
								System.err.println(methodInvocation);
							}
						}
					}
				} catch (NullPointerException e) {
					System.err.println("visitMethodInvocation -> " + e);
				}
				return super.visit(name);
			}
		});
		return elements;
	}

	private int getParametersClosingPar(String method) {
		int lPars = 0;
		int rPars = 0;
		int pos = 0;
		for (char c : method.toCharArray()) {
			if (c == '(')
				lPars++;
			if (c == ')')
				rPars++;
			if (lPars == rPars && lPars != 0 && rPars != 0)
				return pos;
			pos++;
		}
		return pos;
	}

	private boolean isAParameter(List<String> parameters, String var) {
		for (String parameter : parameters) {
			if (parameter.contains(var))
				return true;
		}
		return false;
	}

	private List<SearchMatch> getMethodsMatchesUsingTheseFields(List<IField> fields) throws CoreException {
		List<SearchMatch> matches = new LinkedList<>();
		List<String> uniqueResources = new ArrayList<>();
		for (IField field : fields) {
			SearchPattern pattern = SearchPattern.createPattern(field, IJavaSearchConstants.REFERENCES);
			IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
			SearchRequestor requestor = new SearchRequestor() {
				@Override
				public void acceptSearchMatch(SearchMatch arg0) throws CoreException {
					String element = arg0.getElement().toString();
					if (!isMethod(element.split(" ")))
						return;
					try {
						PseudoMethod method = StringParser.parse(element);
						if (method != null && !pMethods.contains(method)) {
							pMethods.add(method);
						}
						String path = arg0.getResource().getProjectRelativePath().toOSString();
						if (!uniqueResources.contains(path)) {
							uniqueResources.add(path);
							matches.add(arg0);
						}
					} catch (Exception e) {
						System.err.println("String Parser error ->" + element + " : " + e.getMessage());

					}
				}
			};
			SearchEngine searchEngine = new SearchEngine();
			searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
					requestor, new NullProgressMonitor());
		}
		return matches;
	}

	// private List<SearchMatch> getFieldsMatches(MClass arg0) throws CoreException
	// {
	// List<SearchMatch> matches = new LinkedList<>();
	// List<String> uniqueResources = new ArrayList<>();
	// SearchPattern pattern =
	// SearchPattern.createPattern(arg0.getUnderlyingObject(),
	// IJavaSearchConstants.REFERENCES |
	// IJavaSearchConstants.FIELD_DECLARATION_TYPE_REFERENCE);
	// IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
	// SearchRequestor requestor = new SearchRequestor() {
	// @Override
	// public void acceptSearchMatch(SearchMatch arg0) throws CoreException {
	// String element = arg0.getElement().toString();
	// String field = StringParser.parseField(element);
	// pFields.add(field);
	// String path = arg0.getResource().getProjectRelativePath().toOSString();
	// if (!uniqueResources.contains(path)) {
	// uniqueResources.add(path);
	// matches.add(arg0);
	// }
	// }
	// };
	// SearchEngine searchEngine = new SearchEngine();
	// searchEngine.search(pattern, new SearchParticipant[] {
	// SearchEngine.getDefaultSearchParticipant() }, scope,
	// requestor, new NullProgressMonitor());
	// return matches;
	// }

	private List<SearchMatch> getMethodsMatches(MClass arg0) throws CoreException {
		List<SearchMatch> matches = new LinkedList<>();
		List<String> uniqueResources = new ArrayList<>();
		List<String> uniqueFieldsResources = new ArrayList<>();
		SearchPattern pattern = SearchPattern.createPattern(arg0.getUnderlyingObject(),
				IJavaSearchConstants.REFERENCES);
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		SearchRequestor requestor = new SearchRequestor() {
			@Override
			public void acceptSearchMatch(SearchMatch arg0) throws CoreException {

				String element = arg0.getElement().toString();
				if (!isMethod(element.split(" ")))
					return;
				try {
					PseudoMethod method = StringParser.parse(element);
					if (method != null && !pMethods.contains(method)) {
						pMethods.add(method);
					}
					String path = arg0.getResource().getProjectRelativePath().toOSString();
					if (!uniqueResources.contains(path)) {
						uniqueResources.add(path);
						matches.add(arg0);
					}
				} catch (Exception e) {
					String field = StringParser.parseField(element);
					if (!pFields.contains(field))
						pFields.add(StringParser.parseField(element));
					String path = arg0.getResource().getProjectRelativePath().toOSString();
					if (!uniqueFieldsResources.contains(path)) {
						uniqueFieldsResources.add(path);
						fieldsMatches.add(arg0);
					}
				}
			}
		};
		SearchEngine searchEngine = new SearchEngine();
		searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
				requestor, new NullProgressMonitor());
		return matches;
	}

	private boolean isMethod(String[] elements) {
		return !elements[0].equals("import") && !elements[0].equals("class");
	}

	private List<IField> getFields(List<SearchMatch> matches, IType baseType) throws JavaModelException {
		List<IField> fields = new LinkedList<>();
		for (SearchMatch match : matches) {
			IJavaElement element = JavaCore.create(match.getResource());
			if (element.getElementType() == IJavaElement.COMPILATION_UNIT) {
				IType[] types = ((ICompilationUnit) element).getAllTypes();
				for (IType type : types) {
					List<String> remainingFields = new LinkedList<>();
					for (String pField : pFields) {
						boolean added = false;
						for (IField field : type.getFields()) {
							if (pField.equals(StringParser.parseField(field.toString()))) {
								String fieldInfo = field.getTypeSignature();
								if (fieldInfo.contains(baseType.getElementName()))
									fields.add(field);
								added = true;
								break;
							}
						}
						if (added == false) {
							remainingFields.add(pField);
						}
					}
					pFields = remainingFields;
					if (pFields.size() == 0)
						return fields;
				}
			}
		}
		return fields;
	}

	public List<IMethod> getMethods(List<SearchMatch> matches) throws JavaModelException {
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
