package firis.lmlib.api.motion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;

/**
 * メイドさんのお座りモーション制御用
 * @author firis-games
 *
 */
public class LMMotionSitdown implements ILMMotion {
	
	public static final String MOTION_ID = "lml:sitdown";
	
	@Override
	public String getMotionId() {
		return MOTION_ID;
	}
	
	/**
	 * お座りモーションのお座り位置調整用
	 */
	@Override
	public boolean postRotationAngles(ModelLittleMaidBase model, String motion, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps) {
		//カスタム設定
		//お座りモーションの場合はモデル側で位置を調整する
		if (model.isRiding && MOTION_ID.equals(motion)) {
			model.mainFrame.rotationPointY += 5.00F;
		}
		return false;
	}

}
