var PREPARE_STATUS = 0;//准备状态
var MY_HANDLE_STATUS = 1;//己方操作状态
var ENEMY_HANDLE_STATUS = 2;//对方操作状态
var FIGHTING_STATUS = 3;//战斗状态
var THEEND_STATUS = 4;//战斗结束状态

var battleStatus = PREPARE_STATUS;//战斗的初始状态为准备状态


var soldierInfo = new Array();//战斗双方部队信息





/**
 * 初始化函数
 */
function init(){
	
}


function run(){
	switch(battleStatus){
	case PREPARE_STATUS:
		//
		break;
	case MY_HANDLE_STATUS:
		//
		break;
	case ENEMY_HANDLE_STATUS:
		//
		break;
	case FIGHTING_STATUS:
		//
		break;
	case THEEND_STATUS:
		//
		break;
	}
	
	
	
	
	
	//======================================================================
	var soldierInfo = new Array();//战斗双方部队信息
	
	//var background = "图片";//背景图片
	var defenceWork = 0;//城防伤害
	
	var canOperate = false;//是否可操作
	
	//回合信息
	var roundInfo = 
	{
			isNewRound:false,//是否新回合
			roundNum:0,//回合数
			//出场顺序
			order:
			{
				locationId:0,//位置id
				orderNum:0//出手顺序
			}
			
	}
	
	//buff影响
	var buffEffect =new Array();
	//buffEffect[i].locationId//军团位置Id，军团标识
	//buffEffect[i].effectType//影响类型，1普通，2吸收
	//buffEffect[i].effectValue//影响值，带正负号
	//buffEffect[i].isDead//军团是否失败
	
	
	//技能
	var operateSkil = new Array();
	//data.operateSkill操作技能，数组
	//operateSkill[i].skillIcon技能图标
	//operateSkill[i].toolTip技能toolTip
						//toolTip.name//技能名
						//toolTip.level技能等级
						//toolTip.needVnp需要精力
						//toolTip.coolDown冷却回合数
						//toolTip.description描述
	//operateSkill[i].canUse技能是否可用
	//operateSkill[i].remainRound技能剩余冷却回合数

	
	//可以攻击的locationId
	var locationId = new Array();
	
	
	//回合剩余时间
	var battleTime = 20;
	
	//是否自动攻击
	var battleAutoAttack = false;
	
	//防守  更新军团状态
	
	
	
	
	

	/**
	 * 准备工作
	 */
	function prepare(){
		
	}
	
	
	var battleData ;
	/**
	 * 战斗开始
	 */
	
	function battleStart(battleMessageData){
		//data.isNewRound是否是新回合
		//data.roundNum回合数
		//data.order//出手顺序,数组
				//order[i].locationId//军团位置Id，军团标识
				//order[i].orderNum//出手顺序编号
		battleData.isNewRound = data.isNewRound;
		battleData.roundNum = data.roundNum//回合数
		battleData.isNewRound = data.order//出手顺序,数组
		for(var i=0;i<data.order.length;i++){
			battleData.order[i].locationId  = data.order[i].locationId;
			battleData.order[i].orderNum  = data.order[i].orderNum;
		}
	}
	
	
}