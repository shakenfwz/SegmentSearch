<%@ page language="java" import="java.util.*,filter.CsrfFilter" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="no-js" lang="en">

<%
	String csrfToken = (String) request.getAttribute(CsrfFilter.TOKEN_SESSION_KEY);
	if (csrfToken == null) {
		csrfToken = (String) request.getSession().getAttribute(CsrfFilter.TOKEN_SESSION_KEY);
	}
%>
<head>
<meta charset="utf-8">
<title>SegmentSearch</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="csrf-token" content="<%= csrfToken != null ? csrfToken : "" %>">

<!-- Bootstrap core CSS -->
<link href="./css/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="./css/signin.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<%
			String error = (String) request.getAttribute("error");
			if (error == null) {
				error = request.getParameter("error");
			}
			if (error != null && error.length() > 0) {
		%>
		<div class="alert alert-danger text-center"><%= error %></div>
		<%
			}
		%>
		<form class="form-signin" ACTION="Login" METHOD="Post">
			<input type="hidden" name="_csrf" value="<%= csrfToken != null ? csrfToken : "" %>">
			<h1 class="form-signin-heading text-center">欢迎使用基因片段检索分析系统</h1>
			<h2 class="form-signin-heading text-center">简易版</h2>

			<label for="inputUser" class="sr-only">用户名</label>
			<input type="text" id="inputUser" name="username" class="form-control"
				placeholder="用户名" required autofocus>

			<label for="inputPassword" class="sr-only">密码</label>
			<input type="password" id="inputPassword" name="password" class="form-control"
				placeholder="密码" required>

			<label for="inputCaptcha" class="sr-only">验证码</label>
			<div class="row captcha-row">
				<div class="col-xs-5">
					<img id="captchaImg" src="Captcha?t=" alt="验证码"
					style="cursor:pointer;height:36px;" title="点击刷新">
				</div>
				<div class="col-xs-7">
					<input type="text" id="inputCaptcha" name="captcha" class="form-control"
						placeholder="验证码" autocomplete="off" required>
				</div>
			</div>

			<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
			<button class="btn btn-lg btn-default btn-block" name="reset"
				type="reset">重置</button>
		</form>
	</div>
	<!-- /container -->
	<div id="footer" align="center">
		<input class="button green:hover" type="button" value="实验室人员登录"
			onclick="window.location='${pageContext.request.contextPath}/EmployeeLogin.html';">
	</div>
	<!-- jQuery -->
	<script type="text/javascript" src="./vendor/jquery/jquery-3.5.1.min.js"></script>
	<script type="text/javascript" src="./js/jquery.cookie.js"></script>
	<script type="text/javascript">
		// 服务端验证码：点击图片刷新（SEC-01）
		document.getElementById('captchaImg').onclick = function () {
			this.src = 'Captcha?t=' + Date.now();
		};
	</script>
</body>

</html>
