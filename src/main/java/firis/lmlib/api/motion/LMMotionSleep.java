package firis.lmlib.api.motion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelBase;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;

/**
 * メイドさんのおねんねモーション
 * @author firis-games
 *
 */
public class LMMotionSleep implements ILMMotion {

	public static final String MOTION_ID = "lml:sleep";

	@Override
	public String getMotionId() {
		return MOTION_ID;
	}
	
	@Override
	public boolean postRotationAngles(ModelLittleMaidBase model, String motion, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps) {
		
		if (!MOTION_ID.equals(motion)) return false;
		
		// モーション定義
		// 寝転がし
		model.setDefaultPause(0, 0, ageInTicks, 0, 0, 0, entityCaps);
		model.mainFrame.setRotateAngleDegX(-90);
		model.mainFrame.setRotationPoint(0F, 22F, 4F);
		model.bipedHead.setRotateAngleDegY(0);
		model.bipedHead.setRotateAngleDegX(0);

		// 腕の位置
		model.bipedRightArm.setRotateAngleDegX(180);
		model.bipedRightArm.setRotateAngleDegZ(-60);
		model.bipedLeftArm.setRotateAngleDegX(180);
		model.bipedLeftArm.setRotateAngleDegZ(60);
		
		model.bipedRightArm.addRotationPointY(-1.5F);
		model.bipedLeftArm.addRotationPointY(-1.5F);
		
		//足の位置
		model.bipedRightLeg.setRotateAngleDegZ(15);
		model.bipedLeftLeg.setRotateAngleDegZ(-15);

		// 腕の揺らぎ
		// 通常
		float la = ModelBase.mh_sin(ageInTicks * 0.067F) * 0.05F;
		float lc = 0.5F + ModelBase.mh_cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		model.bipedRightArm.addRotateAngleX(la);
		model.bipedLeftArm.addRotateAngleX(-la);
		model.bipedRightArm.addRotateAngleZ(lc);
		model.bipedLeftArm.addRotateAngleZ(-lc);

		model.bipedRightLeg.addRotateAngleX(la);
		model.bipedLeftLeg.addRotateAngleX(-la);
				
		return true;
		
	}
	
	
}
