/**
 */
package emodel;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EGolpe Component Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link emodel.EGolpeComponentModel#getName <em>Name</em>}</li>
 *   <li>{@link emodel.EGolpeComponentModel#getIconAddress <em>Icon Address</em>}</li>
 *   <li>{@link emodel.EGolpeComponentModel#getPlace <em>Place</em>}</li>
 *   <li>{@link emodel.EGolpeComponentModel#getRootModel <em>Root Model</em>}</li>
 *   <li>{@link emodel.EGolpeComponentModel#getConnection <em>Connection</em>}</li>
 *   <li>{@link emodel.EGolpeComponentModel#getConstraint <em>Constraint</em>}</li>
 * </ul>
 * </p>
 *
 * @see emodel.EmodelPackage#getEGolpeComponentModel()
 * @model abstract="true"
 * @generated
 */
public interface EGolpeComponentModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see emodel.EmodelPackage#getEGolpeComponentModel_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link emodel.EGolpeComponentModel#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Icon Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Icon Address</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Icon Address</em>' attribute.
	 * @see #setIconAddress(String)
	 * @see emodel.EmodelPackage#getEGolpeComponentModel_IconAddress()
	 * @model
	 * @generated
	 */
	String getIconAddress();

	/**
	 * Sets the value of the '{@link emodel.EGolpeComponentModel#getIconAddress <em>Icon Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Icon Address</em>' attribute.
	 * @see #getIconAddress()
	 * @generated
	 */
	void setIconAddress(String value);

	/**
	 * Returns the value of the '<em><b>Place</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Place</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Place</em>' reference.
	 * @see #setPlace(EPlaceModel)
	 * @see emodel.EmodelPackage#getEGolpeComponentModel_Place()
	 * @model
	 * @generated
	 */
	EPlaceModel getPlace();

	/**
	 * Sets the value of the '{@link emodel.EGolpeComponentModel#getPlace <em>Place</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Place</em>' reference.
	 * @see #getPlace()
	 * @generated
	 */
	void setPlace(EPlaceModel value);

	/**
	 * Returns the value of the '<em><b>Root Model</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link emodel.EGolpeModel#getComponents <em>Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Root Model</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Root Model</em>' container reference.
	 * @see #setRootModel(EGolpeModel)
	 * @see emodel.EmodelPackage#getEGolpeComponentModel_RootModel()
	 * @see emodel.EGolpeModel#getComponents
	 * @model opposite="components" transient="false"
	 * @generated
	 */
	EGolpeModel getRootModel();

	/**
	 * Sets the value of the '{@link emodel.EGolpeComponentModel#getRootModel <em>Root Model</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Root Model</em>' container reference.
	 * @see #getRootModel()
	 * @generated
	 */
	void setRootModel(EGolpeModel value);

	/**
	 * Returns the value of the '<em><b>Connection</b></em>' reference list.
	 * The list contents are of type {@link emodel.EConnectionModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection</em>' reference list.
	 * @see emodel.EmodelPackage#getEGolpeComponentModel_Connection()
	 * @model upper="2"
	 * @generated
	 */
	EList<EConnectionModel> getConnection();

	/**
	 * Returns the value of the '<em><b>Constraint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraint</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraint</em>' attribute.
	 * @see #setConstraint(Rectangle)
	 * @see emodel.EmodelPackage#getEGolpeComponentModel_Constraint()
	 * @model dataType="emodel.Rectangle"
	 * @generated
	 */
	Rectangle getConstraint();

	/**
	 * Sets the value of the '{@link emodel.EGolpeComponentModel#getConstraint <em>Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraint</em>' attribute.
	 * @see #getConstraint()
	 * @generated
	 */
	void setConstraint(Rectangle value);

} // EGolpeComponentModel
