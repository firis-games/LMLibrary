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
public class LMMotionDance2 implements ILMMotion {

	public static final String MOTION_ID = "lml:dance2";

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

		// 腕の縦振り制御
		model.bipedRightArm.setRotateAngleDegX(270);
		model.bipedLeftArm.setRotateAngleDegX(270);
		model.bipedRightArm.addRotateAngleX(arm);
		model.bipedLeftArm.addRotateAngleX(-arm);

		// 身体全体の動き
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
