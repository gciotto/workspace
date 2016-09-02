/**
 */
package emodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see emodel.EmodelFactory
 * @model kind="package"
 * @generated
 */
public interface EmodelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "emodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "www.cnpem.br";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "br.cnpem.golpe";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EmodelPackage eINSTANCE = emodel.impl.EmodelPackageImpl.init();

	/**
	 * The meta object id for the '{@link emodel.impl.EGolpeModelImpl <em>EGolpe Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see emodel.impl.EGolpeModelImpl
	 * @see emodel.impl.EmodelPackageImpl#getEGolpeModel()
	 * @generated
	 */
	int EGOLPE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Components</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_MODEL__COMPONENTS = 0;

	/**
	 * The feature id for the '<em><b>Connections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_MODEL__CONNECTIONS = 1;

	/**
	 * The number of structural features of the '<em>EGolpe Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_MODEL_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EGolpe Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link emodel.impl.EGolpeComponentModelImpl <em>EGolpe Component Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see emodel.impl.EGolpeComponentModelImpl
	 * @see emodel.impl.EmodelPackageImpl#getEGolpeComponentModel()
	 * @generated
	 */
	int EGOLPE_COMPONENT_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_COMPONENT_MODEL__NAME = 0;

	/**
	 * The feature id for the '<em><b>Icon Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_COMPONENT_MODEL__ICON_ADDRESS = 1;

	/**
	 * The feature id for the '<em><b>Place</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_COMPONENT_MODEL__PLACE = 2;

	/**
	 * The feature id for the '<em><b>Root Model</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_COMPONENT_MODEL__ROOT_MODEL = 3;

	/**
	 * The feature id for the '<em><b>Connection</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_COMPONENT_MODEL__CONNECTION = 4;

	/**
	 * The feature id for the '<em><b>Constraint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_COMPONENT_MODEL__CONSTRAINT = 5;

	/**
	 * The number of structural features of the '<em>EGolpe Component Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_COMPONENT_MODEL_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>EGolpe Component Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_COMPONENT_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link emodel.impl.EGolpeHostModelImpl <em>EGolpe Host Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see emodel.impl.EGolpeHostModelImpl
	 * @see emodel.impl.EmodelPackageImpl#getEGolpeHostModel()
	 * @generated
	 */
	int EGOLPE_HOST_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_HOST_MODEL__NAME = EGOLPE_COMPONENT_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Icon Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_HOST_MODEL__ICON_ADDRESS = EGOLPE_COMPONENT_MODEL__ICON_ADDRESS;

	/**
	 * The feature id for the '<em><b>Place</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_HOST_MODEL__PLACE = EGOLPE_COMPONENT_MODEL__PLACE;

	/**
	 * The feature id for the '<em><b>Root Model</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_HOST_MODEL__ROOT_MODEL = EGOLPE_COMPONENT_MODEL__ROOT_MODEL;

	/**
	 * The feature id for the '<em><b>Connection</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_HOST_MODEL__CONNECTION = EGOLPE_COMPONENT_MODEL__CONNECTION;

	/**
	 * The feature id for the '<em><b>Constraint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_HOST_MODEL__CONSTRAINT = EGOLPE_COMPONENT_MODEL__CONSTRAINT;

	/**
	 * The number of structural features of the '<em>EGolpe Host Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_HOST_MODEL_FEATURE_COUNT = EGOLPE_COMPONENT_MODEL_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>EGolpe Host Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_HOST_MODEL_OPERATION_COUNT = EGOLPE_COMPONENT_MODEL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link emodel.impl.EGolpeSwitchModelImpl <em>EGolpe Switch Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see emodel.impl.EGolpeSwitchModelImpl
	 * @see emodel.impl.EmodelPackageImpl#getEGolpeSwitchModel()
	 * @generated
	 */
	int EGOLPE_SWITCH_MODEL = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_SWITCH_MODEL__NAME = EGOLPE_COMPONENT_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Icon Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_SWITCH_MODEL__ICON_ADDRESS = EGOLPE_COMPONENT_MODEL__ICON_ADDRESS;

	/**
	 * The feature id for the '<em><b>Place</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_SWITCH_MODEL__PLACE = EGOLPE_COMPONENT_MODEL__PLACE;

	/**
	 * The feature id for the '<em><b>Root Model</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_SWITCH_MODEL__ROOT_MODEL = EGOLPE_COMPONENT_MODEL__ROOT_MODEL;

	/**
	 * The feature id for the '<em><b>Connection</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_SWITCH_MODEL__CONNECTION = EGOLPE_COMPONENT_MODEL__CONNECTION;

	/**
	 * The feature id for the '<em><b>Constraint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_SWITCH_MODEL__CONSTRAINT = EGOLPE_COMPONENT_MODEL__CONSTRAINT;

	/**
	 * The number of structural features of the '<em>EGolpe Switch Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_SWITCH_MODEL_FEATURE_COUNT = EGOLPE_COMPONENT_MODEL_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>EGolpe Switch Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EGOLPE_SWITCH_MODEL_OPERATION_COUNT = EGOLPE_COMPONENT_MODEL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link emodel.impl.EPlaceModelImpl <em>EPlace Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see emodel.impl.EPlaceModelImpl
	 * @see emodel.impl.EmodelPackageImpl#getEPlaceModel()
	 * @generated
	 */
	int EPLACE_MODEL = 4;

	/**
	 * The number of structural features of the '<em>EPlace Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLACE_MODEL_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>EPlace Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLACE_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link emodel.impl.EConnectionModelImpl <em>EConnection Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see emodel.impl.EConnectionModelImpl
	 * @see emodel.impl.EmodelPackageImpl#getEConnectionModel()
	 * @generated
	 */
	int ECONNECTION_MODEL = 5;

	/**
	 * The feature id for the '<em><b>Sources</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECONNECTION_MODEL__SOURCES = 0;

	/**
	 * The feature id for the '<em><b>Root Model</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECONNECTION_MODEL__ROOT_MODEL = 1;

	/**
	 * The number of structural features of the '<em>EConnection Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECONNECTION_MODEL_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EConnection Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECONNECTION_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Rectangle</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.draw2d.geometry.Rectangle
	 * @see emodel.impl.EmodelPackageImpl#getRectangle()
	 * @generated
	 */
	int RECTANGLE = 6;


	/**
	 * Returns the meta object for class '{@link emodel.EGolpeModel <em>EGolpe Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EGolpe Model</em>'.
	 * @see emodel.EGolpeModel
	 * @generated
	 */
	EClass getEGolpeModel();

	/**
	 * Returns the meta object for the containment reference list '{@link emodel.EGolpeModel#getComponents <em>Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Components</em>'.
	 * @see emodel.EGolpeModel#getComponents()
	 * @see #getEGolpeModel()
	 * @generated
	 */
	EReference getEGolpeModel_Components();

	/**
	 * Returns the meta object for the containment reference list '{@link emodel.EGolpeModel#getConnections <em>Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Connections</em>'.
	 * @see emodel.EGolpeModel#getConnections()
	 * @see #getEGolpeModel()
	 * @generated
	 */
	EReference getEGolpeModel_Connections();

	/**
	 * Returns the meta object for class '{@link emodel.EGolpeComponentModel <em>EGolpe Component Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EGolpe Component Model</em>'.
	 * @see emodel.EGolpeComponentModel
	 * @generated
	 */
	EClass getEGolpeComponentModel();

	/**
	 * Returns the meta object for the attribute '{@link emodel.EGolpeComponentModel#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see emodel.EGolpeComponentModel#getName()
	 * @see #getEGolpeComponentModel()
	 * @generated
	 */
	EAttribute getEGolpeComponentModel_Name();

	/**
	 * Returns the meta object for the attribute '{@link emodel.EGolpeComponentModel#getIconAddress <em>Icon Address</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Icon Address</em>'.
	 * @see emodel.EGolpeComponentModel#getIconAddress()
	 * @see #getEGolpeComponentModel()
	 * @generated
	 */
	EAttribute getEGolpeComponentModel_IconAddress();

	/**
	 * Returns the meta object for the reference '{@link emodel.EGolpeComponentModel#getPlace <em>Place</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Place</em>'.
	 * @see emodel.EGolpeComponentModel#getPlace()
	 * @see #getEGolpeComponentModel()
	 * @generated
	 */
	EReference getEGolpeComponentModel_Place();

	/**
	 * Returns the meta object for the container reference '{@link emodel.EGolpeComponentModel#getRootModel <em>Root Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Root Model</em>'.
	 * @see emodel.EGolpeComponentModel#getRootModel()
	 * @see #getEGolpeComponentModel()
	 * @generated
	 */
	EReference getEGolpeComponentModel_RootModel();

	/**
	 * Returns the meta object for the reference list '{@link emodel.EGolpeComponentModel#getConnection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Connection</em>'.
	 * @see emodel.EGolpeComponentModel#getConnection()
	 * @see #getEGolpeComponentModel()
	 * @generated
	 */
	EReference getEGolpeComponentModel_Connection();

	/**
	 * Returns the meta object for the attribute '{@link emodel.EGolpeComponentModel#getConstraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Constraint</em>'.
	 * @see emodel.EGolpeComponentModel#getConstraint()
	 * @see #getEGolpeComponentModel()
	 * @generated
	 */
	EAttribute getEGolpeComponentModel_Constraint();

	/**
	 * Returns the meta object for class '{@link emodel.EGolpeHostModel <em>EGolpe Host Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EGolpe Host Model</em>'.
	 * @see emodel.EGolpeHostModel
	 * @generated
	 */
	EClass getEGolpeHostModel();

	/**
	 * Returns the meta object for class '{@link emodel.EGolpeSwitchModel <em>EGolpe Switch Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EGolpe Switch Model</em>'.
	 * @see emodel.EGolpeSwitchModel
	 * @generated
	 */
	EClass getEGolpeSwitchModel();

	/**
	 * Returns the meta object for class '{@link emodel.EPlaceModel <em>EPlace Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EPlace Model</em>'.
	 * @see emodel.EPlaceModel
	 * @generated
	 */
	EClass getEPlaceModel();

	/**
	 * Returns the meta object for class '{@link emodel.EConnectionModel <em>EConnection Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EConnection Model</em>'.
	 * @see emodel.EConnectionModel
	 * @generated
	 */
	EClass getEConnectionModel();

	/**
	 * Returns the meta object for the reference list '{@link emodel.EConnectionModel#getSources <em>Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Sources</em>'.
	 * @see emodel.EConnectionModel#getSources()
	 * @see #getEConnectionModel()
	 * @generated
	 */
	EReference getEConnectionModel_Sources();

	/**
	 * Returns the meta object for the container reference '{@link emodel.EConnectionModel#getRootModel <em>Root Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Root Model</em>'.
	 * @see emodel.EConnectionModel#getRootModel()
	 * @see #getEConnectionModel()
	 * @generated
	 */
	EReference getEConnectionModel_RootModel();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.draw2d.geometry.Rectangle <em>Rectangle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Rectangle</em>'.
	 * @see org.eclipse.draw2d.geometry.Rectangle
	 * @model instanceClass="org.eclipse.draw2d.geometry.Rectangle"
	 * @generated
	 */
	EDataType getRectangle();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EmodelFactory getEmodelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link emodel.impl.EGolpeModelImpl <em>EGolpe Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see emodel.impl.EGolpeModelImpl
		 * @see emodel.impl.EmodelPackageImpl#getEGolpeModel()
		 * @generated
		 */
		EClass EGOLPE_MODEL = eINSTANCE.getEGolpeModel();

		/**
		 * The meta object literal for the '<em><b>Components</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EGOLPE_MODEL__COMPONENTS = eINSTANCE.getEGolpeModel_Components();

		/**
		 * The meta object literal for the '<em><b>Connections</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EGOLPE_MODEL__CONNECTIONS = eINSTANCE.getEGolpeModel_Connections();

		/**
		 * The meta object literal for the '{@link emodel.impl.EGolpeComponentModelImpl <em>EGolpe Component Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see emodel.impl.EGolpeComponentModelImpl
		 * @see emodel.impl.EmodelPackageImpl#getEGolpeComponentModel()
		 * @generated
		 */
		EClass EGOLPE_COMPONENT_MODEL = eINSTANCE.getEGolpeComponentModel();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EGOLPE_COMPONENT_MODEL__NAME = eINSTANCE.getEGolpeComponentModel_Name();

		/**
		 * The meta object literal for the '<em><b>Icon Address</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EGOLPE_COMPONENT_MODEL__ICON_ADDRESS = eINSTANCE.getEGolpeComponentModel_IconAddress();

		/**
		 * The meta object literal for the '<em><b>Place</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EGOLPE_COMPONENT_MODEL__PLACE = eINSTANCE.getEGolpeComponentModel_Place();

		/**
		 * The meta object literal for the '<em><b>Root Model</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EGOLPE_COMPONENT_MODEL__ROOT_MODEL = eINSTANCE.getEGolpeComponentModel_RootModel();

		/**
		 * The meta object literal for the '<em><b>Connection</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EGOLPE_COMPONENT_MODEL__CONNECTION = eINSTANCE.getEGolpeComponentModel_Connection();

		/**
		 * The meta object literal for the '<em><b>Constraint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EGOLPE_COMPONENT_MODEL__CONSTRAINT = eINSTANCE.getEGolpeComponentModel_Constraint();

		/**
		 * The meta object literal for the '{@link emodel.impl.EGolpeHostModelImpl <em>EGolpe Host Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see emodel.impl.EGolpeHostModelImpl
		 * @see emodel.impl.EmodelPackageImpl#getEGolpeHostModel()
		 * @generated
		 */
		EClass EGOLPE_HOST_MODEL = eINSTANCE.getEGolpeHostModel();

		/**
		 * The meta object literal for the '{@link emodel.impl.EGolpeSwitchModelImpl <em>EGolpe Switch Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see emodel.impl.EGolpeSwitchModelImpl
		 * @see emodel.impl.EmodelPackageImpl#getEGolpeSwitchModel()
		 * @generated
		 */
		EClass EGOLPE_SWITCH_MODEL = eINSTANCE.getEGolpeSwitchModel();

		/**
		 * The meta object literal for the '{@link emodel.impl.EPlaceModelImpl <em>EPlace Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see emodel.impl.EPlaceModelImpl
		 * @see emodel.impl.EmodelPackageImpl#getEPlaceModel()
		 * @generated
		 */
		EClass EPLACE_MODEL = eINSTANCE.getEPlaceModel();

		/**
		 * The meta object literal for the '{@link emodel.impl.EConnectionModelImpl <em>EConnection Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see emodel.impl.EConnectionModelImpl
		 * @see emodel.impl.EmodelPackageImpl#getEConnectionModel()
		 * @generated
		 */
		EClass ECONNECTION_MODEL = eINSTANCE.getEConnectionModel();

		/**
		 * The meta object literal for the '<em><b>Sources</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ECONNECTION_MODEL__SOURCES = eINSTANCE.getEConnectionModel_Sources();

		/**
		 * The meta object literal for the '<em><b>Root Model</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ECONNECTION_MODEL__ROOT_MODEL = eINSTANCE.getEConnectionModel_RootModel();

		/**
		 * The meta object literal for the '<em>Rectangle</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.draw2d.geometry.Rectangle
		 * @see emodel.impl.EmodelPackageImpl#getRectangle()
		 * @generated
		 */
		EDataType RECTANGLE = eINSTANCE.getRectangle();

	}

} //EmodelPackage
