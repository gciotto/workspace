/**
 */
package emodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see emodel.EmodelPackage
 * @generated
 */
public interface EmodelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EmodelFactory eINSTANCE = emodel.impl.EmodelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>EGolpe Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EGolpe Model</em>'.
	 * @generated
	 */
	EGolpeModel createEGolpeModel();

	/**
	 * Returns a new object of class '<em>EGolpe Host Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EGolpe Host Model</em>'.
	 * @generated
	 */
	EGolpeHostModel createEGolpeHostModel();

	/**
	 * Returns a new object of class '<em>EGolpe Switch Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EGolpe Switch Model</em>'.
	 * @generated
	 */
	EGolpeSwitchModel createEGolpeSwitchModel();

	/**
	 * Returns a new object of class '<em>EPlace Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EPlace Model</em>'.
	 * @generated
	 */
	EPlaceModel createEPlaceModel();

	/**
	 * Returns a new object of class '<em>EConnection Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EConnection Model</em>'.
	 * @generated
	 */
	EConnectionModel createEConnectionModel();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EmodelPackage getEmodelPackage();

} //EmodelFactory
