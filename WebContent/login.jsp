<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
<meta charset="utf-8">
<title>SegmentSearch</title>
<meta name="viewport"
	content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">

<!-- Bootstrap core CSS -->
<link href="./bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="./css/signin.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<form class="form-signin" ACTION="Login" METHOD="Post">
			<h1 class="form-signin-heading text-center">欢迎使用基因片段检索分析系统</h1>
			<h2 class="form-signin-heading text-center" >简易版</h2>
			<label for="inputEmail" class="sr-only">用户名</label> 
			<input type="text" id="inputUser" name="SigninName" class="form-control"
				placeholder="User Name" required autofocus> 
            <label for="inputPassword" class="sr-only">密码</label> 
			<input	type="password" id="inputPassword" name="SigninPassword" class="form-control"	
				  placeholder="Password" required>
		    <!-- 
			<div class="checkbox">
				<label> <input type="checkbox" value="remember-me">
					记住我
				</label>
			</div> 
			-->
			<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
			<button class="btn btn-lg btn-primary btn-block" name="reset"
				type="reset">重置</button>
		</form>

	</div>
	<!-- /container -->
	<div id="footer" align="center">
		<input class="button green:hover" type="button" value="实验室人员登录"
			onclick="window.location='${pageContext.request.contextPath }/EmployeeLogin.html';">
	</div>

</body>

</html>