package classes.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.search.SearchMatch;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import utils.CastSearchingUtils;
import utils.PseudoMethod;
import utils.SearchUtils;
import utils.StringParser;
import utils.Utils;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;
import visitordetector.metamodel.factory.Factory;

@RelationBuilder
public class MyClients3 implements IRelationBuilder<MMethod, MClass> {
	private CastSearchingUtils csu;
	private IType analyzedType;
	private String fullyQualifiedName;

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		Group<MMethod> clients = new Group<>();
		csu = new CastSearchingUtils(arg0);
		analyzedType = arg0.getUnderlyingObject();
		fullyQualifiedName = analyzedType.getFullyQualifiedName().replace('.', '/');
		List<SearchMatch> methodsMatches;
		try {
			SearchUtils searchUtils = new SearchUtils();
			List<PseudoMethod> pMethods = new ArrayList<>();
			methodsMatches = searchUtils.getMethodsMatches(arg0, pMethods);
			List<IMethod> methods = searchUtils.getMethods(methodsMatches, pMethods);
			List<IField> fields = searchUtils.getFields(arg0.getUnderlyingObject());
			pMethods.clear();
			methodsMatches = searchUtils.getMethodsMatchesUsingTheseFields(fields, pMethods);
			List<IMethod> fieldMethods = searchUtils.getMethods(methodsMatches, pMethods);
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
						System.err.println("visitCompilationUnit -> " + methodDeclaration.getName() + "-" + e);
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
						IMethod method = (IMethod) methodInvocation.resolveMethodBinding().getJavaElement();
						Expression expr = methodInvocation.getExpression();
						if (SearchUtils.visitMethod(method, expr, analyzedType, fullyQualifiedName))
							found.set(true);
					} catch (Exception e) {
						System.err.println("MethodInvocation -> " + methodInvocation);
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
						if (SearchUtils.visitMethod(method, expr, analyzedType, fullyQualifiedName))
							found.set(true);
					} catch (Exception e) {
						System.err.println("SuperMethodInvocation -> " + methodInvocation);
					}
				}
				return super.visit(methodInvocation);
			}
		});
		if (found.get())
			return true;
		List<Statement> statements = methodBody.statements();
		for (Statement statement : statements) {
			if (found.get())
				break;
			if (csu.visitStatement(statement))
				found.set(true);
		}
		return found.get();
	}
}
