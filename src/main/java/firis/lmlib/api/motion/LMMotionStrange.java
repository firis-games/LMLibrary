package firis.lmlib.api.motion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelBase;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;

/**
 * メイドさんの変な動き
 * @author firis-games
 *
 */
public class LMMotionStrange implements ILMMotion {

	public static final String MOTION_ID = "lml:strange";

	@Override
	public String getMotionId() {
		return MOTION_ID;
	}
	
	@Override
	public boolean postRotationAngles(ModelLittleMaidBase model, String motion, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps) {
		
		if (!MOTION_ID.equals(motion)) return false;
		
		//モーション定義
		model.setDefaultPause(0, 0, ageInTicks, 0, 0, 0, entityCaps);
		
		float lc = ModelBase.mh_cos(ageInTicks * 0.8F);
		lc *= 0.2F;
		model.bipedNeck.addRotationPointX(lc);
		model.bipedNeck.addRotationPointY(Math.abs(lc) * 2.5F);
		model.bipedRightArm.addRotationPointX(lc);
		model.bipedRightArm.addRotationPointY(Math.abs(lc) * 5F);
		model.bipedLeftArm.addRotationPointX(lc);
		model.bipedLeftArm.addRotationPointY(Math.abs(lc) * 5F);

		model.bipedBody.addRotationPointX(lc);
		model.bipedBody.addRotationPointY(Math.abs(lc));
		
		return true;
		
	}
	
	
}
