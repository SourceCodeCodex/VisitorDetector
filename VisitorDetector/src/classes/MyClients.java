package classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
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
public class MyClients implements IRelationBuilder<MMethod, MClass> {
	private List<PseudoMethod> pMethods;
	private List<String> pFields;
	private List<SearchMatch> fieldsMatches;

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		pMethods = new LinkedList<>();
		pFields = new ArrayList<>();
		fieldsMatches = new ArrayList<>();
		Group<MMethod> clients = new Group<>();
		try {
			List<SearchMatch> methodsMatches = this.getMethodsMatches(arg0);
			List<IMethod> methods = getMethods(methodsMatches);
			// List<SearchMatch> fieldsMatches = this.getFieldsMatches(arg0);
			List<IField> fields = this.getFields(fieldsMatches, arg0.getUnderlyingObject());
			methodsMatches = getMethodsMatchesUsingTheseFields(fields);
			List<IMethod> allMethods = new ArrayList<>();
			List<IMethod> fieldMethods = getMethods(methodsMatches);
			for (IMethod method : methods) {
				ASTParser parser = ASTParser.newParser(AST.JLS10);
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setSource(method.getCompilationUnit());
				parser.setResolveBindings(true);
				CompilationUnit cUnit = (CompilationUnit) parser.createAST(null);
				boolean found = visitCompilationUnit(cUnit, method, arg0.getUnderlyingObject());
				if (!found || (found && searchMethod(method, fieldMethods))) {
					allMethods.add(method);
				}
			}
			allMethods.addAll(fieldMethods);
			methods = Utils.removeDuplicates(allMethods);
			for (IMethod method : methods) {
				MMethod m = Factory.getInstance().createMMethod(method);
				clients.add(m);
			}
		} catch (CoreException e) {
			System.err.println("MMethod - MClass -> MyClientsGroup:" + e.getMessage());
		}
		return clients;
	}

	private boolean visitCompilationUnit(CompilationUnit cUnit, IMethod methodSearchingFor, IType baseType) {
		AtomicBoolean found = new AtomicBoolean(false);
		AtomicBoolean visited = new AtomicBoolean(false);
		cUnit.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration methodDeclaration) {
				if (!visited.get()) {
					IJavaElement element = methodDeclaration.resolveBinding().getJavaElement();
					if (element != null
							&& StringParser.replaceKey(element.toString()).equals(methodSearchingFor.toString())) {
						boolean result = containsOnlyStaticReferences(methodDeclaration, baseType);
						visited.set(true);
						if (result)
							found.set(true);
					}
				}
				return super.visit(methodDeclaration);
			}
		});
		return found.get();
	}

	private boolean containsOnlyStaticReferences(MethodDeclaration method, IType baseType) {
		Block body = method.getBody();
		if (body == null)
			return true;
		String fullyQualifiedName = baseType.getFullyQualifiedName().replace('.', '/');
		Type returnType = method.getReturnType2();
		if (returnType != null) {
			IJavaElement returnTypeBinding = returnType.resolveBinding().getJavaElement();
			if (returnTypeBinding != null && returnTypeBinding.toString().contains(fullyQualifiedName))
				return false;
		}
		List<SingleVariableDeclaration> parameters = method.parameters();
		for (SingleVariableDeclaration parameter : parameters) {
			Type paramType = parameter.getType();
			IJavaElement paramTypeBinding = paramType.resolveBinding().getJavaElement();
			if (paramTypeBinding != null && paramTypeBinding.toString().contains(fullyQualifiedName))
				return false;
		}

		AtomicBoolean containsStaticReference = new AtomicBoolean(false);
		AtomicBoolean containsNonStaticReference = new AtomicBoolean(false);
		AtomicBoolean constainsTypeAnnotation = new AtomicBoolean(false);
		Javadoc methodDoc = method.getJavadoc();
		if (methodDoc != null && methodDoc.toString().contains(baseType.getElementName()))
			constainsTypeAnnotation.set(true);
		List<Statement> statements = body.statements();
		for (Statement statement : statements) {
			statement.accept(new ASTVisitor() {
				public boolean visit(QualifiedName field) {
					IJavaElement qualifier = field.getQualifier().resolveBinding().getJavaElement();
					if (qualifier != null && qualifier.getElementType() == IJavaElement.TYPE) {
						if (qualifier.toString().contains(fullyQualifiedName))
							containsStaticReference.set(true);
					}
					return super.visit(field);
				}
			});
			statement.accept(new ASTVisitor() {
				public boolean visit(CastExpression field) {
					IJavaElement type = field.getType().resolveBinding().getJavaElement();
					if (type != null && type.toString().contains(fullyQualifiedName))
						containsNonStaticReference.set(true);
					return super.visit(field);
				}
			});
			if (containsNonStaticReference.get())
				return false;
			statement.accept(new ASTVisitor() {
				public boolean visit(VariableDeclarationStatement field) {
					IJavaElement type = field.getType().resolveBinding().getJavaElement();
					if (type != null && type.toString().contains(fullyQualifiedName))
						containsNonStaticReference.set(true);
					return super.visit(field);
				}
			});
			if (containsNonStaticReference.get())
				return false;
			statement.accept(new ASTVisitor() {
				public boolean visit(EnhancedForStatement forStatement) {
					SingleVariableDeclaration param = forStatement.getParameter();
					IJavaElement type = param.getType().resolveBinding().getJavaElement();
					if (type != null && type.toString().contains(fullyQualifiedName))
						containsNonStaticReference.set(true);
					return super.visit(forStatement);
				}
			});
			if (containsNonStaticReference.get())
				return false;
			statement.accept(new ASTVisitor() {
				public boolean visit(InstanceofExpression iExp) {
					IJavaElement type = iExp.getRightOperand().resolveBinding().getJavaElement();
					if (type != null && type.toString().contains(fullyQualifiedName))
						containsNonStaticReference.set(true);
					return super.visit(iExp);
				}
			});
			if (containsNonStaticReference.get())
				return false;
		}
		return containsStaticReference.get() || constainsTypeAnnotation.get();
	}

	private boolean searchMethod(IMethod method, List<IMethod> methods) {
		for (IMethod m : methods) {
			try {
				if (Utils.compareMethods(m, method))
					return true;
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
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
						 System.err.println("String Parser error ->" + element + " : " +
						 e.getMessage());

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
