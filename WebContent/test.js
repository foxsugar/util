var PREPARE_STATUS = 0;//׼��״̬
var MY_HANDLE_STATUS = 1;//��������״̬
var ENEMY_HANDLE_STATUS = 2;//�Է�����״̬
var FIGHTING_STATUS = 3;//ս��״̬
var THEEND_STATUS = 4;//ս������״̬

var battleStatus = PREPARE_STATUS;//ս���ĳ�ʼ״̬Ϊ׼��״̬


var soldierInfo = new Array();//ս��˫��������Ϣ





/**
 * ��ʼ������
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
	var soldierInfo = new Array();//ս��˫��������Ϣ
	
	//var background = "ͼƬ";//����ͼƬ
	var defenceWork = 0;//�Ƿ��˺�
	
	var canOperate = false;//�Ƿ�ɲ���
	
	//�غ���Ϣ
	var roundInfo = 
	{
			isNewRound:false,//�Ƿ��»غ�
			roundNum:0,//�غ���
			//����˳��
			order:
			{
				locationId:0,//λ��id
				orderNum:0//����˳��
			}
			
	}
	
	//buffӰ��
	var buffEffect =new Array();
	//buffEffect[i].locationId//����λ��Id�����ű�ʶ
	//buffEffect[i].effectType//Ӱ�����ͣ�1��ͨ��2����
	//buffEffect[i].effectValue//Ӱ��ֵ����������
	//buffEffect[i].isDead//�����Ƿ�ʧ��
	
	
	//����
	var operateSkil = new Array();
	//data.operateSkill�������ܣ�����
	//operateSkill[i].skillIcon����ͼ��
	//operateSkill[i].toolTip����toolTip
						//toolTip.name//������
						//toolTip.level���ܵȼ�
						//toolTip.needVnp��Ҫ����
						//toolTip.coolDown��ȴ�غ���
						//toolTip.description����
	//operateSkill[i].canUse�����Ƿ����
	//operateSkill[i].remainRound����ʣ����ȴ�غ���

	
	//���Թ�����locationId
	var locationId = new Array();
	
	
	//�غ�ʣ��ʱ��
	var battleTime = 20;
	
	//�Ƿ��Զ�����
	var battleAutoAttack = false;
	
	//����  ���¾���״̬
	
	
	
	
	

	/**
	 * ׼������
	 */
	function prepare(){
		
	}
	
	
	var battleData ;
	/**
	 * ս����ʼ
	 */
	
	function battleStart(battleMessageData){
		//data.isNewRound�Ƿ����»غ�
		//data.roundNum�غ���
		//data.order//����˳��,����
				//order[i].locationId//����λ��Id�����ű�ʶ
				//order[i].orderNum//����˳����
		battleData.isNewRound = data.isNewRound;
		battleData.roundNum = data.roundNum//�غ���
		battleData.isNewRound = data.order//����˳��,����
		for(var i=0;i<data.order.length;i++){
			battleData.order[i].locationId  = data.order[i].locationId;
			battleData.order[i].orderNum  = data.order[i].orderNum;
		}
	}
	
	
}