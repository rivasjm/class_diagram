package es.unican.rivasjm.classd.ui.model;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import es.unican.rivasjm.classd.ui.utils.JavaUtils;

public class ClassDiagramFactory {
	
	/** Parsed type declarations (enums and classes) */
	final Set<AbstractTypeDeclaration> knownTypes;  
	
	/** Fully qualified name -> generate classes map */
	private Map<String, MClass> classes;
	
	public ClassDiagramFactory(IJavaElement... elements) {		
		Set<ICompilationUnit> cunits = new HashSet<ICompilationUnit>();
		
		// find all compilation units
		if (elements != null) {
			for (IJavaElement element: elements) {
				cunits.addAll(JavaUtils.getCompilationUnits(element));
			}
		}
		
		this.knownTypes = JavaUtils.getDeclaredTypesAndEnums(cunits);
		this.classes = new HashMap<>();
	}
	
	public MClassDiagram get() {
		MClassDiagram diagram = new MClassDiagram();
		init(diagram);
		return diagram;
	}
	
	private void init(MClassDiagram diagram) {
		
		// create classes (i.e. nodes)
		for (AbstractTypeDeclaration type : knownTypes) {
			MClass mClass = getMClass(type); 
		
			if (mClass != null) {
				classes.put(mClass.getQualifiedName(), mClass);
				diagram.addClass(mClass);
			}
		}
		
		// get association relationships
		for (AbstractTypeDeclaration type : knownTypes) {
			List<MAssociationRelationship> associations = getAssociationRelationships(type);
			
			for (MAssociationRelationship r : associations) {
				diagram.addRelationship(r);
			}			
		}
		
		// get inheritance relationships (super-classes and super-interfaces)
		for (AbstractTypeDeclaration type : knownTypes) {
			List<MInheritanceRelationship> inheritances = getInheritanceRelationships(type);

			for (MInheritanceRelationship r : inheritances) {
				diagram.addRelationship(r);
			}
			
		}
	}

	private MClass getMClass(AbstractTypeDeclaration type) {
		MClass clazz = new MClass();
		
		clazz.setName(type.getName().getIdentifier());		
		clazz.setQualifiedName(type.resolveBinding().getQualifiedName());
		clazz.setAbstract(Modifier.isAbstract(type.getModifiers()));		
		clazz.setStatic(Modifier.isStatic(type.getModifiers()));
		if (type instanceof TypeDeclaration) {
			clazz.setInterface(((TypeDeclaration) type).isInterface());
		}
		
		// add methods
		for (MethodDeclaration method : JavaUtils.getMethodDeclarations(type)) {
			MOperation operation = getMOperation(method);
			clazz.addOperation(operation);
		}
		
		// add attributes
		// ignore attributes of "known" types (those should be MAssociationRelationship's, i.e., arrows)
		for (FieldDeclaration field : JavaUtils.getFieldDeclarations(type)) {
			if (!fieldIsReference(field)) {
				MAttribute attr = getMAttribute(field);
				clazz.addAttribute(attr);				
			}
		}
		
		return clazz;
	}
	

//	@SuppressWarnings("unchecked")
//	private MClass getMEnum(EnumDeclaration type) {
//		MEnum clazz = new MEnum();
//		
//		clazz.setName(type.getName().getIdentifier());		
//		clazz.setQualifiedName(type.resolveBinding().getQualifiedName());
//		
//		// add values
//		type.enumConstants().stream()
//			.filter(o -> o instanceof EnumConstantDeclaration)
//			.map(e -> JdtDomUtils.getName(((EnumConstantDeclaration) e).getName()))
//			.forEach(n -> clazz.addValue((String) n));
//				
//		// add methods
//		for (MethodDeclaration method : JdtDomUtils.getMethodDeclarations(type)) {
//			MOperation operation = getMOperation(method);
//			clazz.addOperation(operation);
//		}
//		
//		// add attributes
//		// ignore attributes of "known" type (those should be MContentionRelationship's, i.e., arrows)
//		for (FieldDeclaration field : JdtDomUtils.getFieldDeclarations(type)) {
//			if (!fieldIsReference(field)) {
//				MAttribute attr = getMAttribute(field);
//				clazz.addAttribute(attr);				
//			}
//		}
//		
//		return clazz;
//	}
	
	/**
	 * 
	 * @param method
	 * @return
	 */
	private static MOperation getMOperation(MethodDeclaration method) {
		MOperation operation = new MOperation();
		
		operation.setName(method.getName().getIdentifier());
		String typeString = JavaUtils.getTypeString(method.getReturnType2());
//		if (JavaUtils.isMultiple(method.getReturnType2())) {
//			typeString += "[]";
//		}
		operation.setType(typeString);
		operation.setVisibility(getVibility(method.getModifiers()));
		operation.setConstructor(method.isConstructor());
		operation.setStatic(Modifier.isStatic(method.getModifiers()));
		
		// parameters
		for (Object object : method.parameters()) {
			if (object instanceof SingleVariableDeclaration) {
				MOperationParameter param = new MOperationParameter();
				SingleVariableDeclaration svd = (SingleVariableDeclaration) object;
				
				param.setName(JavaUtils.getName(svd.getName()));
				param.setType(JavaUtils.getTypeString(svd.getType()));
				operation.addParameter(param);
			}
		}
		
		return operation;
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	private static MAttribute getMAttribute(FieldDeclaration field) {
		MAttribute attr = new MAttribute();
		
		attr.setName(JavaUtils.getFieldName(field));
		String typeString = JavaUtils.getTypeString(field.getType());
//		if (JdtDomUtils.isMultiple(field.getType())) {
//			typeString += "[]";
//		}
		attr.setType(typeString);
		attr.setVisibility(getVibility(field.getModifiers()));
		attr.setStatic(Modifier.isStatic(field.getModifiers()));
		
		return attr;
	}
	
	/**
	 * Get all the association relationships starting from the given type
	 * @param type
	 * @return
	 */
	private List<MAssociationRelationship> getAssociationRelationships(AbstractTypeDeclaration type) {
		final List<MAssociationRelationship> relationships = new ArrayList<>();
		final MClass source = classes.get(JavaUtils.getFullyQualifiedName(type));
		
		for (FieldDeclaration field : JavaUtils.getFieldDeclarations(type)) {
			String fieldName = JavaUtils.getFieldName(field);
			
			if (fieldIsReference(field)) {
				Type resolvedType = JavaUtils.resolveTypeForUML(field.getType());
				MClass target = classes.get(JavaUtils.getFullyQualifiedName(resolvedType));
				
				MAssociationRelationship r = new MAssociationRelationship();
				r.setSource(source);
				r.setTarget(target);
				r.setName(fieldName);
				r.setMultiplicity(JavaUtils.isMultiple(field.getType()) ? "*" : "");
				relationships.add(r);
			}
		}

		return relationships;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	private List<MInheritanceRelationship> getInheritanceRelationships(AbstractTypeDeclaration type) {
		final List<MInheritanceRelationship> relationships = new ArrayList<>();
		final MClass subclass = classes.get(JavaUtils.getFullyQualifiedName(type));
		
		/*
		 *  Find superclass. There can only be one at most.
		 *  Enum's cannot have a superclass
		 */
		if (type instanceof TypeDeclaration && ((TypeDeclaration) type).getSuperclassType() != null) {
			String superQN = JavaUtils.getFullyQualifiedName(((TypeDeclaration) type).getSuperclassType());
			if (classes.containsKey(superQN)) {
				MClass superClass = classes.get(superQN);
				MInheritanceRelationship relationship = new MInheritanceRelationship();
				relationship.setParent(superClass);
				relationship.setSubclass(subclass);
				relationships.add(relationship);
			}
		}

		/*
		 *  Find super-interfaces. There can be several.
		 *  Enum's can have super-interfaces
		 */
		for (Type interfaceTypeObj : JavaUtils.getSuperInterfaces(type)) {
			String superQN = JavaUtils.getFullyQualifiedName(interfaceTypeObj);
			if (classes.containsKey(superQN)) {
				MClass superInterface = classes.get(superQN);
				MInheritanceRelationship relationship = new MInheritanceRelationship();
				relationship.setParent(superInterface);
				relationship.setSubclass(subclass);
				relationships.add(relationship);
			}
		}
		
		return relationships;
	}
	
	/**
	 * 
	 * @param modifiers
	 * @return
	 */
	private static EVisibility getVibility(int modifiers) {
		if (Modifier.isPublic(modifiers)) {
			return EVisibility.PUBLIC;
		
		} else if (Modifier.isProtected(modifiers)) {
			return EVisibility.PROTECTED;
		
		} else if (Modifier.isPrivate(modifiers)) {
			return EVisibility.PRIVATE;
		}
		
		return EVisibility.PACKAGE;
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	private boolean fieldIsReference(FieldDeclaration field) {
		final Type resolvedType = JavaUtils.resolveTypeForUML(field.getType());
		String fullyQualifiedName = JavaUtils.getFullyQualifiedName(resolvedType);
		return classes.containsKey(fullyQualifiedName);
	}
	
}
