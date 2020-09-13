package firis.lmlib.api.motion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelBase;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;

/**
 * メイドさんのお休みモーション
 * @author firis-games
 *
 */
public class LMMotionSleep2 implements ILMMotion {

	public static final String MOTION_ID = "lml:sleep2";

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

		// 腕の調整
		float la = ModelBase.mh_sin(ageInTicks * 0.067F) * 0.05F;
		float lc = 0.25F + ModelBase.mh_cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		model.bipedRightArm.addRotateAngleX(la);
		model.bipedLeftArm.addRotateAngleX(-la);
		model.bipedRightArm.addRotateAngleZ(lc);
		model.bipedLeftArm.addRotateAngleZ(-lc);
		
		//目を閉じる
		model.linkEyeL.setVisible(true);
		model.linkEyeR.setVisible(true);
				
		return true;
		
	}
	
	
}
