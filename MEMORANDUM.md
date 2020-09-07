LMLibraryメモ
===

LMLibraryでロードされるテクスチャパックやサウンドパックの読込仕様についてのメモ    

---

## firis.lmmmパッケージ
外部から読込マルチモデルクラスの基底クラスをまとめたパッケージ  
マルチモデル側からMinecraftやMODの影響を排しバージョン間の互換を維持することを目指して実装している  
元々の処理がMinecraftの処理とマルチモデルの処理が混在していたためパッケージを整理し処理をリメイクしている  
 - **firis.lmmm.api.caps.IModelCaps**
   - MOD側からマルチモデル側へパラメータを受け渡すためのクラス
   - Entityの状態はすべてModelCapsを経由してアクセスしEntity -> マルチモデル側へ一方通行で値受け渡しを行う
 - **firis.lmmm.api.model**
   - マルチモデルの基底クラスをまとめたパッケージ
   - 標準型のメイドモデルの場合は「ModelLittleMaidBase」を継承しマルチモデルを実装する
   - 標準型でないメイドモデル（大型や人型以外）の場合は「ModelMultiBase」を継承しマルチモデルを実装する
   - 「ModelMultiBase」を継承しクラス名が「ModelLittleMaid_」「ModelMulti_」で始まる場合にマルチモデルとして認識される
 - **firis.lmmm.api.model.motion.ILMMotion**
   - 標準型メイドモデルのモーションを実装するためのインターフェース
   - 「ModelLittleMaidBase」を継承したマルチモデルのみモーション処理が反映される想定
 - **firis.lmmm.api.model.parts**
   - マルチモデル用のモデルパーツをまとめたパッケージ
   - MinecraftのModelBox互換でクラスが実装されている
 - **firis.lmmm.api.renderer**
   - マルチモデル用のレンダラークラスをまとめたパッケージ
   - MinecraftのModelRenderer互換でクラスが実装されている
 - **firis.lmmm.builtin.model**
   - 標準的なメイドモデルの実装クラス
   - 標準搭載されているdefault/SR2/Augモデルが実装されている
   
#### 課題
モーション系の処理が現状では体の動きしか制御できない  
目パチやチークは追加版マルチモデルに独自実装されているタイプがほとんどのため標準拡張だけでは対応ができない
  - 案1
    - モーション系に特化した「ModelLittleMaidBase」を拡張したベースモデルを作成する
    - 独自実装してるタイプは現状通り体の動きのみ適応するようにする
  - 案2
    - 「ModelLittleMaidBase」を拡張し目パチやチークを追加する
    - 独自実装しているものは複数パターン（eyeL/EyeL等）をリフレクションで動的に拾ってうまくいけば内部変数にもたせる
    - 全部の網羅は無理だがよく使うモデルのパターンをもたせる
    - **未検証のため実際にやれるかは要検証**

独自モーションを追加するような仕組みが欲しいが現状だと難しい  
追加するにしてもMODとしてなのかスクリプト的なものなのかなど検討が必要なため保留  

---



## firis.lmlibパッケージ
マルチモデル系をいろいろと使いまわすためにベースとなる処理を実装したパッケージ  
Minecraftとマルチモデルを橋渡しするためのRendererやModelBaseやメイドモデルを利用する際に必要なクラスを実装している  
 - **firis.lmlib.api**
   - MOD側から利用するための各実装クラスを定義している
   - **firis.lmlib.api.LMLibraryAPI**
     - マルチモデル定義クラス・サウンドパックの取得等MODから使用するためのAPIを定義している
   - **firis.lmlib.api.constant**
     - 色情報やサウンド情報などをenum定数で定義している
   - **firis.lmlib.api.resource**
     - マルチモデル・サウンドパック・マルチモデル構造クラスの情報を管理するためのオブジェクトクラス
     - 読み込んだ情報をMultiModelPackとTexturePackに変換しこの二つの情報からLMTextureBoxを生成する
     - LMTextureBoxがテクスチャパック単位の情報をすべて持っているためMOD側から扱う場合はこの定義をメインに使用する   
   - **その他の実装クラスについてはクラスファイル本体にコメント記載**
  
firis.lmlib.api以外の部分についてはテクスチャパックやサウンドパックのローダー定義や汎用マルチモデル選択画面等を実装している  
   
---
  
### Loader
LMLibraryのメインの処理  
テクスチャパックとサウンドパックを読み込み内部クラスへ登録する  
カスタムClassLoaderで読込を行いfindClassのタイミングでASMによるバイトコード変換を実行している  
読込フォーマットの詳細については別記  
   
---
  
### LMLibraryを前提MODとして利用する
LMLibraryを用いてリトルメイドモデルを描画するのは下記の構造を想定している
#### MinecraftMODで利用するための各クラス
  - **LMModelLittleMaid**
    - 「ModelMultiBase」をModelBaseとして扱うためのラッパークラス
    - 基本的な使い方であればRenderで扱っているだけなので意識する必要はない
  - **LMRenderMultiModel**
    - Rendererとして利用するためのクラス
    - LMModelLittleMaidを描画するためにはIModelCompound/IModelCapsインターフェースを実装したクラスが必要
  - **IModelCompound**
    - マルチモデルのテクスチャ名・カラー番号・契約状態を管理するためのインターフェース
    - LMRenderMultiModel.getModelConfigCompoundFromEntityから要求されるためEntityから受け取る/固定クラスを実装するなど描画のために用意する必要がある
    - Entityで利用することを想定したModelCompoundEntityBaseを実装しているので基本的にはこれを利用し必要に応じて拡張する
  - **IModelCaps**
    - Entityの状態をマルチモデルへ引き渡すためのインターフェース
    - マルチモデル側ではMinecraftのクラスへのアクセスは行わずすべてIModelCapsを経由してアクセスする
    - IModelCapsはIModelCompoundを経由して取得される
    - Entityで利用することを想定したModelCapsEntityBaseを実装しているので基本的にはこれを利用し必要に応じて拡張する

---

#### その他描画について
リトルメイドモデルの防具や手持ちアイテムはLayerとして定義している
  - **LMLayerArmorBase**
    - 防具モデルを描画するために必要な処理を実装したクラス
    - 基本構造はLMRenderMultiModelと同じのためIModelCompound/IModelCapsインターフェースが必要
  - **LMLayerHeldItemBase**
    - 手持ちのアイテムを描画するために必要な処理を実装したクラス
    - 利用する場合はEntityから描画アイテムを取得する方法を実装する必要がある
   
---
  
#### テクスチャ選択画面の利用について
IGuiTextureSelectを実装したクラスをLMLibraryAPI.openGuiTextureSelectに渡すことで利用可能  
基本的にはEntityへ継承することを想定しているがそれ以外の場合でも利用可能  
クライアント側のみの実装であるため変更後の同期処理については別途実装する必要がある  


