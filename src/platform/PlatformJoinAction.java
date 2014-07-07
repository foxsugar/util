package com.crystalcg.gamedev.platform;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crystal.game.constant.GameInfoConstants;
import com.crystal.game.constant.OrderStatus;
import com.crystal.game.constant.ResultStatus;
import com.crystal.game.model.Cards;
import com.crystal.game.model.PlatformInfo;
import com.crystal.game.model.User;
import com.crystal.game.model.UserOrder;
import com.crystal.game.service.user.CardsServiceFactory;
import com.crystal.game.service.user.UserAuthServiceFactory;
import com.crystal.game.service.user.UserOrderServiceFactory;
import com.crystal.game.util.CookieUtils;
import com.crystal.game.util.HttpUtils;
import com.crystal.game.util.ParamUtils;
import com.crystal.game.util.StringUtil;
import com.crystal.game.util.UserTransactionUtil;
import com.crystalcg.gamedev.user.domain.UserCharacter;
import com.crystalcg.gamedev.user.service.CharacterService;
import com.crystalcg.gamedev.util.PlatformUtils;

@Controller
public class PlatformJoinAction {

	private static Logger logger = Logger.getLogger(PlatformJoinAction.class.getName());

	private final int SUCCESS = 0;// 支付失败
	private final int ERROR_NOPLAYER = -1;// 未创建角色

	private final int ERROR_REPEAT = -2; // 重复订单
	private final int ERROR_GAME = -3; // 游戏内部错误
	private final int ERROR_SIGN = -4; // 无效的请求签名
	private final int ERROR_PARM = -5; // 参数不合法
	private final int ERROR_UNKNOWN = -6;// 未知异常
	private final int ERROR_SERVER = -7;// 无法识别的服务器
	private final int ERROR_NET = -8; // 网络异常
	private final int ERROR_IP = -9; // ip不合法

	private PlatformJoinService platformJoinService = PlatformJoinService.getInstance();
	private CharacterService characterService;
	private final int RmbExchangeRate = 10;// 人民币对金锭的比率
	
	public static String DOMAIN = null;
	public static String PAY_URL = null;
	public static PlatformInfo PLATFORM = null ;

	
	
	private void genCardKey(){
		String[] typeStr = new String[]{"mt-a","mt-b","mt-c","mt-d"};
		List<Cards> list = new ArrayList<Cards>();
		for(String tys:typeStr){
			
			for(int j=0;j<10;j++){
				String fileName = tys+"-"+j;
				List<String> listLines = new ArrayList<String>();
				for(int i=0;i<10000;i++){
					Cards card = new Cards();
					String index = ""+i;
					String type = tys;
					String key = PlatformUtils.getMD5(type+j,index,"stby","20140429").toUpperCase(Locale.US);
					
					card.setCard_key(key);
					card.setType(type);
					card.setStatus(0);
					listLines.add(key);
					list.add(card);
				}
				try {
					FileUtils.writeLines(new File("d://mt/"+fileName+".csv"), listLines);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		CardsServiceFactory.getInstance().saveCards(list);
	}
	
	@RequestMapping(value = "/login4p")
	public void checkUser(HttpServletResponse response,HttpServletRequest request) throws IOException, ServletException {
		final String username = ParamUtils.getParameter(request, "qid", "");
		final String serverId = ParamUtils.getParameter(request, "server_id", "");
		final long time = ParamUtils.getLongParameter(request, "time", 0L);
		final String sign = ParamUtils.getParameter(request, "sign", "");
		// 0用户未填写实名制信息 1用户填写过实名制信息，且大于18岁 2用户填写过实名制信息，但是小于18岁
		final int isAdult = ParamUtils.getIntParameter(request, "isAdult", 0);
		final String agent = ParamUtils.getParameter(request, "agent", "");
		
		// 参数检查
		if ("".equals(username) || "".equals(serverId) || "".equals(sign)|| time <= 0||"".equals(sign)||"".equals(agent)) {
			logger.error(log_l(username,serverId,agent,time,sign,isAdult,"参数错误"));
			//response.sendRedirect(failedUrl);
			return;
		}

		PlatformInfo platformInfo = getPlatformInfo(agent, serverId);
		if(platformInfo==null){
			logger.error(log_l(username,serverId,agent,time,sign,isAdult,"从数据库找不到数据"));
//			response.sendRedirect(failedUrl);
			return ;
		}else{
			if(PlatformJoinAction.DOMAIN == null){
				PlatformJoinAction.DOMAIN = platformInfo.getDomain_url();
			}
			if(PlatformJoinAction.PAY_URL == null){
				PlatformJoinAction.PAY_URL = platformInfo.getCharge_url();
			}
			//平台信息
			if(PLATFORM==null){
				PLATFORM = platformInfo;
			}
		}
		
//		if(!isWhite(request, platformInfo)){
//			logger.error(log_l(username,serverId,agent,time,sign,isAdult,"ip不合法"));
//			response.sendRedirect(getFailUrl(platformInfo,request));
//			return ;
//		}
		
		Long nowTime = System.currentTimeMillis();
		// 时间验证 登录时间不能小于当前时间15分钟
		if ((nowTime - time) / 1000 > 900) {
			logger.error(log_l(username,serverId,agent,time,sign,isAdult,"时间超时"));
			response.sendRedirect(getFailUrl(platformInfo,request));
			return;
		}
		
		
		// 密钥验证
		String key = platformInfo.getLogin_key();
		String timeStr = ""+time;
		String authKey = PlatformUtils.getMD5(username, serverId, timeStr,agent, key);

		if (!authKey.equals(sign)) {
			logger.error(log_l(username,serverId,agent,time,sign,isAdult,"密钥不正确"));
			logger.error(authKey);
			response.sendRedirect(getFailUrl(platformInfo,request));
			return;
		}
		// 往数据库写数据，记录该用户
		User user = new User();
		user.setCurrentLoginTime(nowTime);
		user.setCurrentLoginIp(HttpUtils.getIp(request));
		user.setUserType(PLATFORM.getPlatform_type());// 本地用户
		user.setIsAdult(isAdult);
		user.setUserName(username);
		user.setPassword(StringUtil.hashPwd(agent + username));// 给每个用户一个默认密码：平台名+用户名
		int result = UserAuthServiceFactory.getInstance().operatePlatformUserInfo(user);
		if (result == ResultStatus.SUCCESS) {
			CookieUtils.loginWriteCookie(username, nowTime,platformInfo.getDomain_url(), response);
			response.sendRedirect(platformInfo.getSuccess_url());
		} else {
			logger.error(log_l(username,serverId,agent,time,sign,isAdult,"写入失败"));
			response.sendRedirect(getFailUrl(platformInfo,request));
		}

	}


	
	
	/**
	 * 用户充值时调用，加游戏币的接口
	 * 
	 * @param request
	 * @param response
	 * @return 成功返回1，重复通知返回2，失败返回0（包括签名校验错误）
	 * 
	 * @author liuxueliang/2014-2-10/下午5:55:04
	 */
	@RequestMapping(value = "/exchange")
	@ResponseBody
	public int addMoney(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		final String username = ParamUtils.getParameter(request, "qid", "");
		final String serverId = ParamUtils.getParameter(request, "server_id", "");
		final String sign = ParamUtils.getParameter(request, "sign", "");
		final String order_id = ParamUtils.getParameter(request, "order_id", "");
		final int order_amount = ParamUtils.getIntParameter(request, "order_amount", -1);
		final long time = ParamUtils.getLongParameter(request, "time", 0);
		final String agent = ParamUtils.getParameter(request, "agent", "");
		
		if ("".equals(username) || "".equals(serverId) || "".equals(sign) || "".equals(agent) ||  order_amount <= 0||time==0L) {
			logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "参数错误"));
			return ERROR_PARM;
		}
		
		PlatformInfo platformInfo = getPlatformInfo(agent, serverId);
		if(platformInfo==null){
			logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "从数据库找不到信息"));
			return ERROR_PARM;
		}
		if(!isWhite(request, platformInfo)){
			logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "ip不合法"));
			return ERROR_IP;
		}
		// check pay_key
		String authKey = PlatformUtils.getMD5(username,serverId,""+time,agent,order_id,Integer.toString(order_amount),getPlatformInfo(agent,serverId).getPay_key());
		if (!authKey.equals(sign)) {
			logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "密钥不合法"));
			return ERROR_SIGN;
		}

		/**
		 * 充值步骤： 1、判断要充值用户是否存在 2、判断订单是否存在或者开通 3、插入订单，默认未开通4、加游戏币，记录日志
		 * 5、将订单更新为开通，提交事务，返回成功，否则回滚
		 **/
		// 判断用户是否存在
		UserCharacter userCharacter = characterService.getCharacterByAccountName(username);
		if (userCharacter == null) {
			logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "无创建角色"));
			return ERROR_NOPLAYER;
		}
		String platName = platformInfo.getName();
		int gameCoinTotal = order_amount * RmbExchangeRate;
		// 创建订单
		UserOrder userOrder = new UserOrder();
		userOrder.setOrderNo(order_id);
		userOrder.setUserName(username);
		//平台类型
		userOrder.setPlatformType(PLATFORM.getPlatform_type());
		userOrder.setRmTotal(order_amount);
		userOrder.setGameCoinTotal(gameCoinTotal);
		userOrder.setOrderType(OrderStatus.USUAL_PAY);
		userOrder.setOrderStatus(OrderStatus.ORDER_OPEN_FAILED);// 默认失败
		userOrder.setOrderTypeName(platName+"联运普通充值");
		userOrder.setGameId(GameInfoConstants.GAME_STBY_ID);
		userOrder.setGameServer(PLATFORM.getServer_id());
		userOrder.setDetail(platName+"联运平台用户充值，订单已经初始化，正等待开通");
		// 创建订单
		int result = UserOrderServiceFactory.getInstance().addUserOrder(userOrder);
		// 初始化分布式事务
		UserTransaction ut = null;
		int platform_type = PLATFORM.getPlatform_type();
		try {
			ut = UserTransactionUtil.getUserTransaction();
		} catch (Exception e) {
			logger.error("创建"+platName+"平台用户订单 获取事务失败", e);
			UserOrderServiceFactory.getInstance().openUserOrder(order_id, platform_type, OrderStatus.ORDER_OPEN_FAILED, "出现异常 订单开通失败");
			return ERROR_GAME;
		}
		// 开始具体加币操作
		if (result == ResultStatus.SUCCESS || result == ResultStatus.ORDER_STATUS_NOT_OPENED) {
			try {
				ut.begin();
				String successDetail = result == ResultStatus.SUCCESS ? "订单已经成功开通" : "订单经再次回调开通成功";
				// 本地加币并记录日志
				result = characterService.addCashForChar(userCharacter.getId(), gameCoinTotal, order_amount, order_id);
				if (result != ResultStatus.SUCCESS) {
					ut.rollback();
					UserOrderServiceFactory.getInstance().openUserOrder(order_id, platform_type, OrderStatus.ORDER_PAY_FAILED, "出现异常 订单失败");
					logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "加金锭失败"));
					return ERROR_GAME;
				}
				// 更新订单为成功状态
				result = UserOrderServiceFactory.getInstance().openUserOrder(order_id, platform_type, OrderStatus.ORDER_OPEN_SUCESS, successDetail);
				if (result != ResultStatus.SUCCESS) {
					ut.rollback();
					UserOrderServiceFactory.getInstance().openUserOrder(order_id, platform_type, OrderStatus.ORDER_PAY_FAILED, "出现异常 订单失败");
					logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "##修改订单失败 "));
					return ERROR_GAME;
				}
			} catch (Exception e) {
				UserOrderServiceFactory.getInstance().openUserOrder(order_id, platform_type, OrderStatus.ORDER_PAY_FAILED, "出现异常 订单失败");
				result = ResultStatus.FAILED;
				e.printStackTrace();
				logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "##抛出异常,事务回滚"),e);
			}
			// 事务提交
			if (result == ResultStatus.SUCCESS) {
				try {
					ut.commit();
					result = SUCCESS;
				} catch (Exception e) {
					logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "##事务提交抛出异常 回滚"),e);
					result = ERROR_GAME;
					UserOrderServiceFactory.getInstance().openUserOrder(order_id, platform_type, OrderStatus.ORDER_PAY_FAILED, "出现异常 订单失败");
				}
			} else {
				result = ERROR_GAME;
				try {
					ut.rollback();
				} catch (Exception e) {
					logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "##platform addmoney fialed,transaction rollback failed"),e);
				}finally{
					UserOrderServiceFactory.getInstance().openUserOrder(order_id, platform_type, OrderStatus.ORDER_PAY_FAILED, "出现异常 订单失败");
				}
			}
			return result;
		} else if (result == ResultStatus.ORDER_STATUS_OPENED_ERROR) {// 订单已经开通，直接返回
			logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "订单重复"));
			return ERROR_REPEAT;

		} else {// 订单开通失败
			logger.error(log_e(username, serverId, agent, time, sign, order_id,order_amount, "未知错误"));
			UserOrderServiceFactory.getInstance().openUserOrder(order_id, platform_type, OrderStatus.ORDER_PAY_FAILED, "未知异常");
			return ERROR_UNKNOWN;
		}
	}

	/**
	 * 判断角色是否存在 用户已经激活过该分区返回1，否则返回0
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @author liuxueliang/2014-3-5/上午10:00:20
	 */
	@RequestMapping(value = "/getCharacter")
	@ResponseBody
	public int getCharacter(HttpServletRequest request,HttpServletResponse response) {
		final String username = ParamUtils.getParameter(request, "qid", "");
		final String serverId = ParamUtils.getParameter(request, "server_id", "");
		final String sign = ParamUtils.getParameter(request, "sign", "");
		final long time = ParamUtils.getLongParameter(request, "time", 0L);
		final String agent = ParamUtils.getParameter(request, "agent", "");
		if ("".equals(username) || "".equals(serverId) || "".equals(sign)||"".equals(agent)||time==0L) {
			logger.error(log_c(username,serverId,agent,time,sign,"参数不合法"));
			return ERROR_PARM;
		}
		
		PlatformInfo platformInfo = getPlatformInfo(agent, serverId);
		if(platformInfo==null){
			logger.error(log_c(username,serverId,agent,time,sign,"参数不合法"));
			return ERROR_PARM;
		}
//		if(!isWhite(request, platformInfo)){
//			logger.error(log_c(username,serverId,agent,time,sign,"ip不合法"));
//			return ERROR_IP;
//		}
		// check pay_key
		String authKey = PlatformUtils.getMD5(username,serverId,""+time,agent,platformInfo.getCheck_key());
		if (!authKey.equals(sign)) {
			logger.error(log_c(username,serverId,agent,time,sign,"密钥不合法"));
			return ERROR_SIGN;
		}
		
		UserCharacter userCharacter = characterService.getCharacterByAccountName(username);
		if (userCharacter == null) {
			logger.error(log_c(username,serverId,agent,time,sign,"无角色"));
			return ERROR_NOPLAYER;
		}
		return SUCCESS;

	}
	
	@RequestMapping(value = "/getChargeUrl")
	@ResponseBody
	public Object getChargeUrl(){
		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap.put("chargeUrl", PLATFORM.getCharge_url());
		return retMap;
	}
	
	@RequestMapping(value = "/reloadIp")
	public void reloadIp (HttpServletRequest request,HttpServletResponse response){
		System.out.println("================================reloadIp");
		genCardKey();
		final String plat = ParamUtils.getParameter(request, "p", "");
		if(PlatformJoinService.reloadMap.get(plat)!=null){
			PlatformJoinService.reloadMap.put(plat, Boolean.TRUE);
		}
		System.out.println(PlatformJoinService.reloadMap.toString());
	}
	
	/**
	 * 是否是白名单ip
	 * @param request
	 * @param platformInfo
	 * @return
	 */
	private boolean isWhite(HttpServletRequest request,PlatformInfo platformInfo){
		if(platformInfo.getWhiteIP() == null){
			return true;
		}
		for(String ip:platformInfo.getWhiteIP()){
			if(HttpUtils.getIp(request).equals(ip)){
				return true;
			}
		}
		System.out.println("拦截ip: "+HttpUtils.getIp(request));
		return false;
	}
	
	public static String generateBeginnerSign(String qid){
//		md5(${game },${qid },${server_id},${ agent },${key})
		qid = qid.toLowerCase(Locale.US);
		String name = "stby";//写死的
		String serverId = PLATFORM.getServer_id().toUpperCase(Locale.US);
		String agent = PLATFORM.getPlatform_id();
		String key = PLATFORM.getBeginner_key();
		
		return PlatformUtils.getMD5(name,qid,serverId,agent,key);
	}
	
//	public static void main(String[] args) {
//		String s = "stby,10005,S1,th_qu247,fb5f3231f3ee653a69dae8ac62ebd545";
////		String s = "stby,user456,S1,fq_32wan,4adc0b262cb16e566c4bd1d6075d3d6d";
//		System.out.println(PlatformUtils.getMD5(s));
//	}

	
	public CharacterService getCharacterService() {
		return characterService;
	}

	public void setCharacterService(CharacterService characterService) {
		this.characterService = characterService;
	}

	/**
	 * 登陆日志
	 * @param qid
	 * @param server_id
	 * @param agent
	 * @param time
	 * @param sign
	 * @param isAdult
	 * @param des
	 */
	private String log_l(String qid,String server_id,String agent,long time,String sign,int isAdult,String des){
		String s = String
				.format("platform logined： param is invalid,[qid=%s serverId=%s agent=%s time=%s sign=%s isAdult=%s],describtion:%s",
						qid, server_id, agent, time, sign, isAdult, des);
		return s;
	}
	
	/**
	 * 拿到Platform
	 * @param platformId
	 * @param serverId
	 * @return
	 */
	private PlatformInfo getPlatformInfo(String platformId,String serverId){
		return platformJoinService.getPlatformInfo(platformId,serverId);
	
	}
	
	/**
	 * 充值日志记录 
	 * @param qid
	 * @param server_id
	 * @param agent
	 * @param time
	 * @param sign
	 * @param order_id
	 * @param order_amount
	 * @param des
	 */
	private String log_e(String qid,String server_id,String agent,long time,String sign,String order_id,int order_amount,String des){
		String s = String
				.format("platform exchange： param is invalid,[qid=%s serverId=%s agent=%s time=%s sign=%s order_id=%s order_amount=%s],describtion:%s",
						qid, server_id, agent, time, sign, order_id,order_amount, des);
		return s;
	}

	
	/**
	 * 得到用户信息日志
	 * @param qid
	 * @param server_id
	 * @param agent
	 * @param time
	 * @param sign
	 * @param des
	 */
	private String log_c(String qid,String server_id,String agent,long time,String sign,String des){
		String s = String
				.format("platform getCharacter： param is invalid,[qid=%s serverId=%s agent=%s time=%s sign=%s ],describtion:%s",
						qid, server_id, agent, time, sign, des);
		return s;
	}
	
	/**
	 * 根据平台得到失败url
	 * 有的平台的地址后面需要跟参数 
	 * @param platformInfo
	 * @param parms
	 * @return
	 */
	private String getFailUrl (PlatformInfo platformInfo,HttpServletRequest request){
		String id = platformInfo.getPlatform_id();
		String result = "";
		if("xd_ufojoy".equals(id)){
			String username = ParamUtils.getParameter(request, "qid", "");
			result += platformInfo.getFail_url() + username;
		}else{
			result += platformInfo.getFail_url();
		}
		return result;
	} 
	
	public static void main(String[] args) throws ParseException {
//		String time = "2014-04-24 10:00:00";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date =  sdf.parse(time);
//		System.out.println(date.getTime());
//		
//		System.out.println(System.currentTimeMillis());
		
		List list = new ArrayList();
		for(int i=0;i<10000;i++){
			Cards card = new Cards();
			String index = ""+i;
			String type = "mt-a";
			card.setCard_key(PlatformUtils.getMD5(type,index,"stby","20140429").toUpperCase(Locale.US));
			card.setType(type);
			card.setStatus(0);
			list.add(card);
		}
		CardsServiceFactory.getInstance().saveCards(list);
	}
	
	
	

}
