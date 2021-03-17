package utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;

import visitordetector.metamodel.entity.MClass;

public class CastSearchingUtils {
	private IType analyzedType;
	private String fullyQualifiedName;
	private List<String> descendants;

	public CastSearchingUtils(MClass arg0) {
		this.analyzedType = arg0.getUnderlyingObject();
		this.fullyQualifiedName = analyzedType.getFullyQualifiedName().replace('.', '/');
		this.descendants = arg0.descendantsGroup().getElements().stream().map(descendent -> descendent.toString())
				.collect(Collectors.toList());
	}

	public boolean containsDownCast(IMethod method) {
		ASTParser parser = ASTParser.newParser(AST.JLS10);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(method.getCompilationUnit());
		parser.setResolveBindings(true);
		CompilationUnit cUnit = (CompilationUnit) parser.createAST(null);
		return visitCompilationUnit(cUnit, method);
	}

	private boolean visitCompilationUnit(CompilationUnit cUnit, IMethod methodSearchingFor) {
		AtomicBoolean found = new AtomicBoolean(false);
		AtomicBoolean visited = new AtomicBoolean(false);
		cUnit.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration methodDeclaration) {
				if (!visited.get()) {
					try {
						IJavaElement element = methodDeclaration.resolveBinding().getJavaElement();
						if (element != null
								&& StringParser.replaceKey(element.toString()).equals(methodSearchingFor.toString())) {
							boolean result = visitMethodDeclaration(methodDeclaration);
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

	private boolean visitMethodDeclaration(MethodDeclaration methodDeclaration) {
		return visitMethodBody(methodDeclaration.getBody());
	}

	private boolean visitMethodBody(Block methodBody) {
		if (methodBody == null || methodBody.statements().size() == 0) {
			return false;
		}
		return methodBody.statements().stream().filter(statement -> visitStatement((Statement) statement)).count() > 0;
	}

	public boolean visitStatement(Statement statement) {
		AtomicBoolean found = new AtomicBoolean(false);
		statement.accept(new ASTVisitor() {
			public boolean visit(CastExpression castExpression) {
				if (!found.get() && isDescendentOfAnalyzedType(castExpression.getType())) {
					Expression expr = castExpression.getExpression();
					try {
						if (SearchUtils.containsType(expr.resolveTypeBinding().getJavaElement().toString(),
								fullyQualifiedName))
							found.set(true);
					} catch (NullPointerException e) {
						System.err.println("Cast Expression - visitStatement ->" + castExpression.getExpression());
					}
				}
				return super.visit(castExpression);
			}
		});
		return found.get();
	}

	private boolean isDescendentOfAnalyzedType(Type type) {
		try {
			IType iType = (IType) type.resolveBinding().getJavaElement();
			return descendants.contains(iType.getFullyQualifiedName());
		} catch (NullPointerException | ClassCastException e) {
			System.err.println("isDescendentOfAnalyzedType " + type);
		}
		return false;
	}
}
