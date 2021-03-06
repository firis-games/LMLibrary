package firis.lmlib.api.motion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelBase;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;

/**
 * メイドさんのバイバイモーション
 * @author firis-games
 *
 */
public class LMMotionByebye implements ILMMotion {

	public static final String MOTION_ID = "lml:byebye";

	@Override
	public String getMotionId() {
		return MOTION_ID;
	}
	
	@Override
	public boolean postRotationAngles(ModelLittleMaidBase model, String motion, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps) {
		
		if (!MOTION_ID.equals(motion)) return false;
		
		//モーション定義
		//バイバイ
		model.setDefaultPause(0, 0, ageInTicks, 0, 0, 0, entityCaps);
		
		float lc = ModelBase.mh_cos(ageInTicks * 0.35F);
		lc *= 0.3F;
		
		model.bipedLeftArm.addRotateAngleDegY(-90);
		model.bipedLeftArm.rotateAngleX = 3.14159F;
		model.bipedLeftArm.rotateAngleZ = 60F * 3.14159F / 180F;
		model.bipedLeftArm.addRotateAngleZ(lc);
		
		model.bipedRightArm.addRotateAngleDegY(90);
		model.bipedRightArm.rotateAngleX = -3.14159F;
		model.bipedRightArm.rotateAngleZ = -60F * 3.14159F / 180F;
		model.bipedRightArm.addRotateAngleZ(-lc);
		
		model.bipedRightArm.addRotationPointY(-2.0F);
		model.bipedLeftArm.addRotationPointY(-2.0F);
				
		return true;
		
	}
	
	
}
