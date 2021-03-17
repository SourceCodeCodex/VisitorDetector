package classes;

import java.util.ArrayList;
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
import utils.SearchUtils;
import utils.StringParser;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;
import visitordetector.metamodel.factory.Factory;

@RelationBuilder
public class DistinctMethodsWithCastsToMe implements IRelationBuilder<MMethod, MClass> {
	private List<PseudoMethod> pMethods;

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		pMethods = new LinkedList<>();
		Group<MMethod> foundMethods = new Group<>();
		try {
			SearchUtils searchUtils = new SearchUtils();
			List<SearchMatch> matches = this.getMatches(arg0);
			List<IMethod> methods = searchUtils.getMethods(matches, pMethods);
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
				try {
					PseudoMethod method = StringParser.parse(arg0.getElement().toString());
					if (!pMethods.contains(method)) {
						pMethods.add(method);
					}
					String path = arg0.getResource().getProjectRelativePath().toOSString();
					if (!uniqueResources.contains(path)) {
						uniqueResources.add(path);
						matches.add(arg0);
					}
				} catch (Exception e) {
					System.err.println("DistinctMethodsWithCastsToMe->" + e);
				}
			}
		};
		SearchEngine searchEngine = new SearchEngine();
		searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
				requestor, new NullProgressMonitor());
		return matches;
	}
}
