/**
 */
package emodel.impl;

import emodel.EConnectionModel;
import emodel.EGolpeComponentModel;
import emodel.EGolpeModel;
import emodel.EmodelPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EGolpe Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link emodel.impl.EGolpeModelImpl#getComponents <em>Components</em>}</li>
 *   <li>{@link emodel.impl.EGolpeModelImpl#getConnections <em>Connections</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EGolpeModelImpl extends MinimalEObjectImpl.Container implements EGolpeModel {
	/**
	 * The cached value of the '{@link #getComponents() <em>Components</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<EGolpeComponentModel> components;

	/**
	 * The cached value of the '{@link #getConnections() <em>Connections</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnections()
	 * @generated
	 * @ordered
	 */
	protected EList<EConnectionModel> connections;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EGolpeModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EmodelPackage.Literals.EGOLPE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EGolpeComponentModel> getComponents() {
		if (components == null) {
			components = new EObjectContainmentWithInverseEList<EGolpeComponentModel>(EGolpeComponentModel.class, this, EmodelPackage.EGOLPE_MODEL__COMPONENTS, EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL);
		}
		return components;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EConnectionModel> getConnections() {
		if (connections == null) {
			connections = new EObjectContainmentWithInverseEList<EConnectionModel>(EConnectionModel.class, this, EmodelPackage.EGOLPE_MODEL__CONNECTIONS, EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL);
		}
		return connections;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EmodelPackage.EGOLPE_MODEL__COMPONENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getComponents()).basicAdd(otherEnd, msgs);
			case EmodelPackage.EGOLPE_MODEL__CONNECTIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getConnections()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EmodelPackage.EGOLPE_MODEL__COMPONENTS:
				return ((InternalEList<?>)getComponents()).basicRemove(otherEnd, msgs);
			case EmodelPackage.EGOLPE_MODEL__CONNECTIONS:
				return ((InternalEList<?>)getConnections()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EmodelPackage.EGOLPE_MODEL__COMPONENTS:
				return getComponents();
			case EmodelPackage.EGOLPE_MODEL__CONNECTIONS:
				return getConnections();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EmodelPackage.EGOLPE_MODEL__COMPONENTS:
				getComponents().clear();
				getComponents().addAll((Collection<? extends EGolpeComponentModel>)newValue);
				return;
			case EmodelPackage.EGOLPE_MODEL__CONNECTIONS:
				getConnections().clear();
				getConnections().addAll((Collection<? extends EConnectionModel>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case EmodelPackage.EGOLPE_MODEL__COMPONENTS:
				getComponents().clear();
				return;
			case EmodelPackage.EGOLPE_MODEL__CONNECTIONS:
				getConnections().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case EmodelPackage.EGOLPE_MODEL__COMPONENTS:
				return components != null && !components.isEmpty();
			case EmodelPackage.EGOLPE_MODEL__CONNECTIONS:
				return connections != null && !connections.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //EGolpeModelImpl
