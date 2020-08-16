package firis.lmlib.api.motion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelBase;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;

/**
 * メイドさんのダンスモーション
 * 
 * @author firis-games
 *
 */
public class LMMotionDance implements ILMMotion {

	public static final String MOTION_ID = "lml:dance";

	@Override
	public String getMotionId() {
		return MOTION_ID;
	}

	@Override
	public boolean postRotationAngles(ModelLittleMaidBase model, String motion, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps) {

		if (!MOTION_ID.equals(motion)) return false;

		// 初期化
		model.setDefaultPause(0, 0, ageInTicks, 0, 0, 0, entityCaps);

		// モーション定義
		float arm = ModelBase.mh_cos(ageInTicks * 0.25F);
		arm *= 0.5F;

		model.bipedRightArm.addRotateAngleDegX(240);
		model.bipedLeftArm.addRotateAngleDegX(240);

		model.bipedRightArm.addRotateAngleZ(arm);
		model.bipedLeftArm.addRotateAngleZ(arm);

		float head = ModelBase.mh_cos(ageInTicks * 0.25F);
		head *= 0.25F;
		model.bipedHead.addRotateAngleZ(head);
		model.bipedBody.addRotateAngleZ(head * 0.1F);

		model.bipedNeck.addRotationPointX(head * 2.0F);
		model.bipedBody.addRotationPointX(head * 3.0F);
		model.bipedPelvic.addRotationPointX(head * 2.0F);

		model.bipedBody.addRotateAngleZ(head * 0.1F);
		model.bipedPelvic.addRotateAngleZ(head * 0.1F);

		return true;
	}

}
