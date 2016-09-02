/**
 */
package emodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EConnection Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link emodel.EConnectionModel#getSources <em>Sources</em>}</li>
 *   <li>{@link emodel.EConnectionModel#getRootModel <em>Root Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see emodel.EmodelPackage#getEConnectionModel()
 * @model
 * @generated
 */
public interface EConnectionModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Sources</b></em>' reference list.
	 * The list contents are of type {@link emodel.EGolpeComponentModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sources</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sources</em>' reference list.
	 * @see emodel.EmodelPackage#getEConnectionModel_Sources()
	 * @model upper="2"
	 * @generated
	 */
	EList<EGolpeComponentModel> getSources();

	/**
	 * Returns the value of the '<em><b>Root Model</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link emodel.EGolpeModel#getConnections <em>Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Root Model</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Root Model</em>' container reference.
	 * @see #setRootModel(EGolpeModel)
	 * @see emodel.EmodelPackage#getEConnectionModel_RootModel()
	 * @see emodel.EGolpeModel#getConnections
	 * @model opposite="connections" transient="false"
	 * @generated
	 */
	EGolpeModel getRootModel();

	/**
	 * Sets the value of the '{@link emodel.EConnectionModel#getRootModel <em>Root Model</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Root Model</em>' container reference.
	 * @see #getRootModel()
	 * @generated
	 */
	void setRootModel(EGolpeModel value);

} // EConnectionModel
