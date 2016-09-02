/**
 */
package emodel.impl;

import emodel.EConnectionModel;
import emodel.EGolpeComponentModel;
import emodel.EGolpeHostModel;
import emodel.EGolpeModel;
import emodel.EGolpeSwitchModel;
import emodel.EPlaceModel;
import emodel.EmodelFactory;
import emodel.EmodelPackage;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EmodelPackageImpl extends EPackageImpl implements EmodelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eGolpeModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eGolpeComponentModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eGolpeHostModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eGolpeSwitchModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ePlaceModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eConnectionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType rectangleEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see emodel.EmodelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EmodelPackageImpl() {
		super(eNS_URI, EmodelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link EmodelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EmodelPackage init() {
		if (isInited) return (EmodelPackage)EPackage.Registry.INSTANCE.getEPackage(EmodelPackage.eNS_URI);

		// Obtain or create and register package
		EmodelPackageImpl theEmodelPackage = (EmodelPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EmodelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EmodelPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theEmodelPackage.createPackageContents();

		// Initialize created meta-data
		theEmodelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEmodelPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EmodelPackage.eNS_URI, theEmodelPackage);
		return theEmodelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEGolpeModel() {
		return eGolpeModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEGolpeModel_Components() {
		return (EReference)eGolpeModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEGolpeModel_Connections() {
		return (EReference)eGolpeModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEGolpeComponentModel() {
		return eGolpeComponentModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEGolpeComponentModel_Name() {
		return (EAttribute)eGolpeComponentModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEGolpeComponentModel_IconAddress() {
		return (EAttribute)eGolpeComponentModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEGolpeComponentModel_Place() {
		return (EReference)eGolpeComponentModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEGolpeComponentModel_RootModel() {
		return (EReference)eGolpeComponentModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEGolpeComponentModel_Connection() {
		return (EReference)eGolpeComponentModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEGolpeComponentModel_Constraint() {
		return (EAttribute)eGolpeComponentModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEGolpeHostModel() {
		return eGolpeHostModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEGolpeSwitchModel() {
		return eGolpeSwitchModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEPlaceModel() {
		return ePlaceModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEConnectionModel() {
		return eConnectionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEConnectionModel_Sources() {
		return (EReference)eConnectionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEConnectionModel_RootModel() {
		return (EReference)eConnectionModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getRectangle() {
		return rectangleEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmodelFactory getEmodelFactory() {
		return (EmodelFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		eGolpeModelEClass = createEClass(EGOLPE_MODEL);
		createEReference(eGolpeModelEClass, EGOLPE_MODEL__COMPONENTS);
		createEReference(eGolpeModelEClass, EGOLPE_MODEL__CONNECTIONS);

		eGolpeComponentModelEClass = createEClass(EGOLPE_COMPONENT_MODEL);
		createEAttribute(eGolpeComponentModelEClass, EGOLPE_COMPONENT_MODEL__NAME);
		createEAttribute(eGolpeComponentModelEClass, EGOLPE_COMPONENT_MODEL__ICON_ADDRESS);
		createEReference(eGolpeComponentModelEClass, EGOLPE_COMPONENT_MODEL__PLACE);
		createEReference(eGolpeComponentModelEClass, EGOLPE_COMPONENT_MODEL__ROOT_MODEL);
		createEReference(eGolpeComponentModelEClass, EGOLPE_COMPONENT_MODEL__CONNECTION);
		createEAttribute(eGolpeComponentModelEClass, EGOLPE_COMPONENT_MODEL__CONSTRAINT);

		eGolpeHostModelEClass = createEClass(EGOLPE_HOST_MODEL);

		eGolpeSwitchModelEClass = createEClass(EGOLPE_SWITCH_MODEL);

		ePlaceModelEClass = createEClass(EPLACE_MODEL);

		eConnectionModelEClass = createEClass(ECONNECTION_MODEL);
		createEReference(eConnectionModelEClass, ECONNECTION_MODEL__SOURCES);
		createEReference(eConnectionModelEClass, ECONNECTION_MODEL__ROOT_MODEL);

		// Create data types
		rectangleEDataType = createEDataType(RECTANGLE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		eGolpeHostModelEClass.getESuperTypes().add(this.getEGolpeComponentModel());
		eGolpeSwitchModelEClass.getESuperTypes().add(this.getEGolpeComponentModel());

		// Initialize classes, features, and operations; add parameters
		initEClass(eGolpeModelEClass, EGolpeModel.class, "EGolpeModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEGolpeModel_Components(), this.getEGolpeComponentModel(), this.getEGolpeComponentModel_RootModel(), "components", null, 0, -1, EGolpeModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEGolpeModel_Connections(), this.getEConnectionModel(), this.getEConnectionModel_RootModel(), "connections", null, 0, -1, EGolpeModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eGolpeComponentModelEClass, EGolpeComponentModel.class, "EGolpeComponentModel", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEGolpeComponentModel_Name(), ecorePackage.getEString(), "name", null, 0, 1, EGolpeComponentModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEGolpeComponentModel_IconAddress(), ecorePackage.getEString(), "iconAddress", null, 0, 1, EGolpeComponentModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEGolpeComponentModel_Place(), this.getEPlaceModel(), null, "place", null, 0, 1, EGolpeComponentModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEGolpeComponentModel_RootModel(), this.getEGolpeModel(), this.getEGolpeModel_Components(), "rootModel", null, 0, 1, EGolpeComponentModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEGolpeComponentModel_Connection(), this.getEConnectionModel(), null, "connection", null, 0, 2, EGolpeComponentModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEGolpeComponentModel_Constraint(), this.getRectangle(), "constraint", null, 0, 1, EGolpeComponentModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eGolpeHostModelEClass, EGolpeHostModel.class, "EGolpeHostModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(eGolpeSwitchModelEClass, EGolpeSwitchModel.class, "EGolpeSwitchModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ePlaceModelEClass, EPlaceModel.class, "EPlaceModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(eConnectionModelEClass, EConnectionModel.class, "EConnectionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEConnectionModel_Sources(), this.getEGolpeComponentModel(), null, "sources", null, 0, 2, EConnectionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEConnectionModel_RootModel(), this.getEGolpeModel(), this.getEGolpeModel_Connections(), "rootModel", null, 0, 1, EConnectionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(rectangleEDataType, Rectangle.class, "Rectangle", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //EmodelPackageImpl
