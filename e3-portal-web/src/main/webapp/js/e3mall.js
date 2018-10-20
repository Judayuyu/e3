var E3MALL = {
	checkLogin : function(){
		var _ticket = $.cookie("token");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8099/user/token/" + _ticket,
			//跨域请求时，服务端虽然返回了json数据，但是js却不能拿到此json
			///若请求时指定类型jsonp，则在请求时增加一个参数callback，在contoller将
			//返回的json数据封装成js语句便可以解决此问题
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "，欢迎来到宜立方购物网！<a href=\"http://www.e3mall.cn/user/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	E3MALL.checkLogin();
});