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
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import es.unican.rivasjm.classd.ui.utils.JdtDomUtils;

public class ClassDiagramFactory {
	
	final IJavaElement[] elements;
	final Set<AbstractTypeDeclaration> knownTypes;  // classes and enums
	
	private Map<String, MClass> classes;
	
	public ClassDiagramFactory(IJavaElement... elements) {
		this.elements = elements;
		
		Set<ICompilationUnit> cunits = new HashSet<ICompilationUnit>();
		for (IJavaElement element: elements) {
			cunits.addAll(JdtDomUtils.getCompilationUnits(element));
		}
		
		this.knownTypes = JdtDomUtils.getDeclaredTypesAndEnums(cunits);
		classes = new HashMap<>();
	}
	
	public MClassDiagram get() {
		MClassDiagram diagram = new MClassDiagram();
		init(diagram);
		return diagram;
	}
	
	private void init(MClassDiagram diagram) {
		// get classes (i.e. nodes)
		for (AbstractTypeDeclaration type : knownTypes) {
			MClass mClass = null; 
			
			if (type instanceof TypeDeclaration) {
				mClass = getMClass((TypeDeclaration) type);
				diagram.addClass(mClass);
			
			} else if (type instanceof EnumDeclaration) {
				mClass = getMEnum((EnumDeclaration) type);
				diagram.addClass(mClass);
			}
			
			if (mClass != null) {
				classes.put(mClass.getQualifiedName(), mClass);
			}
		}
		
		// get class contention relationships
		for (AbstractTypeDeclaration type : knownTypes) {
			List<MAssociationRelationship> associations = getAssociationRelationships(type);
			for (MAssociationRelationship relationship : associations) {
				diagram.addRelationship(relationship);
			}			
		}
		
		// get class inheritance relationships
		for (AbstractTypeDeclaration type : knownTypes) {
			if (type instanceof TypeDeclaration) {
				List<MInheritanceRelationship> inheritances = getInheritanceRelationships((TypeDeclaration) type);
				for (MInheritanceRelationship relationship : inheritances) {
					diagram.addRelationship(relationship);
				}
			}
		}
	}

	private MClass getMClass(TypeDeclaration type) {
		MClass clazz = new MClass();
		
		clazz.setName(type.getName().getIdentifier());		
		clazz.setQualifiedName(type.resolveBinding().getQualifiedName());
		clazz.setInterface(type.isInterface());
		clazz.setAbstract(Modifier.isAbstract(type.getModifiers()));
		
		
		// add methods
		for (MethodDeclaration method : type.getMethods()) {
			MOperation operation = getMOperation(method);
			clazz.addOperation(operation);
		}
		
		// add attributes
		// ignore attributes of "known" type (those should be MContentionRelationship's, i.e., arrows)
		for (FieldDeclaration field : type.getFields()) {
			if (!fieldIsReference(field)) {
				MAttribute attr = getMAttribute(field);
				clazz.addAttribute(attr);				
			}
		}
		
		return clazz;
	}
	

	@SuppressWarnings("unchecked")
	private MClass getMEnum(EnumDeclaration type) {
		MEnum clazz = new MEnum();
		
		clazz.setName(type.getName().getIdentifier());		
		clazz.setQualifiedName(type.resolveBinding().getQualifiedName());
		
		// add values
		type.enumConstants().stream()
			.filter(o -> o instanceof EnumConstantDeclaration)
			.map(e -> JdtDomUtils.getName(((EnumConstantDeclaration) e).getName()))
			.forEach(n -> clazz.addValue((String) n));
				
		// add methods
		for (MethodDeclaration method : JdtDomUtils.getMethodDeclarations(type)) {
			MOperation operation = getMOperation(method);
			clazz.addOperation(operation);
		}
		
		// add attributes
		// ignore attributes of "known" type (those should be MContentionRelationship's, i.e., arrows)
		for (FieldDeclaration field : JdtDomUtils.getFieldDeclarations(type)) {
			if (!fieldIsReference(field)) {
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
		operation.setVisibility(getVibility(method.getModifiers()));
		operation.setConstructor(method.isConstructor());
		
		// parameters
		for (Object object : method.parameters()) {
			if (object instanceof SingleVariableDeclaration) {
				MOperationParameter param = new MOperationParameter();
				SingleVariableDeclaration svd = (SingleVariableDeclaration) object;
				
				param.setName(JdtDomUtils.getName(svd.getName()));
				param.setType(JdtDomUtils.getTypeString(svd.getType()));
				operation.addParameter(param);
			}
		}
		
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
		attr.setVisibility(getVibility(field.getModifiers()));
		
		return attr;
	}
	
	private List<MAssociationRelationship> getAssociationRelationships(AbstractTypeDeclaration type) {
		List<MAssociationRelationship> relationships = new ArrayList<>();
		MClass source = classes.get(type.resolveBinding().getQualifiedName());
		
		for (FieldDeclaration field : JdtDomUtils.getFieldDeclarations(type)) {
			String fieldName = JdtDomUtils.getFieldName(field);
			
			if (fieldIsReference(field)) {
				ITypeBinding fieldType = JdtDomUtils.resolveUMLBinding(field.getType());
				MClass target = classes.get(fieldType.getQualifiedName());
				
				MAssociationRelationship r = new MAssociationRelationship();
				r.setSource(source);
				r.setTarget(target);
				r.setName(fieldName);
				r.setMultiplicity(JdtDomUtils.isMultiple(field.getType()) ? "*" : "");
				relationships.add(r);
			}
		}

		return relationships;
	}

	private List<MInheritanceRelationship> getInheritanceRelationships(TypeDeclaration type) {
		final List<MInheritanceRelationship> relationships = new ArrayList<>();
		final MClass subclass = classes.get(type.resolveBinding().getQualifiedName());
		
		// find superclass (there can only be one at most)
		if (type.getSuperclassType() != null) {
			String superQN = JdtDomUtils.getQualifiedName(type.getSuperclassType());
			if (classes.containsKey(superQN)) {
				MClass superClass = classes.get(superQN);
				MInheritanceRelationship relationship = new MInheritanceRelationship();
				relationship.setParent(superClass);
				relationship.setSubclass(subclass);
				relationships.add(relationship);
			}
		}

		// find super-interfaces (there can be several)
		if (!type.superInterfaceTypes().isEmpty()) {
			for (Object interfaceTypeObj : type.superInterfaceTypes()) {
				if (interfaceTypeObj instanceof Type) {
					String superQN = JdtDomUtils.getQualifiedName((Type) interfaceTypeObj);
					if (classes.containsKey(superQN)) {
						MClass superInterface = classes.get(superQN);
						MInheritanceRelationship relationship = new MInheritanceRelationship();
						relationship.setParent(superInterface);
						relationship.setSubclass(subclass);
						relationships.add(relationship);
					}
				}
			}
		}
		
		return relationships;
	}
	
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
	
	private boolean fieldIsReference(FieldDeclaration field) {
		ITypeBinding type = JdtDomUtils.resolveUMLBinding(field.getType());
		return classes.containsKey(type.getQualifiedName());
	}
	
}
