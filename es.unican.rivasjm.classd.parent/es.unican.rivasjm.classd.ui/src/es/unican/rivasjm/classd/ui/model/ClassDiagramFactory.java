package es.unican.rivasjm.classd.ui.model;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import es.unican.rivasjm.classd.ui.utils.JDTUtils;

public class ClassDiagramFactory {
	
	/** Parsed type declarations (enum's and classes) */
	final Set<AbstractTypeDeclaration> knownTypes;  
	
	/** Fully qualified name -> generated class */
	private Map<String, MClass> classes;
	
	public ClassDiagramFactory(IJavaElement... elements) {		
		Set<ICompilationUnit> cunits = new HashSet<ICompilationUnit>();
		
		// find all compilation units
		if (elements != null) {
			for (IJavaElement element: elements) {
				cunits.addAll(JDTUtils.getCompilationUnits(element));
			}
		}
		
		this.knownTypes = JDTUtils.getDeclaredTypesAndEnums(cunits);
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
			MClass mClass = getMClassWithoutAttributes(type); 
		
			if (mClass != null) {
				classes.put(mClass.getQualifiedName(), mClass);
				diagram.addClass(mClass);
			}
		}
		
		/*
		 * Now that I know what classes will appear in the diagram, add the attributes.
		 * I define an attribute as a field with an "unknown" type.
		 * An "unknown" type is a type that will not have a node in the class diagram.
		 */
		for (AbstractTypeDeclaration type : knownTypes) {
			for (FieldDeclaration field : JDTUtils.getFieldDeclarations(type)) {
				if (!fieldIsReference(field)) {
					final String qn = JDTUtils.getFullyQualifiedName(type);
					final MClass clazz = classes.get(qn);
					final MAttribute attr = getMAttribute(field);
					clazz.addAttribute(attr);
				}
			}
		}
		
		// add association relationships
		for (AbstractTypeDeclaration type : knownTypes) {
			List<MAssociationRelationship> associations = getAssociationRelationships(type);
			
			for (MAssociationRelationship r : associations) {
				diagram.addRelationship(r);
			}			
		}
		
		// merge bidirectional associations
		mergeBidirectionalAssociations(diagram);
		
		// get inheritance relationships (super-classes and super-interfaces)
		for (AbstractTypeDeclaration type : knownTypes) {
			List<MInheritanceRelationship> inheritances = getInheritanceRelationships(type);

			for (MInheritanceRelationship r : inheritances) {
				diagram.addRelationship(r);
			}
		}
	}

	private static void mergeBidirectionalAssociations(MClassDiagram diagram) {
		final List<MAssociationRelationship> associations = diagram.getRelationships().stream()
			.filter(r -> r instanceof MAssociationRelationship)
			.map(r -> (MAssociationRelationship)r)
			.collect(toList());
		
		final Set<MAssociationRelationship> removed = new HashSet<>();
		
		for (MAssociationRelationship assoc : associations) {
			if (removed.contains(assoc)) {
				continue;
			}
			
			final MClass source = assoc.getSource();
			final MClass target = assoc.getTarget();
			
			// find all associations that are opposite
			final List<MAssociationRelationship> opposites = associations.stream()
				.filter(a -> a.getSource().equals(target) && a.getTarget().equals(source) && !removed.contains(a))
				.collect(toList());
			
			// only merge if there is only one opposite
			if (opposites.size() == 1) {
				MBidirectionalAssociationRelationship bidi = new MBidirectionalAssociationRelationship();
				bidi.setDirect(assoc);
				bidi.setOpposite(opposites.get(0));
				diagram.removeRelationship(assoc);
				diagram.removeRelationship(opposites.get(0));
				removed.add(assoc);
				removed.add(opposites.get(0));
				diagram.addRelationship(bidi);
			}
		}
	}

	private MClass getMClassWithoutAttributes(AbstractTypeDeclaration type) {
		MClass clazz = new MClass();
		
		clazz.setName(type.getName().getIdentifier());		
		clazz.setQualifiedName(type.resolveBinding().getQualifiedName());
		clazz.setAbstract(Modifier.isAbstract(type.getModifiers()));		
		clazz.setStatic(Modifier.isStatic(type.getModifiers()));
		if (type instanceof TypeDeclaration) {
			clazz.setInterface(((TypeDeclaration) type).isInterface());
		}
		
		// add methods
		for (MethodDeclaration method : JDTUtils.getMethodDeclarations(type)) {
			MOperation operation = getMOperation(method);
			clazz.addOperation(operation);
		}
		
//		// add attributes
//		// ignore attributes of "known" types (those should be MAssociationRelationship's, i.e., arrows)
//		for (FieldDeclaration field : JDTUtils.getFieldDeclarations(type)) {
//			if (!fieldIsReference(field)) {
//				MAttribute attr = getMAttribute(field);
//				clazz.addAttribute(attr);				
//			}
//		}
		
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
		String typeString = JDTUtils.getTypeString(method.getReturnType2());
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
				
				param.setName(JDTUtils.getName(svd.getName()));
				param.setType(JDTUtils.getTypeString(svd.getType()));
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
		
		attr.setName(JDTUtils.getFieldName(field));
		String typeString = JDTUtils.getTypeString(field.getType());
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
		final MClass source = classes.get(JDTUtils.getFullyQualifiedName(type));
		
		for (FieldDeclaration field : JDTUtils.getFieldDeclarations(type)) {
			String fieldName = JDTUtils.getFieldName(field);
			
			if (fieldIsReference(field)) {
				Type resolvedType = JDTUtils.resolveTypeForUML(field.getType());
				MClass target = classes.get(JDTUtils.getFullyQualifiedName(resolvedType));
				
				MAssociationRelationship r = new MAssociationRelationship();
				r.setSource(source);
				r.setTarget(target);
				r.setName(fieldName);
				r.setMultiplicity(JDTUtils.isMultiple(field.getType()) ? "*" : "");
				r.setVisibility(getVibility(field.getModifiers()));
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
		final MClass subclass = classes.get(JDTUtils.getFullyQualifiedName(type));
		
		/*
		 *  Find superclass. There can only be one at most.
		 *  Enum's cannot have a superclass
		 */
		if (type instanceof TypeDeclaration && ((TypeDeclaration) type).getSuperclassType() != null) {
			String superQN = JDTUtils.getFullyQualifiedName(((TypeDeclaration) type).getSuperclassType());
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
		for (Type interfaceTypeObj : JDTUtils.getSuperInterfaces(type)) {
			String superQN = JDTUtils.getFullyQualifiedName(interfaceTypeObj);
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
		final Type resolvedType = JDTUtils.resolveTypeForUML(field.getType());
		String fullyQualifiedName = JDTUtils.getFullyQualifiedName(resolvedType);
		return classes.containsKey(fullyQualifiedName);
	}
	
}
