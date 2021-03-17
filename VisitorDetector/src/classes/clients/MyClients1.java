package classes.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
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
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.search.SearchMatch;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import utils.PseudoMethod;
import utils.SearchUtils;
import utils.StringParser;
import utils.Utils;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;
import visitordetector.metamodel.factory.Factory;

@RelationBuilder
public class MyClients1 implements IRelationBuilder<MMethod, MClass> {

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		Group<MMethod> clients = new Group<>();
		try {
			SearchUtils searchUtils = new SearchUtils();
			List<PseudoMethod> pMethods = new ArrayList<>();
			List<SearchMatch> methodsMatches = searchUtils.getMethodsMatches(arg0, pMethods);
			List<IMethod> methods = searchUtils.getMethods(methodsMatches, pMethods);
			pMethods.clear();
			List<IField> fields = searchUtils.getFields(arg0.getUnderlyingObject());
			methodsMatches = searchUtils.getMethodsMatchesUsingTheseFields(fields, pMethods);
			List<IMethod> allMethods = new ArrayList<>();
			List<IMethod> fieldMethods = searchUtils.getMethods(methodsMatches, pMethods);
			AtomicBoolean visited = new AtomicBoolean();
			for (IMethod method : methods) {
				visited.set(false);
				ASTParser parser = ASTParser.newParser(AST.JLS10);
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setSource(method.getCompilationUnit());
				parser.setResolveBindings(true);
				CompilationUnit cUnit = (CompilationUnit) parser.createAST(null);
				boolean found = visitCompilationUnit(cUnit, method, arg0.getUnderlyingObject(), visited);
				if (!visited.get())
					continue;
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

	private boolean visitCompilationUnit(CompilationUnit cUnit, IMethod methodSearchingFor, IType baseType,
			AtomicBoolean visited) {
		AtomicBoolean found = new AtomicBoolean(false);
		cUnit.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration methodDeclaration) {
				if (!visited.get()) {
					try {
						IJavaElement element = methodDeclaration.resolveBinding().getJavaElement();
						if (element != null
								&& StringParser.replaceKey(element.toString()).equals(methodSearchingFor.toString())) {
							boolean result = containsOnlyStaticReferences(methodDeclaration, baseType);
							visited.set(true);
							if (result)
								found.set(true);
						}
					} catch (NullPointerException e) {
						System.err.println("visitCompilationUnit -> " + methodDeclaration.getName() + "-" + e);
					}
				}
				return super.visit(methodDeclaration);
			}
		});
		return found.get();
	}

	private boolean containsOnlyStaticReferences(MethodDeclaration method, IType baseType) {
		Block body = method.getBody();
		if (body == null || body.statements().size() == 0)
			return true;
		String fullyQualifiedName = baseType.getFullyQualifiedName().replace('.', '/');
		AtomicBoolean containsStaticReference = new AtomicBoolean(false);
		AtomicBoolean containsNonStaticReference = new AtomicBoolean(false);
		AtomicBoolean constainsTypeAnnotation = new AtomicBoolean(false);
		Type returnType = method.getReturnType2();
		if (returnType != null) {
			try {
				IJavaElement returnTypeBinding = returnType.resolveBinding().getJavaElement();
				if (returnTypeBinding != null
						&& SearchUtils.containsType(returnTypeBinding.toString(), fullyQualifiedName)) {
					return false;
				}
			} catch (NullPointerException e) {
				System.err.println("returnType ->" + returnType);
			}
		}
		List<SingleVariableDeclaration> parameters = method.parameters();
		for (SingleVariableDeclaration parameter : parameters) {
			Type paramType = parameter.getType();
			try {
				IJavaElement paramTypeBinding = paramType.resolveBinding().getJavaElement();
				if (paramTypeBinding != null
						&& SearchUtils.containsType(paramTypeBinding.toString(), fullyQualifiedName)) {
					return false;
				}
			} catch (NullPointerException e) {
				System.err.println("paramType ->" + paramType);
			}

		}

		Javadoc methodDoc = method.getJavadoc();
		if (methodDoc != null && methodDoc.toString().contains(baseType.getElementName()))
			constainsTypeAnnotation.set(true);
		List<Statement> statements = body.statements();
		for (Statement statement : statements) {
			statement.accept(new ASTVisitor() {
				public boolean visit(QualifiedName field) {
					try {
						IJavaElement qualifier = field.getQualifier().resolveBinding().getJavaElement();
						if (qualifier != null && qualifier.getElementType() == IJavaElement.TYPE) {
							if (SearchUtils.containsType(qualifier.toString(), fullyQualifiedName))
								containsStaticReference.set(true);
						}
					} catch (NullPointerException e) {
						System.err.println("qualifiedName ->" + field);
					}
					return super.visit(field);
				}
			});
			statement.accept(new ASTVisitor() {
				public boolean visit(MethodInvocation methodInvocation) {
					try {
						IJavaElement element = methodInvocation.resolveMethodBinding().getJavaElement();
						IMethod method = (IMethod) element;
						if (method.getDeclaringType().toString().equals(baseType.toString())
								&& Flags.isStatic(method.getFlags())) {
							containsStaticReference.set(true);
						}
					} catch (NullPointerException | JavaModelException e) {
						System.err.println("methodInvocation ->" + methodInvocation);
					}
					return super.visit(methodInvocation);
				}
			});
			statement.accept(new ASTVisitor() {
				public boolean visit(SuperMethodInvocation methodInvocation) {
					try {
						IJavaElement element = methodInvocation.resolveMethodBinding().getJavaElement();
						IMethod method = (IMethod) element;
						if (method.getDeclaringType().toString().equals(baseType.toString())
								&& Flags.isStatic(method.getFlags())) {
							containsStaticReference.set(true);
						}
					} catch (NullPointerException | JavaModelException e) {
						System.err.println("methodInvocation ->" + methodInvocation);
					}
					return super.visit(methodInvocation);
				}
			});
			statement.accept(new ASTVisitor() {
				public boolean visit(CastExpression field) {
					try {
						IJavaElement type = field.getType().resolveBinding().getJavaElement();
						if (type != null && SearchUtils.containsType(type.toString(), fullyQualifiedName))
							containsNonStaticReference.set(true);
					} catch (NullPointerException e) {
						System.err.println("castExpression ->" + field);
					}
					return super.visit(field);
				}
			});
			if (containsNonStaticReference.get()) {
				return false;

			}

			statement.accept(new ASTVisitor() {
				public boolean visit(VariableDeclarationStatement field) {
					try {
						IJavaElement type = field.getType().resolveBinding().getJavaElement();
						if (type != null && SearchUtils.containsType(type.toString(), fullyQualifiedName))
							containsNonStaticReference.set(true);
					} catch (NullPointerException e) {
						System.err.println("variableDeclarationStatement ->" + field);
					}
					return super.visit(field);
				}
			});
			if (containsNonStaticReference.get()) {
				return false;
			}
			statement.accept(new ASTVisitor() {
				public boolean visit(EnhancedForStatement forStatement) {
					SingleVariableDeclaration param = forStatement.getParameter();
					try {
						IJavaElement type = param.getType().resolveBinding().getJavaElement();
						if (type != null && SearchUtils.containsType(type.toString(), fullyQualifiedName))
							containsNonStaticReference.set(true);
					} catch (NullPointerException e) {
						System.err.println("enhancedForStatement ->" + forStatement);
					}
					return super.visit(forStatement);
				}
			});
			if (containsNonStaticReference.get()) {
				return false;
			}
			statement.accept(new ASTVisitor() {
				public boolean visit(InstanceofExpression iExp) {
					try {
						IJavaElement type = iExp.getRightOperand().resolveBinding().getJavaElement();
						if (type != null && SearchUtils.containsType(type.toString(), fullyQualifiedName))
							containsNonStaticReference.set(true);
					} catch (NullPointerException e) {
						System.err.println("instanceOfExpression ->" + iExp);
					}
					return super.visit(iExp);
				}
			});
			if (containsNonStaticReference.get()) {
				return false;
			}
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
}
