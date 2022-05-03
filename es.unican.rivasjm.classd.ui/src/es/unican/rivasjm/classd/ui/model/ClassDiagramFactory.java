package es.unican.rivasjm.classd.ui.model;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import es.unican.rivasjm.classd.ui.utils.JdtDomUtils;

public class ClassDiagramFactory {
	
	final IJavaElement[] elements;
	final Set<TypeDeclaration> knownTypes;
	
	private Map<String, MClass> classes;
	
	public ClassDiagramFactory(IJavaElement... elements) {
		this.elements = elements;
		
		List<ICompilationUnit> cunits = new ArrayList<ICompilationUnit>();
		for (IJavaElement element: elements) {
			cunits.addAll(JdtDomUtils.getCompilationUnits(element));
		}
		
		this.knownTypes = JdtDomUtils.getDeclaredTypes(cunits);
		
		classes = new HashMap<>();
	}
	
	public MClassDiagram get() {
		MClassDiagram diagram = new MClassDiagram();
		init(diagram);
		return diagram;
	}
	
	private void init(MClassDiagram diagram) {
		// get classes (i.e. nodes)
		for (TypeDeclaration type : knownTypes) {
			MClass mClass = getMClass(type);
			diagram.addClass(mClass);
		}
		
		// get class contention relationships
		for (TypeDeclaration type : knownTypes) {
			List<MContentionRelationship> contentions = getContentionRelationships(type);
			for (MContentionRelationship relationship : contentions) {
				diagram.addRelationship(relationship);
			}			
		}
	}

	private MClass getMClass(TypeDeclaration type) {
		MClass clazz = new MClass();
		
		clazz.setName(type.getName().getIdentifier());		
		clazz.setQualifiedName(type.resolveBinding().getQualifiedName());
		clazz.setInterface(type.isInterface());
		clazz.setAbstract(Modifier.isAbstract(type.getModifiers()));
		
		classes.put(clazz.getQualifiedName(), clazz);
		
		// add methods
		for (MethodDeclaration method : type.getMethods()) {
			MOperation operation = getMOperation(method);
			clazz.addOperation(operation);
		}
		
		// add attributes
		// ignore attributes of "known" type (those should be MContentionRelationship's, i.e., arrows)
		for (FieldDeclaration field : type.getFields()) {
			if (!JdtDomUtils.fieldIsReference(field, knownTypes)) {
				MAttribute attr = getMAttribute(field);
				clazz.addAttribute(attr);				
			}
		}
		
		return clazz;
	}
	
	private static MOperation getMOperation(MethodDeclaration method) {
		MOperation operation = new MOperation();
		
		operation.setName(method.getName().getIdentifier());
		String typeString = JdtDomUtils.getTypeString(method.getReturnType2());
		if (JdtDomUtils.isMultiple(method.getReturnType2())) {
			typeString += "[]";
		}
		operation.setType(typeString);
		
		return operation;
	}
	
	private static MAttribute getMAttribute(FieldDeclaration field) {
		MAttribute attr = new MAttribute();
		
		attr.setName(JdtDomUtils.getFieldName(field));
		String typeString = JdtDomUtils.getTypeString(field.getType());
		if (JdtDomUtils.isMultiple(field.getType())) {
			typeString += "[]";
		}
		attr.setType(typeString);
		
		return attr;
	}
	
	private List<MContentionRelationship> getContentionRelationships(TypeDeclaration type) {
		List<MContentionRelationship> relationships = new ArrayList<>();
		MClass source = classes.get(type.resolveBinding().getQualifiedName());
		
		for (FieldDeclaration field : type.getFields()) {
			String fieldName = JdtDomUtils.getFieldName(field);
			
			if (JdtDomUtils.fieldIsReference(field, knownTypes)) {
				ITypeBinding fieldType = JdtDomUtils.resolveUMLBinding(field.getType());
				MClass target = classes.get(fieldType.getQualifiedName());
				
				MContentionRelationship r = new MContentionRelationship();
				r.setSource(source);
				r.setTarget(target);
				r.setName(fieldName);
				r.setMultiplicity(JdtDomUtils.isMultiple(field.getType()) ? "*" : "");
				relationships.add(r);
			}
		}

		return relationships;
	}

}
