/**
 */
package emodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EGolpe Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link emodel.EGolpeModel#getComponents <em>Components</em>}</li>
 *   <li>{@link emodel.EGolpeModel#getConnections <em>Connections</em>}</li>
 * </ul>
 * </p>
 *
 * @see emodel.EmodelPackage#getEGolpeModel()
 * @model
 * @generated
 */
public interface EGolpeModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Components</b></em>' containment reference list.
	 * The list contents are of type {@link emodel.EGolpeComponentModel}.
	 * It is bidirectional and its opposite is '{@link emodel.EGolpeComponentModel#getRootModel <em>Root Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Components</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Components</em>' containment reference list.
	 * @see emodel.EmodelPackage#getEGolpeModel_Components()
	 * @see emodel.EGolpeComponentModel#getRootModel
	 * @model opposite="rootModel" containment="true"
	 * @generated
	 */
	EList<EGolpeComponentModel> getComponents();

	/**
	 * Returns the value of the '<em><b>Connections</b></em>' containment reference list.
	 * The list contents are of type {@link emodel.EConnectionModel}.
	 * It is bidirectional and its opposite is '{@link emodel.EConnectionModel#getRootModel <em>Root Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connections</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connections</em>' containment reference list.
	 * @see emodel.EmodelPackage#getEGolpeModel_Connections()
	 * @see emodel.EConnectionModel#getRootModel
	 * @model opposite="rootModel" containment="true"
	 * @generated
	 */
	EList<EConnectionModel> getConnections();

} // EGolpeModel
