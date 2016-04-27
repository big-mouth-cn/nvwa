<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="base" value="${pageContext.request.scheme}://${pageContext.request.serverName }:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>nvwa-zkadmin</title>

    <link href="${ctx }/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx }/css/console.min.css" rel="stylesheet">
	<link href="${ctx }/js/nprocess/nprogress.css" rel="stylesheet">
	<link href="${ctx }/js/ztree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
    <link href="${ctx }/css/starter-template.css" rel="stylesheet">

	<style type="text/css">
	.ztree {font-family: "Consolas"}
	.ztree li span.button.parent_ico_open, .ztree li span.button.parent_ico_close{margin-right:2px; background: url(${ctx}/js/ztree/css/zTreeStyle/img/diy/3.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
	.ztree li span.button.custom_ico_docu{margin-right:2px; background: url(${ctx}/js/ztree/css/zTreeStyle/img/diy/3.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
	.list-group-item:first-child {border-top-left-radius:0px;border-top-right-radius:0px;}
	.list-group-item:last-child {border-bottom-right-radius:0px;border-bottom-left-radius:0px;}
	.badge {color: #333;background-color: transparent;font-weight: normal;}
	</style>
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">ZkAdmin</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav" id="nav">
          </ul>
          <ul class="nav navbar-nav navbar-right hidden-sm">
            <li><a href="http://big-mouth.cn" target="_blank"><i class="glyphicon glyphicon-info-sign"></i> Abount</a></li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container">
		<div class="row starter">
	      <div class="col-md-12">
	      	<div class="panel panel-default">
	      		<div class="panel-body">
	      			<div class="ztree" id="tree"></div>
	      		</div>
	      	</div>
	      </div>
		</div>
    </div>

    <script src="${ctx }/js/jquery/1.11.3/jquery.min.js"></script>
    <script src="${ctx }/js/bootstrap.min.js"></script>
    
    <script src="${ctx }/js/nvwa/nvwa-modal.js"></script>
    <script src="${ctx }/js/nprocess/nprogress.js"></script>
    
    <script src="${ctx }/js/ztree/js/jquery.ztree.core-3.5.min.js"></script>
    <script src="${ctx }/js/ztree/js/jquery.ztree.excheck-3.5.js"></script>
	<script src="${ctx }/js/ztree/js/jquery.ztree.exedit-3.5.js"></script>
    
	<script src="${ctx }/js/node.js"></script>
  </body>
</html>
<script>
window.baseUrl = '${ctx}';
</script>