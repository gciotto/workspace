/**
 */
package emodel.impl;

import emodel.EConnectionModel;
import emodel.EGolpeComponentModel;
import emodel.EGolpeModel;
import emodel.EmodelPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EConnection Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link emodel.impl.EConnectionModelImpl#getSources <em>Sources</em>}</li>
 *   <li>{@link emodel.impl.EConnectionModelImpl#getRootModel <em>Root Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EConnectionModelImpl extends MinimalEObjectImpl.Container implements EConnectionModel {
	/**
	 * The cached value of the '{@link #getSources() <em>Sources</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSources()
	 * @generated
	 * @ordered
	 */
	protected EList<EGolpeComponentModel> sources;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EConnectionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EmodelPackage.Literals.ECONNECTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EGolpeComponentModel> getSources() {
		if (sources == null) {
			sources = new EObjectResolvingEList<EGolpeComponentModel>(EGolpeComponentModel.class, this, EmodelPackage.ECONNECTION_MODEL__SOURCES);
		}
		return sources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EGolpeModel getRootModel() {
		if (eContainerFeatureID() != EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL) return null;
		return (EGolpeModel)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRootModel(EGolpeModel newRootModel, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newRootModel, EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRootModel(EGolpeModel newRootModel) {
		if (newRootModel != eInternalContainer() || (eContainerFeatureID() != EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL && newRootModel != null)) {
			if (EcoreUtil.isAncestor(this, newRootModel))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newRootModel != null)
				msgs = ((InternalEObject)newRootModel).eInverseAdd(this, EmodelPackage.EGOLPE_MODEL__CONNECTIONS, EGolpeModel.class, msgs);
			msgs = basicSetRootModel(newRootModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL, newRootModel, newRootModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetRootModel((EGolpeModel)otherEnd, msgs);
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
			case EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL:
				return basicSetRootModel(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL:
				return eInternalContainer().eInverseRemove(this, EmodelPackage.EGOLPE_MODEL__CONNECTIONS, EGolpeModel.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EmodelPackage.ECONNECTION_MODEL__SOURCES:
				return getSources();
			case EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL:
				return getRootModel();
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
			case EmodelPackage.ECONNECTION_MODEL__SOURCES:
				getSources().clear();
				getSources().addAll((Collection<? extends EGolpeComponentModel>)newValue);
				return;
			case EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL:
				setRootModel((EGolpeModel)newValue);
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
			case EmodelPackage.ECONNECTION_MODEL__SOURCES:
				getSources().clear();
				return;
			case EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL:
				setRootModel((EGolpeModel)null);
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
			case EmodelPackage.ECONNECTION_MODEL__SOURCES:
				return sources != null && !sources.isEmpty();
			case EmodelPackage.ECONNECTION_MODEL__ROOT_MODEL:
				return getRootModel() != null;
		}
		return super.eIsSet(featureID);
	}

} //EConnectionModelImpl
