package classes.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
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
import utils.CastSearchingUtils;
import utils.Utils;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;
import visitordetector.metamodel.factory.Factory;

@RelationBuilder
public class MyClients implements IRelationBuilder<MMethod, MClass> {
	private CastSearchingUtils csu;
	private IType analyzedType;
	private String fullyQualifiedName;
	private List<IMethod> methods;

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		Group<MMethod> clients = new Group<>();
		csu = new CastSearchingUtils(arg0);
		analyzedType = arg0.getUnderlyingObject();
		fullyQualifiedName = analyzedType.getFullyQualifiedName().replace('.', '/');
		try {
			methods = getMethods(arg0);
			for (IMethod method : methods) {
				ASTParser parser = ASTParser.newParser(AST.JLS10);
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				ICompilationUnit iCUnit = method.getCompilationUnit();
				if (iCUnit == null) {
					continue;
				}
				parser.setSource(iCUnit);
				parser.setResolveBindings(true);
				CompilationUnit cUnit = (CompilationUnit) parser.createAST(null);
				boolean found = visitCompilationUnit(cUnit, method);
				if (found)
					clients.add(Factory.getInstance().createMMethod(method));
			}
		} catch (CoreException e) {
			System.err.println("MMethod - MClass -> MyClients:" + e.getMessage());
		}
		return clients;
	}

	private boolean visitCompilationUnit(CompilationUnit cUnit, IMethod methodSearchingFor) {
		AtomicBoolean found = new AtomicBoolean(false);
		AtomicBoolean visited = new AtomicBoolean(false);
		String methodInfo = methodSearchingFor.toString();
		cUnit.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration methodDeclaration) {
				if (!visited.get()) {
					try {
						IJavaElement element = methodDeclaration.resolveBinding().getJavaElement();
						IMethod method = (IMethod) element;
						if (element != null && method.equals(methodSearchingFor)) {
							boolean result = visitMethodBody(methodDeclaration, methodSearchingFor);
							visited.set(true);
							if (result)
								found.set(true);
						}
					} catch (Exception e) {
						// System.err.println("visitCompilationUnit -> " + methodDeclaration.getName() +
						// "-" + e);
					}
				}
				return super.visit(methodDeclaration);
			}
		});
		return found.get();
	}

	private boolean visitMethodBody(MethodDeclaration methodDeclaration, IMethod methodSearchingFor) {
		Block methodBody = methodDeclaration.getBody();
		if (methodBody == null || methodBody.statements().size() == 0)
			return false;
		AtomicBoolean found = new AtomicBoolean(false);
		methodBody.accept(new ASTVisitor() {
			public boolean visit(MethodInvocation methodInvocation) {
				if (!found.get()) {
					try {
						IMethod method = (IMethod) methodInvocation.resolveMethodBinding().getJavaElement();
						Expression expr = methodInvocation.getExpression();
						if (Utils.visitMethod(method, expr, analyzedType, fullyQualifiedName)
								&& !Utils.isInAInnerMethod(methodInvocation, methodDeclaration))
							found.set(true);
					} catch (Exception e) {
						// System.err.println("MethodInvocation -> " + methodInvocation);
					}
				}
				return super.visit(methodInvocation);
			}
		});
		if (found.get())
			return true;
		methodBody.accept(new ASTVisitor() {
			public boolean visit(SuperMethodInvocation methodInvocation) {
				if (!found.get()) {
					try {
						IMethod method = (IMethod) methodInvocation.resolveMethodBinding().getJavaElement();
						Expression expr = methodInvocation.getQualifier();
						if (Utils.visitMethod(method, expr, analyzedType, fullyQualifiedName)
								&& !Utils.isInAInnerMethod(methodInvocation, methodDeclaration))
							found.set(true);
					} catch (Exception e) {
						// System.err.println("SuperMethodInvocation -> " + methodInvocation);
					}
				}
				return super.visit(methodInvocation);
			}
		});
		if (found.get())
			return true;
		if (csu.visitMethodDeclaration(methodDeclaration))
			return true;
		return found.get();
	}

	private List<IMethod> getMethods(MClass arg0) throws CoreException {
		List<IMethod> methods = new ArrayList<>();
		List<String> fields = new ArrayList<>();
		SearchPattern pattern = SearchPattern.createPattern(arg0.getUnderlyingObject(),
				IJavaSearchConstants.REFERENCES);
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		SearchRequestor requestor = new SearchRequestor() {
			@Override
			public void acceptSearchMatch(SearchMatch arg0) throws CoreException {
				IJavaElement element = (IJavaElement) arg0.getElement();
				if (element.getElementType() == IJavaElement.METHOD) {
					IMethod method = (IMethod) element;
					addMethodToCollection(method, methods);
				} else if (element.getElementType() == IJavaElement.FIELD) {
					IField field = (IField) element;
					if (!fields.contains(field.toString())) {
						fields.add(field.toString());
						addMethodsToCollection(getMethodsUsingThisField(field), methods);
					}
				}
			}
		};
		SearchEngine searchEngine = new SearchEngine();
		searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
				requestor, new NullProgressMonitor());
		return methods;
	}

	private void addMethodToCollection(IMethod method, List<IMethod> methods) {
		if (method.isLambdaMethod()) {
			IJavaElement parent = method.getParent();
			while (parent != null && parent.toString().contains("<lambda")) {
				parent = parent.getParent();
			}
			if (parent == null || parent.getElementType() != IJavaElement.METHOD) {
				return;
			}
			method = (IMethod) parent;
		}
		if (!methods.contains(method)) {
			methods.add(method);
		}
	}

	private void addMethodsToCollection(List<IMethod> ms, List<IMethod> methods) {
		for (IMethod method : ms) {
			addMethodToCollection(method, methods);
		}
	}

	private List<IMethod> getMethodsUsingThisField(IField field) throws CoreException {
		List<IMethod> methods = new ArrayList<>();
		SearchPattern pattern = SearchPattern.createPattern(field, IJavaSearchConstants.REFERENCES);
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		SearchRequestor requestor = new SearchRequestor() {
			@Override
			public void acceptSearchMatch(SearchMatch arg0) throws CoreException {
				IJavaElement element = (IJavaElement) arg0.getElement();
				if (element.getElementType() == IJavaElement.METHOD) {
					addMethodToCollection((IMethod) element, methods);
				}
			}
		};
		SearchEngine searchEngine = new SearchEngine();
		searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
				requestor, new NullProgressMonitor());
		return methods;
	}
}
