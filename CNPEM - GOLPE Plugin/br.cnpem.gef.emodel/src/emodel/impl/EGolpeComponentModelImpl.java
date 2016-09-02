/**
 */
package emodel.impl;

import emodel.EConnectionModel;
import emodel.EGolpeComponentModel;
import emodel.EGolpeModel;
import emodel.EPlaceModel;
import emodel.EmodelPackage;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Rectangle;

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
 * An implementation of the model object '<em><b>EGolpe Component Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link emodel.impl.EGolpeComponentModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link emodel.impl.EGolpeComponentModelImpl#getIconAddress <em>Icon Address</em>}</li>
 *   <li>{@link emodel.impl.EGolpeComponentModelImpl#getPlace <em>Place</em>}</li>
 *   <li>{@link emodel.impl.EGolpeComponentModelImpl#getRootModel <em>Root Model</em>}</li>
 *   <li>{@link emodel.impl.EGolpeComponentModelImpl#getConnection <em>Connection</em>}</li>
 *   <li>{@link emodel.impl.EGolpeComponentModelImpl#getConstraint <em>Constraint</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class EGolpeComponentModelImpl extends MinimalEObjectImpl.Container implements EGolpeComponentModel {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getIconAddress() <em>Icon Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIconAddress()
	 * @generated
	 * @ordered
	 */
	protected static final String ICON_ADDRESS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIconAddress() <em>Icon Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIconAddress()
	 * @generated
	 * @ordered
	 */
	protected String iconAddress = ICON_ADDRESS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPlace() <em>Place</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlace()
	 * @generated
	 * @ordered
	 */
	protected EPlaceModel place;

	/**
	 * The cached value of the '{@link #getConnection() <em>Connection</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnection()
	 * @generated
	 * @ordered
	 */
	protected EList<EConnectionModel> connection;

	/**
	 * The default value of the '{@link #getConstraint() <em>Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraint()
	 * @generated
	 * @ordered
	 */
	protected static final Rectangle CONSTRAINT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getConstraint() <em>Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraint()
	 * @generated
	 * @ordered
	 */
	protected Rectangle constraint = CONSTRAINT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EGolpeComponentModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EmodelPackage.Literals.EGOLPE_COMPONENT_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmodelPackage.EGOLPE_COMPONENT_MODEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIconAddress() {
		return iconAddress;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIconAddress(String newIconAddress) {
		String oldIconAddress = iconAddress;
		iconAddress = newIconAddress;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmodelPackage.EGOLPE_COMPONENT_MODEL__ICON_ADDRESS, oldIconAddress, iconAddress));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPlaceModel getPlace() {
		if (place != null && place.eIsProxy()) {
			InternalEObject oldPlace = (InternalEObject)place;
			place = (EPlaceModel)eResolveProxy(oldPlace);
			if (place != oldPlace) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EmodelPackage.EGOLPE_COMPONENT_MODEL__PLACE, oldPlace, place));
			}
		}
		return place;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPlaceModel basicGetPlace() {
		return place;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPlace(EPlaceModel newPlace) {
		EPlaceModel oldPlace = place;
		place = newPlace;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmodelPackage.EGOLPE_COMPONENT_MODEL__PLACE, oldPlace, place));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EGolpeModel getRootModel() {
		if (eContainerFeatureID() != EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL) return null;
		return (EGolpeModel)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRootModel(EGolpeModel newRootModel, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newRootModel, EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRootModel(EGolpeModel newRootModel) {
		if (newRootModel != eInternalContainer() || (eContainerFeatureID() != EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL && newRootModel != null)) {
			if (EcoreUtil.isAncestor(this, newRootModel))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newRootModel != null)
				msgs = ((InternalEObject)newRootModel).eInverseAdd(this, EmodelPackage.EGOLPE_MODEL__COMPONENTS, EGolpeModel.class, msgs);
			msgs = basicSetRootModel(newRootModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL, newRootModel, newRootModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EConnectionModel> getConnection() {
		if (connection == null) {
			connection = new EObjectResolvingEList<EConnectionModel>(EConnectionModel.class, this, EmodelPackage.EGOLPE_COMPONENT_MODEL__CONNECTION);
		}
		return connection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rectangle getConstraint() {
		return constraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstraint(Rectangle newConstraint) {
		Rectangle oldConstraint = constraint;
		constraint = newConstraint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmodelPackage.EGOLPE_COMPONENT_MODEL__CONSTRAINT, oldConstraint, constraint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL:
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
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL:
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
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL:
				return eInternalContainer().eInverseRemove(this, EmodelPackage.EGOLPE_MODEL__COMPONENTS, EGolpeModel.class, msgs);
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
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__NAME:
				return getName();
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ICON_ADDRESS:
				return getIconAddress();
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__PLACE:
				if (resolve) return getPlace();
				return basicGetPlace();
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL:
				return getRootModel();
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__CONNECTION:
				return getConnection();
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__CONSTRAINT:
				return getConstraint();
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
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__NAME:
				setName((String)newValue);
				return;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ICON_ADDRESS:
				setIconAddress((String)newValue);
				return;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__PLACE:
				setPlace((EPlaceModel)newValue);
				return;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL:
				setRootModel((EGolpeModel)newValue);
				return;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__CONNECTION:
				getConnection().clear();
				getConnection().addAll((Collection<? extends EConnectionModel>)newValue);
				return;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__CONSTRAINT:
				setConstraint((Rectangle)newValue);
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
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ICON_ADDRESS:
				setIconAddress(ICON_ADDRESS_EDEFAULT);
				return;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__PLACE:
				setPlace((EPlaceModel)null);
				return;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL:
				setRootModel((EGolpeModel)null);
				return;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__CONNECTION:
				getConnection().clear();
				return;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__CONSTRAINT:
				setConstraint(CONSTRAINT_EDEFAULT);
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
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ICON_ADDRESS:
				return ICON_ADDRESS_EDEFAULT == null ? iconAddress != null : !ICON_ADDRESS_EDEFAULT.equals(iconAddress);
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__PLACE:
				return place != null;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__ROOT_MODEL:
				return getRootModel() != null;
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__CONNECTION:
				return connection != null && !connection.isEmpty();
			case EmodelPackage.EGOLPE_COMPONENT_MODEL__CONSTRAINT:
				return CONSTRAINT_EDEFAULT == null ? constraint != null : !CONSTRAINT_EDEFAULT.equals(constraint);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", iconAddress: ");
		result.append(iconAddress);
		result.append(", constraint: ");
		result.append(constraint);
		result.append(')');
		return result.toString();
	}

} //EGolpeComponentModelImpl
