/**
 */
package emodel.util;

import emodel.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see emodel.EmodelPackage
 * @generated
 */
public class EmodelSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static EmodelPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmodelSwitch() {
		if (modelPackage == null) {
			modelPackage = EmodelPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case EmodelPackage.EGOLPE_MODEL: {
				EGolpeModel eGolpeModel = (EGolpeModel)theEObject;
				T result = caseEGolpeModel(eGolpeModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EmodelPackage.EGOLPE_COMPONENT_MODEL: {
				EGolpeComponentModel eGolpeComponentModel = (EGolpeComponentModel)theEObject;
				T result = caseEGolpeComponentModel(eGolpeComponentModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EmodelPackage.EGOLPE_HOST_MODEL: {
				EGolpeHostModel eGolpeHostModel = (EGolpeHostModel)theEObject;
				T result = caseEGolpeHostModel(eGolpeHostModel);
				if (result == null) result = caseEGolpeComponentModel(eGolpeHostModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EmodelPackage.EGOLPE_SWITCH_MODEL: {
				EGolpeSwitchModel eGolpeSwitchModel = (EGolpeSwitchModel)theEObject;
				T result = caseEGolpeSwitchModel(eGolpeSwitchModel);
				if (result == null) result = caseEGolpeComponentModel(eGolpeSwitchModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EmodelPackage.EPLACE_MODEL: {
				EPlaceModel ePlaceModel = (EPlaceModel)theEObject;
				T result = caseEPlaceModel(ePlaceModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EmodelPackage.ECONNECTION_MODEL: {
				EConnectionModel eConnectionModel = (EConnectionModel)theEObject;
				T result = caseEConnectionModel(eConnectionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EGolpe Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EGolpe Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEGolpeModel(EGolpeModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EGolpe Component Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EGolpe Component Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEGolpeComponentModel(EGolpeComponentModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EGolpe Host Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EGolpe Host Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEGolpeHostModel(EGolpeHostModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EGolpe Switch Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EGolpe Switch Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEGolpeSwitchModel(EGolpeSwitchModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EPlace Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EPlace Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEPlaceModel(EPlaceModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EConnection Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EConnection Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEConnectionModel(EConnectionModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //EmodelSwitch
