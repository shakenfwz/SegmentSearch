<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
    <title>添加案例</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="./bootstrap/3.3.7/css/bootstrap.min.css">
    <script type="text/javascript" src="./js/jquery.js"></script>
    <script src="./bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<body>
    <!-- 导航条 -->
    <!-- Fixed navbar -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="Home.jsp">基因片段检索</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="#">首页</a></li>
                <li><a href="#about">关于</a></li>
                <li><a href="#contact">联系</a></li>
                <li class="dropdown active">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">功能   <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">变异分析</a></li>
                        <li><a href="#">案例管理</a></li>
                        <li class="divider"></li>
                        <li><a href="#">系统设置</a></li>
                    </ul>
                </li>
            </ul>
            <div class="navbar-form navbar-right">
                <c:if test="${user==null }">
                    <form action="${pageContext.request.contextPath }/Login" method="post">
                        <input class="form-control" type="text" name="username">
                        <input class="form-control" type="password" name="password">
                        <input type="submit" value="登陆">
                        <input type="button" value="注册" onclick="javascript:window.parent.body.location.href='${pageContext.request.contextPath }/client/register.jsp'">
                    </form>
                </c:if>
                <c:if test="${user!=null }">
                    欢迎您：${user.username }
                    <a href="${pageContext.request.contextPath }/client/LoginOutServlet">注销</a>
                </c:if>
            </div>
        </div>
        <!--/.nav-collapse -->
    </nav>
    <!-- 导航条结束 -->
    <div class="container-fluid">
        <div class="jumbotron">
            <div class="col-md-8"></div>
            <div class="col-md-4"></div>
        </div>
        <div class="row"></div>
    </div>
    <div class="container">
        <form class="form-horizontal" role="form" id="edit" name="edit" ACTION="AddExample" METHOD="POST">
            <div class="row">
                <!-- 表单第一列 -->
                <div class="col-md-4">
                    <p>患者基本信息</p>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="sampleName">受检者姓名*</label>
                        <div class="col-sm-4 controls">
                            <input type="text" id="sampleName" placeholder="sampleName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="sampleSex">受检者性别*</label>
                        <div class="col-sm-4">
                            <select class="form-control" name="sampleSex">
                                <option value="" selected></option>
                                <option value="女">女</option>
                                <option value="男">男</option>
                                <option value="未知">未知</option>
                                <option value="/">/</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="sampleAge">受检者年龄*</label>
                        <div class="col-sm-4 controls">
                            <input type="text" id="sampleAge" placeholder="sampleAge">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="sampleType">受检者类型* </label>
                        <div class="col-sm-6 controls">
                            <select class="form-control" name="sampleType">
                                <option value="" selected></option>
                                <option value="先证者">先证者</option>
                                <option value="表型正常者">表型正常者</option>
                                <option value="疑似者">疑似者</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="samplePhone">受检者电话</label>
                        <div class="col-sm-4 controls">
                            <input type="text" id="samplePhone" placeholder="samplePhone">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="sampleAddress">受检者地址</label>
                        <div class="col-sm-4 controls">
                            <input type="text" id="sampleAddress" placeholder="sampleAddress">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="sampleClinic">临床表现*</label>
                        <div class="col-sm-4controls">
                            <textarea class="form-control" rows="4" placeholder="sampleClinic"></textarea>
                        </div>
                    </div>
                </div>
                <!-- 表单第二列 -->
                <div class="col-md-4">
                    <p>患者附加信息</p>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">与患者关系</label>
                        <INPUT TYPE="TEXT" NAME="genre">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="genre"> 标本1编号</label>
                        <INPUT TYPE="TEXT" NAME="genre">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">联系人姓名</label>
                        <INPUT TYPE="TEXT" NAME="genre">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">样本编号</label>
                        <INPUT TYPE="TEXT" NAME="sampleID">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">标本类型</label>
                        <INPUT TYPE="TEXT" NAME="SampleType">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">送检日期</label>
                        <INPUT TYPE="TEXT" NAME="sendDate">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">检测日期</label>
                        <INPUT TYPE="TEXT" NAME="CheckDate">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">检测平台</label>
                        <INPUT TYPE="TEXT" NAME="testMethod">
                    </div>
                </div>
                <!-- 表单第三列 -->
                <div class="col-md-4">
                    <p>检测结果：</p>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">染色体</label>
                        <INPUT TYPE="TEXT" NAME="sgmName">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">位置</label>
                        <INPUT TYPE="TEXT" NAME="sgmPos">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">长度</label>
                        <INPUT TYPE="TEXT" NAME="sgmLen">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">类型</label>
                        <INPUT TYPE="TEXT" NAME="sgmType">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">基因</label>
                        <INPUT TYPE="TEXT" NAME="sgmGen">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">探针</label>
                        <INPUT TYPE="TEXT" NAME="sgmProbe">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">多态性</label>
                        <INPUT TYPE="TEXT" NAME="sgmPmp">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">decipher</label>
                        <INPUT TYPE="TEXT" NAME="sgmDcp">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">OMIM</label>
                        <INPUT TYPE="TEXT" NAME="sgmOMIM">
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label " for="">备注</label>
                        <INPUT TYPE="TEXT" NAME="sgmCmt">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <div class="row col-sm-12 vertical-middle-sm">
                        <div class="col-sm-6">
                            <button type="submit" class="btn btn-sm btn-primary btn-block">提交</button>
                        </div>
                        <div class="col-sm-6">
                            <button type="reset" class="btn btn-sm btn-primary btn-block">重置</button>
                        </div>
                    </div>
                </div>
                <div class="col-md-4"></div>
            </div>
        </form>
    </div>
</body>

</html>
