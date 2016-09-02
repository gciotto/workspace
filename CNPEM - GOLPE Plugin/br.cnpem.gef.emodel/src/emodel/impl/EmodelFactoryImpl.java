/**
 */
package emodel.impl;

import emodel.*;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EmodelFactoryImpl extends EFactoryImpl implements EmodelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EmodelFactory init() {
		try {
			EmodelFactory theEmodelFactory = (EmodelFactory)EPackage.Registry.INSTANCE.getEFactory(EmodelPackage.eNS_URI);
			if (theEmodelFactory != null) {
				return theEmodelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EmodelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmodelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case EmodelPackage.EGOLPE_MODEL: return createEGolpeModel();
			case EmodelPackage.EGOLPE_HOST_MODEL: return createEGolpeHostModel();
			case EmodelPackage.EGOLPE_SWITCH_MODEL: return createEGolpeSwitchModel();
			case EmodelPackage.EPLACE_MODEL: return createEPlaceModel();
			case EmodelPackage.ECONNECTION_MODEL: return createEConnectionModel();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case EmodelPackage.RECTANGLE:
				return createRectangleFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case EmodelPackage.RECTANGLE:
				return convertRectangleToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EGolpeModel createEGolpeModel() {
		EGolpeModelImpl eGolpeModel = new EGolpeModelImpl();
		return eGolpeModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EGolpeHostModel createEGolpeHostModel() {
		EGolpeHostModelImpl eGolpeHostModel = new EGolpeHostModelImpl();
		return eGolpeHostModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EGolpeSwitchModel createEGolpeSwitchModel() {
		EGolpeSwitchModelImpl eGolpeSwitchModel = new EGolpeSwitchModelImpl();
		return eGolpeSwitchModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPlaceModel createEPlaceModel() {
		EPlaceModelImpl ePlaceModel = new EPlaceModelImpl();
		return ePlaceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EConnectionModel createEConnectionModel() {
		EConnectionModelImpl eConnectionModel = new EConnectionModelImpl();
		return eConnectionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Rectangle createRectangleFromString(EDataType eDataType, String initialValue) {

		if(initialValue == null)
			return null;

		initialValue.replaceAll("\\s", "");

		String[] values = initialValue.split(",");

		if(values.length != 4)
			return null;

		Rectangle rect = new Rectangle();

		try {

			rect.setLocation(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
			rect.setSize(Integer.parseInt(values[2]), Integer.parseInt(values[3]));

		} catch(NumberFormatException e) {

			EcorePlugin.INSTANCE.log(e);
			rect = null;
		}

		return rect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertRectangleToString(EDataType eDataType, Object instanceValue) {
		
		if(instanceValue == null) 
			return null;
		
		Rectangle rect = (Rectangle) instanceValue;
		
		return rect.x+","+rect.y+","+rect.width+","+rect.height;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmodelPackage getEmodelPackage() {
		return (EmodelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EmodelPackage getPackage() {
		return EmodelPackage.eINSTANCE;
	}

} //EmodelFactoryImpl
