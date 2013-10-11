


/**
 * 得到数字的字符串数组
 */
numAnimation.getNumArray = function(num){
	return (""+num).split("");
}


numAnimation.getImageByNum = function(type,num){
	var result;
	//如果是回合
	if(type==0){
		"zc_tb_"+(parseFloat("zc_tb_31".split("_")[2])+num);
	}
	//伤害
	if(type==1){
		"zc_tb_"+(parseFloat("zc_tb_63".split("_")[2])+num);
	}
	//治疗
	if(type==2){
		"zc_tb_"+(parseFloat("zc_tb_73".split("_")[2])+num);
	}
	//结果  闪避 吸收 暴击 免疫 未命中
	if(type==3){
		"zc_tb_"+(parseFloat("zc_tb_83".split("_")[2])+num);
	}
	//加减号   21是减号
	if(type==4){
		"zc_tb_"+(parseFloat("zc_tb_21".split("_")[2])+num);
	}
}

/**
 * 拿到回合的图片数组
 */
numAnimation.getRoundImage = function(num){
	var roundImage = new Array;
	var nums = numAnimation.getNumArray(num);
	for(var i=0;i<nums.length;i++){
		Img_str = numAnimation.getImageByNum(0,parseFloat(nums[i]));
		roundImage.push(Img_str);
}

/**
 * 拿到伤害图片数组
 * hurtImage.hurt
 * hurtImage.num{数组}
 */
numAnimation.getHurtImage = function (num){
	if(num==0){
		return;
	}
	var hurtImage;
	//数字字符串
	var nums = numAnimation.getNumArray(num);
	//如果是伤害
	if(num<0){
		hurtImage.hurt = "zc_tb_21";
		for(var i=0;i<nums.length;i++){
			Img_str = numAnimation.getImageByNum(1,parseFloat(nums[i]));
			hurtImage.num.push(Img_str);
		}
	}
	if(num>0){
		hurtImage.hurt = "zc_tb_22";
		for(var i=0;i<nums.length;i++){
			Img_str = numAnimation.getImageByNum(2,parseFloat(nums[i]));
			hurtImage.num.push(Img_str);
		}
	}
}

/**
 * 吸收:0
 * 暴击:1
 */
numAnimation.getResultImage(type){
	if(type==0){
		return "zc_tb_84";
	}
	else if(type==1){
		return "zc_tb_85";
	}
	else{
		return;
	}
}

}










